package com.radwan1311.salahly;

import android.app.Application;
import android.content.Context;

import com.radwan1311.salahly.Helper.LocaleHelper;

public class MainApplication extends Application {

    @Override
    protected void attachBaseContext (Context base){
        super.attachBaseContext(LocaleHelper.onAttach(base , "en"));

    }
}


