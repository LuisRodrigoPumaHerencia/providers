package com.rodrigo.providers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        solicitar_permisos();

        //ONTENIENDO LOS COMPONENTES
        MaterialButton btn_todos = findViewById(R.id.todos);
        MaterialButton btn_personalizado = findViewById(R.id.personalizado);
        TextView txv_contactos = findViewById(R.id.contactos);
        TextInputEditText input_nombre = findViewById(R.id.nombre_contacto);

        btn_todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String [] listaProjections = new String[]{
                        ContactsContract.Data._ID,
                        ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE
                };

                String selectionClause = ContactsContract.Data.MIMETYPE+"= '"+
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE+"' AND "+
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";

                Cursor cursor = getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI,
                        listaProjections,
                        selectionClause,
                        null,
                        null
                );

                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()){
                    builder.append(cursor.getString(0)+"-");
                    builder.append(cursor.getString(1)+"-");
                    builder.append(cursor.getString(2)+"-");
                    builder.append(cursor.getString(3)+"-");
                    builder.append("\n");
                }
                txv_contactos.setText(builder.toString());

            }
        });

        btn_personalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String [] listaProjections = new String[]{
                        ContactsContract.Data._ID,
                        ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE
                };

                String selectionClause = ContactsContract.Data.MIMETYPE+"= '"+
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE+"' AND "+
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL AND "+
                        ContactsContract.Data.DISPLAY_NAME+" LIKE '%"+String.valueOf(input_nombre.getText())+"%'";

                String order = ContactsContract.Data.DISPLAY_NAME+" DESC";

                Cursor cursor = getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI,
                        listaProjections,
                        selectionClause,
                        null,
                        order
                );

                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()){
                    builder.append(cursor.getString(0)+" ");
                    builder.append(cursor.getString(1)+" ");
                    builder.append(cursor.getString(2)+" ");
                    builder.append(cursor.getString(3)+" ");
                    builder.append("\n");
                }
                txv_contactos.setText(builder.toString());

            }
        });


    }

    public void solicitar_permisos(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)){
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1000);
            }
        }
    }
}