package com.legendfarmer.loanemicalculator;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.legendfarmer.loanemicalculator.EMIUtil.isETEmpty;

/**
 * Created by babu-3708 on 24/12/16.
 */

public class CompareLoansActivity extends CalcTemplateActivity
{
    public EditText loan1loanAmount;
    public EditText loan1ROI;
    private static final String TAG = "CompareLoans";
    public EditText loan1tenure;
    public EditText loan2loanAmount;
    public EditText loan2ROI;
    public EditText loan2tenure;
    public TextView loan1resEMI;
    public TextView loan2resEMI;
    public TextView loan1resInt;
    public TextView loan2resInt;
    public TextView loan1resTot;
    public TextView loan2resTot;

    public View compareSummRes;
    public Button compareBut;
    public Button resetBut;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialiseGATracker();
        setContentView(R.layout.compare_loan_actitivy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initAssociatedViewElements();
        gaSendScreenName(TAG);
    }

    public void initAssociatedViewElements()
    {
        loan1loanAmount = (EditText) findViewById(R.id.loan1amt);
        loan2loanAmount = (EditText) findViewById(R.id.loan2amt);
        loan1ROI = (EditText) findViewById(R.id.loan1ROI);
        loan2ROI = (EditText) findViewById(R.id.loan2roi);
        loan1tenure = (EditText) findViewById(R.id.loan1mm);
        loan2tenure = (EditText) findViewById(R.id.loan2mm);

        loan1resEMI = (TextView) findViewById(R.id.loan1EMI);
        loan2resEMI = (TextView) findViewById(R.id.loan2Emi);
        loan1resInt = (TextView) findViewById(R.id.loan1interest);
        loan2resInt = (TextView) findViewById(R.id.loan2interest);
        loan1resTot = (TextView) findViewById(R.id.loan1tot);
        loan2resTot = (TextView) findViewById(R.id.loan2tot);
        compareBut = (Button) findViewById(R.id.compareBtn);
        resetBut = (Button) findViewById(R.id.compareClear);

        compareSummRes = (View) findViewById(R.id.compareRes);
        setOnChangeListenerForET(loan1loanAmount,loan2loanAmount,loan1tenure,loan2tenure,loan1ROI,loan2ROI ); //loan1loanAmount, interest, loan2loanAmount,loan1tenure,loan2tenure);

        resetBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EMIUtil.resetAllFields(loan1loanAmount, loan2loanAmount,loan1ROI, loan2ROI,loan1tenure,loan2tenure);
                compareSummRes.setVisibility(View.INVISIBLE);
                gaTrackEvent(getString(R.string.compare_loans),getString(R.string.action),getString(R.string.reset));
            }
        });

        compareBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                toggleResLayout();
                if(isAnyElementEmpty())
                {
                    return;
                }
                hideKeyPad();
                gaTrackEvent(getString(R.string.compare_loans),getString(R.string.oprn),getString(R.string.compare));
                validateDoCalculation();

            }
        });
    }

    public void checkAndHideResLayout()
    {
        if(compareSummRes.getVisibility()==View.VISIBLE)
        {
            compareSummRes.setVisibility(View.INVISIBLE);
        }
    }

    public void toggleResLayout()
    {
        if(compareSummRes.getVisibility()==View.INVISIBLE) {
            compareSummRes.setVisibility(View.VISIBLE);
        }
    }

    public void etTxtChangeListener(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                checkAndHideResLayout();
            }
        });
    }

    void validateDoCalculation()
    {
        calcEMIres( loan1loanAmount, loan1tenure,loan1ROI, loan1resEMI, loan1resTot, loan1resInt);
        calcEMIres(loan2loanAmount, loan2tenure,loan2ROI, loan2resEMI, loan2resTot, loan2resInt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compare_emi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_home)
        {
            goHome();
            gaTrackEvent(getString(R.string.compare_loans),getString(R.string.action),getString(R.string.home));
            finish();
            return true;
        } else if (id == R.id.calcEMI)
        {
            goCalcEMI();
            gaTrackEvent(getString(R.string.compare_loans),getString(R.string.action),getString(R.string.calculate_emi));
            finish();
            return true;
        }
        else if(id==R.id.loanAfford)
        {
            goAffordability();
            gaTrackEvent(getString(R.string.compare_loans),getString(R.string.action),getString(R.string.loanAfford));
            finish();
            return true;
        }
        else if (id == R.id.exitapp)
        {
            gaTrackEvent(getString(R.string.compare_loans),getString(R.string.action),getString(R.string.action_exit));
            finish();
            System.exit(0);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    public void calcEMIres(EditText loanAmount, EditText tenure, EditText roi, TextView resEMI, TextView resTot,  TextView resInt)
    {
        Double loan = Double.parseDouble(EMIUtil.getNumETAsString(loanAmount));
        Float n = Float.parseFloat(EMIUtil.getNumETAsString(tenure));
        Float rate = 0f,r=0f;
        try
        {
            rate = Float.parseFloat(EMIUtil.getNumETAsString(roi));
        }
        catch (Exception ex)
        {
            rate = 0f;
        }
        r = rate /12f/ 100f;

        Float emi = 0f, intAmount = 0f;
        emi = 0f;


        if (isAnyElementEmpty())
        {
            return;
        }
        else
        {
            emi = EMIUtil.emiCalc(loan, n, r);
            intAmount = emi == 0 ? 0 : emi.floatValue() * n - loan.floatValue();

            //set result to views
            resTot.setText(bNotation(loan.floatValue() + intAmount));
            resEMI.setText(bNotation(emi.floatValue()));
            resInt.setText(bNotation(intAmount));
        }
    }
    public boolean isAnyElementEmpty()
    {
        return (isETEmpty(loan1loanAmount) || isETEmpty(loan1ROI) || isETEmpty(loan1tenure) ||
                isETEmpty(loan2loanAmount) || isETEmpty(loan2ROI) || isETEmpty(loan2tenure));
    }


}
