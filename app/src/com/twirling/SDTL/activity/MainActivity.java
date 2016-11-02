package com.twirling.SDTL.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.fragment.FragmentAudio;
import com.twirling.SDTL.fragment.FragmentDownload;
import com.twirling.SDTL.fragment.FragmentOnline;
import com.twirling.SDTL.module.ModuleAlertDialog;
import com.twirling.www.libgvr.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {
    //
    private int pageIndex = 0;
    private Toolbar toolbar = null;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private ViewPager viewPager = null;

    private ModuleAlertDialog dialog = null;
    private ModuleAlertDialog dialog2 = null;
    private MenuItem menuItem;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //
        dialog = new ModuleAlertDialog(MainActivity.this);
        dialog2 = new ModuleAlertDialog(MainActivity.this) {
            @Override
            protected void onConfirm() {
                Constants.USER_MOBILE = Constants.USER_MOBILE_DEFAULT;
                Constants.USER_IMAGE = Constants.USER_IMAGE_DEFAULT;
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                result.closeDrawer();
            }
        };
        //
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager manager = this.getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        adapter.addFragment(new FragmentOnline());
        adapter.addFragment(new FragmentDownload());
        adapter.addFragment(new FragmentAudio());
//        adapter.addFragment(webFragment);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        //
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageIndex = position;
                String title = "";
                Drawable icon = null;
                if (pageIndex == 0) {
                    title = "在线";
                    icon = new IconicsDrawable(MainActivity.this)
                            .icon(FontAwesome.Icon.faw_cloud_download)
                            .color(Color.parseColor("#DDFFFF"))
                            .sizeDp(30);
                    result.setSelection(1);
                } else if (pageIndex == 1) {
                    title = "本地";
                    icon = new IconicsDrawable(MainActivity.this)
                            .icon(FontAwesome.Icon.faw_play_circle_o)
                            .color(Color.parseColor("#FFDDFF"))
                            .sizeDp(24);
                    result.setSelection(2);
                } else if (pageIndex == 2) {
                    title = "声音";
                    icon = new IconicsDrawable(MainActivity.this)
                            .icon(FontAwesome.Icon.faw_heartbeat)
                            .color(Color.parseColor("#FFFFDD"))
                            .sizeDp(24);
                    result.setSelection(3);
                }
                try {
                    menuItem.setIcon(icon);
                } catch (Exception e) {
                    Log.w("title", title);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        //
        IProfile profile = new ProfileDrawerItem()
                .withName(Constants.USER_MOBILE.equals(Constants.USER_MOBILE_DEFAULT) ? "匿名访客" : Constants.USER_MOBILE)
                .withIcon(Constants.USER_IMAGE.equals(Constants.USER_IMAGE_DEFAULT) ? Constants.USER_IMAGE_DEFAULT : Constants.USER_IMAGE);
        //
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profile)
                .withOnlyMainProfileImageVisible(true)
                .withHeaderBackground(R.drawable.fl_drawer_head)
                .withSavedInstance(savedInstanceState)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        if (Constants.USER_MOBILE == Constants.USER_MOBILE_DEFAULT) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            dialog2.setMessage("确定登出账户吗？");
                        }
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withHasStableIds(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.download).withIcon(FontAwesome.Icon.faw_cloud_download).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.local).withIcon(FontAwesome.Icon.faw_play_circle_o).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.atmos).withIcon(FontAwesome.Icon.faw_heartbeat).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.hls).withIcon(GoogleMaterial.Icon.gmd_youtube_play).withIdentifier(4),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.homepage).withIcon(FontAwesome.Icon.faw_home).withIdentifier(5),
                        new SecondaryDrawerItem().withName(R.string.products).withIcon(GoogleMaterial.Icon.gmd_playlist_plus).withIdentifier(6),
                        new SecondaryDrawerItem().withName(R.string.audio).withIcon(FontAwesome.Icon.faw_headphones).withIdentifier(7),
                        new SecondaryDrawerItem().withName(R.string.machine).withIcon(GoogleMaterial.Icon.gmd_comment_video).withIdentifier(8),
                        new SecondaryDrawerItem().withName(R.string.contact).withIcon(GoogleMaterial.Icon.gmd_phone_ring).withIdentifier(9)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.w("MainActivity", position + "");
                        Intent intent = null;
                        switch (position) {
                            case 1:
                                viewPager.setCurrentItem(0);
                                break;
                            case 2:
                                viewPager.setCurrentItem(1);
                                break;
                            case 3:
                                viewPager.setCurrentItem(2);
                                break;
                            case 4:
                                intent = new Intent(getBaseContext(), HLSActivity.class);
                                startActivity(intent);
                                break;
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            default:
                                intent = new Intent(getBaseContext(), WebActivity.class);
                                intent.setFlags(position);
                                startActivity(intent);
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menuItem = menu.findItem(R.id.action_edit);
        if (menuItem == null) {
            menuItem = menu.getItem(0);
        }
        Drawable icon = new IconicsDrawable(MainActivity.this)
                .icon(FontAwesome.Icon.faw_cloud_download)
                .color(Color.parseColor("#DDFFFF"))
                .sizeDp(30);
        menuItem.setIcon(icon);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        dialog.setMessage("确定关闭app吗");
    }

    @Override
    protected void onResume() {
        IProfile profile = new ProfileDrawerItem()
                .withName(Constants.USER_MOBILE.equals(Constants.USER_MOBILE_DEFAULT) ? "匿名访客" : Constants.USER_MOBILE)
                .withIcon(Constants.USER_IMAGE.equals(Constants.USER_IMAGE_DEFAULT) ? Constants.USER_IMAGE_DEFAULT : Constants.USER_IMAGE);
        headerResult.removeProfile(0);
        headerResult.addProfiles(profile);
        super.onResume();
    }
}