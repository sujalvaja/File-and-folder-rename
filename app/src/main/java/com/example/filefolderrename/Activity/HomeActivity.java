package com.example.filefolderrename.Activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.filefolderrename.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final float KILOBYTE = 1024;
    TextView freestorage, txtpre;
    PieChart pieChart;
    DownloadManager.Request chart;
    ImageView rename;
    TextView totalstorage;
    TextView usedstorage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        freestorage = findViewById(R.id.freestorage);
        totalstorage = findViewById(R.id.total);
        usedstorage = findViewById(R.id.used);
        rename = findViewById(R.id.btnrename);
        pieChart = findViewById(R.id.pieChart);
        txtpre = findViewById(R.id.txtpre);

        checkPermission();
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        StatFs internalStatFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        float internalTotal;
        float internalFree;

        StatFs externalStatFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        float externalTotal;
        float externalFree;

        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            internalTotal = (internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong()) / (KILOBYTE * KILOBYTE * KILOBYTE);
            internalFree = (internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong()) / (KILOBYTE * KILOBYTE * KILOBYTE);
            externalTotal = (externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong()) / (KILOBYTE * KILOBYTE * KILOBYTE);
            externalFree = (externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong()) / (KILOBYTE * KILOBYTE * KILOBYTE);
        } else {
            internalTotal = ((float) internalStatFs.getBlockCount() * (float) internalStatFs.getBlockSize()) / (KILOBYTE * KILOBYTE * KILOBYTE);
            internalFree = ((float) internalStatFs.getAvailableBlocks() * (float) internalStatFs.getBlockSize()) / (KILOBYTE * KILOBYTE * KILOBYTE);
            externalTotal = ((float) externalStatFs.getBlockCount() * (float) externalStatFs.getBlockSize()) / (KILOBYTE * KILOBYTE * KILOBYTE);
            externalFree = ((float) externalStatFs.getAvailableBlocks() * (float) externalStatFs.getBlockSize()) / (KILOBYTE * KILOBYTE * KILOBYTE);
        }

        float total = internalTotal + externalTotal;
        float free = internalFree + externalFree;
        float used = total - free;

        float usedPercentage = used / total * 100;
        @SuppressLint("DefaultLocale") String percentageString = String.format("%.2f%%", usedPercentage);
        txtpre.setText(percentageString);
        pieChart.addPieSlice(
                new PieModel(
                        "Used",
                        Integer.parseInt(Integer.toString((int) Math.abs(used))),
                        Color.parseColor("#F37254")));
        pieChart.addPieSlice(
                new PieModel(
                        "Free",
                        Integer.parseInt(Integer.toString((int) Math.abs(free))),
                        Color.parseColor("#DEDDE7")));


        freestorage.setText(String.format("%.2f", free) + " GB");
        totalstorage.setText(String.format("%.2f", total) + "GB");
        usedstorage.setText(String.format("%.2f", used) + " GB");
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(0, 0);
                }
                startActivity(i);
            }
        });
    }

    private void checkPermission() {

        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,
                permissions, grantResults, this);
    }

}
