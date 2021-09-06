package com.example.textscanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ShowData extends AppCompatActivity {
    ListView listView;
    String[] name;
    int[] id;
    SQLiteDatabase sqLiteDatabase;
    private Database database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        listView = findViewById(R.id.listviewid);
            database = new Database(this);
        //loadData();
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        loadData();
        ImageView delete = findViewById(R.id.delete);



//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                new AlertDialog.Builder(ShowData.this).setIcon(android.R.drawable.ic_delete).setTitle("Are you sure to Delete?").setMessage("Do you want to delete this item?").setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int position) {
//
//                        database.delete_byID(position);
//                    }
//                }).setNegativeButton("No",null).show();
//
//                return true;
//            }
//        });

    }

    public void loadData() {
        ArrayList<String> listData = new ArrayList<>();
        Cursor cursor = database.displayAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No Data in Database", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                listData.add(cursor.getString(0) + " \t " + cursor.getString(1));
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textview_id, listData);
        listView.setAdapter(adapter);


    }


    private class Custom extends BaseAdapter {


        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView edit,delete;
            TextView textView;
            convertView = LayoutInflater.from(ShowData.this).inflate(R.layout.sample_view,parent,false);

            delete = convertView.findViewById(R.id.delete);
            textView = convertView.findViewById(R.id.textview_id);
            textView.setText(name[position]);

//            edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("id",id[position]);
//                    bundle.putString("name",name[position]);
//                    Intent intent = new Intent(ShowData.this,MainActivity.class);
//                    intent.putExtra("userdata",bundle);
//                    startActivity(intent);
//                }
//            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqLiteDatabase= database.getWritableDatabase();
                    long recd = sqLiteDatabase.delete("thing","id="+id[position],null);
                    if (recd!=1){
                        Toast.makeText(ShowData.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                }
            });

            return convertView;
        }
    }


}





