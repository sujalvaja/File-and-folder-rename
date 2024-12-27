package com.example.filefolderrename.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.filefolderrename.CommonFunction;
import com.example.filefolderrename.Fragments.ImageFragment;
import com.example.filefolderrename.R;
import com.example.filefolderrename.models.PictureFacer;

import java.util.ArrayList;
import java.util.List;

import io.ak1.BubbleTabBar;

public class Image_folder extends AppCompatActivity implements View.OnClickListener {
    public static final int MAIN_CONTAINER = 200;
    public ArrayList<Uri> itemList = new ArrayList<>();
    public ArrayList<PictureFacer> mediaselectedlist = new ArrayList<>();
    BubbleTabBar bubbleTabBar;
    CommonFunction commonFunction;
    LinearLayout okbutton;
    TextView okbutton_text;
    LinearLayout loader;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senddata);
        initobj();
        loader = findViewById(R.id.loader);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bubbleTabBarsetup();
            }
        }, 0);

    }

    private void bubbleTabBarsetup() {
        bubbleTabBar = findViewById(R.id.bubbleTabBar);
        setupViewPager(viewPager);
        bubbleTabBar.addBubbleListener(id -> {
            switch (id) {
                case R.id.images:
                    viewPager.setCurrentItem(0);
                    break;

            }
        });
        viewPager.setOffscreenPageLimit(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void initobj() {

        okbutton = findViewById(R.id.okbutton);
        okbutton_text = findViewById(R.id.okbutton_text);
        viewPager = findViewById(R.id.viewpager);
        okbutton.setOnClickListener(this);
        commonFunction = new CommonFunction();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ImageFragment(this, this, mediaselectedlist), "Images");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.okbutton:
                if (!itemList.isEmpty()) {
                    senddataviabt();
                } else {
                    Toast.makeText(this, "Select some files..", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    void senddataviabt() {
        Log.e("Tag", "item size -- " + itemList.size());
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        sharingIntent.setType("*/*");
        sharingIntent.setComponent(new ComponentName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
        sharingIntent.putExtra(Intent.EXTRA_STREAM, itemList);
        startActivity(sharingIntent);
        finish();
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}