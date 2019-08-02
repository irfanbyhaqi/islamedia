package com.unikom.islamedia;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.unikom.islamedia.adapter.LokasiAdapter;
import com.unikom.islamedia.api.ApiEndPoint;
import com.unikom.islamedia.api.ApiService;
import com.unikom.islamedia.model.LokasiModel;
import com.unikom.islamedia.model.ResLokasiModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LokasiActivity extends AppCompatActivity {

    private Double latitude;
    private Double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean connected = false;
    private LokasiAdapter lokasiAdapter;
    private List<LokasiModel> mItem = new ArrayList<>();

    RecyclerView rView;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    GridLayoutManager gridLayoutManager;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.jml_masjid)
    TextView jml_masjid;
    @BindView(R.id.jml_ustadz)
    TextView jml_ustadz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);
        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(LokasiActivity.this, 1, GridLayoutManager.VERTICAL, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        rView = findViewById(R.id.tempat_lokasi);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);


        cekAll();

    }

    private void setInterval(){
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {


            @Override
            public void run()
            {


                if(connected){
                    Toast.makeText(LokasiActivity.this, "internet is connected", Toast.LENGTH_SHORT).show();
                    cekAll();
                }else{
                    if(!cekAksesInternet()){
                        h.postDelayed(this, 1000);
                    }else{
                        h.postDelayed(this, 1000);
                    }

                }
            }
        }, 1000); // 1 second delay (takes millis)
    }

    private void cekAll(){
        if(cekAksesInternet()){
            if(internetIsConnected()){
                fetLocation();

            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Required Access Internet")
                        .setMessage("Wah enggak bisa akses intent nih")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }

        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Required Internet Permission")
                    .setMessage("Islamedia butuh akses ke internet, pastikan intenet mu aktif")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setInterval();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void fetLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permisson to access the featured")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LokasiActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                loadMasjidTerdekay(latitude, longitude);
//                                Log.i("Lokasi", String.valueOf(latitude)+" "+String.valueOf(longitude));
                            }
                        }
                    });
        }
    }

    private boolean cekAksesInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

    private boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    private void loadMasjidTerdekay(Double lat, Double lon){
        ApiService api;
        api = ApiEndPoint.getClient().create(ApiService.class);

        Call<ResLokasiModel> call = api.getLokasiTerdekat(lat, lon);
        call.enqueue(new Callback<ResLokasiModel>() {
            @Override
            public void onResponse(Call<ResLokasiModel> call, Response<ResLokasiModel> response) {
                String status_code;
                assert response.body() != null;
                status_code = response.body().getStatus_code();

                progressBar.setVisibility(View.GONE);
                if(status_code.equals("200")){
                    jml_masjid.setText(response.body().getJumlah_masjid());
                    jml_ustadz.setText(response.body().getJumlah_ustadz());
                    mItem = response.body().getResult();
                    lokasiAdapter = new LokasiAdapter(mItem, LokasiActivity.this, new LokasiAdapter.ClickListener() {
                        @Override
                        public void onPositionClicked(View v, int position) {

                            Uri gmmIntentUri = Uri.parse("google.navigation:q="+mItem.get(position).getLat()+","+mItem.get(position).getLon()+"");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                    rView.setAdapter(lokasiAdapter);
                }else{
                    Toast.makeText(LokasiActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResLokasiModel> call, Throwable t) {

            }
        });
    }


    public void rowBack(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
