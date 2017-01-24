package com.android.ww.mmcore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ww.com.core.ScreenUtil;

/**
 * Created by fighter on 2016/5/20.
 */
public class CustomWidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtil.init(this, 1080);
        setContentView(R.layout.activity_custom_widgets);
    }
}
