package com.theleafapps.pro.hydrationreminder.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

/*
 * Add this to your XML resource.
 */
public class NumberPickerPreference extends DialogPreference {

    private NumberPicker numberPicker;
    private String mText;
    private boolean mTextSet;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateDialogView() {
        return generateNumberPicker();
    }

    public NumberPicker generateNumberPicker() {
        numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(15);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(15);

        /*
         * Anything else you want to add to this.
         */

        return numberPicker;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            int value = numberPicker.getValue();
            Log.d("NumberPickerPreference", "NumberPickerValue : " + value);
            persistString(String.valueOf(value));
            if (callChangeListener(value)) {
                setText(String.valueOf(value));
            }
        }
    }

    /**
     * Saves the text to the {@link SharedPreferences}.
     *
     * @param text The text to save
     */
    public void setText(String text) {
        // Always persist/notify the first time.
        final boolean changed = !TextUtils.equals(mText, text);
        if (changed || !mTextSet) {
            mText = text;
            mTextSet = true;
            persistString(text);
            if (changed) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

}