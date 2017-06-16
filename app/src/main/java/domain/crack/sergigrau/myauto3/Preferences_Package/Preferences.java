package domain.crack.sergigrau.myauto3.Preferences_Package;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


import domain.crack.sergigrau.myauto3.R;

/**
 * Created by SergiGrau on 27/5/17.
 */

public class Preferences extends PreferenceActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();



    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        public CheckBoxPreference notifications, network;
        public EditTextPreference name;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            notifications = (CheckBoxPreference)findPreference("notificaciones");
            name = (EditTextPreference) findPreference("user");
            network = (CheckBoxPreference) findPreference("network");
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = SP.edit();
            editor.putBoolean("notifications",notifications.isChecked());
            editor.putBoolean("network", network.isChecked());
            editor.putString("name",name.getText().toString());
            editor.commit();


        }
    }

}