package com.yjchang.weatherman;

/**
 * Created by yjchang on 3/25/16.
 */
public interface MainPresenter {
    void setView(View view);
    void onRefresh();

    interface View {
        void setWeather(String forecast);
    }
}
