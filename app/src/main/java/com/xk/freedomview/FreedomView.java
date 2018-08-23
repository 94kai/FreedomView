package com.xk.freedomview;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author xuekai1
 * @date 2018/8/22
 */
public class FreedomView extends FrameLayout implements SensorEventListener {

//    private SeekBar s1;
//    private SeekBar s2;
//    private SeekBar s3;


    private int leftBorder = 0;
    private int rightBorder = 1000;
    private int topBorder = 0;
    private int bottomBorder = 1000;

    public FreedomView(@NonNull Context context) {
        super(context);
    }

    public FreedomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();


        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


//SensorManager.SENSOR_DELAY_FASTEST(0微秒)：最快。最低延迟，一般不是特别敏感的处理不推荐使用，该模式可能在成手机电力大量消耗，由于传递的为原始数据，诉法不处理好会影响游戏逻辑和UI的性能

//SensorManager.SENSOR_DELAY_GAME(20000微秒)：游戏。游戏延迟，一般绝大多数的实时性较高的游戏都是用该级别

//SensorManager.SENSOR_DELAY_NORMAL(200000微秒):普通。标准延时，对于一般的益智类或EASY级别的游戏可以使用，但过低的采样率可能对一些赛车类游戏有跳帧现象

//SensorManager.SENSOR_DELAY_UI(60000微秒):用户界面。一般对于屏幕方向自动旋转使用，相对节省电能和逻辑处理，一般游戏开发中不使用


    }

    public FreedomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private SensorManager sensorManager;

    private Sensor accelerometerSensor;


    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean jiasudu = true;


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //加速度传感器
            if (!jiasudu) {
                return;
            }
            // x,y,z分别存储坐标轴x,y,z上的加速度

            final float x = event.values[0];

            final float y = event.values[1];

            final float z = event.values[2];

            // 根据三个方向上的加速度值得到总的加速度值a
            float a = (float) Math.sqrt(x * x + y * y + z * z);
            System.out.println("总加速度->" + a);
            // 传感器从外界采集数据的时间间隔为10000微秒
            // 加速度传感器的最大量程
            System.out.println("xyz->" + x + "\n" + y + "\n" + z);
        }

    }

    float lastX, lastY, lastZ = -999;

    private void setLayoutParams(float x, float y, float z) {
        if (lastX == -999 || lastY == -999 || lastZ == -999) {
            lastX = x;
            lastY = y;
            lastZ = z;
        } else {
            x = x - lastX;
            y = y - lastY;
            z = z - lastZ;

            if ((getY() - z * 5) < topBorder || (getY() - z * 5) > bottomBorder) {
            } else {
                setY(getY() - z * 5);
            }


            if ((getX() - x * 7) < leftBorder || (getX() - x * 7) > rightBorder) {
            } else {
                setX(getX() - x * 7);

            }

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void reset() {

        lastX = lastY = lastZ = -999;


    }

    public void close() {
        sensorManager.unregisterListener(this);

    }

    public void open() {
        sensorManager.registerListener(FreedomView.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
}
