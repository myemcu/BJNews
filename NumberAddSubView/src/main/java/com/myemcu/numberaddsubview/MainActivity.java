package com.myemcu.numberaddsubview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private NumberAddSubView number_add_sub_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number_add_sub_view = (NumberAddSubView) findViewById(R.id.number_add_sub_view);
        number_add_sub_view.setOnNumClickListener(new NumberAddSubView.OnNumClickListener() {
            @Override
            public void onButtonSub(View view, int value) {
                Toast.makeText(MainActivity.this,"当前值："+value,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonAdd(View view, int value) {
                Toast.makeText(MainActivity.this,"当前值："+value,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
