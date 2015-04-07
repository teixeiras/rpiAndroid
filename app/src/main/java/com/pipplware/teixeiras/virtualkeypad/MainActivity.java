package com.pipplware.teixeiras.virtualkeypad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.pipplware.teixeiras.virtualkeypad.keyboard.KeyboardFragment;
import com.pipplware.teixeiras.virtualkeypad.psutil.PSUtil;
import com.pipplware.teixeiras.virtualkeypad.torrents.TorrentFragment;
import com.pipplware.teixeiras.network.NetInput;

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,
        KeyboardFragment.OnFragmentInteractionListener,
        PSUtil.OnFragmentInteractionListener, TorrentFragment.OnFragmentInteractionListener {

    public static int RESULT_IP = 1;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        Intent serverFind = new Intent(this, ServerFindActivity.class);
        startActivityForResult(serverFind, RESULT_IP);


        Intent splashScreen = new Intent(this, SplashActivity.class);
        startActivity(splashScreen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server_find, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position) {
                case 0:
                    return new KeyboardFragment();
                case 1:
                    return new PSUtil();
                case 2:
                    return new TorrentFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);

            }
            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_IP) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    NetInput.VolumeUp();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    NetInput.VolumeDown();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        addFromIntent();
    }



    /**
     * If required, add torrents from the supplied intent extras.
     */
    protected void addFromIntent() {
        Intent intent = getIntent();
        Uri dataUri = intent.getData();
        String data = intent.getDataString();
        String action = intent.getAction();

        /*
        // Adding multiple torrents at the same time (as found in the Intent extras Bundle)
        if (action != null && action.equals("org.transdroid.ADD_MULTIPLE")) {
            // Intent should have some extras pointing to possibly multiple torrents
            String[] urls = intent.getStringArrayExtra("TORRENT_URLS");
            String[] titles = intent.getStringArrayExtra("TORRENT_TITLES");
            if (urls != null) {
                for (int i = 0; i < urls.length; i++) {
                  /*  String title = (titles != null && titles.length >= i ? titles[i] : NavigationHelper
                            .extractNameFromUri(Uri.parse(urls[i])));
                    if (intent.hasExtra("PRIVATE_SOURCE")) {
                        // This is marked by the Search Module as being a private source site; get the url locally first
                        addTorrentFromPrivateSource(urls[i], title, intent.getStringExtra("PRIVATE_SOURCE"));
                    } else {
                        addTorrentByUrl(urls[i], title);
                    }
                }
            }
            return;
        }

        // Add a torrent from a local or remote data URI?
        if (dataUri == null)
            return;
        if (dataUri.getScheme() == null) {
            //Crouton.showText(this, R.string.error_invalid_url_form, NavigationHelper.CROUTON_ERROR_STYLE);
            return;
        }

        // Get torrent title
        String title = NavigationHelper.extractNameFromUri(dataUri);
        if (intent.hasExtra("TORRENT_TITLE")) {
            title = intent.getStringExtra("TORRENT_TITLE");
        }

        // Adding a torrent from the Android downloads manager
        if (dataUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            addTorrentFromDownloads(dataUri, title);
            return;
        }

        // Adding a torrent from http or https URL
        if (dataUri.getScheme().equals("http") || dataUri.getScheme().equals("https")) {

            String privateSource = getIntent().getStringExtra("PRIVATE_SOURCE");

            WebsearchSetting match = null;
            if (privateSource == null) {
                // Check if the target URL is also defined as a web search in the user's settings
                List<WebsearchSetting> websearches = applicationSettings.getWebsearchSettings();
                for (WebsearchSetting setting : websearches) {
                    Uri uri = Uri.parse(setting.getBaseUrl());
                    if (uri.getHost() != null && uri.getHost().equals(dataUri.getHost())) {
                        match = setting;
                        break;
                    }
                }
            }

            // If the URL is also a web search and it defines cookies, use the cookies by downloading the targeted
            // torrent file (while supplies the cookies to the HTTP request) instead of sending the URL directly to the
            // torrent client. If instead it is marked (by the Torrent Search module) as being form a private site, use
            // the Search Module instead to download the url locally first.
            if (match != null && match.getCookies() != null) {
                addTorrentFromWeb(data, match, title);
            } else if (privateSource != null) {
                addTorrentFromPrivateSource(data, title, privateSource);
            } else {
                // Normally send the URL to the torrent client
                addTorrentByUrl(data, title);
            }
            return;
        }

        // Adding a torrent from magnet URL
        if (dataUri.getScheme().equals("magnet")) {
            addTorrentByMagnetUrl(data, title);
            return;
        }

        // Adding a local .torrent file; the title we show is just the file name
        if (dataUri.getScheme().equals("file")) {
            addTorrentByFile(data, title);
            return;
        }*/

    }
}
