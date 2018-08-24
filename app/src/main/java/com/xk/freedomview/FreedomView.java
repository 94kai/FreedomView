package com.xk.freedomview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * @author xuekai1
 * @date 2018/8/22
 */
public class FreedomView extends FrameLayout implements SensorEventListener {


    //x、y方向的速度
    int vY = 5;
    int vX = 7;


    private int screenWidth;
    private int screenHeight;

    private final int leftBorder = -500;
    private final int rightBorder = 0;
    private final int topBorder = 0;
    private final int bottomBorder = 600;


    private final int realLeftBorder = leftBorder;
    private final int realTopBorder = topBorder;
    private int realRightBorder;
    private int realBottomBorder;
    private RelativeLayout.LayoutParams layoutParams;

    public FreedomView(@NonNull Context context) {
        super(context);
    }

    public FreedomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        postDelayed(new Runnable() {
            @Override
            public void run() {

                WindowManager wm = (WindowManager) getContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                screenWidth = wm.getDefaultDisplay().getWidth();
                screenHeight = wm.getDefaultDisplay().getHeight();

                realRightBorder = screenWidth - getMeasuredWidth() + rightBorder;
                realBottomBorder = screenHeight + -getMeasuredHeight() + bottomBorder;
                originalX = getX();
                originalY = getY();
                layoutParams = new RelativeLayout.LayoutParams(getLayoutParams());
                Log.i("FreedomView", "run-->" + originalX + "==" + originalY);
            }
        }, 1000);

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
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float x = event.values[0];
            final float y = event.values[1];
            final float z = event.values[2];
            setLayoutParams(x, y, z);
        }
    }

    float lastX, lastY, lastZ = -999;
    float originalX;
    float originalY;

    private synchronized void setLayoutParams(float x, float y, float z) {

        if (lastX == -999 || lastY == -999 || lastZ == -999) {
            lastX = x;
            lastY = y;
            lastZ = z;
        } else {
            x = x - lastX;
            y = y - lastY;
            z = z - lastZ;

            if ((getY() - z * vY) < realTopBorder || (getY() - z * vY) > realBottomBorder) {
            } else {
                setY(getY() - z * vY);
            }


            if ((getX() - x * vX) < realLeftBorder || (getX() - x * vX) > realRightBorder) {
            } else {
                setX(getX() - x * vX);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 重置平衡点
     */
    public void resetBalance() {
        lastX = lastY = lastZ = -999;
    }

    public void close() {
        sensorManager.unregisterListener(this);
    }

    public void open() {
        resetBalance();
        sensorManager.registerListener(FreedomView.this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void resetLocal() {
        resetBalance();
        setX(originalX);
        setY(originalY);
    }
}
