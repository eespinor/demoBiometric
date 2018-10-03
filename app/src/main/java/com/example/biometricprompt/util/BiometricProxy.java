package com.example.biometricprompt.util;

import android.util.Log;

import java.security.Signature;

import androidx.biometrics.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

/**
 * see https://android-developers.googleblog.com/2018/06/better-biometrics-in-android-p.html
 * see https://source.android.com/security/biometric
 * see https://android-developers.googleblog.com/2018/08/introducing-android-9-pie.html
 *
 */
public class BiometricProxy {
    private final String TAG = BiometricPrompt.class.getCanonicalName();
    private InterfaceBiometricSuccess interfaceBiometricSuccess;
    private InterfaceBiometricError interfaceBiometricError;
    private Signature signature;

    public BiometricProxy(FragmentActivity fragmentActivity, InterfaceBiometricSuccess interfaceBiometricSuccess, InterfaceBiometricError interfaceBiometricError, String key) {
        this.interfaceBiometricError = interfaceBiometricError;
        this.interfaceBiometricSuccess = interfaceBiometricSuccess;
        try {
            this.signature = UtilCripto.initSignature(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setDescription("Description")
                .setTitle("Title")
                .setSubtitle("Subtitle")
                .setDescription("Cancel")
                .setNegativeButtonText("Cancel")
//                        .setNegativeButton("Cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Log.i(TAG, "Cancel button clicked");
//                            }
//                        })
                .build();
//                CancellationSignal cancellationSignal = getCancellationSignal();
        BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationCallback();
        BiometricPrompt mBiometricPrompt = new BiometricPrompt(fragmentActivity, fragmentActivity.getMainExecutor(), authenticationCallback);

        if (signature != null) {
//                    mBiometricPrompt.authenticate(new BiometricPrompt.CryptoObject(signature), cancellationSignal, getMainExecutor(), authenticationCallback);
            mBiometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(signature));
        }
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {
        // Callback for biometric authentication result
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

//            @Override
//            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//                super.onAuthenticationHelp(helpCode, helpString);
//            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                Log.i(TAG, "onAuthenticationSucceeded");
                super.onAuthenticationSucceeded(result);
                Signature signature = result.getCryptoObject().getSignature();
                interfaceBiometricSuccess.success(signature);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                interfaceBiometricError.error();
            }
        };
    }


}
