package com.ydyazilim.barkodokuma;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    public Button barkodButton,konumEkleButton,konumSilButton,turEkleButton,turSilButton,listeleButton;
    public EditText turText,konumText;
    SQLiteDatabase database;

    public void init(){

        barkodButton=findViewById(R.id.barkodButton);
        turText=findViewById(R.id.turText);
        konumText=findViewById(R.id.konumText);
        konumEkleButton=findViewById(R.id.konumEkleButton);
        konumSilButton=findViewById(R.id.konumSilButton);
        turEkleButton=findViewById(R.id.turEkleButton);
        turSilButton=findViewById(R.id.turSilButton);
        listeleButton=findViewById(R.id.listeleButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
        init();
    }

    public void barkod(View view){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

        turText.setText("");
        konumText.setText("");
    }
    public void listele(View view){
        Intent intent=new Intent(getApplicationContext(),MainActivity4.class);
        startActivity(intent);

        turText.setText("");
        konumText.setText("");
    }

    public void turEkle(View view){

        String t= turText.getText().toString();

        if(turArama(t)==1){
            Toast.makeText(getApplicationContext(), "Tür zaten kayıtlı...", Toast.LENGTH_LONG).show();
            turText.setText("");
        }else{
            try {
                database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS tur ( id INTEGER PRIMARY KEY, Tur VARCHAR)");

                String sqlString = "INSERT INTO tur ( Tur) VALUES ( ?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindString(1,t);
                sqLiteStatement.execute();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Tür kaydedilemedi!!!", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "Tür kaydedildi", Toast.LENGTH_LONG).show();
            turText.setText("");
        }
    }

    public void turSil(View view){

        String t= turText.getText().toString();

        if(turArama(t)==0){
            Toast.makeText(getApplicationContext(), "Bu tür kayıtlı değil...", Toast.LENGTH_LONG).show();
            turText.setText("");
        }else{
            try{
                database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
                database.execSQL("DELETE FROM tur WHERE Tur=? ",new String[]{t});
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Tür silinemedi!!", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "Tür silindi...", Toast.LENGTH_LONG).show();
            turText.setText("");
        }

    }
    public void konumEkle(View view){

        String k = konumText.getText().toString();

        if(konumArama(k)==1){
            Toast.makeText(getApplicationContext(), "Konum zaten kayıtlı...", Toast.LENGTH_LONG).show();
            konumText.setText("");
        }else{
            try {
                database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS konum ( id INTEGER PRIMARY KEY, Konum VARCHAR)");

                String sqlString = "INSERT INTO konum ( Konum) VALUES ( ?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindString(1,k);
                sqLiteStatement.execute();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Konum kaydedilemedi!!!", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "Konum kaydedildi", Toast.LENGTH_LONG).show();
            konumText.setText("");
        }
    }
    public void konumSil(View view){
        String k = konumText.getText().toString();
        if(konumArama(k)==0){
            Toast.makeText(getApplicationContext(), "Bu konum kayıtlı değil...", Toast.LENGTH_LONG).show();
            konumText.setText("");
        }else{
            try{
                database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);
                database.execSQL("DELETE FROM konum WHERE Konum=? ",new String[]{k});
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Konum silinemedi!!", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "Konum silindi...", Toast.LENGTH_LONG).show();
            konumText.setText("");
        }
    }
    public int konumArama(String aranan){
        database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);

        Cursor cursor = database.rawQuery("SELECT Konum FROM konum WHERE Konum = ?",new String[] {aranan});
        int konumIx=cursor.getColumnIndex("Konum");

        //cursor.getString(nameıx);
        if(cursor.getCount() >= 1){
            return 1;
        }else{
            return 0;
        }
    }
    public int turArama(String aranan){
        database = this.openOrCreateDatabase("Nesneler",MODE_PRIVATE,null);

        Cursor cursor = database.rawQuery("SELECT Tur FROM tur WHERE Tur = ?",new String[] {aranan});
        int turIx=cursor.getColumnIndex("Tur");

        //cursor.getString(nameıx);
        if(cursor.getCount() >= 1){
            return 1;
        }else{
            return 0;
        }
    }
}