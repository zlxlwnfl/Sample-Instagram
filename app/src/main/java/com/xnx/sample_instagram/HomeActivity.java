package com.xnx.sample_instagram;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    private static final int POSTING_GALLERY_CODE = 10;
    private static final int USERINFO_GALLERY_CODE = 11;
    private static final int USERINFO_USERUPDATE_CODE = 12;
    private static final int USERINFO_USERUPDATE_CLEAR_CODE = 13;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    FragmentManager fragmentManager;

    HomeFragment homeFragment;
    PostingFragment postingFragment;
    UserInfoFragment userInfoFragment;
    UserUpdateFragment userUpdateFragment;

    private ImageView imageView;
    private String imagePath;
    public Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment = new HomeFragment();
        postingFragment = new PostingFragment();
        userInfoFragment = new UserInfoFragment();
        userUpdateFragment = new UserUpdateFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, homeFragment).commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.navigation_writing:
                        fragmentManager.beginTransaction().replace(R.id.container, postingFragment).commit();
                        return true;
                    case R.id.navigation_userInfo:
                        fragmentManager.beginTransaction().replace(R.id.container, userInfoFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bundle = new Bundle();

        switch(requestCode) {
            case POSTING_GALLERY_CODE:
                if(data != null) {
                    imagePath = getPath(data.getData());
                    File f = new File(imagePath);

                    imageView = (ImageView) findViewById(R.id.imageView_writing);
                    imageView.setImageURI(Uri.fromFile(f));

                    bundle.putString("imagePath", imagePath);
                    postingFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.container, postingFragment).commit();
                }
                break;
            case USERINFO_GALLERY_CODE:
                if(data != null) {
                    imagePath = getPath(data.getData());
                    File f = new File(imagePath);

                    imageView = (ImageView) findViewById(R.id.userImageView);
                    imageView.setImageURI(Uri.fromFile(f));

                    bundle.putString("imagePath", imagePath);
                    userInfoFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.container, userInfoFragment).commit();
                }
                break;
            case USERINFO_USERUPDATE_CODE:
                Log.e("유저소개글", "화면 전환 전");
                fragmentManager.beginTransaction().replace(R.id.container, userUpdateFragment).commit();
                break;
            case USERINFO_USERUPDATE_CLEAR_CODE:
                fragmentManager.beginTransaction().replace(R.id.container, userInfoFragment).commit();
                break;
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

}
