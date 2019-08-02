package com.unikom.islamedia.api;

import com.unikom.islamedia.model.ResBeritaModel;
import com.unikom.islamedia.model.ResCuaca;
import com.unikom.islamedia.model.ResLokasiModel;
import com.unikom.islamedia.model.ResMasjidModel;
import com.unikom.islamedia.model.ResUstadzModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("andro_get_masjid.php")
    Call<ResMasjidModel> getMasjid();

    @GET("get_News.php")
    Call<ResBeritaModel> getBerita();

    @GET("andro_get_ustadz.php")
    Call<ResUstadzModel> getUstadz();

    @FormUrlEncoded
    @POST("cari_Masjid.php")
    Call<ResMasjidModel> getSearchMasjid(@Field("searchQuery") String searchQuery);

    @FormUrlEncoded
    @POST("andro_cari_ustadz.php")
    Call<ResUstadzModel> getSearchUstadz(@Field("searchQuery") String searchQuery);

    @FormUrlEncoded
    @POST("andro_get_lokasi.php")
    Call<ResLokasiModel> getLokasiTerdekat(@Field("latitude") Double latitude, @Field("longitude") Double longitude);

    @GET("2.5/weather")
    Call<ResCuaca> getCuaca(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("appid") String appid
    );
}
