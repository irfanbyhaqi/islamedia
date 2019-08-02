package com.unikom.islamedia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.unikom.islamedia.adapter.MasjidAdapter;
import com.unikom.islamedia.api.ApiEndPoint;
import com.unikom.islamedia.api.ApiService;
import com.unikom.islamedia.model.MasjidModel;
import com.unikom.islamedia.model.ResMasjidModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasjidActivity extends AppCompatActivity implements MasjidAdapter.OnItemClickListener {

    private MasjidAdapter masjidAdapter;
    private List<MasjidModel> mItems = new ArrayList<>();
    private RecyclerView rView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.cari_masjid)
    EditText txt_cari_masjid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masjid);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MasjidActivity.this, 1, GridLayoutManager.VERTICAL, false);
        rView = findViewById(R.id.rv_masjid);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);

        txt_cari_masjid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        String isiSearch = txt_cari_masjid.getText().toString();

                        if(isiSearch.matches("")){

                            Toast.makeText(MasjidActivity.this, "Apa yang kamu cari?", Toast.LENGTH_SHORT).show();
                        }else{
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            progressBar.setVisibility(View.VISIBLE);

                            loadSearchMasjid(isiSearch);
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        txt_cari_masjid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String isiSearch = txt_cari_masjid.getText().toString();

                if(isiSearch.matches("")){
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    progressBar.setVisibility(View.VISIBLE);
                                    loadDataMasjid();
                                }
                            },
                            500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loadDataMasjid();
    }


    public void loadDataMasjid(){
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResMasjidModel> call = api.getMasjid();
        call.enqueue(new Callback<ResMasjidModel>() {
            @Override
            public void onResponse(Call<ResMasjidModel> call, Response<ResMasjidModel> response) {
                assert response.body() != null;
                String statusCode = response.body().getStatus_code();

                progressBar.setVisibility(View.GONE);
                if (statusCode.equals("200")) {
                    mItems = response.body().getResult();
                    masjidAdapter = new MasjidAdapter(mItems, MasjidActivity.this);
                    rView.setAdapter(masjidAdapter);
                    masjidAdapter.setOnitemListener(MasjidActivity.this);
                }else{
                    Toast.makeText(MasjidActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResMasjidModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MasjidActivity.this, "Aduh, coba cek akses internet mu ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void loadSearchMasjid(String searchQuery){
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResMasjidModel> call = api.getSearchMasjid(searchQuery);
        call.enqueue(new Callback<ResMasjidModel>() {
            @Override
            public void onResponse(Call<ResMasjidModel> call, Response<ResMasjidModel> response) {
                assert response.body() != null;
                String statusCode = response.body().getStatus_code();

                progressBar.setVisibility(View.GONE);
                if (statusCode.equals("200")) {
                    mItems = response.body().getResult();
                    masjidAdapter = new MasjidAdapter(mItems, MasjidActivity.this);
                    rView.setAdapter(masjidAdapter);
                    masjidAdapter.setOnitemListener(MasjidActivity.this);
                }else{
                    Toast.makeText(MasjidActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResMasjidModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MasjidActivity.this, "Aduh, coba cek akses internet mu ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void rowBack(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemListener(int position) {
        MasjidModel clickedItem = mItems.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query="+clickedItem.getNama_masjid()+""));
        startActivity(intent);

    }
}
