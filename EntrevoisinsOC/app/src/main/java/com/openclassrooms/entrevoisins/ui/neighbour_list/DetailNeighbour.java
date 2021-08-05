package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.Objects;

public class DetailNeighbour extends AppCompatActivity {

    private ImageView ivDnAvatar;
    private TextView tvDnHname;
    private FloatingActionButton ivDnIsfav;
    private TextView tvDnName;
    private TextView tvDnAddress;
    private TextView tvDnPhoneNumber;
    private TextView tvDnFacebook;
    private TextView tvDnAbout;

    private NeighbourApiService mApiService;
    protected Neighbour mNeighbour;
    String neighbourfb;
    public static String NEIGHBOUR = "NEIGHBOUR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);

        mNeighbour = (Neighbour) getIntent().getSerializableExtra(NEIGHBOUR);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // initialise le boutton retour de l'actionbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mApiService = DI.getNeighbourApiService();
        initView();
        setFavoriteImage();
        setUserInfos();
        ivDnIsfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavorite();
            }
        });
    }

    private void setFavorite() {

        if (mApiService.isFavorite(mNeighbour)){
            mApiService.removeFavoriteNeighbour(mNeighbour);
            Toast.makeText(this,"Supprimé des favoris", Toast.LENGTH_SHORT).show();
        }
        else{
            mApiService.addNeighbourFavorite(mNeighbour);
            Toast.makeText(this,"Ajoutés aux favoris", Toast.LENGTH_SHORT).show();
        }
        setFavoriteImage();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setFavoriteImage() {
        if (mApiService.isFavorite(mNeighbour)){
            ivDnIsfav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white_24dp));
        }
        else{
            ivDnIsfav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
        }
    }

    private void setUserInfos() {

        // retrouver les infos du user avec l'intent
        String neighbourName = getIntent().getStringExtra("neighbourName");
        String neighbourAvatarUrl = getIntent().getStringExtra("neighbourAvatar");
        String neighbourAddress = getIntent().getStringExtra("neighbourAddress");
        String neighbourPhoneNumber = getIntent().getStringExtra("neighbourPhoneNumber");
        String neighbourDescription = getIntent().getStringExtra("neighbourDescription");

        //
        Glide.with(this)
                .load(neighbourAvatarUrl)
                .into(ivDnAvatar);
        tvDnHname.setText(neighbourName);
        tvDnName.setText(neighbourName);
        tvDnAddress.setText(neighbourAddress);
        tvDnPhoneNumber.setText(neighbourPhoneNumber);
        tvDnAbout.setText(neighbourDescription);

        neighbourfb = getString(R.string.fburlhead) + neighbourName;
        tvDnFacebook.setText(neighbourfb);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void initView() {
        ivDnAvatar = findViewById(R.id.iv_dn_avatar);
        tvDnHname = findViewById(R.id.tv_dn_hname);
        ivDnIsfav = findViewById(R.id.iv_dn_isfav);
        tvDnName = findViewById(R.id.tv_dn_name);
        tvDnAddress = findViewById(R.id.tv_dn_address);
        tvDnPhoneNumber = findViewById(R.id.tv_dn_phone_number);
        tvDnFacebook = findViewById(R.id.tv_dn_facebook);
        tvDnAbout = findViewById(R.id.tv_dn_about);
    }
}