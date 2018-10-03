package com.example.biometricprompt.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import java.security.KeyPair;
import java.util.List;


public class UtilBiometrics {

    public static final String FEATURE_IRIS = "android.hardware.iris";
    public static final String FEATURE_FACE = "android.hardware.face";


    public static boolean isSupportBiometricPrompt(PackageManager packageManager) {
        List<PackageInfo> lista = packageManager.getInstalledPackages(PackageManager.GET_PROVIDERS);

        for (PackageInfo p : lista) {
            Log.i("TAG", p.packageName);
        }

//        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
        if (packageManager.hasSystemFeature(FEATURE_FACE)) {
            return true;
        }
        return false;
    }


    //SOLO PARA DEMO
    public static String generateKey(String key) {
        try {
            return new StringBuilder()
                    .append(key)
                    .append(":")
                    .append("0000001")
                    .toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //SOLO PARA DEMO
    public static String generateKeyCripto(String key) {
        try {

            KeyPair keyPair = UtilCriptos.buildKey(key, true);

            return new StringBuilder()
                    .append(Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.URL_SAFE))
                    .append(":")
                    .append(key)
                    .append(":")
                    .append("0000001")
                    .toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
