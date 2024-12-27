package com.example.filefolderrename.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.filefolderrename.Activity.Apk_folder;
import com.example.filefolderrename.Activity.MainActivity;
import com.example.filefolderrename.Adapter.AppAdapter;
import com.example.filefolderrename.CommonFunction;
import com.example.filefolderrename.MarginDecoration;
import com.example.filefolderrename.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppFragment extends Fragment {

    public Context context;
    Apk_folder Apk_folder;
    CommonFunction commonFunction;
    AppFragment AppFragment;

    public AppFragment(Context context, Apk_folder Apk_folder,
                       List<ApplicationInfo> selectedItem) {
        this.context = context;
        this.Apk_folder = Apk_folder;
        this.selectedItem = selectedItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RecyclerView imageRecycler;
    List<ApplicationInfo> selectedItem = new ArrayList<>();
    private static ArrayList<File> datalist = new ArrayList<>();
    ProgressBar load;
    ImageView btnclose;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app, container, false);
        commonFunction = new CommonFunction();
        datalist = new ArrayList<>();
        imageRecycler = view.findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(context));
        imageRecycler.hasFixedSize();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        imageRecycler.setLayoutManager(layoutManager);
        imageRecycler.setItemAnimator(new DefaultItemAnimator());
        load = view.findViewById(R.id.loader);

        if (datalist.isEmpty()) {
            imageRecycler.setAdapter(new AppAdapter(datalist, selectedItem, getContext(), AppFragment.this));
            load.setVisibility(View.GONE);
        }
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

    class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null, "Loading data...");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            datalist = new ArrayList<>();
            datalist = Search_Dir(Environment.getExternalStorageDirectory());
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Collections.sort(datalist);
            Collections.reverse(datalist);
            imageRecycler.setAdapter(new AppAdapter(datalist, selectedItem, getContext(), AppFragment.this));
            load.setVisibility(View.GONE);
            progress.dismiss();
            super.onPostExecute(aVoid);
        }

    }

    public void onBackPressed() {
        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
        startActivity(intent);

    }
    private ArrayList<File> Search_Dir(File dir) {
        File[] filelist = dir.listFiles();
        if (filelist != null) {
            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].isDirectory()) {
                    Log.e("Tag", "name --- " + filelist[i].getName());
                    Search_Dir(filelist[i]);
                } else {
                    if (filelist[i].getName().endsWith(".apk")) {

                        datalist.add(filelist[i]);
                    }
                }
            }
        }
        return datalist;
    }


}