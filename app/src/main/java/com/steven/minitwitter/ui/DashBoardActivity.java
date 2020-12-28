package com.steven.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.steven.minitwitter.R;
import com.steven.minitwitter.common.Constante;
import com.steven.minitwitter.common.SharedPreferencesManager;
import com.steven.minitwitter.data.PerfilViewModel;
import com.steven.minitwitter.ui.perfil.PerfilFragment;
import com.steven.minitwitter.ui.tweets.NuevoTweetDialogFragment;
import com.steven.minitwitter.ui.tweets.TweetListFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class DashBoardActivity extends AppCompatActivity implements PermissionListener {
    FloatingActionButton fab;
    ImageView imgUsuario;
    PerfilViewModel perfilViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            item -> {
                Fragment f = null;

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        f = TweetListFragment.newInstance(Constante.TWEET_LIST_ALL);
                        fab.show();
                        break;
                    case R.id.navigation_tweets_like:
                        f = TweetListFragment.newInstance(Constante.TWEET_LIST_FAV);
                        fab.hide();
                        break;
                    case R.id.navigation_perfil:
                        f = new PerfilFragment();
                        fab.hide();
                        break;
                }

                if (f != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContenedor, f)
                            .commit();
                    return true;
                }

                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        fab = findViewById(R.id.fab);
        imgUsuario = findViewById(R.id.imgFoto);

        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContenedor, TweetListFragment.newInstance(Constante.TWEET_LIST_ALL))
                .commit();

        fab.setOnClickListener(view -> {
            NuevoTweetDialogFragment dialogFragment = new NuevoTweetDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "NuevoTweetDialogFragment");
        });

        String foto = SharedPreferencesManager.getStringValue(Constante.PREF_FOTOURL);
        if (!foto.isEmpty()) {
            Glide.with(this)
                    .load(Constante.MINITWITTER_FILE_URL + foto)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(imgUsuario);
        }

        perfilViewModel.fotoUsuario.observe(this, s ->
                Glide.with(DashBoardActivity.this)
                .load(Constante.MINITWITTER_FILE_URL + s)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .skipMemoryCache(true)
                .into(imgUsuario));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constante.SELECT_PHOTO_GALLERY) {
                if (data != null) {
                    Uri imagen = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imagen, filePath, null
                    , null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int imagenIndex = cursor.getColumnIndex(filePath[0]);
                        String fotoPath = cursor.getString(imagenIndex);
                        perfilViewModel.subirFoto(fotoPath);
                        cursor.close();
                    }
                }
            }
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constante.SELECT_PHOTO_GALLERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}