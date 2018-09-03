package com.xk.freedomview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @author xuekai1
 * @date 2018/8/24
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toGravity(View view) {
        startActivity(new Intent(this, DemoActivity.class));
    }
}
