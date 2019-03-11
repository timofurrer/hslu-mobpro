package me.tuxtimo.hslu.mobpro.persistenz;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AppPreferenceActivity extends AppCompatActivity {

    public static final class TeaPreferenceInitializer extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_preference);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new TeaPreferenceInitializer()).commit();
    }
}
