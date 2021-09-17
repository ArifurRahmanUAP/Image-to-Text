package com.example.textscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Update extends AppCompatActivity {
    Database database;
    SQLiteDatabase sqLiteDatabase;
    String name, date;
    ImageView edit;
    EditText nameedt;
    TextView textView1;
    private static int id = 0;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nameedt = findViewById(R.id.textview_id1);
        edit = findViewById(R.id.edit1);
        database = new Database(Update.this);
        textView1 = findViewById(R.id.textview_date1);
        date = getIntent().getStringExtra("date");
        name = getIntent().getStringExtra("name");

        nameedt.setText(name);
        textView1.setText(date);


        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-7148413509095909/1143566527");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Update.this)
                        .setMessage("Are you sure you want to Update?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("date", textView1.getText().toString());
                                contentValues.put("name", nameedt.getText().toString());
                                sqLiteDatabase = database.getWritableDatabase();
                                long recid = sqLiteDatabase.update("info",contentValues,"name=?", new String[]{name});
                                if (recid != -1) {
                                    Toast.makeText(Update.this, "Data update successfully", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(Update.this, "Something wrong try again", Toast.LENGTH_SHORT).show();
                                }

                                Intent i = new Intent(Update.this, ShowData.class);
                                startActivity(i);
                            }

                        }).create().show();
            }

        });
    }
}
