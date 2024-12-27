package com.example.filefolderrename.Fragments;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.filefolderrename.Activity.MainActivity;
import com.example.filefolderrename.Activity.Video_folder;
import com.example.filefolderrename.Adapter.VideoAdapter;
import com.example.filefolderrename.MarginDecoration;
import com.example.filefolderrename.R;
import com.example.filefolderrename.models.PictureFacer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class VideoFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public Context context;
    Video_folder Video_folder;
    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allpictures;
    ArrayList<PictureFacer> selectedItem = new ArrayList<>();
    ProgressBar load;
    ImageView btnclose;

    public VideoFragment(Context context, Video_folder Video_folder, ArrayList<PictureFacer> selectedItem) {
        this.context = context;
        this.Video_folder = Video_folder;
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
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        imageRecycler = view.findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(context));
        imageRecycler.hasFixedSize();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        imageRecycler.setLayoutManager(layoutManager);
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
        new LoadApplications().execute();
    }


    @Override
    public void onResume() {
        super.onResume();
        new LoadApplications().execute();
    }

    public void onBackPressed() {
        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
        startActivity(intent);
    }

    private FragmentManager getSupportFragmentManager() {
        return null;
    }

    private void finish() {
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
            allpictures = getAllVideosByFolder();
            return null;
        }

        private ArrayList<PictureFacer> getAllVideosByFolder() {
            ArrayList<PictureFacer> video = new ArrayList<>();
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = contentResolver.query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    PictureFacer videoModel = new PictureFacer();
                    videoModel.setPicturName(title);
                    videoModel.setImageUri(data);
                    videoModel.setPicturePath(data);
                    videoModel.setPicturName(name);
                    File fileObject = new File(data);
                    long fileModified = fileObject.lastModified();
                    videoModel.setDateTime(new Date(fileModified));
                    video.add(videoModel);

                } while (cursor.moveToNext());
            }
            return video;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Collections.sort(allpictures);
            Collections.reverse(allpictures);
            VideoAdapter aa = new VideoAdapter(allpictures, selectedItem, getContext(), VideoFragment.this);
            imageRecycler.setAdapter(aa);
            load.setVisibility(View.GONE);
            progress.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}
