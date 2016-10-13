package com.twirling.SDTL.activity;

import android.content.Intent;
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

import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
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
import com.twirling.SDTL.adapter.ViewPagerAdapter;
import com.twirling.SDTL.fragment.FragmentDownload;
import com.twirling.SDTL.fragment.FragmentOnline;
import com.twirling.SDTL.fragment.WebFragment;
import com.twirling.SDTL.module.ModuleAlertDialog;

public class MainActivity extends AppCompatActivity {
    //
    private int pageIndex = 0;
    private Toolbar toolbar = null;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    private ViewPager viewPager = null;
    private WebFragment webFragment = null;
    private ModuleAlertDialog dialog = null;
    private ModuleAlertDialog dialog2 = null;
    private MenuItem menuItem;

    private void onToolbarItemClicked(MenuItem menuItem) {
//                startActivity(new Intent(MainActivity.this, ListShowActivity.class));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        ButterKnife.bind(this);
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
//      RxToolbar.itemClicks(toolbar).subscribe(this::onToolbarItemClicked);
        webFragment = new WebFragment();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager manager = this.getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        adapter.addFragment(new FragmentOnline());
        adapter.addFragment(new FragmentDownload());
        adapter.addFragment(webFragment);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        //
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageIndex = position;
                String title = "";
                if (pageIndex == 0) {
                    title = "在线";
                    result.setSelection(1);
                } else if (pageIndex == 1) {
                    title = "本地";
                    result.setSelection(2);
                } else if (pageIndex == 2) {
                    title = "关于";
                    result.setSelection(3);
                }
                try {
                    menuItem.setTitle(title);
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
                        new PrimaryDrawerItem().withName(R.string.local).withIcon(FontAwesome.Icon.faw_play_circle).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.homepage).withIcon(FontAwesome.Icon.faw_home).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.hls).withIcon(GoogleMaterial.Icon.gmd_comment_video).withIdentifier(4),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.audio).withIcon(FontAwesome.Icon.faw_headphones).withIdentifier(5),
                        new SecondaryDrawerItem().withName(R.string.products).withIcon(GoogleMaterial.Icon.gmd_playlist_plus).withIdentifier(6),
                        new SecondaryDrawerItem().withName(R.string.blog).withIcon(FontAwesome.Icon.faw_desktop).withIdentifier(7),
                        new SecondaryDrawerItem().withName(R.string.cloud).withIcon(FontAwesome.Icon.faw_cloud).withIdentifier(8)
//                        new SecondaryDrawerItem().withName(R.string.contact).withIcon(GoogleMaterial.Icon.gmd_code_smartphone).withIdentifier(9)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.w("MainActivity", position + "");
                        switch (position) {
                            case 1:
                                viewPager.setCurrentItem(0);
                                break;
                            case 2:
                                viewPager.setCurrentItem(1);
                                break;
                            case 4:
                                Intent intent = new Intent(getBaseContext(), HLSActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            default:
                                webFragment.loadPage(position);
                                viewPager.setCurrentItem(2);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menuItem = menu.findItem(R.id.action_edit);
        if (menuItem == null) {
            menuItem = menu.getItem(0);
        }
        return true;
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