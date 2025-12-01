package com.example.masnur.Fitur_Acara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.masnur.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailAcaraFragment extends Fragment {

    private AcaraModel acara;
    private ImageView imgUtama, imgVideoThumbnail;
    private TextView tvJudul, tvTanggal, tvLokasi, tvDeskripsi, btnKembali, tvVideoTitle, tvVideoFallback;
    private LinearLayout layoutVideoContainer, layoutVideoWithThumbnail;
    private RecyclerView rvDokumentasi;
    private DokumentasiAdapter dokumentasiAdapter;

    private static final String BASE_URL = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/kegiatan/";
    private static final String YOUTUBE_OEMBED = "https://www.youtube.com/oembed?url=%s&format=json";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            acara = getArguments().getParcelable("acara");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_acara, container, false);

        // Inisialisasi view
        imgUtama = view.findViewById(R.id.imgUtama);
        tvJudul = view.findViewById(R.id.tvJudul);
        tvTanggal = view.findViewById(R.id.tvTanggal);
        tvLokasi = view.findViewById(R.id.tvLokasi);
        tvDeskripsi = view.findViewById(R.id.tvDeskripsi);
        rvDokumentasi = view.findViewById(R.id.rvDokumentasi);
        btnKembali = view.findViewById(R.id.btnKembali);

        // Komponen video (baru)
        imgVideoThumbnail = view.findViewById(R.id.imgVideoThumbnail);
        tvVideoTitle = view.findViewById(R.id.tvVideoTitle);
        tvVideoFallback = view.findViewById(R.id.tvVideoFallback);
        layoutVideoContainer = view.findViewById(R.id.layoutVideoContainer);
        layoutVideoWithThumbnail = view.findViewById(R.id.layoutVideoWithThumbnail);

        rvDokumentasi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dokumentasiAdapter = new DokumentasiAdapter();
        rvDokumentasi.setAdapter(dokumentasiAdapter);

        if (acara != null) {
            tvJudul.setText(acara.getNamaEvent());
            tvTanggal.setText(acara.getTanggalEventFormatted());
            tvLokasi.setText(acara.getLokasiEvent());
            tvDeskripsi.setText(acara.getDeskripsiEvent());

            Glide.with(this)
                    .load(acara.getGambarEventAbsolut())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(imgUtama);

            dokumentasiAdapter.setDokumentasiList(acara.getDokumentasi());

            // ✅ TANGANI VIDEO DENGAN JUDUL
            String videoUrls = acara.getVideoUrls();
            handleVideoWithYouTubeInfo(videoUrls);

        } else {
            Toast.makeText(getContext(), "Data acara tidak ditemukan", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }

        btnKembali.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void handleVideoWithYouTubeInfo(String videoUrls) {
        if (videoUrls == null || videoUrls.trim().isEmpty()) {
            showFallbackText("Tidak ada video");
            return;
        }

        String firstUrl = getFirstValidYouTubeUrl(videoUrls);
        if (firstUrl != null) {
            fetchYouTubeInfo(firstUrl);
        } else {
            showFallbackText(videoUrls);
        }
    }

    private String getFirstValidYouTubeUrl(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        String[] parts = input.split("[,\\n\\r\\s]+");
        for (String part : parts) {
            String clean = part.trim();
            if (!clean.isEmpty() && isYouTubeUrl(clean)) {
                return clean;
            }
        }
        return null;
    }

    private boolean isYouTubeUrl(String url) {
        return url != null && (
                url.contains("youtube.com") ||
                        url.contains("youtu.be")
        );
    }

    private String extractYouTubeId(String url) {
        if (url == null) return null;
        String pattern = "(?:v=|vi=|youtu\\.be/|/v/|/vi/|embed/|shorts/)([\\w-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private void fetchYouTubeInfo(String youtubeUrl) {
        new Thread(() -> {
            try {
                String oembedUrl = String.format(YOUTUBE_OEMBED, Uri.encode(youtubeUrl, "UTF-8"));
                URL url = new URL(oembedUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    String title = json.optString("title", "Video YouTube");
                    String thumbnailUrl = json.optString("thumbnail_url", "");

                    // Gunakan thumbnail dari oEmbed jika tersedia (lebih besar kualitas)
                    if (thumbnailUrl.isEmpty()) {
                        String videoId = extractYouTubeId(youtubeUrl);
                        if (videoId != null) {
                            thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
                        }
                    }

                    final String finalTitle = title;
                    final String finalThumbnailUrl = thumbnailUrl;
                    requireActivity().runOnUiThread(() -> {
                        showYouTubeWithTitle(finalThumbnailUrl, finalTitle, youtubeUrl);
                    });

                } else {
                    Log.e("YOUTUBE_OEMBED", "Error: " + responseCode);
                    requireActivity().runOnUiThread(() -> showFallbackText(youtubeUrl));
                }

            } catch (Exception e) {
                Log.e("YOUTUBE_OEMBED", "Exception", e);
                requireActivity().runOnUiThread(() -> showFallbackText(youtubeUrl));
            }
        }).start();
    }

    private void showYouTubeWithTitle(String thumbnailUrl, String title, String fullUrl) {
        Glide.with(this)
                .load(thumbnailUrl)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imgVideoThumbnail);

        // Atur judul (HTML decode jika perlu)
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvVideoTitle.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvVideoTitle.setText(Html.fromHtml(title));
            }
        } catch (Exception e) {
            tvVideoTitle.setText(title);
        }

        // Tampilkan layout thumbnail + judul
        imgVideoThumbnail.setVisibility(View.VISIBLE);
        tvVideoTitle.setVisibility(View.VISIBLE);
        layoutVideoWithThumbnail.setVisibility(View.VISIBLE);
        tvVideoFallback.setVisibility(View.GONE);

        // Setup klik
        View.OnClickListener listener = v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl));
            startActivity(intent);
        };
        imgVideoThumbnail.setOnClickListener(listener);
        tvVideoTitle.setOnClickListener(listener);
    }

    private void showFallbackText(String text) {
        imgVideoThumbnail.setVisibility(View.GONE);
        tvVideoTitle.setVisibility(View.GONE);
        layoutVideoWithThumbnail.setVisibility(View.GONE);
        tvVideoFallback.setVisibility(View.VISIBLE);
        tvVideoFallback.setText(text);
        tvVideoFallback.setMovementMethod(LinkMovementMethod.getInstance());
    }

    // Adapter dokumentasi
    private static class DokumentasiAdapter extends RecyclerView.Adapter<DokumentasiAdapter.ViewHolder> {
        private List<String> dokumentasiList = new ArrayList<>();

        public void setDokumentasiList(List<String> list) {
            this.dokumentasiList = list != null ? list : new ArrayList<>();
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_dokumentasi_kecil, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String fileName = dokumentasiList.get(position).trim();
            String url = BASE_URL + fileName;
            Glide.with(holder.itemView.getContext())
                    .load(url)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .centerCrop()
                    .into(holder.ivDok);
        }

        @Override
        public int getItemCount() {
            return dokumentasiList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDok;
            ViewHolder(View itemView) {
                super(itemView);
                ivDok = itemView.findViewById(R.id.ivDok);
            }
        }
    }
}