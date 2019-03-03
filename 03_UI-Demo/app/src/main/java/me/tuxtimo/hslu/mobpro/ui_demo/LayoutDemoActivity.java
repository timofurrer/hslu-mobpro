package me.tuxtimo.hslu.mobpro.ui_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LayoutDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int view = getIntent().getIntExtra("view", R.layout.activity_layout_demo);

        if(view == R.id.radioButton) {
            view = R.layout.layoutdemo_linearlayout;
        } else if(view == R.id.radioButton2) {
            view = R.layout.layoutdemo_constraintlayout;
        }

        setContentView(view);
    }
}
