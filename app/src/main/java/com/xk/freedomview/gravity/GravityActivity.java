package com.xk.freedomview.gravity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xk.freedomview.FreedomView;
import com.xk.freedomview.R;
import com.xk.freedomview.suspension.SuspensionView;

public class GravityActivity extends AppCompatActivity {

    private FreedomView freedomView;

    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        freedomView = findViewById(R.id.fd);
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkBox.setText(b?"重力模式":"摇杆模式");
            }
        });

        SuspensionView suspensionView = (SuspensionView) findViewById(R.id.suspension);

        suspensionView.setListener(new SuspensionView.Listener() {
            @Override
            public void onDown() {
                if (checkBox.isChecked()) {
                    freedomView.openGravity();
                }
            }

            @Override
            public void onUpOrCancel() {
                if (checkBox.isChecked()) {
                    freedomView.closeGravity();
                }
            }

            @Override
            public void doubleClick() {
                freedomView.resetLocal();
            }

            @Override
            public void onMovie(float dx, float dy) {
                if (!checkBox.isChecked()) {
                    freedomView.moveByRockingBar(dx, dy);
                }

            }
        });

    }
}
