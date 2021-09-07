package com.example.textscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    private ImageView take,copy,retake,history;
        private TextView textView, date;

        private static final int REQUEST_CAMERA_CODE =100;
        Bitmap bitmap;
        Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDateFormat sdf = new SimpleDateFormat(" HH.mm  dd-MM-yyyy ", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        Toast.makeText(MainActivity.this,"Developed by Arifur Rahman",Toast.LENGTH_LONG).show();
        take = findViewById(R.id.take_photo);
        textView = findViewById(R.id.textview);
        copy = findViewById(R.id.copyid);
        retake = findViewById(R.id.retake);
        history = findViewById(R.id.historyid);
        db = new Database(this);

    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED);
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_CODE);
        }

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }
        });

        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copy_text = textView.getText().toString();
                copytoClip(copy_text);
                ContentValues contentValues = new ContentValues();
                contentValues.put("name",textView.getText().toString());
                contentValues.put("date",currentDateandTime);

                Toast.makeText(MainActivity.this, currentDateandTime, Toast.LENGTH_SHORT).show();
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                Long recid = sqLiteDatabase.insert("info",null,contentValues);

                if(recid!=null){

                    Toast.makeText(MainActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                }
                else {

                    Toast.makeText(MainActivity.this, "Something is Wrong pls try again ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowData.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private  void getTextFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational())
        {
            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
        }
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i<textBlockSparseArray.size(); i++)
            {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            textView.setText(stringBuilder.toString());
            copy.setVisibility(View.VISIBLE);
            retake.setVisibility(View.VISIBLE);
            take.setVisibility(View.GONE);
        }
    }

    private void copytoClip(String text)
    {
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Data",text);

        clipBoard.setPrimaryClip(clip);


        Toast.makeText(MainActivity.this,"Copied",Toast.LENGTH_LONG).show();
    }

}