package com.pipplware.teixeiras.virtualkeypad;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.pipplware.teixeiras.ConnectionStatus;
import com.pipplware.teixeiras.network.JSonRequest;
import com.pipplware.teixeiras.network.NetworkRequest;
import com.pipplware.teixeiras.network.NetworkService;
import com.pipplware.teixeiras.network.WebSocketService;
import com.pipplware.teixeiras.network.models.Info;
import com.pipplware.teixeiras.network.models.Torrents;
import com.pipplware.teixeiras.virtualkeypad.keyboard.InputGridFragment;
import com.pipplware.teixeiras.virtualkeypad.psutil.PSUtil;
import com.pipplware.teixeiras.virtualkeypad.torrents.TorrentFragment;
import com.pipplware.teixeiras.network.NetInput;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Handler;

import de.keyboardsurfer.android.widget.crouton.Crouton;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,
        PSUtil.OnFragmentInteractionListener, TorrentFragment.OnFragmentInteractionListener,
        CreditsFragment.OnFragmentInteractionListener, JSonRequest.JSonRequestCallback<Info>,
        WebSocketService.Callback, Observer {

    public final static int RESULT_IP = 1;
    public final static int RESULT_PREFERNCES = 2;

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

        if (!Preferences.sharedInstance(this).getBoolean(Preferences.PREFERENCE_AUTOLOGIN, false)) {
            Intent serverFind = new Intent(this, ServerFindActivity.class);
            startActivityForResult(serverFind, RESULT_IP);
        } else {
           NetworkRequest.ip = Preferences.sharedInstance(this).getString(Preferences.PREFERENCE_IP, "");
           NetworkRequest.port = Preferences.sharedInstance(this).getString(Preferences.PREFERENCE_PORT, "");
           NetworkRequest.password = Preferences.sharedInstance(this).getString(Preferences.PREFERENCE_PASSWORD, "");
        }


        Intent splashScreen = new Intent(this, SplashActivity.class);
        startActivity(splashScreen);

        ConnectionStatus.sharedInstance().addObserver(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
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
            Intent preferences= new Intent(this, PreferencesActivity.class);
            startActivityForResult(preferences, RESULT_PREFERNCES);

            return true;
        }
         if(id == R.id.ic_action_socket_connection) {
             if (!ConnectionStatus.sharedInstance().isWebSocketAvailable()) {
                 connectionStart();
             }
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
                    return new InputGridFragment();
                case 1:
                    return new PSUtil();
                case 2:
                    return new TorrentFragment();
                case 3:
                    return new CreditsFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
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
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);

            }
            return null;
        }
    }


    protected void connectionStart(){
        new Thread(new Runnable(){

            @Override
            public void run() {
                try{
                    new JSonRequest<>(MainActivity.this,Info.class, NetworkRequest.address()+"/info");
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof ConnectionStatus) {
            final ActionMenuItemView item = (ActionMenuItemView) findViewById(R.id.ic_action_socket_connection);
            if (item != null) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ConnectionStatus.sharedInstance().isWebSocketAvailable()) {

                            item.setIcon(getResources().getDrawable(R.drawable.online));
                        } else {
                            item.setIcon(getResources().getDrawable(R.drawable.offline));
                        }
                    }
                });
            }

        }





    }

    @Override
    public void onJSonRequestResponse(JSonRequest<Info> request, Info response) {
        NetworkRequest.info = response;
        WebSocketService socket = new WebSocketService(this);
        socket.connectWebSocket();
        NetworkRequest.service = socket;

    }

    @Override
    public void jsonRequesFailed(JSonRequest<Info> request) {
        Log.d("s","sdds");
    }

    @Override
    public void onMessage(String s) {
        Log.d("WEBSOCKET", s);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode ) {
            case RESULT_IP: {
                if (resultCode == RESULT_OK) {
                    connectionStart();
                }
            }break;

            case RESULT_PREFERNCES: {
                if (resultCode == PreferencesActivity.RESULT_RESTART) {
                    Intent serverFind = new Intent(this, ServerFindActivity.class);
                    startActivityForResult(serverFind, RESULT_IP);
                }
            }break;

        }

    }




    public void requestAuthorizationToAddTorrent(String title,final Runnable onYesRunnable) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        new Thread(onYesRunnable).start();

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        addFromIntent();
    }


    private static String getQueryParameter(Uri uri, String parameter) {
        int start = uri.toString().indexOf(parameter + "=");
        if (start >= 0) {
            int begin = start + (parameter + "=").length();
            int end = uri.toString().indexOf("&", begin);
            return uri.toString().substring(begin, end >= 0 ? end : uri.toString().length());
        }
        return null;
    }

    public static String extractNameFromUri(Uri rawTorrentUri) {

        if (rawTorrentUri.getScheme() == null) {
            // Probably an incorrect URI; just return the whole thing
            return rawTorrentUri.toString();
        }

        if (rawTorrentUri.getScheme().equals("magnet")) {
            // Magnet links might have a dn (display name) parameter
            String dn = getQueryParameter(rawTorrentUri, "dn");
            if (dn != null && !dn.equals("")) {
                return dn;
            }
            // If not, try to return the hash that is specified as xt (exact topci)
            String xt = getQueryParameter(rawTorrentUri, "xt");
            if (xt != null && !xt.equals("")) {
                return xt;
            }
        }

        if (rawTorrentUri.isHierarchical()) {
            String path = rawTorrentUri.getPath();
            if (path != null) {
                if (path.contains("/")) {
                    path = path.substring(path.lastIndexOf("/"));
                }
                return path;
            }
        }

        // No idea what to do with this; return as is
        return rawTorrentUri.toString();
    }
    /**
     * If required, add torrents from the supplied intent extras.
     */
    protected void addFromIntent() {
        Intent intent = getIntent();
        Uri dataUri = intent.getData();
        String data = intent.getDataString();
        String action = intent.getAction();


        // Adding multiple torrents at the same time (as found in the Intent extras Bundle)
        if (action != null && action.equals("org.transdroid.ADD_MULTIPLE")) {
            // Intent should have some extras pointing to possibly multiple torrents
            String[] urls = intent.getStringArrayExtra("TORRENT_URLS");
            String[] titles = intent.getStringArrayExtra("TORRENT_TITLES");
            if (urls != null) {
                for (int i = 0; i < urls.length; i++) {
                    String title = (titles != null && titles.length >= i ? titles[i] :
                            extractNameFromUri(Uri.parse(urls[i])));
                     addTorrentByUrl(urls[i], title);

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
        String title = extractNameFromUri(dataUri);
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

            addTorrentByUrl(data, title);
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
        }

    }

    public void addTorrentByMagnetUrl(String url, String title) {


        // Since v39 Chrome sends application/x-www-form-urlencoded magnet links and most torrent clients do not understand those, so decode first
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Ignore: UTF-8 is always available on Android devices
        }
        final String value = url;
        requestAuthorizationToAddTorrent(title, new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("uri", value));
                NetworkRequest.makeRequest("transmission_add", list);
            }
        });

    }

    protected void addTorrentByFile(final String localFile, String title) {
        try {
            addTorrentFromStream(new FileInputStream(localFile), title);
        }catch(Exception e) {

        }
    }

    private void addTorrentFromDownloads(final Uri contentUri, String title) {
        try {
            addTorrentFromStream(getContentResolver().openInputStream(contentUri), title);
        }catch(Exception e) {

        }
    }


    protected void addTorrentFromStream(final InputStream input, String title) {

        requestAuthorizationToAddTorrent(title, new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream =  input;//You can get an inputStream using any IO API
                    byte[] bytes;
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    try {
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bytes = output.toByteArray();
                    String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
                    List<NameValuePair> list = new ArrayList<>();
                    list.add(new BasicNameValuePair("file", encodedString));
                    NetworkRequest.makeRequest("transmission_add_file", list);
                }catch(Exception e) {

                }

            }
        });
    }

    public void addTorrentByUrl(final String url, String title) {
        requestAuthorizationToAddTorrent(title, new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("uri", url));
                NetworkRequest.makeRequest("transmission_add", list);
            }
        });

    }

}
