package com.tariqaliiman.tariqaliiman;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.tariqaliiman.tariqaliiman.fragments.InitialConfigFragment;
import com.tariqaliiman.tariqaliiman.fragments.KaabaLocatorFragment;
import com.tariqaliiman.tariqaliiman.fragments.LocationHelper;
import com.tariqaliiman.tariqaliiman.fragments.SalaatTimesFragment;
import com.tariqaliiman.tariqaliiman.utils.AppSettings;
import com.tariqaliiman.tariqaliiman.utils.PermissionUtil;
import com.tariqaliiman.tariqaliiman.utils.ScreenUtils;
import com.tariqaliiman.tariqaliiman.widget.FragmentStatePagerAdapter;
import com.tariqaliiman.tariqaliiman.widget.SlidingTabLayout;

public class SalaatTimesActivity extends AppCompatActivity implements Constants,
        InitialConfigFragment.OnOptionSelectedListener, ViewPager.OnPageChangeListener,
        LocationHelper.LocationCallback {

    private LocationHelper mLocationHelper;
    private Location mLastLocation = null;

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mAdapter;
    private SlidingTabLayout mTabs;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppSettings settings = AppSettings.getInstance(this);

        //INIT APP
        if (!settings.getBoolean(AppSettings.Key.IS_INIT)) {
            settings.set(settings.getKeyFor(AppSettings.Key.IS_ALARM_SET, 0), true);
            settings.set(settings.getKeyFor(AppSettings.Key.IS_FAJR_ALARM_SET, 0), true);
            settings.set(settings.getKeyFor(AppSettings.Key.IS_DHUHR_ALARM_SET, 0), true);
            settings.set(settings.getKeyFor(AppSettings.Key.IS_ASR_ALARM_SET, 0), true);
            settings.set(settings.getKeyFor(AppSettings.Key.IS_MAGHRIB_ALARM_SET, 0), true);
            settings.set(settings.getKeyFor(AppSettings.Key.IS_ISHA_ALARM_SET, 0), true);
            settings.set(AppSettings.Key.USE_ADHAN, true);
            settings.set(AppSettings.Key.IS_INIT, true);
        }

        setContentView(R.layout.activity_salaat_times);
        ScreenUtils.lockOrientation(this);

        // Initialize Calss Mobile Ads
        MobileAds.initialize(this, getString(R.string.IDAPP));

        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.mainBanner3));
        adView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("logads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("logfailed", "onAdFailedToLoad = " + i);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("logonadclosed", "onAdClosed");
            }
        });

        // Interstitial Ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter4));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("log" + "132", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("log" + "132", "onAdFailedToLoad = " + i);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("log" + "132", "onAdClosed");
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLocationHelper = (LocationHelper) getFragmentManager().findFragmentByTag(LOCATION_FRAGMENT);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        mAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), 0);

        // Assigning ViewPager View and setting the adapter
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(this);

        // Assiging the Sliding Tab Layout View
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
/*
    mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
      @Override
      public int getIndicatorColor(int position) {
        return getResources().getColor(R.color.teal_accent);
      }
    });
*/
        mTabs.setSelectedIndicatorColors(getResources().getColor(android.R.color.primary_text_dark));
        mTabs.setTextColor(android.R.color.primary_text_dark);

        // Setting the ViewPager For the SlidingTabsLayout
        mTabs.setViewPager(mPager);

        if (mLocationHelper == null) {
            mLocationHelper = LocationHelper.newInstance();
            getFragmentManager().beginTransaction().add(mLocationHelper, LOCATION_FRAGMENT).commit();
        }

        if (!settings.getBoolean(AppSettings.Key.IS_TNC_ACCEPTED, false)) {
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SalaatTimesActivity.this, TermsAndConditionsActivity.class);
                    overridePendingTransition(R.anim.enter_from_bottom, R.anim.no_animation);
                    startActivityForResult(intent, REQUEST_TNC);
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mLastLocation == null) {
            fetchLocation();
        }
    }

    @Override
    protected void onDestroy() {
        //Just to be sure memory is cleaned up.
        mPager.removeOnPageChangeListener(this);
        mPager = null;
        mAdapter = null;
        mTabs = null;
        mLastLocation = null;

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_salaat_times, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startOnboardingFor(0);
            return true;
        } else if (id == R.id.action_terms) {
            Intent intent = new Intent(SalaatTimesActivity.this, TermsAndConditionsActivity.class);
            intent.putExtra(TermsAndConditionsActivity.EXTRA_DISPLAY_ONLY, true);
            overridePendingTransition(R.anim.enter_from_bottom, R.anim.no_animation);
            startActivityForResult(intent, REQUEST_TNC);
        }

        return super.onOptionsItemSelected(item);
    }

    private void startOnboardingFor(int index) {
        Intent intent = new Intent(getApplicationContext(), OnboardingActivity.class);
        intent.putExtra(OnboardingActivity.EXTRA_CARD_INDEX, index);
        startActivityForResult(intent, REQUEST_ONBOARDING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    fetchLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    onLocationSettingsFailed();
                    break;
                default:
                    onLocationSettingsFailed();
                    break;
            }
        } else if (requestCode == REQUEST_ONBOARDING) {
            if (resultCode == RESULT_OK) {
                onUseDefaultSelected();
            }
        } else if (requestCode == REQUEST_TNC) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            } else {
                AppSettings settings = AppSettings.getInstance(this);
                settings.set(AppSettings.Key.IS_TNC_ACCEPTED, true);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    private void fetchLocation() {
        if (mLocationHelper != null) {
            mLocationHelper.checkLocationPermissions();
        }
    }

    @Override
    public void onLocationSettingsFailed() {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        // NOT THE BEST SOLUTION, THINK OF SOMETHING ELSE
        mAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), 0);
        mPager.setAdapter(mAdapter);
    }

    @Override
    public void onConfigNowSelected(int num) {
        startOnboardingFor(num);
    }

    @Override
    public void onUseDefaultSelected() {
        if (mLastLocation != null) {
            // NOT THE BEST SOLUTION, THINK OF SOMETHING ELSE
            mAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), 0);
            mPager.setAdapter(mAdapter);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {
            if (mAdapter.mKaabaLocatorFragment != null &&
                    PermissionUtil.hasSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mAdapter.mKaabaLocatorFragment.showMap();
            }
        } else {
            mAdapter.mKaabaLocatorFragment.hideMap();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private int mCardIndex;
        public KaabaLocatorFragment mKaabaLocatorFragment;

        public ScreenSlidePagerAdapter(FragmentManager fm, int index) {
            super(fm);
            mCardIndex = index;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (AppSettings.getInstance(getApplicationContext()).isDefaultSet()) {
                        return SalaatTimesFragment.newInstance(mCardIndex, mLastLocation);
                    } else {
                        return InitialConfigFragment.newInstance();
                    }
                case 1:
                    return mKaabaLocatorFragment = KaabaLocatorFragment.newInstance(mLastLocation);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.salaat_times);
            } else {
                return getString(R.string.kaaba_position);
            }
        }


    }
}
