package com.xk.freedomview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xk.freedomview.suspension.SuspensionView;

public class MainActivity extends AppCompatActivity {

    private FreedomView freedomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        freedomView = findViewById(R.id.fd);


        SuspensionView suspensionView = (SuspensionView) findViewById(R.id.suspension);

        suspensionView.setListener(new SuspensionView.Listener() {
            @Override
            public void onStart() {
                freedomView.open();
            }

            @Override
            public void onStop() {
                freedomView.close();
            }

            @Override
            public void reset() {
                freedomView.resetLocal();
            }
        });

    }

    public void click(View view) {
    }
}
