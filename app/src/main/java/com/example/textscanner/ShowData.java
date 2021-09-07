package com.example.textscanner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ShowData extends AppCompatActivity {
    ListView listView;
    ImageView empty;
    String[] name , date;
    int[] id;
    SQLiteDatabase sqLiteDatabase;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        listView = findViewById(R.id.listviewid);
            database = new Database(this);
            empty = findViewById(R.id.empty);
            dis();
    }

    private void dis() {
        sqLiteDatabase=database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select *from info",null);
        if(cursor.getCount()>0){
            id= new int[cursor.getCount()];
            name= new String[cursor.getCount()];
            date = new String[cursor.getCount()];

            int i=0;
            while (cursor.moveToNext()){
                id[i]=cursor.getInt(0);
                name[i]=cursor.getString(1);
                date[i]=cursor.getString(2);
                i++;
            }
            Custom adapter=new Custom();
            listView.setAdapter(adapter);
        }
    else if (cursor.getCount()==0)
        {
            empty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            Toast.makeText(ShowData.this,"Empty",Toast.LENGTH_LONG).show();
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.textview_id);
                final String test = textView.getText().toString();
                copytoClip(test);

                return true;
            }
        });
    }

    private void copytoClip(String text)
    {
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Data",text);
        clipBoard.setPrimaryClip(clip);

        Toast.makeText(ShowData.this,"Copied",Toast.LENGTH_SHORT).show();
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
            ImageView delete;
            TextView textView, textView1;
            convertView = LayoutInflater.from(ShowData.this).inflate(R.layout.sample_view,parent,false);
            delete = convertView.findViewById(R.id.delete);
            textView = convertView.findViewById(R.id.textview_id);
            textView1 = convertView.findViewById(R.id.textview_date);
            textView.setText(name[position]);
            textView1.setText(date[position]);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqLiteDatabase= database.getWritableDatabase();
                    long recd = sqLiteDatabase.delete("info","id="+id[position],null);
                    if (recd!=1){
                        Toast.makeText(ShowData.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                    dis();
                }
            });

            return convertView;
        }
    }

}
