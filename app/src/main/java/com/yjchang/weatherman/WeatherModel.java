package com.yjchang.weatherman;

import com.google.android.gms.maps.model.LatLng;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import retrofit.Callback;

/**
 * Created by yjchang on 3/28/16.
 */
public interface WeatherModel {
    void setPresenter(MainPresenter mainPresenter);
    LatLng getLocation();
    void fetchForecast(LatLng location, Callback<WeatherResponse> weatherResponseCallback);
    String getLocality(LatLng location);
}
