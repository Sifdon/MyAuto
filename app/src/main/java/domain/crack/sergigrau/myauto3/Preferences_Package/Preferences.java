package domain.crack.sergigrau.myauto3.Preferences_Package;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;


import domain.crack.sergigrau.myauto3.R;

/**
 * Created by SergiGrau on 27/5/17.
 */

public class Preferences extends PreferenceActivity {

    public CheckBoxPreference notifications, network;
    public EditTextPreference name;
    public ListPreference language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onStop() {
        super.onStop();

        notifications = (CheckBoxPreference)findPreference("notificaciones");
        language = (ListPreference)findPreference("idioma");
        name = (EditTextPreference) findPreference("user");
        network = (CheckBoxPreference) findPreference("network");

        SharedPreferences prefs =  getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("notifications",notifications.isChecked());
        editor.putString("idioma", language.getValue());
        editor.putBoolean("network", network.isChecked());
        editor.putString("name",name.getText().toString());
        editor.commit();
    }

}