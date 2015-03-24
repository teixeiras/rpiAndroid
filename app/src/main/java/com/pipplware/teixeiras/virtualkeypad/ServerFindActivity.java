package com.pipplware.teixeiras.virtualkeypad;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pipplware.teixeiras.virtualkeypad.network.NetworkRequest;
import com.pipplware.teixeiras.virtualkeypad.services.NDSService;


public class ServerFindActivity extends FragmentActivity implements NDSService.CallBack, NetworkRequest.URLReachableCallBack {
    private NDSService mService;
    private boolean mBound = false;
    ProgressDialog progress;
    private ArrayAdapter<String> bonjourServersArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_find);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    public void onClickButton(View view) {
        String a = ((EditText)findViewById(R.id.server_find_manual_ip_a)).getText().toString();
        String b = ((EditText)findViewById(R.id.server_find_manual_ip_b)).getText().toString();
        String c = ((EditText)findViewById(R.id.server_find_manual_ip_c)).getText().toString();
        String d = ((EditText)findViewById(R.id.server_find_manual_ip_d)).getText().toString();
        String ip = a + "." + b + "." + c + "." + d;
        String port = ((EditText)findViewById(R.id.server_find_manual_ip_port)).getText().toString();
        nextActivityWithIp(ip, port);
    }


    @Override
    public void couldConnectToRemoteServer(final String server, final String port) {
        Preferences.sharedInstance(this).edit().putString(Preferences.PREFERENCE_IP, server).apply();
        Preferences.sharedInstance(this).edit().putString(Preferences.PREFERENCE_PORT, port).apply();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(MainActivity.EXTRA_IP, server);
                returnIntent.putExtra(MainActivity.EXTRA_PORT, port);
                setResult(RESULT_OK,returnIntent);
                finish();

            }
        });

    }

    @Override
    public void errorConnectingToRemoteServer(final String server, final String port) {
        Log.d("CONNECTION", "Could not connect to server");
        progress.hide();
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              TextView view = (TextView)findViewById(R.id.server_error);
                              view.setText(R.string.could_not_connect_server);
                              view.setVisibility(View.VISIBLE);
                          }
                      });

    }

    private void nextActivityWithIp(final NDSService.Server server) {
        progress = ProgressDialog.show(this, "Loading", "Wait while loading...");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView view = (TextView) findViewById(R.id.server_error);
                view.setVisibility(View.GONE);
            }
        });
        NetworkRequest.isURLReachable(this, server.ip, server.port, this);

    }

    private void nextActivityWithIp(final String ip, final String port) {
        progress = ProgressDialog.show(this, "Loading", "Wait while loading...");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView view = (TextView) findViewById(R.id.server_error);
                view.setVisibility(View.GONE);
            }
        });

        NetworkRequest.isURLReachable(this, ip, port, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, NDSService.class);
        getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        ListView bonjourServersListView = (ListView)findViewById(R.id.bonjour_server_listview);
        bonjourServersArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        bonjourServersListView.setAdapter(bonjourServersArrayAdapter);
        bonjourServersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nextActivityWithIp(mService.getmRPiAddress().get(position));
            }
        });
        updatedData();


        EditText a = ((EditText)findViewById(R.id.server_find_manual_ip_a));
        EditText b = ((EditText)findViewById(R.id.server_find_manual_ip_b));
        EditText c = ((EditText)findViewById(R.id.server_find_manual_ip_c));
        EditText d = ((EditText)findViewById(R.id.server_find_manual_ip_d));

        EditText port = ((EditText)findViewById(R.id.server_find_manual_ip_port));

        if (Preferences.sharedInstance(this).getString(Preferences.PREFERENCE_IP, "").length() > 0) {
            String ip[] = Preferences.sharedInstance(this).getString(Preferences.PREFERENCE_IP, "").split(".");;
            a.setText(ip[0]);
            b.setText(ip[1]);
            c.setText(ip[2]);
            d.setText(ip[3]);

            port.setText(Preferences.sharedInstance(this).getString(Preferences.PREFERENCE_PORT, ""));

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            mService.setCallBack(null);
            try{
                unbindService(mConnection);
            }catch(Exception e) {

            }
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NDSService.LocalBinder binder = (NDSService.LocalBinder) service;
            mService = binder.getService();
            mService.setCallBack(ServerFindActivity.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void serverListUpdated() {
        updatedData();
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_server_find, container, false);
        }
    }

    public void updatedData() {
        if (bonjourServersArrayAdapter!= null && mBound) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bonjourServersArrayAdapter.clear();

                    if (mService.getmRPiAddress() != null){

                        for (NDSService.Server object : mService.getmRPiAddress()) {

                            bonjourServersArrayAdapter.insert(object.toString(), bonjourServersArrayAdapter.getCount());
                        }
                    }

                    bonjourServersArrayAdapter.notifyDataSetChanged();

                }
            });
        }


    }
}
