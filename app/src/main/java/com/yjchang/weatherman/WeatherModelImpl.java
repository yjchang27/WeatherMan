package com.yjchang.weatherman;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.io.IOException;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yjchang on 3/25/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class WeatherModelImpl implements WeatherModel, GoogleApiClient.ConnectionCallbacks,
                                     GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "WeatherModel";
    private static final LatLng defaultLatLng = new LatLng(37.532600, 127.024612);

    private MainPresenter presenter;
    private GoogleApiClient mGoogleApiClient;
    private Geocoder mGeocoder;
    private LatLng lastLatLng;

    public WeatherModelImpl(Context context) {
        ForecastApi.create("95eee69eb033b8558f61643ffadff21f");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        mGeocoder = new Geocoder(context, Locale.getDefault());
        Log.d(TAG, "INSTANCE CREATED");
    }

    @Override
    public void finalize() throws Throwable {
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        Log.d(TAG, "INSTANCE DESTROYED");
        super.finalize();
    }

    @Override
    public void setPresenter(MainPresenter mainPresenter) {
        this.presenter = mainPresenter;
    }

    @Override
    public void onConnected(Bundle bundle) {
        updateLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        presenter.notifyError("Connection to Google API suspended");
        Log.d(TAG, "Google Api Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        presenter.notifyError("Unable to connect Google API");
        Log.d(TAG, "Google Api Connection Failed");
    }

    @Override
    public LatLng getLocation() {
        if (lastLatLng != null)
            return lastLatLng;
        updateLocation();
        return defaultLatLng;
    }

    @Override
    public void fetchForecast(LatLng location, Callback<WeatherResponse> weatherResponseCallback) {
        RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        request.setLat(String.valueOf(getLocation().latitude));
        request.setLng(String.valueOf(getLocation().longitude));
        request.setUnits(Request.Units.SI);
        request.setLanguage(Request.Language.ENGLISH);

        weather.getWeather(request, weatherResponseCallback);
    }

    @Override
    public String getLocality(LatLng location) {
        String addr = "";
        try {
            addr = mGeocoder.getFromLocation(getLocation().latitude,
                    getLocation().longitude, 1).get(0).getLocality();
        } catch (IOException e) {
            presenter.notifyError("Error finding the name of location");
        }
        return addr;
    }

    @Background
    protected void updateLocation() {
        Log.d(TAG, "RETRIEVING LAST LOCATION....");
        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (loc != null) {
            lastLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            Log.d(TAG, "SUCCEED RETRIEVING LOCATION: " + lastLatLng.toString());
        } else {
            presenter.notifyError("Failed retrieving location");
            Log.d(TAG, "FAILED RETRIEVING LOCATION");
        }
    }
}
