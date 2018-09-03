package com.xk.freedomview;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * @author xuekai1
 * @date 2018/9/3
 */
public class BaseActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        ViewGroup contentViewParent = findViewById(android.R.id.content);
        ViewGroup contentView = (ViewGroup) contentViewParent.getChildAt(0);
        FreedomView freedomView = new FreedomView(this,0,0,0,100);
        contentViewParent.removeView(contentView);
        contentViewParent.addView(freedomView);
        freedomView.addView(contentView,0);
    }
}
