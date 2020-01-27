package com.soprahr.moodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.soprahr.moodapp.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.mainContainer, new LoginFragment()).commit();
        }
    }
}
