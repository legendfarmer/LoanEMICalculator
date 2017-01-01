package com.legendfarmer.loanemicalculator;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by babu-3708 on 24/12/16.
 */

public class EMIUtil
{
    public static boolean isETEmpty(EditText et)
    {
        return et.getText().toString().trim().length() == 0;
    }

    public static boolean isTVEmpty(TextView tv)
    {
        return tv.getText().toString().trim().length() == 0;
    }

    public static String getNumETAsString(EditText et)
    {
        if (isETEmpty(et)) {
            return "0";
        } else {
            return et.getText().toString();
        }
    }

    public static String getNumTVAsString(TextView tv)
    {
        if (isTVEmpty(tv))
        {
            return "0";
        } else {
            return tv.getText().toString();
        }
    }

    public static Float emiCalc(Double p, Float n, Float r)
    {
        double onePlusr = 1d + r;
        Double emi = (p*r* Math.pow((onePlusr),n))/(Math.pow((onePlusr),n)-1d);
        return emi.floatValue();
    }

    public static Float affordableLoan(Double emi, Float n, Float r)
    {
        double onePlusr = 1d + r;
        Double p = emi/ ((r* Math.pow((onePlusr),n))/(Math.pow((onePlusr),n)-1d));
        return p.floatValue();
    }


    public static void resetAllFields(EditText... ets)
    {
        for (EditText e : ets) {
            e.setText("");
        }

    }
}