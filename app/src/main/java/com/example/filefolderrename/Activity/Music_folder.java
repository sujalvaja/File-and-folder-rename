package com.example.filefolderrename.Activity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.multidex.BuildConfig;
import androidx.viewpager.widget.ViewPager;
/*
import com.example.filefolderrename.Fragments.AppFragment;*/
import com.example.filefolderrename.CommonFunction;
import com.example.filefolderrename.Fragments.AppFragment;
import com.example.filefolderrename.Fragments.AudiosFragment;
import com.example.filefolderrename.Fragments.DocumentFragment;
import com.example.filefolderrename.Fragments.ImageFragment;

import com.example.filefolderrename.Fragments.VideoFragment;
import com.example.filefolderrename.R;
import com.example.filefolderrename.models.Filedata;
import com.example.filefolderrename.models.PictureFacer;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.ak1.BubbleTabBar;
import io.ak1.OnBubbleClickListener;

public class Music_folder extends AppCompatActivity implements View.OnClickListener {
    public static final int MAIN_CONTAINER = 200;
    private ViewPager viewPager;
    BubbleTabBar bubbleTabBar;
    CommonFunction commonFunction;
    LinearLayout okbutton;
    TextView okbutton_text;
    LinearLayout loader;

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
        }, 1000);

    }

    private void bubbleTabBarsetup() {
        bubbleTabBar = findViewById(R.id.bubbleTabBar);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bubbleTabBar.setSelected(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int id) {
                switch (id) {
                    case R.id.images:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.videos:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.audios:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.documents:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.apps:
                        viewPager.setCurrentItem(4);
                        break;
                    case R.id.files:
                        viewPager.setCurrentItem(5);
                        break;
                    case R.id.archives:
                        viewPager.setCurrentItem(6);
                        break;
                }
            }
        });
        viewPager.setOffscreenPageLimit(7);
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
         adapter.addFragment(new AudiosFragment(this, this, mediaselectedlist), "Audios");
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

    public ArrayList<Uri> itemList = new ArrayList<>();
    private final List<String> apkList = new ArrayList<>();
    public ArrayList<PictureFacer> mediaselectedlist = new ArrayList<>();
    public ArrayList<ApplicationInfo> appselectedlist = new ArrayList<>();


    public void click(PictureFacer selectedItem, boolean isadd) {
        File file = new File(selectedItem.getPicturePath());
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        if (isadd) {
            itemList.add(uri);
            mediaselectedlist.add(selectedItem);
            okbutton_text.setText("Send Files (" + itemList.size() + ")");
        } else {
            itemList.remove(uri);
            mediaselectedlist.remove(selectedItem);
            okbutton_text.setText("Send Files (" + itemList.size() + ")");
        }
        if (itemList.isEmpty()) {
            okbutton.setVisibility(View.GONE);
        } else {
            okbutton.setVisibility(View.VISIBLE);
        }
    }
    public void click(Filedata selectedItem, boolean isadd) {
        Uri uri = selectedItem.furi;
        if (isadd) {
            itemList.add(uri);
            okbutton_text.setText("Send Files (" + itemList.size() + ")");
        } else {
            itemList.remove(uri);
            okbutton_text.setText("Send Files (" + itemList.size() + ")");
        }
        btnvisible();
    }

    private void btnvisible() {
        if (itemList.isEmpty()) {
            okbutton.setVisibility(View.GONE);
        } else {
            okbutton.setVisibility(View.VISIBLE);
        }
    }

    public void click(ApplicationInfo selectedItem, boolean isadd) {
        try {
            File originalApk = new File(selectedItem.sourceDir);
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            tempFile = new File(tempFile.getPath() + "/" + commonFunction.GetAppName(selectedItem.packageName.replace(" ", ""), this) + ".apk");
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            Uri uri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider", tempFile);
            in.close();
            out.close();
            if (isadd) {
                itemList.add(uri);
                appselectedlist.add(selectedItem);
                okbutton_text.setText("Send Files (" + itemList.size() + ")");
            } else {
                itemList.remove(uri);
                appselectedlist.remove(selectedItem);
                okbutton_text.setText("Send Files (" + itemList.size() + ")");
            }
        } catch (Exception e) {
            Log.e("Tag", "Error -- " + e.getMessage());
        }
        btnvisible();
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
}