package com.tariqaliiman.tariqaliiman.activities;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tariqaliiman.tariqaliiman.Database.AppPreference;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.fragments.BookmarkFragment;
import com.tariqaliiman.tariqaliiman.fragments.PartsFragment;
import com.tariqaliiman.tariqaliiman.fragments.QuarterFragment;
import com.tariqaliiman.tariqaliiman.Popups.JumpToPopup;
import com.tariqaliiman.tariqaliiman.Utilities.AppConstants;
import com.tariqaliiman.tariqaliiman.Utilities.CloseApplication;
import com.tariqaliiman.tariqaliiman.Utilities.QuranConfig;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

/**
 * Activity class Home page to choose Quran page read
 */
public class HomeActivity extends AppCompatActivity {
    int k;
    boolean doubleBackToExitPressedOnce = false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check is tablet or not
        if (isTablet(getApplicationContext())) {
            setContentView(R.layout.activity_home);
        } else {
            setContentView(R.layout.activity_home1);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //get and set screen resolution
        QuranConfig.getResolutionURLLink(this);

        //init application views
        init();


    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private void init() {
        k = 0;

        AppPreference.setSelectionVerse(null);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name_main));
        setSupportActionBar(toolbar);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(mViewPager);
        if (getIntent() != null) {
            String message = getIntent().getStringExtra("Setting");
            if (message != null && message.equalsIgnoreCase("x")) {
                /*Intent i = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(i);*/
            }
        }

        startService(new Intent(getBaseContext(), CloseApplication.class));
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_activity_home, menu);

        /*//init search in the toolbar
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //listener for click in search button
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                *//*startActivity(new Intent(HomeActivity.this, SearchActivity.class)
                        .putExtra(AppConstants.General.SEARCH_TEXT, query));*//*
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_translations) {
            //start translation activity
//            startActivity(new Intent(this, TranslationsActivity.class));
            return true;
        } else if (id == R.id.last) {
            int lastPage = AppPreference.getLastPageRead();
            //check of there is no saved last page
            if (lastPage != -1) {
                startActivity(new Intent(this, QuranPageReadActivity.class)
                        .putExtra(AppConstants.General.PAGE_NUMBER, lastPage));
            } else {
                startActivity(new Intent(this, QuranPageReadActivity.class)
                        .putExtra(AppConstants.General.PAGE_NUMBER, 603));
            }

        } else if (id == R.id.search) {
            //Search view created in onCreateOptionMenu
            return false;
        } else */
        if (id == R.id.action_jump) {
            //show popup
            new JumpToPopup(this);
        } else /*if (id == R.id.action_settings) {
            //settings activity
            startActivity(new Intent(this, SettingsActivity.class));
        } else */if (id == R.id.action_about) {
            //about activity
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.action_share) {
            //share intent for the application
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "#" + getString(R.string.app_name) + "\n https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
//            Toast.makeText(this, "App not published", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_rate_app) {
            //market url of the application
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
//            Toast.makeText(this, "App not published", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        startActivity(new Intent(this, HomeActivity.class).putExtra("Setting", "x"));

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new PartsFragment();
                case 1:
                    return new QuarterFragment();
                case 2:
                    return new BookmarkFragment();
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                case 0:
                    return getResources().getString(R.string.fragment1);
                case 1:
                    return getResources().getString(R.string.fragment2);
                case 2:
                    return getResources().getString(R.string.fragment3);

            }

            return null;
        }
    }

    @Override
    public void onBackPressed() {
//        k++;
//        if(k == 1)
//            finish();
        super.onBackPressed();
    }

}
