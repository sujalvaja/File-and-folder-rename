package com.example.filefolderrename.Activity;


import static com.example.filefolderrename.ClsGlobal.ClsGlobal.requestPermission;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filefolderrename.R;
import com.example.filefolderrename.databinding.ActivityMainBinding;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_DIRECTORY = 200;
    private static final int TAG13 = 122;
    private final int PICK_AUDIO = 1;
    public ImageView imgfloder;
    public ImageView videofloder;
    public ImageView documentfloder;
    public ImageView apkfloder;
    public ImageView musicfloder;
    Uri AudioUri;
    TextView select_Audio;
    @SuppressLint("MissingInflatedId")

    private ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestPermission(MainActivity.this);


        imgfloder = findViewById(R.id.imagefolder);
        videofloder = findViewById(R.id.videofolder);
        musicfloder = findViewById(R.id.musicfolder);
        documentfloder = findViewById(R.id.documentfolder);
        apkfloder = findViewById(R.id.apkfolder);

        requestPermission(MainActivity.this);

        imgfloder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Image_folder.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);


            }
        });

        videofloder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Video_folder.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(0, 0);
                startActivity(i);
            }
        });


        musicfloder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Music_folder.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(0, 0);
                startActivity(i);
            }
        });

        documentfloder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Document_folder.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(0, 0);
                startActivity(i);
            }
        });

        apkfloder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Apk_folder.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(0, 0);
                startActivity(i);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,
                permissions, grantResults, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == SELECT_DIRECTORY && data != null) {

            if (data != null) {
            }
            if (requestCode == PICK_AUDIO && resultCode == RESULT_OK) {
                AudioUri = data.getData();
                select_Audio.setText("Audio Selected");
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }


}
