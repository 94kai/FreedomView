package com.xk.freedomview;

import android.app.Activity;
import android.os.Bundle;

import com.xk.freedomview.util.FreedomUtil;

public class DemoActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        FreedomUtil.init(this);
    }
}
