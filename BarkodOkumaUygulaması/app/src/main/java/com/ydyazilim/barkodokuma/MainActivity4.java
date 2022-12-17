package com.ydyazilim.barkodokuma;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {

    public TextView turAdet;
    public Spinner spinnerKonum,spinnerTur;
    public ListView listView;

    SQLiteDatabase database;

    ArrayList<String> turList;
    ArrayList<String> konumList;
    ArrayAdapter<String> arrayAdapterTur;
    ArrayAdapter<String> arrayAdapterKonum;

    ArrayList<String> barkodAd;
    ArrayAdapter arrayAdapter;

    String konumspin,turspin;

    public void init(){

        spinnerKonum=findViewById(R.id.spinnerKonum);
        spinnerTur=findViewById(R.id.spinnerTur);
        listView=findViewById(R.id.listView);
        turAdet=findViewById(R.id.turAdet);

        konumList = new ArrayList<String>();
        konumList.add(0,"Konum Seçiniz:");
        turList = new ArrayList<String>();
        turList.add(0,"Tür Seçiniz:");

        barkodAd =new ArrayList<String>();

        arrayAdapterKonum = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, konumList);
        arrayAdapterKonum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKonum.setAdapter(arrayAdapterKonum);

        arrayAdapterTur = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, turList);
        arrayAdapterTur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTur.setAdapter(arrayAdapterTur);

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,barkodAd);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);

        init();
        konumAl();
        turAl();
        spinnerAl();
    }

    public void konumAl(){
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery("SELECT * FROM konum ORDER BY Konum", null);
            int nameIx = cursor.getColumnIndex("Konum");

            while (cursor.moveToNext()) {
                konumList.add(cursor.getString(nameIx));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void turAl(){
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery("SELECT * FROM tur ORDER BY Tur", null);
            int nameIx = cursor.getColumnIndex("Tur");

            while (cursor.moveToNext()) {
                turList.add(cursor.getString(nameIx));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spinnerAl(){
        spinnerKonum.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                barkodAd.clear();
                konumspin = parent.getItemAtPosition(position).toString();
                listView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        arrayAdapterKonum.notifyDataSetChanged();

        spinnerTur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                turAdet.setText("");
                turspin = parent.getItemAtPosition(position).toString();
                turAdetGoster();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        arrayAdapterTur.notifyDataSetChanged();
    }

    public void listView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getData();
    }

    public void getData() {

        try {
            database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);

            Cursor cursor = database.rawQuery("SELECT * FROM nesneler WHERE KonumAd=? ORDER BY TurAd",new String[] {konumspin});
            int ProductNameIx = cursor.getColumnIndex("BarkodNumara");

            while (cursor.moveToNext()) {

                barkodAd.add(cursor.getString(ProductNameIx));
            }
            arrayAdapter.notifyDataSetChanged();
            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Bilgiler görüntülenemiyor..", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void turAdetGoster(){
        try {
            database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);

            Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM nesneler WHERE KonumAd=? and TurAd=?",new String[] {konumspin,turspin});
            cursor.moveToFirst();
            int count= cursor.getInt(0);
            turAdet.setText(turspin+" sayısı: "+count);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}