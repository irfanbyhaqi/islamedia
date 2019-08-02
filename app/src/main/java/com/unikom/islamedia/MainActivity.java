package com.unikom.islamedia;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azan.Azan;
import com.azan.AzanTimes;
import com.azan.Method;
import com.azan.astrologicalCalc.Location;
import com.azan.astrologicalCalc.SimpleDate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.unikom.islamedia.api.ApiEndPoint;
import com.unikom.islamedia.api.ApiService;
import com.unikom.islamedia.model.CuacaModel;
import com.unikom.islamedia.model.IconCuaca;
import com.unikom.islamedia.model.ResCuaca;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtShubuh, txtDzuhur, txtAshar, txtMagrib, txtIsya, txtWaktu;

    private static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final String API_KEY_CUACA = "8121ad9c3d7331ed6088ef307a6e2137";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private LocationManager lm;
    public Double latitude, longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean connected = false;
    private LottieAlertDialog alertDialog;


    @BindView(R.id.txt_suhu)
    TextView txt_suhu;
    @BindView(R.id.gambar_cuaca)
    ImageView gambar_cuaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        txtShubuh = findViewById(R.id.txtShubuh);
        txtDzuhur = findViewById(R.id.txtDzuhur);
        txtAshar = findViewById(R.id.txtAshar);
        txtMagrib = findViewById(R.id.txtMagrib);
        txtIsya = findViewById(R.id.txtIsya);
        txtWaktu = findViewById(R.id.txtWaktu);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        lm = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        alertDialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING).setTitle("Tunggu Sebentar").setDescription("Sedang mencari lokasi").build();
