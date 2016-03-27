package com.yjchang.weatherman;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.VideoView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    @Bean(MainPresenterImpl.class)
    MainPresenter presenter;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.weather_anim)
    VideoView weatherVideo;

    @AfterInject
    void initObject() {
        setSupportActionBar(toolbar);
        presenter.setView(this);
    }

    @Click(R.id.fab)
    void onFabClick(View view) {
        presenter.onRefresh();
    }

    @UiThread
    @Override
    public void setWeather(String forecast) {
        Snackbar.make(this.findViewById(R.id.fab), forecast, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        weatherVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName()
                                           + "/" + R.raw.sunny));
        weatherVideo.start();
    }

}
