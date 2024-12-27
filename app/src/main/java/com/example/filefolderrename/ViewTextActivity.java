package com.example.filefolderrename;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;

import com.example.filefolderrename.ClsGlobal.EncryptConfiguration;
import com.example.filefolderrename.ClsGlobal.Storage;
import com.example.filefolderrename.db.Helper;


/**
 * Created by sromku on June, 2017.
 */
public class ViewTextActivity extends AppCompatActivity {

    public final static String EXTRA_FILE_NAME = "name";
    public final static String EXTRA_FILE_PATH = "path";

    private final static String IVX = "abcdefghijklmnop";
    private final static String SECRET_KEY = "secret1234567890";
    private final static byte[] SALT = "0000111100001111".getBytes();

    private TextView mContentView;
    private String mPath;
    private Storage mStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra(EXTRA_FILE_NAME);
        mPath = getIntent().getStringExtra(EXTRA_FILE_PATH);

        setContentView(R.layout.activity_view_text_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerArrowDrawable drawerDrawable = new DrawerArrowDrawable(this);
        drawerDrawable.setColor(getResources().getColor(android.R.color.white));
        drawerDrawable.setProgress(1f);
        toolbar.setNavigationIcon(drawerDrawable);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setHomeButtonEnabled(true);

        mContentView = (TextView) findViewById(R.id.content);
        mStorage = new Storage(this);
        byte[] bytes = mStorage.readFile(mPath);
        mContentView.setText(new String(bytes));
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.text_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.decrypt:
                mStorage.setEncryptConfiguration(new EncryptConfiguration.Builder()
                        .setEncryptContent(IVX, SECRET_KEY, SALT)
                        .build());
                byte[] bytes = mStorage.readFile(mPath);
                if (bytes != null) {
                    mContentView.setText(new String(bytes));
                } else {
                    Helper.showSnackbar("Failed to decrypt", mContentView);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