//        alertDialog.setCancelable(false);

        cekAll();

    }

    private void setInterval(){
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                // do stuff then
                // can call h again after work!

                if(connected){
                    Toast.makeText(MainActivity.this, "internet is connected", Toast.LENGTH_SHORT).show();
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
                aktifSemuaFitur();
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Required Access Internet")
                        .setMessage("Wah enggak bisa akses internet nih")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setInterval();
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

    private void aktifSemuaFitur(){
        getAccess();
        fetLocation();
        time();
    }

    private boolean cekAksesInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
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
                                ActivityCompat.requestPermissions(MainActivity.this,
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
                    .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(android.location.Location location) {
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                waktuSholat();
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private boolean hasLocationPermission() {
        return EasyPermissions.hasPermissions(this, ACCESS_COARSE_LOCATION);
    }

    @AfterPermissionGranted(MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
    public void getAccess() {
        if (hasLocationPermission()) {
            alertDialog.show();
            getLocation();
        }else{
            EasyPermissions.requestPermissions(
                    this,
                    "Butuh akses lokasi kamu nih",
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION,
                    ACCESS_COARSE_LOCATION);
        }
    }

    public void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            EasyPermissions.requestPermissions(
                    this,
                    "Butuh akses lokasi kamu nih",
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION,
                    ACCESS_COARSE_LOCATION);
            return;
        }


        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {

                if (!String.valueOf(location.getLatitude()).equals("")) {
                    alertDialog.dismiss();
                    getCuaca(location.getLatitude(), location.getLongitude());
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("Status", String.valueOf(status));
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                alertDialog.dismiss();
                statusCheck();
            }
        };

        lm.requestLocationUpdates("gps",500, 0, locationListener);

    }


    public void statusCheck() {

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ignored) {}

        if(!gps_enabled && !network_enabled) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    public void clickMenu(View v) {
        Intent i = new Intent(this, MasjidActivity.class);
        startActivity(i);
    }

    public void clickMenuBerita(View v) {
        Intent i = new Intent(this, BeritaActivity.class);
        startActivity(i);
    }

    public void clickMenuLokasi(View v) {
        Intent i = new Intent(this, LokasiActivity.class);
        i.putExtra(LATITUDE, latitude);
        i.putExtra(LONGITUDE, longitude);
        startActivity(i);
    }

    public void clickMenuUstadz(View v) {
        Intent i = new Intent(this, UstadzActivity.class);
        startActivity(i);
    }

    //Ini Method Untuk Mendapatkan Waktu Adzan
    private void waktuSholat() {
        //Instansiasi Format Waktu dari API jadwal sholat
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDate calFormat = new SimpleDate(new GregorianCalendar());

        //Instansiasi Lokasi
        Location location = new Location(latitude, longitude, 7.0, 0);

        //Instansiasi API Jadwal Sholat
        Azan azan = new Azan(location, Method.Companion.getFIXED_ISHAA());
        AzanTimes jadwalSholat = azan.getPrayerTimes(calFormat);

        //Mengambil Waktu Sholat ke String
        String subuh = jadwalSholat.fajr().toString();
        String zuhur = jadwalSholat.thuhr().toString();
        String asar = jadwalSholat.assr().toString();
        String magrib = jadwalSholat.maghrib().toString();
        String isa = jadwalSholat.ishaa().toString();
        //Conversi Waktu Sholat ke Format Date

        try {
            Date shubuh = sdf.parse(subuh);
            Date dzuhur = sdf.parse(zuhur);
            Date ashar = sdf.parse(asar);
            Date maghrib = sdf.parse(magrib);
            Date isya = sdf.parse(isa);

            //Method Khusus Agar Jadwal Sama dengan Jadwal Kemenag Bandung
            long menit = 60000;
            long cursubuh = shubuh.getTime();
            long curdzuhur = dzuhur.getTime();
            long curasar = ashar.getTime();
            long curmagrib = maghrib.getTime();
            long curisa = isya.getTime();

            Date newShubuh = new Date(cursubuh);
            Date newDzuhur = new Date(curdzuhur + (3 * menit));
            Date newAshar = new Date(curasar + (2 * menit));
            Date newMagrib = new Date(curmagrib + (2 * menit));
            Date newIsha = new Date(curisa - (15 * menit));

            //Format hasil method khusus ke format jam biasa
            subuh = format.format(newShubuh);
            zuhur = format.format(newDzuhur);
            asar = format.format(newAshar);
            magrib = format.format(newMagrib);
            isa = format.format(newIsha);

            //Tampilkan Jadwal Sholat ke Layar
            txtShubuh.setText(subuh);
            txtDzuhur.setText(zuhur);
            txtAshar.setText(asar);
            txtMagrib.setText(magrib);
            txtIsya.setText(isa);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void time() {
        try {
            //Instansiasi Tanggal Hari Ini
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String today = sdf.format(new Date());

            //Instansiasi Waktu
            Date now = sdf.parse(today);
            Date pagi = sdf.parse("05:00");
            Date siang = sdf.parse("11:00");
            Date sore = sdf.parse("16:00");
            Date malam = sdf.parse("18:00");

            //Validasi Waktu
            if (now.after(pagi) && now.before(siang)) {
                txtWaktu.setText(R.string.selamat_pagi);
            } else if (now.after(siang) && now.before(sore)) {
                txtWaktu.setText(R.string.selamat_siang);
            } else if (now.after(sore) && now.before(malam)) {
                txtWaktu.setText(R.string.selamat_sore);
            } else {
                txtWaktu.setText(R.string.selamat_malam);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void getCuaca(Double lat, Double lon){
        ApiService api = ApiEndPoint.getCuaca().create(ApiService.class);

        Call<ResCuaca> call = api.getCuaca(lat, lon, API_KEY_CUACA);
        call.enqueue(new Callback<ResCuaca>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResCuaca> call, Response<ResCuaca> response) {
                assert response.body() != null;
                CuacaModel get_main = response.body().getMain();
                List<IconCuaca> icon = response.body().getWeather();
                String url = "http://openweathermap.org/img/w/" + icon.get(0).getIcon() + ".png";

                Log.i("Cuaca", url);

                Glide.with(MainActivity.this).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(gambar_cuaca);

                double celcius = get_main.getTemp() - 273.15;

                txt_suhu.setText(Math.round(celcius) + " C");
            }

            @Override
            public void onFailure(Call<ResCuaca> call, Throwable t) {

            }
        });

    }

}
