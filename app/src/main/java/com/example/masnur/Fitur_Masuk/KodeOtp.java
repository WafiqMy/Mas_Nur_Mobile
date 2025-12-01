package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.R;
import com.example.masnur.Fitur_Masuk.OtpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KodeOtp extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4;
    private Button buttonLanjut;
    private String email;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kode_otp);

        email = getIntent().getStringExtra("email");
        apiService = ApiClient.getService();

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        buttonLanjut = findViewById(R.id.buttonLanjut);

        setupOtpAutoFocus();

        buttonLanjut.setOnClickListener(v -> {
            String kodeOTP = otp1.getText().toString().trim() +
                    otp2.getText().toString().trim() +
                    otp3.getText().toString().trim() +
                    otp4.getText().toString().trim();

            if (kodeOTP.length() != 4) {
                Toast.makeText(this, "Lengkapi semua 4 digit OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            verifyOtp(email, kodeOTP);
        });
    }

    private void setupOtpAutoFocus() {
        otp1.addTextChangedListener(new DigitTextWatcher(otp1, otp2));
        otp2.addTextChangedListener(new DigitTextWatcher(otp2, otp3));
        otp3.addTextChangedListener(new DigitTextWatcher(otp3, otp4));
        otp4.addTextChangedListener(new DigitTextWatcher(otp4, null));

        // Handle backspace
        otp2.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && otp2.getText().toString().isEmpty()) {
                otp1.requestFocus();
                return true;
            }
            return false;
        });
        otp3.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && otp3.getText().toString().isEmpty()) {
                otp2.requestFocus();
                return true;
            }
            return false;
        });
        otp4.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && otp4.getText().toString().isEmpty()) {
                otp3.requestFocus();
                return true;
            }
            return false;
        });
    }

    // ✅ Helper class untuk auto-focus OTP — lebih bersih & reusable
    private class DigitTextWatcher implements TextWatcher {
        private EditText currentView;
        private EditText nextView;

        DigitTextWatcher(EditText current, EditText next) {
            this.currentView = current;
            this.nextView = next;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    private void verifyOtp(String email, String kodeOTP) {
        Call<OtpResponse> call = apiService.verifyOtp(email, kodeOTP);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();

                    if ("valid".equals(status)) {
                        Toast.makeText(KodeOtp.this, "Verifikasi berhasil!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(KodeOtp.this, SandiBaru.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        // ✅ Sudah diperbaiki: hanya satu )
                        Toast.makeText(KodeOtp.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMsg = "HTTP " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ": " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(KodeOtp.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(KodeOtp.this, "Gagal verifikasi OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }
}