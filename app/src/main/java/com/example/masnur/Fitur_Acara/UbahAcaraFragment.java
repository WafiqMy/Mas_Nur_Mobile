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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahAcaraFragment extends Fragment {

    private EditText etJudul, etDeskripsi;
    private ImageView imgGambar;
    private Button btnUbahGambar, btnKembali, btnSimpan;
    private AcaraModel acara;
    private Uri selectedImageUri;
    private ApiService apiService;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getService();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imgGambar.setImageURI(selectedImageUri);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ubah_acara, container, false);

        // Inisialisasi view
        etJudul = view.findViewById(R.id.etJudul);
        etDeskripsi = view.findViewById(R.id.etDeskripsi);
        imgGambar = view.findViewById(R.id.imgGambar);
        btnUbahGambar = view.findViewById(R.id.btnUbahGambar);
        btnKembali = view.findViewById(R.id.btnKembali);
        btnSimpan = view.findViewById(R.id.btnSimpan);

        // Ambil data acara dari arguments
        // Ambil data acara dari arguments
        if (getArguments() != null) {
            acara = getArguments().getParcelable("acara");
            if (acara != null) {
                etJudul.setText(acara.getNamaEvent());
                etDeskripsi.setText(acara.getDeskripsiEvent());

                String url = acara.getGambarEvent();
                if (url != null && !url.isEmpty() && !url.endsWith("/acara/")) {
                    Glide.with(this).load(url).into(imgGambar);
                }
            } else {
                Toast.makeText(getContext(), "Data acara tidak ditemukan", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
                return view;
            }
        } else {
            Toast.makeText(getContext(), "Data acara tidak ditemukan", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return view;
        }

        // Event listener
        btnUbahGambar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnKembali.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        btnSimpan.setOnClickListener(v -> {
            String judul = etJudul.getText().toString().trim();
            String deskripsi = etDeskripsi.getText().toString().trim();

            if (judul.isEmpty()) {
                Toast.makeText(getContext(), "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            // Siapkan request body
            RequestBody idEvent = RequestBody.create(acara.getIdEvent(), MediaType.get("text/plain"));
            RequestBody namaEvent = RequestBody.create(judul, MediaType.get("text/plain"));
            RequestBody tanggalEvent = RequestBody.create("2025-12-31", MediaType.get("text/plain")); // TODO: ambil dari input
            RequestBody deskripsiEvent = RequestBody.create(deskripsi, MediaType.get("text/plain"));
            RequestBody lokasiEvent = RequestBody.create("-", MediaType.get("text/plain"));
            RequestBody username = RequestBody.create("admin", MediaType.get("text/plain"));

            // Upload gambar jika ada
            MultipartBody.Part gambarPart = null;
            if (selectedImageUri != null) {
                try {
                    java.io.File file = new java.io.File(getRealPathFromURI(selectedImageUri));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    gambarPart = MultipartBody.Part.createFormData("gambar_event", file.getName(), requestFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Gagal baca gambar", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Panggil API edit
            Call<AcaraResponse> call = apiService.editAcara(
                    idEvent, namaEvent, tanggalEvent, deskripsiEvent, lokasiEvent, username, gambarPart
            );

            call.enqueue(new Callback<AcaraResponse>() {
                @Override
                public void onResponse(Call<AcaraResponse> call, Response<AcaraResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AcaraResponse res = response.body();
                        if ("success".equals(res.getStatus()) || "1".equals(res.getStatus())) {
                            Toast.makeText(getContext(), "✓ " + res.getMessage(), Toast.LENGTH_SHORT).show();
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
        });

        return view;
    }

    private String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = requireActivity().getContentResolver().query(uri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}