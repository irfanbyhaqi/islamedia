package com.unikom.islamedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailBeritaActivity extends AppCompatActivity {

    @BindView(R.id.foto_berita)
    ImageView foto_berita;
    @BindView(R.id.admin)
    TextView admin;
    @BindView(R.id.tanggal)
    TextView tanggal;
    @BindView(R.id.judul_berita)
    TextView judul_berita;
    @BindView(R.id.des_detail)
    TextView des_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        String imgUrl = intent.getStringExtra(BeritaActivity.EXTRA_URL);
        String judul = intent.getStringExtra(BeritaActivity.JUDUL_BERIRA);
        String nama_admin = intent.getStringExtra(BeritaActivity.NAMA_ADMIN);
        String tanggal_post = intent.getStringExtra(BeritaActivity.TANGGAL_POST);
        String deskripsi = intent.getStringExtra(BeritaActivity.DES_BERITA);

        Glide.with(DetailBeritaActivity.this).load(imgUrl).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(foto_berita);

        admin.setText(nama_admin);
        tanggal.setText(tanggal_post);
        judul_berita.setText(judul);
        des_detail.setText(deskripsi);

    }
}
