package com.example.biometricprompt;


import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.biometricprompt.util.BiometricProxy;
import com.example.biometricprompt.util.InterfaceBiometricError;
import com.example.biometricprompt.util.InterfaceBiometricSuccess;
import com.example.biometricprompt.util.UtilBiometrics;

import java.security.Signature;
import java.security.SignatureException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String KEY = "KEY_PREMIA";

    private String mMessage;

    InterfaceBiometricSuccess interfaceBiometricSuccess = new InterfaceBiometricSuccess() {
        @Override
        public void success(Signature signature) {
            try {
                signature.update(mMessage.getBytes());
                String cadBase64 = Base64.encodeToString(signature.sign(), Base64.URL_SAFE);
                Toast.makeText(getApplicationContext(), mMessage + ":" + cadBase64, Toast.LENGTH_SHORT).show();
            } catch (SignatureException e) {
                throw new RuntimeException();
            }
        }
    };
    InterfaceBiometricError interfaceBiometricError = new InterfaceBiometricError() {
        @Override
        public void error() {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

        }
    };

    // Unique identifier of a key pair

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onRegistar(View v) {

        if (UtilBiometrics.isSupportBiometricPrompt(this.getPackageManager())) {

            mMessage = UtilBiometrics.generateKeyCripto(KEY);

            new BiometricProxy(this, interfaceBiometricSuccess, interfaceBiometricError, KEY);
        }
    }

    public void onValidar(View v) {

        if (UtilBiometrics.isSupportBiometricPrompt(this.getPackageManager())) {

            mMessage = UtilBiometrics.generateKey(KEY);

            new BiometricProxy(this, interfaceBiometricSuccess, interfaceBiometricError, KEY);
        }

    }


}
