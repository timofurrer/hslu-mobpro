package me.tuxtimo.hslu.mobpro.persistenz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    private final String COUNTER_KEY = "BATMAN";
    private final String localTextFilename = "text";
    private final int STORE_PERMISSIONS_REQUEST_CODE = 42;
    private final int LOAD_PERMISSIONS_REQUEST_CODE = 43;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final int newResumeCount = preferences.getInt(COUNTER_KEY, 0) + 1;
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(COUNTER_KEY, newResumeCount);
        editor.apply();

        showUpdatedResumeCount(newResumeCount);

        showTeaPrefs();
    }

    private void showUpdatedResumeCount(final int resumeCount) {
        ((TextView) findViewById(R.id.resumeCounterLabel)).setText(String.format("Wow! I counted %d resumes so far.", resumeCount));
    }

    private void showTeaPrefs() {
        final SharedPreferences teaPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean sweetened = teaPreferences.getBoolean("teaWithSugar", false);
        final String sweetener = teaPreferences.getString("teaSweetener", "na");
        final String preferred = teaPreferences.getString("teaPreferred", "meinen");

        String sweetenedString = "";
        String preferredString = "";

        if(sweetened) {
            sweetenedString = String.format(", mit %s gesuesst", sweetener);
        }

        ((TextView) findViewById(R.id.teaPrefsLabel)).setText(String.format("Ich trinke am liebsten %s Tee%s", preferred, sweetenedString));
    }

    public void showPreferencesView(View button) {
        Intent intent = new Intent(this, AppPreferenceActivity.class);
        startActivity(intent);
    }

    public void setDefaultPreferences(View button) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("teaWithSugar", true);
        editor.putString("teaSweetener", "Zucker");
        editor.putString("teaPreferred", "Medina");
        editor.apply();

        showTeaPrefs();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case STORE_PERMISSIONS_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), String.format("Permission %s denied!", permissions[0]), Toast.LENGTH_LONG).show();
                } else {
                    storeExternalFile();
                }
                break;
            case LOAD_PERMISSIONS_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), String.format("Permission %s denied!", permissions[0]), Toast.LENGTH_LONG).show();
                } else {
                    loadExternalFile();
                }
                break;
        }
    }

    public void storeText(View button) {
        final boolean useExternalStorage = ((CheckBox) findViewById(R.id.storeCheckbox)).isChecked();

        if(useExternalStorage) {
            storeExternalFileWithPermissions();
        } else {
            final String text = ((TextInputEditText) findViewById(R.id.textInput)).getText().toString();
            try {
                FileOutputStream outputStream = openFileOutput(localTextFilename, Context.MODE_PRIVATE);
                outputStream.write(text.getBytes());
                outputStream.close();
            } catch(final IOException exc) {
                Toast.makeText(getApplicationContext(), String.format("Uuuops: %s", exc.toString()), Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(getApplicationContext(), String.format("Stored: %s", text), Toast.LENGTH_LONG).show();
        }
    }

    private void storeExternalFileWithPermissions() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(getApplicationContext(), "Externer Speicher ist momentan nicht verfuegbar", Toast.LENGTH_LONG).show();
        } else {
            int grant = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (grant != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORE_PERMISSIONS_REQUEST_CODE);
            } else {
                storeExternalFile();
            }
        }
    }

    private void storeExternalFile() {
        Path path = Paths.get(Environment.getExternalStorageDirectory().getAbsolutePath(), localTextFilename);
        final String text = ((TextInputEditText) findViewById(R.id.textInput)).getText().toString();
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path.toString()));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), String.format("Stored: %s", text), Toast.LENGTH_LONG).show();
    }

    public void loadText(View button) {
        final boolean useExternalStorage = ((CheckBox) findViewById(R.id.storeCheckbox)).isChecked();

        if (useExternalStorage) {
            loadExternalFileWithPermissions();
        } else {
            try {
                String text = readFile(localTextFilename);

                final EditText editText = (EditText) findViewById(R.id.textInput);
                Toast.makeText(getApplicationContext(), String.format("Loaded: %s", text), Toast.LENGTH_LONG).show();
                editText.setText(text);
            } catch(FileNotFoundException exc) {
                Toast.makeText(getApplicationContext(), String.format("Speichere zuerst einen Text!"), Toast.LENGTH_LONG).show();
                return;
            } catch(IOException exc){
                Toast.makeText(getApplicationContext(), String.format("Die Datei konnte nicht ausgelesen werden: %s", exc.toString()), Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private void loadExternalFileWithPermissions() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(getApplicationContext(), "Externer Speicher ist momentan nicht verfuegbar", Toast.LENGTH_LONG).show();
        } else {
            int grant = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if(grant != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, LOAD_PERMISSIONS_REQUEST_CODE);
            } else {
                loadExternalFile();
            }
        }
    }

    private void loadExternalFile() {
        Path path = Paths.get(Environment.getExternalStorageDirectory().getAbsolutePath(), localTextFilename);
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final EditText editText = (EditText) findViewById(R.id.textInput);
        String text = new String(encoded, StandardCharsets.UTF_8);
        Toast.makeText(getApplicationContext(), String.format("Loaded: %s", text), Toast.LENGTH_LONG).show();
        editText.setText(text);
    }

    public String readFile(String fileName) throws FileNotFoundException, IOException {
        StringBuilder text = new StringBuilder();
        FileInputStream fis = openFileInput(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(fis)));

        try {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
            return text.toString();
        }
        finally {
            br.close();
        }
    }
}
