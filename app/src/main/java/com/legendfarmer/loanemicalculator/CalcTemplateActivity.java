package com.legendfarmer.loanemicalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by babu-3708 on 29/12/16.
 */

public abstract class CalcTemplateActivity extends AppCompatActivity
{
    public Tracker mTracker;
    public abstract void initAssociatedViewElements();
    public abstract void etTxtChangeListener(EditText et);
    public abstract boolean isAnyElementEmpty();

    public void setOnChangeListenerForET(EditText... args)
    {
        for (EditText et : args)
        {
            etTxtChangeListener(et);
        }
    }

    public void initialiseGATracker()
    {
        if(mTracker==null)
        {
            GoogleAnalyticsApp gaApp = (GoogleAnalyticsApp) getApplication();
            mTracker = gaApp.getDefaultTracker();
        }
    }

    public String bNotation(Float num)
    {
        String res = Float.toString(num);
        if (num > 10000000000000000000000f) {
            res = getString(R.string.infinity);
        } else if (num > 1000000000000f) {
            num = num / 1000000000000f;
            res = Float.toString(num) + " " + getString(R.string.trillion);
        } else if (num > 100000000) {
            num = num / 100000000;
            res = Float.toString(num) + " " + getString(R.string.billion);
        } else if (num > 10000000) {
            num = num / 10000000;
            res = Float.toString(num) + " " + getString(R.string.crore);
        } else if (num > 1000000) {
            num = num / 100000;
            res = Float.toString(num) + " " + getString(R.string.lakh);
        }
        return res;
    }

    public void setSharingIntent(String subject, String shareBody)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }



    public void hideKeyPad()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public void goHome()
    {
        Intent myIntent = new Intent(this,MainActivity.class);
        startActivity(myIntent);
    }

    public void goCalcEMI()
    {
        Intent myIntent = new Intent(this,CalculateLoanEMI.class);
        startActivity(myIntent);
    }

    public void goCompare()
    {
        Intent myIntent = new Intent(this,CompareLoansActivity.class);
        startActivity(myIntent);
    }

    public void goAffordability()
    {
        Intent myIntent = new Intent(this,LoanAffordActivity.class);
        startActivity(myIntent);
    }


    public void gaSendScreenName(String screenName)
    {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void gaTrackEvent(String category, String event, String label)
    {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(event)
                .setAction(label)
                .build());
    }
}
