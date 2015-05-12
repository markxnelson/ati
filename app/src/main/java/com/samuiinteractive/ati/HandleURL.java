package com.samuiinteractive.ati;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class HandleURL extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startIntent = new Intent(HandleURL.this, Start.class);
        // add data bundle with url

        Uri data = ((Uri) getIntent().getData());
        String address = data.toString().replace("http://offline.accesstoinsight.org", "file://");

        Bundle dataBundle = new Bundle();
        dataBundle.putString("url", address);
        System.out.println("[ATI] the url is " + address);
        startIntent.putExtras(dataBundle);
        HandleURL.this.startActivity(startIntent);
        HandleURL.this.finish();

    }

}
