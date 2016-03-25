package com.yjchang.weatherman;

import android.util.Log;

import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import org.androidannotations.annotations.EBean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yjchang on 3/25/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class WeatherModel {

    private static final String TAG = "WeatherModel";

    public WeatherModel() {
        ForecastApi.create("95eee69eb033b8558f61643ffadff21f");
    }

    public void testAPI() {
        RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        request.setLat("32.00");
        request.setLng("-81.00");
        request.setUnits(Request.Units.SI);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);
        request.removeExcludeBlock(Request.Block.CURRENTLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.d(TAG, "Temp: " + weatherResponse.getCurrently().getTemperature());
                Log.d(TAG, "Summary: " + weatherResponse.getCurrently().getSummary());
                Log.d(TAG, "Hourly Sum: " + weatherResponse.getHourly().getSummary());
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
                Log.d(TAG, retrofitError.toString());
            }
        });
    }
}
