package com.example.masnur.Fitur_Acara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahAcaraFragment extends Fragment {

    private EditText etJudul, etDeskripsi;
    private ImageView imgPreview;
    private LinearLayout layoutPlaceholder;
    private Button btnUnggah;
    private AcaraModel editModeData;
    private Uri selectedImageUri;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imgPreview.setImageURI(selectedImageUri);
                            imgPreview.setVisibility(View.VISIBLE);
                            layoutPlaceholder.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tambah_acara, container, false);

        etJudul = view.findViewById(R.id.et_judul);
        etDeskripsi = view.findViewById(R.id.et_deskripsi);
        imgPreview = view.findViewById(R.id.imgPreview);
        layoutPlaceholder = view.findViewById(R.id.layoutPlaceholder);
        btnUnggah = view.findViewById(R.id.btnUnggah);

        // Klik area upload → pilih gambar
        LinearLayout layoutUpload = view.findViewById(R.id.layout_upload_poster);
        layoutUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Mode edit
        if (getArguments() != null) {
            editModeData = getArguments().getParcelable("acara");
            if (editModeData != null) {
                etJudul.setText(editModeData.getNamaEvent());
                etDeskripsi.setText(editModeData.getDeskripsiEvent());

                String url = editModeData.getGambarEvent();
                if (url != null && !url.isEmpty() && !url.endsWith("/acara/")) {
                    imgPreview.setVisibility(View.VISIBLE);
                    layoutPlaceholder.setVisibility(View.GONE);
                    // Untuk edit, kita bisa load URL via Glide, tapi di sini pakai placeholder dulu
                }
            }
        }

        btnUnggah.setOnClickListener(v -> {
            String judul = etJudul.getText().toString().trim();
            String deskripsi = etDeskripsi.getText().toString().trim();

            if (judul.isEmpty()) {
                Toast.makeText(getContext(), "Judul wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Siapkan request body
            RequestBody namaEvent = RequestBody.create(judul, MediaType.get("text/plain"));
            RequestBody tanggalEvent = RequestBody.create("2025-12-31", MediaType.get("text/plain"));
            RequestBody deskripsiEvent = RequestBody.create(deskripsi, MediaType.get("text/plain"));
            RequestBody lokasiEvent = RequestBody.create("-", MediaType.get("text/plain"));
            RequestBody username = RequestBody.create("admin", MediaType.get("text/plain"));

            ApiService api = ApiClient.getService();

            if (editModeData == null) {
                // TAMBAH
                Call<AcaraResponse> call = api.tambahAcara(
                        namaEvent, tanggalEvent, deskripsiEvent, lokasiEvent, username, null
                );
                prosesAPI(call, "Acara berhasil ditambah");
            } else {
                // EDIT
                RequestBody idEvent = RequestBody.create(editModeData.getIdEvent(), MediaType.get("text/plain"));
                Call<AcaraResponse> call = api.editAcara(
                        idEvent, namaEvent, tanggalEvent, deskripsiEvent, lokasiEvent, username, null
                );
                prosesAPI(call, "Acara berhasil diupdate");
            }
        });

        return view;
    }

    private void prosesAPI(Call<AcaraResponse> call, String successMessage) {
        call.enqueue(new Callback<AcaraResponse>() {
            @Override
            public void onResponse(Call<AcaraResponse> call, Response<AcaraResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AcaraResponse res = response.body();
                    if ("success".equals(res.getStatus()) || "1".equals(res.getStatus())) {
                        Toast.makeText(getContext(), "✓ " + successMessage, Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    } else {
                        Toast.makeText(getContext(), "Gagal: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AcaraResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Jaringan error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}