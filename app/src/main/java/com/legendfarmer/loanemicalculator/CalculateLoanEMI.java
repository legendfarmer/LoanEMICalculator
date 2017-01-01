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

import static com.legendfarmer.loanemicalculator.EMIUtil.emiCalc;
import static com.legendfarmer.loanemicalculator.EMIUtil.getNumTVAsString;
import static com.legendfarmer.loanemicalculator.EMIUtil.isETEmpty;

/**
 * Created by babu-3708 on 22/12/16.
 */

public class CalculateLoanEMI extends CalcTemplateActivity
{
    private static final String TAG = "CalculateEMI";
    private EditText loanAmount ;
    private EditText interest;
    private EditText periodsYY;
    private EditText periodsMM;
    private Button reset;
    private Button emiCalcLoanSummBtn;
    private View emiCalcLoanSummRes;

    //Result Views
    private TextView emiCalcEMI;
    private TextView resTenure;
    private TextView resROI;
    private TextView resLoan;
    private TextView resEMI;
    public TextView resInterest;
    public TextView resTotal;
    public Button resShare;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emi_calc_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null)
        {
            setSupportActionBar(toolbar);
        }
        initAssociatedViewElements();
        initialiseGATracker();
        gaSendScreenName(TAG);
    }

    public void initAssociatedViewElements()
    {
        loanAmount = (EditText) findViewById(R.id.emiCalcPrincipal);
        loanAmount.setFilters(new InputFilter[]{new InputFilterForDecimalValues(11, 2)});
        interest = (EditText) findViewById(R.id.emiCalcROI);

        interest.setFilters(new InputFilter[] { new InputFilterForDecimalValues(2,2) });

        periodsYY = (EditText) findViewById(R.id.emiCalcPeriodYY);
        periodsMM = (EditText) findViewById(R.id.emiCalcPeriodMM);
        periodsMM.setFilters(new InputFilter[]{new InputFilterMinMax(0, 11), new InputFilter.LengthFilter(2)});
        periodsYY.setFilters(new InputFilter[]{new InputFilterMinMax(0, 99), new InputFilter.LengthFilter(2)});

        resEMI = (TextView) findViewById(R.id.loanResEMI);
        emiCalcEMI = (TextView) findViewById(R.id.emiCalcEMI);
        resInterest = (TextView) findViewById(R.id.loanResInterest);
        resTotal = (TextView) findViewById(R.id.loanResTot);
        resTenure = (TextView) findViewById(R.id.loanResTenure);
        resLoan = (TextView) findViewById(R.id.loanResLoan);

        reset = (Button) findViewById(R.id.emiCalcReset);
        resShare = (Button) findViewById(R.id.loanResShare);
        emiCalcLoanSummBtn = (Button) findViewById(R.id.emiCalcLoanSummBtn);
        emiCalcLoanSummRes =  findViewById(R.id.emiCalcResLayout);
        //Assign methods to events
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EMIUtil.resetAllFields(interest, periodsYY,periodsMM, loanAmount);
                emiCalcEMI.setText("0.0");
                emiCalcLoanSummRes.setVisibility(View.INVISIBLE);
            }
        });


        emiCalcLoanSummBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                hideKeyPad();
                int loanSummaryVisibility = emiCalcLoanSummRes.getVisibility();
                if(emiCalcLoanSummRes.getVisibility()==View.INVISIBLE)
                {
                    emiCalcLoanSummRes.setVisibility(View.VISIBLE);
                }
                else
                {
                    emiCalcLoanSummRes.setVisibility(View.INVISIBLE);
                }
                gaTrackEvent(getString(R.string.calculate_emi),getString(R.string.action),getString(R.string.loanSumm));
            }
        });

        //Assign methods to events
        resShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                StringBuilder buildContent = new StringBuilder();//
                buildContent.append(getString(R.string.app_name)).append("\n\n");
                buildContent.append(getString(R.string.loanAmt)).append(" : ").append(getNumTVAsString(resLoan)).append("\n");
                buildContent.append(getString(R.string.tenure)).append(" : ").append(getNumTVAsString(resTenure)).append("\n");
                buildContent.append(getString(R.string.perAnnumROI)).append(" : ").append(getNumTVAsString(interest)).append("\n");
                buildContent.append(getString(R.string.intPayable)).append(" : ").append(getNumTVAsString(resInterest)).append("\n");
                buildContent.append(getString(R.string.totPay)).append(" : ").append(getNumTVAsString(resTotal)).append("\n\n");
                buildContent.append(getString(R.string.EMI)).append(" : ").append(getNumTVAsString(resEMI)).append("\n");
                String shareContent = getString(R.string.shareApp)+(MainActivity.PACKAGE_NAME);
                buildContent.append(shareContent);
                gaTrackEvent(getString(R.string.calculate_emi),getString(R.string.action),getString(R.string.shareRes));

                setSharingIntent(getString(R.string.app_name), buildContent.toString());
            }
        });

        setOnChangeListenerForET(loanAmount, interest, periodsYY,periodsMM);
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
        Double p = Double.parseDouble(EMIUtil.getNumETAsString(loanAmount));
        Float n = Float.parseFloat(EMIUtil.getNumETAsString(periodsYY)) *12f + Float.parseFloat(EMIUtil.getNumETAsString(periodsMM));
        Float r = 0f, rate=0f;
        try
        {
            rate = Float.parseFloat(EMIUtil.getNumETAsString(interest));
        }
        catch (Exception ex)
        {
            rate = 0f;
        }
        r = rate /12f/ 100f;

        Float emi = 0f, intAmount = 0f;
        emi = 0f;

        if (!isAnyElementEmpty())
        {
            emi = emiCalc(p, n, r);
            intAmount = emi == 0 ? 0 : emi*n - p.floatValue();
        }

        //set result to views
        resEMI.setText(bNotation(emi));
        emiCalcEMI.setText(bNotation(emi));
        resLoan.setText(bNotation(p.floatValue()));
        resInterest.setText(bNotation(intAmount));
        resTotal.setText(bNotation(p.floatValue() + intAmount));
        resTenure.setText(n.toString() + getString(R.string.months) );
        gaTrackEvent(getString(R.string.calculate_emi),getString(R.string.oprn),getString(R.string.calculate_emi));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calc_emi, menu);
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
            gaTrackEvent(getString(R.string.calculate_emi),getString(R.string.action),getString(R.string.home));
            finish();
            return true;

        } else if (id == R.id.compare)
        {
            goCompare();
            gaTrackEvent(getString(R.string.calculate_emi),getString(R.string.action),getString(R.string.compare));
            finish();
            return true;
        }
        else if(id==R.id.loanAfford)
        {
            goAffordability();
            gaTrackEvent(getString(R.string.calculate_emi),getString(R.string.action),getString(R.string.loanAfford));
            finish();
            return true;
        }
        else if (id == R.id.exitapp) {
            gaTrackEvent(getString(R.string.calculate_emi),getString(R.string.action),getString(R.string.action_exit));
            finish();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean isAnyElementEmpty()
    {
        return isETEmpty(loanAmount) || isETEmpty(interest) || (isETEmpty(periodsYY)  && isETEmpty(periodsMM));
    }

    public void hideResLayout()
    {
        if(emiCalcLoanSummRes.getVisibility()==View.VISIBLE)
        {
            emiCalcLoanSummRes.setVisibility(View.INVISIBLE);
        }
    }

}