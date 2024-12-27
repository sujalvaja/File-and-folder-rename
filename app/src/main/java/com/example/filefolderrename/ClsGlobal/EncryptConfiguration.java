package com.example.filefolderrename.ClsGlobal;

import android.os.Build;
import android.util.Log;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class EncryptConfiguration {

    private final static String TAG = "EncryptConfiguration";
    private int mChunkSize;
    private boolean mIsEncrypted;
    private byte[] mIvParameter;
    private byte[] mSecretKey;

    private EncryptConfiguration(Builder builder) {
        mChunkSize = builder._chunkSize;
        mIsEncrypted = builder._isEncrypted;
        mIvParameter = builder._ivParameter;
        mSecretKey = builder._secretKey;
    }

    public int getChuckSize() {
        return mChunkSize;
    }

    public boolean isEncrypted() {
        return mIsEncrypted;
    }


    public byte[] getSecretKey() {
        return mSecretKey;
    }

    public byte[] getIvParameter() {
        return mIvParameter;
    }


    public static class Builder {
        private int _chunkSize = 8192;
        private boolean _isEncrypted = false;
        private byte[] _ivParameter = null;
        private byte[] _secretKey = null;

        private static final String UTF_8 = "UTF-8";

        public Builder() {
        }

        public EncryptConfiguration build() {
            return new EncryptConfiguration(this);
        }


        public Builder setChuckSize(int chunkSize) {
            _chunkSize = chunkSize;
            return this;
        }


        public Builder setEncryptContent(String ivx, String secretKey, byte[] salt) {
            _isEncrypted = true;


            try {
                _ivParameter = ivx.getBytes(UTF_8);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "UnsupportedEncodingException", e);
            }


            try {

                int iterationCount = 1000; // recommended by PKCS#5
                int keyLength = 128;

                KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount, keyLength);
                SecretKeyFactory keyFactory = null;
                if (Build.VERSION.SDK_INT >= 19) {

                    keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1And8bit");
                } else {

                    keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                }
                byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();

                _secretKey = keyBytes;

            } catch (InvalidKeySpecException e) {
                Log.e(TAG, "InvalidKeySpecException", e);
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, "NoSuchAlgorithmException", e);
            }

            return this;
        }

    }

}
