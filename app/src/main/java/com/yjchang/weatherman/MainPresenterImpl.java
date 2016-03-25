package com.yjchang.weatherman;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by yjchang on 3/25/16.
 */
@EBean
public class MainPresenterImpl implements MainPresenter {

    private View view;

    @Bean(WeatherModel.class)
    WeatherModel model;

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Background
    @Override
    public void onRefresh() {
        view.setWeather("Hello");
        model.testAPI();
    }

}
