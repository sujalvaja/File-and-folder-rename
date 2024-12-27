package com.example.filefolderrename.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filefolderrename.Activity.Document_folder;
import com.example.filefolderrename.Activity.MainActivity;
import com.example.filefolderrename.Adapter.DocumentAdapter;
import com.example.filefolderrename.MarginDecoration;
import com.example.filefolderrename.R;
import com.example.filefolderrename.models.Doc;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class DocumentFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE = 200;
    public Context context;
    Document_folder Document_folder;
    RecyclerView imageRecycler;
    ArrayList<Doc> allpictures;
    ArrayList<Doc> selectedItem;
    ProgressBar load;
    ImageView btnclose;

    public DocumentFragment(Context context, Document_folder Document_folder) {
        this.context = context;
        this.Document_folder = Document_folder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documents, container, false);
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE);
        }
        allpictures = new ArrayList<>();
        imageRecycler = view.findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(context));
        imageRecycler.hasFixedSize();
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


    public static String math(float f) {
        DecimalFormat format = new DecimalFormat("0.##");
        return format.format(f);
    }

    public void onClick(int position, String rename) {
        new LoadApplications().execute();
    }

    private Object getText() {
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        new LoadApplications().execute();
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
            allpictures = getPdfList();
            return null;
        }

        private ArrayList<Doc> getPdfList() {

            ArrayList<Doc> pdfList = new ArrayList<>();
            Uri collection;

            String[] projection = new String[]{MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATE_ADDED, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.MIME_TYPE,};
            String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";
            String selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?";
            final String mimeype1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
            String[] selectionArgs = new String[]{mimeype1};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
            } else {
                collection = MediaStore.Files.getContentUri("external");
            }

            try (Cursor cursor = context.getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
                assert cursor != null;

                if (cursor.moveToFirst()) {
                    int columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int columnName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    do {
                        Doc doc = new Doc();
                        doc.setName((cursor.getString(columnName)));
                        doc.setPath((cursor.getString(columnData)));
                        pdfList.add(doc);
                        Log.d("TAG", "getPdf: " + cursor.getString(columnName));


                    } while (cursor.moveToNext());
                }
            }

            final String mimeype2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
            selectionArgs = new String[]{mimeype2};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
            } else {
                collection = MediaStore.Files.getContentUri("external");
            }


            try (Cursor cursor = context.getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
                assert cursor != null;

                if (cursor.moveToFirst()) {
                    int columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int columnName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    do {

                        Doc doc = new Doc();
                        doc.setName((cursor.getString(columnName)));
                        doc.setPath((cursor.getString(columnData)));
                        pdfList.add(doc);

                        Log.d("TAG", "getPdf: " + cursor.getString(columnName));
                    } while (cursor.moveToNext());
                }
            }

            final String mimeype3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
            selectionArgs = new String[]{mimeype3};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
            } else {
                collection = MediaStore.Files.getContentUri("external");
            }


            try (Cursor cursor = context.getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
                assert cursor != null;

                if (cursor.moveToFirst()) {
                    int columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int columnName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    do {

                        Doc doc = new Doc();
                        doc.setName((cursor.getString(columnName)));
                        doc.setPath((cursor.getString(columnData)));
                        pdfList.add(doc);
                        Log.d("TAG", "getPdf: " + cursor.getString(columnName));
                        //you can get your pdf files
                    } while (cursor.moveToNext());
                }
            }
            final String mimeype4 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
            selectionArgs = new String[]{mimeype4};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
            } else {
                collection = MediaStore.Files.getContentUri("external");
            }


            try (Cursor cursor = context.getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
                assert cursor != null;

                if (cursor.moveToFirst()) {
                    int columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int columnName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    do {

                        Doc doc = new Doc();
                        doc.setName((cursor.getString(columnName)));
                        doc.setPath((cursor.getString(columnData)));
                        pdfList.add(doc);

                    } while (cursor.moveToNext());
                }
            }

            final String mimeype6 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("zip");
            selectionArgs = new String[]{mimeype6};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
            } else {
                collection = MediaStore.Files.getContentUri("external");
            }


            try (Cursor cursor = context.getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
                assert cursor != null;

                if (cursor.moveToFirst()) {
                    int columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int columnName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    do {

                        Doc doc = new Doc();
                        doc.setName((cursor.getString(columnName)));
                        doc.setPath((cursor.getString(columnData)));
                        pdfList.add(doc);
                        Log.d("TAG", "getPdf: " + cursor.getString(columnName));
                        //you can get your pdf files
                    } while (cursor.moveToNext());
                }
            }
            final String mimeype7 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("apk");
            selectionArgs = new String[]{mimeype4};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
            } else {
                collection = MediaStore.Files.getContentUri("external");
            }


            try (Cursor cursor = context.getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
                assert cursor != null;

                if (cursor.moveToFirst()) {
                    int columnData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int columnName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    do {

                        Doc doc = new Doc();
                        doc.setName((cursor.getString(columnName)));
                        doc.setPath((cursor.getString(columnData)));
                        pdfList.add(doc);

                    } while (cursor.moveToNext());
                }
            }

            return pdfList;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Collections.reverse(allpictures);
            imageRecycler.setAdapter(new DocumentAdapter(allpictures, getContext(), DocumentFragment.this));
            load.setVisibility(View.GONE);
            progress.dismiss();
            super.onPostExecute(aVoid);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(requireContext(), "Permission denied to access manage all file ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
        startActivity(intent);
    }

    private FragmentManager getSupportFragmentManager() {
        return null;}

    }
