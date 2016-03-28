package com.yjchang.weatherman;

import android.graphics.drawable.AnimationDrawable;
import android.view.View.OnClickListener;

import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import java.util.List;

/**
 * Created by yjchang on 3/25/16.
 */
public interface MainPresenter {
    void setView(View view);

    void onRefresh();
    void notifyError(String message);
    void updateForecast(WeatherResponse weatherResponse);

    interface View {
        void setTemp(String temprature);
        void setIcon(int resid);
        void setHourlyForecast(List<DataPoint> hourlyForecast);
        void setDailyForecast(List<DataPoint> dailyForecast);
        void makeSnackbar(String message, String action, OnClickListener onClickListener);

        // TEMP
        void setWeatherText(String forecast);
    }
}
