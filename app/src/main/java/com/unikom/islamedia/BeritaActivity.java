package com.unikom.islamedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.unikom.islamedia.adapter.BeritaAdapter;
import com.unikom.islamedia.api.ApiEndPoint;
import com.unikom.islamedia.api.ApiService;
import com.unikom.islamedia.model.BeritaModel;
import com.unikom.islamedia.model.ResBeritaModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeritaActivity extends AppCompatActivity implements BeritaAdapter.OnItemClickListener{

    GridLayoutManager gridLayoutManager;
    private BeritaAdapter beritAdapter;
    private List<BeritaModel> mItems = new ArrayList<>();
    private RecyclerView rView;

    public static final String EXTRA_URL = "imageUrl";
    public static final String JUDUL_BERIRA = "judulBerita";
    public static final String NAMA_ADMIN = "namaAdmin";
    public static final String TANGGAL_POST = "tanggalPost";
    public static final String DES_BERITA = "descBerita";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);
        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rView =  findViewById(R.id.tempat_berita);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);


        loadDataBerita();

    }

    public void loadDataBerita() {
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResBeritaModel> call = api.getBerita();
        call.enqueue(new Callback<ResBeritaModel>() {
            @Override
            public void onResponse(Call<ResBeritaModel> call, Response<ResBeritaModel> response) {
                assert response.body() != null;
                String statusCode = response.body().getStatus_code();

                progressBar.setVisibility(View.GONE);

                if (statusCode.equals("200")) {
                    mItems = response.body().getResult();

                    beritAdapter = new BeritaAdapter(mItems, BeritaActivity.this, new BeritaAdapter.ClickListener() {
                        @Override
                        public void onPositionClicked(View v, int position) {
                            Intent detail_berita = new Intent(BeritaActivity.this, DetailBeritaActivity.class);
                            BeritaModel item = mItems.get(position);

                            detail_berita.putExtra(EXTRA_URL, item.getGambar());
                            detail_berita.putExtra(JUDUL_BERIRA, item.getJudul());
                            detail_berita.putExtra(NAMA_ADMIN, item.getNama_admin());
                            detail_berita.putExtra(TANGGAL_POST, item.getTanggal());
                            detail_berita.putExtra(DES_BERITA, item.getKet());

                            startActivity(detail_berita);
                        }
                    });
                    rView.setAdapter(beritAdapter);
                    beritAdapter.setOnitemListener(BeritaActivity.this);
                }else{
                    Toast.makeText(BeritaActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResBeritaModel> call, Throwable t) {

            }
        });
    }

    public void rowBack(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    @Override
    public void onItemListener(int position) {
        Intent detail_berita = new Intent(BeritaActivity.this, DetailBeritaActivity.class);
        BeritaModel item = mItems.get(position);

        detail_berita.putExtra(EXTRA_URL, item.getGambar());
        detail_berita.putExtra(JUDUL_BERIRA, item.getJudul());
        detail_berita.putExtra(NAMA_ADMIN, item.getNama_admin());
        detail_berita.putExtra(TANGGAL_POST, item.getTanggal());
        detail_berita.putExtra(DES_BERITA, item.getKet());

        startActivity(detail_berita);
    }
}
