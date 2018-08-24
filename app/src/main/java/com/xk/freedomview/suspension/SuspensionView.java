package com.xk.freedomview.suspension;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author xuekai1
 * @date 2018/8/24
 */
public class SuspensionView extends android.support.v7.widget.AppCompatImageView {
    private Listener listener;

    public SuspensionView(Context context) {
        super(context);
    }

    public SuspensionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public SuspensionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    float lastX = 0;
    float lastY = 0;


    long lastUp = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (System.currentTimeMillis() - lastUp < 100) {
                    if (listener != null) {
                        listener.reset();
                    }
                    return false;
                }
                lastUp = 0;
                if (listener != null) {
                    listener.onStart();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getRawX() - lastX;
                float dy = event.getRawY() - lastY;
                setX(getX() + dx);
                setY(getY() + dy);
                break;
            case MotionEvent.ACTION_UP:
                lastUp = System.currentTimeMillis();
                if (listener != null) {
                    listener.onStop();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (listener != null) {
                    listener.onStop();
                }
                break;
            default:
                break;
        }

        lastX = event.getRawX();
        lastY = event.getRawY();
        return true;
    }


    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public interface Listener {
        void onStart();

        void onStop();

        void reset();
    }
}
