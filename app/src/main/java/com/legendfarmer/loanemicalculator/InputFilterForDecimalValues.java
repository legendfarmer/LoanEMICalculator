package com.legendfarmer.loanemicalculator;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Boosan on 24/12/16.
 */

public class InputFilterForDecimalValues implements InputFilter
{
    private int maxDigitsBeforeDecimalPoint, maxDigitsAfterDecimalPoint;

    public InputFilterForDecimalValues(int digitsBeforeDecPoint, int digitsAfterDecPoint)
    {
        this.maxDigitsBeforeDecimalPoint = digitsBeforeDecPoint;
        this.maxDigitsAfterDecimalPoint = digitsAfterDecPoint;
    }

    public InputFilterForDecimalValues(String before, String after) {
        this.maxDigitsBeforeDecimalPoint = Integer.parseInt(before);
        this.maxDigitsAfterDecimalPoint = Integer.parseInt(after);
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder(dest);
        builder.replace(dstart, dend, source.subSequence(start, end).toString());
        if (!builder.toString().matches(
                "(([1-9]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?)?(\\.[0-9]{0,"+maxDigitsAfterDecimalPoint+"})?"

        )) {
            if(source.length()==0)
                return dest.subSequence(dstart, dend);
            return "";
        }

        return null;

    }
}


