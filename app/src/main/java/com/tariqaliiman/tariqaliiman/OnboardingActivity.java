package com.tariqaliiman.tariqaliiman;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tariqaliiman.tariqaliiman.fragments.OnOnboardingOptionSelectedListener;
import com.tariqaliiman.tariqaliiman.fragments.OnboardingAdjustmentHighLatitudesFragment;
import com.tariqaliiman.tariqaliiman.fragments.OnboardingAsrCalculationMethodFragment;
import com.tariqaliiman.tariqaliiman.fragments.OnboardingCalculationMethodFragment;
import com.tariqaliiman.tariqaliiman.fragments.OnboardingTimeFormatFragment;
import com.tariqaliiman.tariqaliiman.utils.AppSettings;
import com.tariqaliiman.tariqaliiman.utils.ScreenUtils;
import com.tariqaliiman.tariqaliiman.widget.FragmentStatePagerAdapter;


public class OnboardingActivity extends AppCompatActivity implements OnOnboardingOptionSelectedListener {

    public static final String EXTRA_CARD_INDEX = "card_index";

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private int mCardIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.lockOrientation(this);
        setContentView(R.layout.activity_onboarding);

        // Initialize Calss Mobile Ads
        MobileAds.initialize(this, getString(R.string.IDAPP));

        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.mainBanner2));
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Instantiate a ViewPager and a PagerAdapter.
        Intent intent = getIntent();
        mCardIndex = intent.getIntExtra(EXTRA_CARD_INDEX, 0);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), mCardIndex);
        mPager.setAdapter(mPagerAdapter);
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
    public void onOptionSelected() {
        if (mPager.getCurrentItem() + 1 == mPagerAdapter.getCount()) {
            AppSettings.getInstance(this).set(AppSettings.Key.HAS_DEFAULT_SET, true);
            Intent data = new Intent();
            if (getParent() == null) {
                setResult(RESULT_OK, data);
            } else {
                getParent().setResult(RESULT_OK, data);
            }
            finish();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        int mCardIndex = 0;

        public ScreenSlidePagerAdapter(FragmentManager fm, int cardIndex) {
            super(fm);
            mCardIndex = cardIndex;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return OnboardingCalculationMethodFragment.newInstance(mCardIndex);
                case 1:
                    return OnboardingAsrCalculationMethodFragment.newInstance(mCardIndex);
                case 2:
                    return OnboardingAdjustmentHighLatitudesFragment.newInstance(mCardIndex);
                case 3:
                    return OnboardingTimeFormatFragment.newInstance(mCardIndex);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
