package com.ydyazilim.barkodokuma;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    public Button kaydetButton,guncelleButton,silButton;
    public TextView ProductId,urunAdi,konumText,turText;
    Spinner spinner,spinner2;
    SQLiteDatabase database;

    ArrayList<String> turList;
    ArrayList<String> konumList;
    ArrayAdapter<String> arrayAdapterTur;
    ArrayAdapter<String> arrayAdapterKonum;
    ArrayAdapter arrayAdapter;

    String  konumspin,turspin;



    public void init(){
        urunAdi=findViewById(R.id.urunAdi);
        ProductId=findViewById(R.id.ProductId);
        konumText=findViewById(R.id.konumText);
        turText=findViewById(R.id.turText);
        kaydetButton=findViewById(R.id.kaydetButton);
        guncelleButton=findViewById(R.id.guncelleButton);
        silButton=findViewById(R.id.silButton);
        spinner=findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinner2);

        turList = new ArrayList<String>();
        konumList = new ArrayList<String>();
        turList.add(0,"Tür Seçiniz:");
        konumList.add(0,"Konum Seçiniz:");

        spinner=findViewById(R.id.spinner);
        spinner2=findViewById(R.id.spinner2);
        arrayAdapterKonum = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, konumList);
        arrayAdapterTur = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, turList);
        arrayAdapterKonum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterTur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapterKonum);
        spinner2.setAdapter(arrayAdapterTur);

        /*try {
            database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
            database.execSQL("INSERT INTO nesneler (BarkodNumara) VALUES (?)",new String[] {"deneme"});
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Bilgiler kaydedilemedi!!!", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        init();
        konumAl();
        turAl();
        spinnerAl();

        Intent intent=getIntent();
        String productId=intent.getStringExtra("UrunId");
        ProductId.setText(productId);

        database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);

        if (arama(productId)==1) {

            kaydetButton.setVisibility(View.INVISIBLE);
            guncelleButton.setVisibility(View.VISIBLE);
            silButton.setVisibility(View.VISIBLE);

        } else{

            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Kaydedilsin mi");
            alert.setMessage("Emin misin?");
            alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    kaydetButton.setVisibility(View.VISIBLE);
                    guncelleButton.setVisibility(View.INVISIBLE);
                    silButton.setVisibility(View.INVISIBLE);

                }
            });
            alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent=new Intent(getApplicationContext(),MainActivity3.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Kaydedilemedi...", Toast.LENGTH_LONG).show();
                }
            });
            alert.show();
        }
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
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                konumspin = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        arrayAdapterKonum.notifyDataSetChanged();

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                turspin = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        arrayAdapterTur.notifyDataSetChanged();
    }

    public void kaydet(View view){

        String productName = ProductId.getText().toString();


        try {
            database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS nesneler ( id INTEGER PRIMARY KEY, BarkodNumara VARCHAR, KonumAd VARCHAR, TurAd VARCHAR)");

            String sqlString = "INSERT INTO nesneler ( BarkodNumara, KonumAd, TurAd) VALUES ( ?, ?, ?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,productName);
            sqLiteStatement.bindString(2,konumspin);
            sqLiteStatement.bindString(3,turspin);
            sqLiteStatement.execute();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Bilgiler kaydedilemedi!!!", Toast.LENGTH_LONG).show();
        }
        Intent iintent = new Intent(MainActivity2.this,MainActivity3.class);
        iintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iintent);
        Toast.makeText(getApplicationContext(), "Bilgiler kaydedildi", Toast.LENGTH_LONG).show();
    }

    public void guncelle(View view){

        Intent intent=getIntent();
        String productId=intent.getStringExtra("UrunId");

        try {
            database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);

            database.execSQL("UPDATE nesneler SET KonumAd=? WHERE BarkodNumara=? ",new String[]{konumspin,productId});
            database.execSQL("UPDATE nesneler SET TurAd=? WHERE BarkodNumara=? ",new String[]{turspin,productId});

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Bilgiler güncellenemedi!!!", Toast.LENGTH_LONG).show();
        }
        Intent iintent = new Intent(MainActivity2.this,MainActivity3.class);
        iintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iintent);
        Toast.makeText(getApplicationContext(), "Bilgiler güncellendi...", Toast.LENGTH_LONG).show();
    }

    public void sil(View view){

        Intent intent=getIntent();
        String productId=intent.getStringExtra("UrunId");

        try{
            database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
            database.execSQL("DELETE FROM nesneler WHERE BarkodNumara=? ",new String[]{productId});
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Ürün silinemedi!!", Toast.LENGTH_LONG).show();
        }
        Intent iintent = new Intent(MainActivity2.this,MainActivity3.class);
        iintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iintent);
        Toast.makeText(getApplicationContext(), "Ürün silindi...", Toast.LENGTH_LONG).show();

    }

    public int arama(String aranan){
        database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);

        Cursor cursor = database.rawQuery("SELECT BarkodNumara FROM nesneler WHERE BarkodNumara = ?",new String[] {aranan});
        int nameıx=cursor.getColumnIndex("BarkodNumara");

        //cursor.getString(nameıx);

        if(cursor.getCount() >= 1){
            return 1;
        }else{
            return 0;
        }
    }

}