package com.yjchang.weatherman;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.johnhiott.darkskyandroidlib.models.DataPoint;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    @Bean(MainPresenterImpl.class)
    MainPresenter presenter;

    @ViewById(R.id.weather_anim)
    ImageView weatherAnim;

    @ViewById(R.id.weather_info)
    TextView weatherText;

    @AfterInject
    void initObject() {
        presenter.setView(this);
    }

    @Override
    @UiThread
    public void setTemp(String temprature) {}

    @Override
    @UiThread
    public void setIcon(int resid) {
        weatherAnim.setBackgroundResource(resid);
        AnimationDrawable anim = (AnimationDrawable) weatherAnim.getBackground();
        anim.start();
    }

    @Override
    @UiThread
    public void setHourlyForecast(List<DataPoint> hourlyForecast) {}

    @Override
    @UiThread
    public void setDailyForecast(List<DataPoint> dailyForecast) {}

    @Override
    @UiThread
    public void makeSnackbar(String message, String action, View.OnClickListener onClickListener) {
        Snackbar.make(weatherText, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show();
    }

    @Click(R.id.refresh_button)
    void onButtonClick(View view) {
        presenter.onRefresh();
    }

    @UiThread
    public void setWeatherText(String forecast) {
        weatherText.setText(forecast);
    }

}
