package org.wcodes.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashActivity extends FragmentActivity {

    //private static final int SPLASH_DISPLAY_LENGTH = 195;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;

    private ViewPager mPager;
    //private FrameLayout mPager_frame;
    private LinearLayout mSplashImageView;
    private RelativeLayout mSplashContentView;
    private ImageView[] dots;
    private Button mEnterButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private int prevPosition = 0;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPref = SplashActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isAlreadyLaunched;
        boolean isReplay;
        isAlreadyLaunched = sharedPref.getBoolean(getString(R.string.launched), false);
        if(getIntent().getExtras() != null)
            isReplay = getIntent().getExtras().getBoolean("replay");
        else
            isReplay = false;
        super.onCreate(savedInstanceState);
        if(!isAlreadyLaunched || isReplay) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.launched), true);
            editor.apply();

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);


            mEnterButton = (Button) findViewById(R.id.enter_button);
            mEnterButton.setOnClickListener(view -> beginEnter());

            mNextButton = (ImageButton) findViewById(R.id.next_button);
            mNextButton.setOnClickListener(view -> mPager.setCurrentItem(mPager.getCurrentItem() + 1));
            mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
            mPreviousButton.setOnClickListener(view -> mPager.setCurrentItem(mPager.getCurrentItem()-1));

            // Instantiate a ViewPager and a PagerAdapter.
            mSplashImageView = (LinearLayout) findViewById(R.id.splashImageView);
            mPager = (ViewPager) findViewById(R.id.pager);
            mSplashContentView = (RelativeLayout) findViewById(R.id.splashContent);
            PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int newPosition) {
                    Log.d("x", String.valueOf(newPosition));
                    LinearLayout pager_indicator;
                    pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
                    ImageView newDot = (ImageView) pager_indicator.getChildAt(newPosition);
                    newDot.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.drawable_dot_selected, null));
                    ImageView prevDot = (ImageView) pager_indicator.getChildAt(prevPosition);
                    prevDot.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.drawable_dot_normal, null));
                    prevPosition = newPosition;

                    switch(newPosition) {
                        case 1:
                            mPreviousButton.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            mNextButton.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            mNextButton.setVisibility(View.INVISIBLE);
                            mEnterButton.setVisibility(View.VISIBLE);
                    }

                }
            });

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                LinearLayout pager_indicator;
                pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
                dots = new ImageView[4];
                dots[0] = new ImageView(getApplicationContext());
                dots[0].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.drawable_dot_selected, null));
                pager_indicator.addView(dots[0], params);
                for (int i = 1; i < 4; i++) {
                    dots[i] = new ImageView(getApplicationContext());
                    dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.drawable_dot_normal, null));
                    pager_indicator.addView(dots[i], params);
                }
                animateSplash();
            }, 2000);

        }
        else {
            beginEnter();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void beginEnter() {
        Intent enter = new Intent(SplashActivity.this, HomeActivity.class);
        SplashActivity.this.startActivity(enter);
        SplashActivity.this.finish();
    }

    public void animateSplash() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            mSplashContentView.setAlpha(0f);
            mSplashContentView.setVisibility(View.VISIBLE);
            int mShortAnimationDuration = 3000;
            mSplashContentView.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
            mSplashImageView.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mSplashImageView.setVisibility(View.GONE);
                        }
                    });
            mNextButton.setAlpha(0f);
            mNextButton.setVisibility(View.VISIBLE);
            mNextButton.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Splash Page",
                Uri.parse("http://wcodes.org/decode"),
                Uri.parse("android-app://"+getPackageName()+"/http/wcodes.org/decode")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Splash Page",
                Uri.parse("http://wcodes.org/decode"),
                Uri.parse("android-app://"+getPackageName()+"/http/wcodes.org/decode")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /**
     * A simple pager adapter that represents SplashPageFragment objects
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SplashPageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
