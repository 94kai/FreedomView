package com.xk.freedomview.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author xuekai1
 * @date 2018/9/8
 */
public class FreedomUtil {

    public static void init(Activity activity){
        init(activity,0,0,0,0,0,0);
    }
    /**
     * 网络>本地>参数设置
     * @param activity
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param autoResetTime
     */
    public static void init(Activity activity, int left, int top, int right, int bottom, int autoResetTime, int mode) {
        StringBuilder json = new StringBuilder();
        // TODO: by xk 2018/9/8 下午2:54 从网络获取数据
        if (json.length() == 0) {
            //网络无数据，从本地读取
            InputStream open = null;
            try {
                open = activity.getAssets().open("config.json");

                BufferedReader reader = new BufferedReader(new InputStreamReader(open));
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //解析网络或者本地的json数据
        try {
            JSONArray jsonArray = new JSONArray(json.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String activityName = object.getString("activity");

                if (activity.getClass().getSimpleName().equals(activityName)) {
                    left = object.getInt("left");
                    right = object.getInt("right");
                    top = object.getInt("top");
                    bottom = object.getInt("bottom");
                    autoResetTime = object.getInt("autoResetTime");
                    autoResetTime = object.getInt("autoResetTime");
                    mode = object.getInt("mode");
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FreedomView freedomView = new FreedomView(activity, left, right, top, bottom, autoResetTime,mode);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup parentView = (ViewGroup) decorView.getChildAt(0);
        View contentView = parentView.findViewById(android.R.id.content);
        parentView.removeView(contentView);
        freedomView.setLayoutParams(contentView.getLayoutParams());
        parentView.addView(freedomView);
        freedomView.addView(contentView, 0);
    }
}
