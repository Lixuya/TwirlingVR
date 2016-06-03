package com.twirlingvr.www.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.twirlingvr.www.R;
import com.twirlingvr.www.adapter.ViewPagerAdapter;
import com.twirlingvr.www.fragment.FragmentDownload;
import com.twirlingvr.www.fragment.FragmentOnline;
import com.twirlingvr.www.player.OpenMXPlayer;

public class MainActivity extends AppCompatActivity {
    //
    int pageIndex = 0;
    private Toolbar toolbar = null;
    boolean toggle = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                OpenMXPlayer openMXPlayer = new OpenMXPlayer();
                openMXPlayer.setDataSource(getBaseContext(), R.raw.music_aac_320k);
                if (toggle == false) {
                    openMXPlayer.play();
                } else {
                    openMXPlayer.stop();
                }
//                startActivity(new Intent(MainActivity.this, ListShowActivity.class));
                return false;
            }
        });
        //
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager manager = this.getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        adapter.addFragment(new FragmentOnline());
        adapter.addFragment(new FragmentDownload());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
//      viewPager.setOffscreenPageLimit(0);
        //
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageIndex = position;
                if (pageIndex == 0) {
                    toolbar.getMenu().getItem(0).setTitle("下载");
                } else if (pageIndex == 1) {
                    toolbar.getMenu().getItem(0).setTitle("本地");
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}