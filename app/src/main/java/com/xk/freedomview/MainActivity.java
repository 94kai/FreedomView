package com.xk.freedomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FreedomView viewById1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewById1 = findViewById(R.id.fd);

        final View viewById = findViewById(R.id.abc);


        viewById.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    viewById1.open();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    viewById1.close();

                }
                return false;
            }
        });
    }

    public void click(View view) {
        viewById1.reset();
    }
}
