package me.tuxtimo.hslu.mobpro.ui_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private CounterViewModel counter;
    private TextView counterLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup grp = (RadioGroup) findViewById(R.id.radioGroup);
        grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup grp, int checkedId) {
                Log.i("foo", String.format("Checked %d", checkedId));

                Intent intent = new Intent(MainActivity.this, LayoutDemoActivity.class);
                intent.putExtra("view", checkedId);
                startActivity(intent);
            }
        });

        Spinner pokeSpinner = (Spinner) findViewById(R.id.spinner);
        pokeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.i("pokemon", String.format("Selected %s", selectedItem));
                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //counter = ViewModelProviders.of(this).get(CounterViewModel.class);
        counterLabel = findViewById(R.id.counterLabel);
        //updateCounter();
    }

    public void increaseCounter(View button) {
        counter.incrementCounter();
        updateCounter();
    }

    private void updateCounter() {
        counterLabel.setText(String.format("Counter: %d", counter.getCounter()));
    }
}
