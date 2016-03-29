package com.yjchang.weatherman;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;

import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yjchang on 3/25/16.
 */
@EBean
public class MainPresenterImpl implements MainPresenter {

    private View view;

    @Bean(WeatherModelImpl.class)
    WeatherModel model;

    @RootContext
    Context mContext;

    @AfterInject
    public void initObject() {
        model.setPresenter(this);
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Background
    @Override
    public void onRefresh() {
        model.fetchForecast(model.getLocation(), new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                updateForecast(weatherResponse);
                view.makeSnackbar("Updated", "Action", null);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyError("Failed to fetch weather forecast");
            }
        });
    }

    @Override
    public void updateForecast(WeatherResponse weatherResponse) {
        view.setIcon(getWeatherIconResId(weatherResponse.getCurrently().getIcon()));

        String forecast = "";
        forecast += model.getLocality(model.getLocation()) + "\n\n";
        forecast += weatherResponse.getCurrently().getSummary() + "\n\n";
        forecast += weatherResponse.getHourly().getSummary() + "\n\n";
        forecast += weatherResponse.getDaily().getSummary() + "\n\n";

        forecast += "--------------------\n\n";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (DataPoint dp : weatherResponse.getHourly().getData().subList(1,6)) {
            String time = sdf.format(new Date(dp.getTime() * 1000));
            forecast += time + " : " + "\n";
            forecast += "\t" + dp.getSummary() + "\n";
            forecast += "\ttemp: " + dp.getTemperature() + "\n";
            forecast += "\ticon: " + dp.getIcon() + "\n";
            forecast += "\train%: " + dp.getPrecipProbability() + "\n\n";
        }
        forecast += "--------------------\n\n";
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (DataPoint dp : weatherResponse.getDaily().getData().subList(1,4)) {
            String time = sdf.format(new Date(dp.getTime() * 1000));
            forecast += time + " : " + "\n";
            forecast += "\t" + dp.getSummary() + "\n";
            forecast += "\tmax-temp: " + dp.getTemperatureMax() + "\n";
            forecast += "\tmin-temp: " + dp.getTemperatureMin() + "\n";
            forecast += "\ticon: " + dp.getIcon() + "\n";
            forecast += "\train%: " + dp.getPrecipProbability() + "\n\n";
        }
        view.setWeatherText(forecast);
    }

    @Override
    public void notifyError(String message) {
        view.makeSnackbar(message, "Retry", new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                onRefresh();
            }
        });
    }

    private int getWeatherIconResId(String icon) {
        switch(icon) {
            case "clear-day":
                return R.drawable.clear_day;
            case "rain":
                return R.drawable.rain;
            case "snow":
            case "sleet":
                return R.drawable.snow;
            case "wind":
                return R.drawable.wind;
            case "fog":
                return R.drawable.fog;
            case "cloudy":
            case "partly-cloudy-day":
                return R.drawable.partly_cloudy_day;
            case "clear-night":
            case "partly-cloudy-night":
                return R.drawable.partly_cloudy_night;
            default:
                return R.drawable.wind;
        }
    }
}
