package com.legendfarmer.loanemicalculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends AppCompatActivity {

    private Button calcEMI ;
    private Button compareLoan;
    private Button loanAfford;

    private Button rateUs;
    private Button shareApp;
    private Button moreFromUs;
    private Button feedback;
    public static String PACKAGE_NAME;
    public Tracker mTracker;
    private static final String TAG = "EMI LOAN HOME";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        GoogleAnalyticsApp gaApp = (GoogleAnalyticsApp) getApplication();
        mTracker = gaApp.getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        calcEMI = (Button) findViewById(R.id.emiCalcBtn);
        compareLoan = (Button) findViewById(R.id.compareLoans);
        loanAfford = (Button) findViewById(R.id.loanAffordBtn);

        rateUs = (Button) findViewById(R.id.button_rate);
        moreFromUs = (Button) findViewById(R.id.button_more);
        shareApp = (Button) findViewById(R.id.button_share);
        feedback = (Button) findViewById(R.id.button_about);
        //Assign methods to events

        calcEMI.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Start NewActivity.class
                gaTrackEvent(getString(R.string.home),"Action", getString(R.string.emiCalcScreenName));
                Intent myIntent = new Intent(MainActivity.this,CalculateLoanEMI.class);
                startActivity(myIntent);
            }
        });

        loanAfford.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Start NewActivity.class
                gaTrackEvent(getString(R.string.home),"Action",getString(R.string.loanAfford));
                Intent myIntent = new Intent(MainActivity.this,LoanAffordActivity.class);
                startActivity(myIntent);
            }
        });

        compareLoan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Start NewActivity.class
                gaTrackEvent(getString(R.string.home),"Action",getString(R.string.compare));
                Intent myIntent = new Intent(MainActivity.this,CompareLoansActivity.class);
                startActivity(myIntent);
            }
        });


        rateUs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                rateApp();
            }
        });

        moreFromUs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                moreFromUs();
            }
        });

        shareApp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String body = getString(R.string.shareApp) + PACKAGE_NAME;
                String subject = "Loan-EMI Calculator";
                shareApp(subject,body);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                feedback();
            }
        });

    }


    public void rateApp()
    {
        Uri uri = Uri.parse("market://details?id=" + PACKAGE_NAME);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        gaTrackEvent(getString(R.string.home), "Action", getString(R.string.rateApp));
        try
        {
            // trackEvent(getString(R.string.rateApp));
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + PACKAGE_NAME)));
        }
    }

    public void moreFromUs()
    {
        Uri uri = Uri.parse("market://search?q=pub:LegendFarmer");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        gaTrackEvent(getString(R.string.home), "Action", getString(R.string.tellus));
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try
        {
            // trackEvent(getString(R.string.rateApp));
            startActivity(goToMarket);
        }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/search?q=pub:LegendFarmer")));
        }
    }

    public void shareApp(String subject, String shareBody)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        gaTrackEvent(getString(R.string.home), "Action", getString(R.string.shareApp));

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.shareVia)));
    }

    public void feedback()
    {
        gaTrackEvent(getString(R.string.home),"Action",  getString(R.string.tellus));
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://docs.google.com/forms/d/1lUv1H6pFIkoSke0Pwmzv1LnAHDgoQB8DgxJlex2peEw/viewform?edit_requested=true")));
    }

    public void gaTrackEvent(String category, String event,String label)
    {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(event)
                .setLabel(label)
                .build());
    }

    public void gaTrackEvent(String category, String event)
    {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(event)
                .build());
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
