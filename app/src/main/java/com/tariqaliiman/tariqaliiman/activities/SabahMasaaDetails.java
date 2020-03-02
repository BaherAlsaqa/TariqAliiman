package com.tariqaliiman.tariqaliiman.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.classes.Muslim;

public class SabahMasaaDetails extends AppCompatActivity {

    private static final String TAG = "baher";
    String[] ids;
    String[] contents;
    String[] references;
    String[] counters;
    TextView contentTV, referenceTV;
    Button counter;
    ImageButton next, previous;
    int index = 0;
    int counterAthkar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabah_masaa_details);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent != null) {

            final String nameS = intent.getStringExtra(Contains.name);

            LayoutInflater inflator = LayoutInflater.from(this);
            @SuppressLint("InflateParams") View v = inflator.inflate(R.layout.titleview, null);
            ((TextView)v.findViewById(R.id.title1)).setText(nameS);
            getSupportActionBar().setCustomView(v);

            int athkarId = intent.getIntExtra(Constants.athkar_id, -1);


            contentTV = findViewById(R.id.content);
            referenceTV = findViewById(R.id.reference);
            counter = findViewById(R.id.counter);
            next = findViewById(R.id.next);
            previous = findViewById(R.id.previous);

            Resources resources = getResources();

            if (athkarId == 27){
                ids = resources.getStringArray(R.array.sabah_ids);
                contents = resources.getStringArray(R.array.sabah_content);
                references = resources.getStringArray(R.array.reference_sabah);
                counters = resources.getStringArray(R.array.counter_sabah);
            }else if (athkarId == 28){
                ids = resources.getStringArray(R.array.masaa_ids);
                contents = resources.getStringArray(R.array.masaa_content);
                references = resources.getStringArray(R.array.reference_masaa);
                counters = resources.getStringArray(R.array.counter_masaa);
            }

            counter.setText(counters[index]);
            contentTV.setText(contents[index]);
            referenceTV.setText(references[index]);

            counterAthkar = Integer.parseInt(counters[index]);

            counter.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    Log.d(Constants.log + "counter", "counters[index] = " + counters[index]);
                    if (counterAthkar > 0) {
                        counterAthkar -= 1;
                        counter.setText(counterAthkar + "");
                    }
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(Constants.log + "next", "index = " + index + "| ids length = " + ids.length);
                    if (index < ids.length - 1) {
                        index += 1;
                        counter.setText(counters[index]);
                        contentTV.setText(contents[index]);
                        referenceTV.setText(references[index]);
                        counterAthkar = Integer.parseInt(counters[index]);
                    }
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (index > 0) {
                        index -= 1;
                        counter.setText(counters[index]);
                        contentTV.setText(contents[index]);
                        referenceTV.setText(references[index]);
                        counterAthkar = Integer.parseInt(counters[index]);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
