package com.twirling.libtwirling.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.twirling.libtwirling.R;

public class MainActivity extends AppCompatActivity {
    //
    private ViewPager viewPager = null;
    private MenuItem menuItem;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Toolbar  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager manager = this.getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menuItem = menu.findItem(R.id.action);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}