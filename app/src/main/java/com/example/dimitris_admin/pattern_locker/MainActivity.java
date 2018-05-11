package com.example.dimitris_admin.pattern_locker;
//kentriki klasi

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.List;

//edw ginontai eisagwgi twn dedomenwn tou xristi
public class MainActivity extends AppCompatActivity {
    private Button go;
    private TextView name_text;
   private EditText name_edit_text,finger_num,hand_num;
    private Intent in;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ftiaxnete  o katalogos pou tha periexei ta stoixeia tou xristi
        final File users_file=new File(getFilesDir(),"Users");
        users_file.mkdir();

        go=(Button) findViewById(R.id.go_button);
        name_edit_text=(EditText)findViewById(R.id.set_name_edit_text);
        finger_num=(EditText)findViewById(R.id.set_finger_num_text);
        hand_num=(EditText)findViewById(R.id.set_hand_num_text);
       share=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
       editor=share.edit();
       editor.putString("Path",users_file.getAbsolutePath());
       editor.apply();
       editor.commit();


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=name_edit_text.getText().toString();
                String finger_n=finger_num.getText().toString();
                String hand_n=hand_num.getText().toString();
                File file=new File(users_file,name);
                if(file.exists())
                {flag=true;}
                else
                {flag=false;}


                //elegxos gia to an ta pedia einai keno
                //kai emfanisi analogou munimatos

                    if(name.equals(""))
                { Toast.makeText(MainActivity.this,"Yo put your name", Toast.LENGTH_SHORT).show();}
                if(flag&&!share.getString("Name","Null").equals(name))
                {Toast.makeText(MainActivity.this,"Name allready exists try another name", Toast.LENGTH_SHORT).show();}
               else if(finger_n.equals(""))
                { Toast.makeText(MainActivity.this,"Yo put your finger number", Toast.LENGTH_SHORT).show();}
                else if(hand_n.equals(""))
                { Toast.makeText(MainActivity.this,"Yo put your hand number", Toast.LENGTH_SHORT).show();}

                else {


                        editor.putString("Name",name);
                        editor.putString("Hand Num",hand_n);
                        editor.putString("Finger Num",finger_n);
                        editor.apply();
                        editor.commit();
                        in = new Intent(MainActivity.this, Locker.class);
                        startActivity(in);

                }




            }
        });






    }
}
