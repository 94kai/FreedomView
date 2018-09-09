package com.xk.freedomview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xk.freedomview.util.FreedomUtil;

/**
 * @author xuekai1
 * @date 2018/8/24
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FreedomUtil.init(this);
    }

    public void toGravity(View view) {
        startActivity(new Intent(this, DemoActivity.class));
    }
}
