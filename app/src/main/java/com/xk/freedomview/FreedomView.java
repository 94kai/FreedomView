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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * @author xuekai1
 * @date 2018/8/22
 */
public class FreedomView extends FrameLayout implements SensorEventListener {


    //x、y方向的速度
    private final int vY = 5;
    private final int vX = 7;


    //屏幕宽高
    private int screenWidth;
    private int screenHeight;

    //边距
    private int leftBorder = -500;
    private int rightBorder = 0;
    private int topBorder = 0;
    private int bottomBorder = 600;


    private int realLeftBorder = leftBorder;
    private int realTopBorder = topBorder;
    private int realRightBorder;
    private int realBottomBorder;
    private RelativeLayout.LayoutParams layoutParams;
    private SuspensionView suspensionView;
    private CheckBox checkBox;

    public FreedomView(@NonNull Context context, int left, int right, int top, int bottom) {
        this(context, null);
        leftBorder = left;
        rightBorder = right;
        bottomBorder = bottom;
        topBorder = top;
        realLeftBorder = leftBorder;
        realTopBorder = topBorder;
    }

    public FreedomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        checkBox = new CheckBox(getContext());
        checkBox.setText("摇杆模式");
        checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBox.setText("摇杆模式");
                } else {
                    checkBox.setText("重力模式");
                }
            }
        });

        suspensionView = new SuspensionView(getContext());

        suspensionView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        suspensionView.setX(100);
        suspensionView.setY(800);
        addView(checkBox);
        addView(suspensionView);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //删除susxxxx  垃圾代码。。
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getClass().getSimpleName().contains("SuspensionView") || childView.getClass().getSimpleName().contains("CheckBox")) {
                removeView(childView);
                if (getParent() instanceof ViewGroup) {
                    ((ViewGroup) getParent()).addView(childView);
                }
                break;
            }
        }
        //删除checkbox
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getClass().getSimpleName().contains("SuspensionView") || childView.getClass().getSimpleName().contains("CheckBox")) {
                removeView(childView);
                if (getParent() instanceof ViewGroup) {
                    ((ViewGroup) getParent()).addView(childView);
                }
                break;
            }
        }
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

        suspensionView.setListener(new SuspensionView.Listener() {
            @Override
            public void onDown() {
                if (checkBox.isChecked()) {
                    openGravity();
                }
            }

            @Override
            public void onUpOrCancel() {
                if (checkBox.isChecked()) {
                    closeGravity();
                }
            }

            @Override
            public void doubleClick() {
                resetLocal();
            }

            @Override
            public void onMovie(float dx, float dy) {
                if (!checkBox.isChecked()) {
                    moveByRockingBar(dx, dy);
                }

            }
        });

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
            setLayoutParamsByGravity(x, y, z);
        }
    }

    float lastX, lastY, lastZ = -999;
    float originalX;
    float originalY;

    private synchronized void setLayoutParamsByGravity(float x, float y, float z) {

        if (lastX == -999 || lastY == -999 || lastZ == -999) {
            lastX = x;
            lastY = y;
            lastZ = z;
        } else {
            x = x - lastX;
            y = y - lastY;
            z = z - lastZ;


            setLayoutParams(x * vX, z * vY);
        }
    }


    private void setLayoutParams(float x, float y) {


        if ((getY() - y) < realTopBorder || (getY() - y) > realBottomBorder) {
        } else {
            setY(getY() - y);
        }


        if ((getX() - x) < realLeftBorder || (getX() - x) > realRightBorder) {
        } else {
            setX(getX() - x);

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

    public void closeGravity() {
        sensorManager.unregisterListener(this);
    }

    public void openGravity() {
        resetBalance();
        sensorManager.registerListener(FreedomView.this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }


    public void moveByRockingBar(float x, float y) {
        setLayoutParams(-x * 3, -y * 6);
    }

    public void resetLocal() {
        resetBalance();
        setX(originalX);
        setY(originalY);
    }
}
