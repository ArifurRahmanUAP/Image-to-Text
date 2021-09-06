package com.example.textscanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;


public class ShowData extends AppCompatActivity {
    ListView listView;
    Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        listView = findViewById(R.id.listviewid);

        Cursor cursor = database.displayAllData();
        if (cursor.getCount()==0)
        {
            showData("Error","No Data Found");
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext())
        {
            stringBuffer.append("ID: "+cursor.getString(0)+"\n");
            stringBuffer.append("Name: "+cursor.getString(1)+"\n\n");
        }
        showData("ResultSet",stringBuffer.toString());
    }

    public void  showData(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();

    }


//    public void delete(View v){
//        database.delete(Integer.parseInt(id));
//        Toast.makeText(getApplicationContext(),"deleted", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//    }
    }





