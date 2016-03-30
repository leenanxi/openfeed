package com.leenanxi.android.open.feed.settings.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.AttributeSet;
import android.widget.Toast;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.widget.colorpicker.ColorPickerDialog;
import com.leenanxi.android.open.feed.widget.colorpicker.ColorPickerSwatch;

/**
 * Created by leenanxi on 16/3/29.
 */
public class ThemePickerDialogPreference extends DialogPreference {
    private static final String TAG_DIALOG_THEME_PIKER =
            "TAG_DIALOG_THEME_PIKER";

    public ThemePickerDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ThemePickerDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ThemePickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemePickerDialogPreference(Context context) {
        super(context);
    }

    public static boolean onDisplayPreferenceDialog(final PreferenceFragmentCompat preferenceFragment,
                                                    Preference preference) {
        if (preference instanceof ThemePickerDialogPreference) {
            // getChildFragmentManager() will lead to looking for target fragment in the child
            // fragment manager.
            FragmentManager fragmentManager = preferenceFragment.getFragmentManager();
            if (fragmentManager.findFragmentByTag(TAG_DIALOG_THEME_PIKER) == null) {

                int[] colors = preferenceFragment.getResources().getIntArray(R.array.theme_colors);
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

                colorPickerDialog.initialize(
                        R.string.settings_theme_picker_title, colors, R.color.quantum_googred, 4, colors.length);
                colorPickerDialog.setTargetFragment(preferenceFragment, 0);
                colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        if (color == ContextCompat.getColor(preferenceFragment.getContext(),R.color.quantum_cyan)){
                            Toast.makeText(preferenceFragment.getContext()," haha",Toast.LENGTH_LONG).show();
                        }

                    }
                });
                colorPickerDialog.show(fragmentManager,TAG_DIALOG_THEME_PIKER);
            }
            return true;
        }
        return false;
    }




}
