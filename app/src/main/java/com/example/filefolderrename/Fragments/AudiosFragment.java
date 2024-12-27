package com.example.filefolderrename.Fragments;

import static com.example.filefolderrename.Fragments.DocumentFragment.math;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filefolderrename.Activity.MainActivity;
import com.example.filefolderrename.Activity.Music_folder;
import com.example.filefolderrename.Adapter.AudioAdapter;
import com.example.filefolderrename.MarginDecoration;
import com.example.filefolderrename.R;
import com.example.filefolderrename.models.PictureFacer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AudiosFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public Context context;
    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allpictures;
    ArrayList<PictureFacer> selectedItem;
    ProgressBar load;
    Music_folder Music_folder;
    ImageView btnclose;
    private FragmentManager supportFragmentManager;

    public AudiosFragment(Context context, Music_folder Music_folder, ArrayList<PictureFacer> selectedItem) {
        this.context = context;
        this.Music_folder = Music_folder;
        this.selectedItem = selectedItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audios, container, false);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        imageRecycler = view.findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(context));
        imageRecycler.hasFixedSize();
        imageRecycler.setItemAnimator(new DefaultItemAnimator());
        load = view.findViewById(R.id.loader);
        btnclose = view.findViewById(R.id.close);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        return view;
    }

    public void onClick(int position, String rename) {
        new AudiosFragment.LoadApplications().execute();
    }


    @Override
    public void onResume() {
        super.onResume();

        new LoadApplications().execute();
    }

    public FragmentManager getSupportFragmentManager() {
        return null;
    }

    public void setSupportFragmentManager(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    public void onBackPressed() {
        Intent intent = new Intent(requireActivity().getApplication(), MainActivity.class);
        startActivity(intent);
    }

    class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null, "Loading data...");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            allpictures = new ArrayList<>();
            allpictures = getAllaudiosByFolder();
            return null;
        }

        private ArrayList<PictureFacer> getAllaudiosByFolder() {
            ArrayList<PictureFacer> images = new ArrayList<>();
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            Cursor cursor = contentResolver.query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {

                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                    PictureFacer videoModel = new PictureFacer();
                    videoModel.setPicturName(title);
                    videoModel.setImageUri(data);
                    videoModel.setPicturePath(data);
                    float sizes = Integer.valueOf(size) / (1024f * 1024f);
                    videoModel.setPictureSize(math(sizes) + " mb");
                    File fileObject = new File(data);
                    Long fileModified = fileObject.lastModified();
                    videoModel.setDateTime(new Date(fileModified));
                    images.add(videoModel);
                } while (cursor.moveToNext());
            }
            return images;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Collections.sort(allpictures);
            Collections.reverse(allpictures);
            imageRecycler.setAdapter(new AudioAdapter(allpictures, selectedItem, getContext(), AudiosFragment.this));
            load.setVisibility(View.GONE);
            progress.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}
