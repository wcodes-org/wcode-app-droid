package org.wcodes.android;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

import org.wcodes.core.AlphaTree;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.InputStream;

import android.support.design.widget.TabLayout;
import android.view.Window;

/**
 * The entry activity
 */
public class HomeActivity extends AppCompatActivity {

    ModePagerAdapter mModePagerAdapter;
    ViewPager mViewPager;

    boolean encode_bar_decode;

    org.wcodes.core.Dictionary dictionary;
    AlphaTree alphaTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
        Fabric.with(this, new Answers());

        setContentView(R.layout.activity_home);

        mModePagerAdapter = new ModePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.modePager);
        mViewPager.setAdapter(mModePagerAdapter);

        TabLayout tableLayout = (TabLayout) findViewById(R.id.tabLayout);
        tableLayout.setupWithViewPager(mViewPager);

        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.wordlist);

            byte[] b = new byte[in_s.available()];
            //noinspection ResultOfMethodCallIgnored
            in_s.read(b);
            dictionary = new org.wcodes.core.Dictionary(b);
            alphaTree = new AlphaTree();
            alphaTree.load(dictionary);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_menu_actionIntro:
                Intent splash = new Intent(HomeActivity.this, SplashActivity.class);
                splash.putExtra("replay", true);
                HomeActivity.this.startActivity(splash);
                HomeActivity.this.finish();
                return true;
            case R.id.main_menu_actionAbout:
                AboutFragment aboutFragment = new AboutFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .add(R.id.fragmentContainer, aboutFragment)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ModePagerAdapter extends FragmentPagerAdapter {

        public ModePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        final String[] title = {"Stored", "Encode", "Decode"};

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return StoredFragment.newInstance(dictionary);
                case 1:
                    encode_bar_decode = true;
                    return EncodeFragment.newInstance(dictionary);
                case 2:
                    encode_bar_decode = false;
                    Answers.getInstance().logCustom(new CustomEvent("Decode mode"));
                    return DecodeFragment.newInstance(dictionary);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

}
