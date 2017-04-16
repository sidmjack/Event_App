package com.uima.event_app;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        MyLiveEventsFragment liveFrag = new MyLiveEventsFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, liveFrag)
                .commit();
    }
}
