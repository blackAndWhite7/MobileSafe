package com.example.wei.fragmentdemo2;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentLeft fragment_left = (FragmentLeft) fragmentManager.findFragmentById(R.id.fragment_left);
        FragmentRight fragment_right = (FragmentRight) fragmentManager.findFragmentById(R.id.fragment_right);
        fragment_left.setFragmentRight(fragment_right);

    }
}
