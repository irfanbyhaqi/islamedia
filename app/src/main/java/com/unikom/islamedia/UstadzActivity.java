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

import com.unikom.islamedia.adapter.UstadzAdapter;
import com.unikom.islamedia.api.ApiEndPoint;
import com.unikom.islamedia.api.ApiService;
import com.unikom.islamedia.model.ResUstadzModel;
import com.unikom.islamedia.model.UstadzModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UstadzActivity extends AppCompatActivity implements UstadzAdapter.OnItemClickListener {

    GridLayoutManager gridLayoutManager;
    private UstadzAdapter ustadzAdapter;
    private List<UstadzModel> mItems = new ArrayList<>();
    private RecyclerView rView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.cari_ustadz)
    EditText txt_cari_ustadz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustadz);
        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(UstadzActivity.this, 1, GridLayoutManager.VERTICAL, false);
        rView = findViewById(R.id.tempat_ustadz);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);


        loadDataUstadz();

        txt_cari_ustadz.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        String isiSearch = txt_cari_ustadz.getText().toString();

                        if(isiSearch.matches("")){

                            Toast.makeText(UstadzActivity.this, "Apa yang kamu cari?", Toast.LENGTH_SHORT).show();
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

        txt_cari_ustadz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String isiSearch = txt_cari_ustadz.getText().toString();

                if(isiSearch.matches("")){
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    progressBar.setVisibility(View.VISIBLE);
                                    loadDataUstadz();
                                }
                            },
                            500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void loadDataUstadz(){
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResUstadzModel> call = api.getUstadz();
        call.enqueue(new Callback<ResUstadzModel>() {
            @Override
            public void onResponse(Call<ResUstadzModel> call, Response<ResUstadzModel> response) {
                assert response.body() != null;
                String statusCode = response.body().getStatus_code();

                progressBar.setVisibility(View.GONE);
                if (statusCode.equals("200")) {
                    mItems = response.body().getResult();
                    ustadzAdapter = new UstadzAdapter(mItems, UstadzActivity.this, new UstadzAdapter.ClickListener() {
                        @Override
                        public void onPositionClicked(View v, int position) {
                            String phone = mItems.get(position).getNo_kontak_ustadz().trim();
                            String formatNumber = phone.replaceFirst("0","+62");
                            String url = "https://api.whatsapp.com/send?phone="+formatNumber;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                    rView.setAdapter(ustadzAdapter);
                    ustadzAdapter.setOnitemListener(UstadzActivity.this);
                }else{
                    Toast.makeText(UstadzActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResUstadzModel> call, Throwable t) {

            }
        });

    }

    public void loadSearchMasjid(String data){
        ApiService api = ApiEndPoint.getClient().create(ApiService.class);
        Call<ResUstadzModel> call = api.getSearchUstadz(data);
        call.enqueue(new Callback<ResUstadzModel>() {
            @Override
            public void onResponse(Call<ResUstadzModel> call, Response<ResUstadzModel> response) {
                assert response.body() != null;
                String statusCode = response.body().getStatus_code();
                progressBar.setVisibility(View.GONE);

                if (statusCode.equals("200")) {
                    mItems = response.body().getResult();
                    ustadzAdapter = new UstadzAdapter(mItems, UstadzActivity.this, new UstadzAdapter.ClickListener() {
                        @Override
                        public void onPositionClicked(View v, int position) {
                            String phone = mItems.get(position).getNo_kontak_ustadz().trim();
                            String formatNumber = phone.replaceFirst("0","+62");
                            String url = "https://api.whatsapp.com/send?phone="+formatNumber;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                    rView.setAdapter(ustadzAdapter);
                    ustadzAdapter.setOnitemListener(UstadzActivity.this);
                }else{
                    Toast.makeText(UstadzActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResUstadzModel> call, Throwable t) {

            }
        });
    }


    public void rowBack(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemListener(int position) {

    }
}
