package com.legendfarmer.loanemicalculator;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.legendfarmer.loanemicalculator.EMIUtil.getNumTVAsString;
import static com.legendfarmer.loanemicalculator.EMIUtil.isETEmpty;
import static com.legendfarmer.loanemicalculator.R.id.affordROI;

/**
 * Created by babu-3708 on 24/12/16.
 */

public class LoanAffordActivity extends CalcTemplateActivity {
    private EditText loanEMI;
    private EditText interest;
    private EditText periodsYY;
    private EditText periodsMM;
    public TextView eligibleLoan;
    private Button reset;
    private Button emiCalcLoanSummBtn;
    private View emiCalcLoanSummRes;

    private TextView loanResInterest;
    private TextView loanResTot;
    private TextView loanResLoan;
    private TextView loanResEMI;
    private TextView loanResTenure;
    private Button loanResShare;
    private static final String TAG = "LoanAffordability";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialiseGATracker();
        setContentView(R.layout.loan_afforadbility);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initAssociatedViewElements();
        gaSendScreenName(TAG);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_loan_afford, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.menu_home)
        {
            goHome();
            gaTrackEvent(getString(R.string.loanAfford),getString(R.string.action),getString(R.string.home));
            finish();
            return true;
        } else if (id == R.id.compare)
        {
            goCompare();
            gaTrackEvent(getString(R.string.loanAfford),getString(R.string.action),getString(R.string.compare));
            finish();
            return true;
        }
        else if(id==R.id.calcEMI)
        {
            goCalcEMI();
            gaTrackEvent(getString(R.string.loanAfford),getString(R.string.action),getString(R.string.calculate_emi));
            finish();
            return true;
        }
        else if (id == R.id.exitapp) {
            gaTrackEvent(getString(R.string.loanAfford),getString(R.string.action),getString(R.string.action_exit));
            finish();
            System.exit(0);
            return true;
        }
        else
        {
           return super.onOptionsItemSelected(item);
        }

    }

    public void initAssociatedViewElements()
    {
        loanEMI = (EditText) findViewById(R.id.affordEMI);
        loanEMI.setFilters(new InputFilter[]{new InputFilterForDecimalValues(9, 2)});
        interest = (EditText) findViewById(affordROI);
        interest.setFilters(new InputFilter[]{new InputFilterForDecimalValues(2, 2)});
        periodsYY = (EditText) findViewById(R.id.affordYY);
        periodsMM = (EditText) findViewById(R.id.affordMM);
        periodsMM.setFilters(new InputFilter[]{new InputFilterMinMax(0, 11), new InputFilter.LengthFilter(2)});
        periodsYY.setFilters(new InputFilter[]{new InputFilterMinMax(0, 99), new InputFilter.LengthFilter(2)});
        eligibleLoan = (TextView) findViewById(R.id.affordLoanRes);

        loanResInterest = (TextView) findViewById(R.id.loanResInterest);
        loanResTot = (TextView) findViewById(R.id.loanResTot);
        loanResTenure = (TextView) findViewById(R.id.loanResTenure);
        loanResLoan = (TextView) findViewById(R.id.loanResLoan);
        loanResEMI = (TextView) findViewById(R.id.loanResEMI);
        loanResShare = (Button) findViewById(R.id.loanResShare);
        reset = (Button) findViewById(R.id.afforReset);
        emiCalcLoanSummBtn = (Button) findViewById(R.id.affordLoanSummBtn);
        emiCalcLoanSummRes = findViewById(R.id.affordLoanSummLayout);


        //Assign methods to events
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMIUtil.resetAllFields(interest, periodsYY, periodsMM, loanEMI);
                eligibleLoan.setText("0.0");
                emiCalcLoanSummRes.setVisibility(View.INVISIBLE);
                gaTrackEvent(getString(R.string.loanAfford),getString(R.string.action),getString(R.string.reset));
            }
        });


        emiCalcLoanSummBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyPad();
                int loanSummaryVisibility = emiCalcLoanSummRes.getVisibility();
                if (emiCalcLoanSummRes.getVisibility() == View.INVISIBLE) {
                    emiCalcLoanSummRes.setVisibility(View.VISIBLE);
                } else {
                    emiCalcLoanSummRes.setVisibility(View.INVISIBLE);
                }
                gaTrackEvent(getString(R.string.loanAfford),getString(R.string.action),getString(R.string.loanSumm));
            }
        });


        //Assign methods to events
        loanResShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                StringBuilder buildContent = new StringBuilder();//
                buildContent.append(getString(R.string.app_name)).append("\n\n");
                buildContent.append(getString(R.string.loanAmt)).append(" : ").append(getNumTVAsString(loanResLoan)).append("\n");
                buildContent.append(getString(R.string.tenure)).append(" : ").append(getNumTVAsString(loanResTenure)).append("\n");
                buildContent.append(getString(R.string.perAnnumROI)).append(" : ").append(getNumTVAsString(interest)).append("\n");
                buildContent.append(getString(R.string.intPayable)).append(" : ").append(getNumTVAsString(loanResInterest)).append("\n");
                buildContent.append(getString(R.string.totPay)).append(" : ").append(getNumTVAsString(loanResTot)).append("\n\n");
                buildContent.append(getString(R.string.EMI)).append(" : ").append(getNumTVAsString(loanResEMI)).append("\n");
                String shareContent = getString(R.string.shareApp)+(MainActivity.PACKAGE_NAME);
                buildContent.append(shareContent);
                gaTrackEvent(getString(R.string.loanAfford),getString(R.string.action),getString(R.string.shareRes));
                setSharingIntent(getString(R.string.loanAfford), buildContent.toString());
            }
        });

        setOnChangeListenerForET(loanEMI, interest, periodsYY, periodsMM);
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
                hideResLayout();
                if(isAnyElementEmpty())
                {
                    return;
                }
                validateDoCalculation();
            }
        });
    }

    public void validateDoCalculation()
    {
        Double emi = Double.parseDouble(EMIUtil.getNumETAsString(loanEMI));
        Float n = Float.parseFloat(EMIUtil.getNumETAsString(periodsYY)) *12f + Float.parseFloat(EMIUtil.getNumETAsString(periodsMM));
        Float rate = 0f,r=0f;
        try
        {
            rate = Float.parseFloat(EMIUtil.getNumETAsString(interest));
        }
        catch (Exception ex)
        {
            rate = 0f;
        }
        r = rate /12f/ 100f;

        Float p = 0f, intAmount = 0f;
        p = 0f;

        if (!isAnyElementEmpty())
        {
            p = EMIUtil.affordableLoan(emi, n, r);
            intAmount = emi == 0 ? 0 : emi.floatValue()*n - p.floatValue();
        }

        //set result to views
        eligibleLoan.setText(bNotation(p));


        loanResTot.setText(bNotation(p.floatValue() + intAmount));
        loanResEMI.setText(bNotation(emi.floatValue()));
        loanResTenure.setText(n.toString() + " mon" );
        loanResInterest.setText(bNotation(intAmount));
        loanResLoan.setText(bNotation(p));
        gaTrackEvent(getString(R.string.loanAfford),getString(R.string.oprn),getString(R.string.loanAfford));
    }


    public boolean isAnyElementEmpty()
    {
        return isETEmpty(loanEMI) || isETEmpty(interest) || (isETEmpty(periodsYY)  && isETEmpty(periodsMM));
    }

    public void hideResLayout()
    {
        if(emiCalcLoanSummRes.getVisibility()==View.VISIBLE)
        {
            emiCalcLoanSummRes.setVisibility(View.INVISIBLE);
        }
    }

}
