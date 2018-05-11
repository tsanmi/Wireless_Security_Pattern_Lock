package com.example.dimitris_admin.pattern_locker;
//klasi stin opoia me tin boithia tis etoimis
//ulopoiisis tou pattern lock ginetai sulogi twn dedomenwn


import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.math.*;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;
import com.example.dimitris_admin.pattern_locker.listener.*;
import com.example.dimitris_admin.pattern_locker.utils.*;
import com.example.dimitris_admin.pattern_locker.file_utils.*;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Locker extends AppCompatActivity implements SensorEventListener{
    private String pat;
    private Button Start,Results,Stop;

    private PatternLockView mPatternLockView;
    private TextView text_user_name;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private  int count26=1;
    private  int count10=1;
    private  int count3=1;
    private ArrayList<String> paterns10=new ArrayList<>();
    private ArrayList<String> paterns26=new ArrayList<>();
    private static int dot_count=0;
    private  View view;
    private String eucl_distance,avg_sp,avg_pre;
    private String X_Coor_A,Y_Coor_A,X_Coor_B,Y_Coor_B;
    private String resolution;
    private String my_pat="";


    private ArrayList<Float> Xpoints=new ArrayList<>();
     private ArrayList<Float> Ypoints=new ArrayList<>();
    private ArrayList<String> Sensor_Readings=new ArrayList<>();
    private ArrayList<String> Raw_Pat=new ArrayList<>();
    private HashMap mapxpoints=new HashMap();
    private HashMap mapypoints=new HashMap();

    private file_io file;
    private String user_name,hand_n,finger_n,m_p,l_p;
    private SensorManager sm;
    private Sensor my_guro,my_acc,my_li_acc;
    private float guro_x,guro_y,guro_z,acc_x,acc_y,acc_z,li_acc_x,li_acc_y,li_acc_z;
      static float low_press=2;
      static float max_press=-1;
      static float pres_count=0;
      static float press_sum=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locker);
        share=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor=share.edit();
        user_name=share.getString("Name","Null");
        finger_n=share.getString("Finger Num","Null");
        hand_n=share.getString("Hand Num","Null");
        String path=share.getString("Path","Null");
        DisplayMetrics ds=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ds);
        int height=ds.heightPixels;
        int width=ds.widthPixels;
        resolution=String.valueOf(height)+" x "+String.valueOf(width);
        sm=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        my_guro=sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        my_acc=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        my_li_acc=sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);


        sm.unregisterListener(this);

        text_user_name=(TextView)findViewById(R.id.user_name_text);
        text_user_name.setText(user_name+" set your patterns");

        File u_f = new File(path,user_name);
        u_f.mkdir();

       file=new file_io(user_name,u_f);






        mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);
        Start=(Button) findViewById(R.id.Start_button);
        Stop=(Button) findViewById(R.id.Stop_button);
        Results=(Button) findViewById(R.id.Results_Button);
        Results.setVisibility(Results.GONE);




//arxika to pattern einai "aorato" mexri na patithei to koumpi start

        mPatternLockView.setInStealthMode(true);












//etoimos listener tou kwdika pou pirame gia tin ulopoiisi tou pattern lock
         final PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {


            @Override
            public void onStarted() {



            }

            //klasi oi opooia ginetai trigger kathe fora
             //pou sto pattern ginetai epilogi mias koukidas

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                HashMap map=new HashMap();
                map=PatternLockUtils.getMap(mPatternLockView);

                //pernoume tin timi tis kathe koukidas kai prosthetoume 1 giati
                //kseniaei i arithmisi apo to miden
                //kai epis theloume na ksekinaei apo to 1 (3x3, me teleutaio stoixeio to 9)
                int dot=progressPattern.get(dot_count).getId()+1;

                my_pat=my_pat.concat(String.valueOf(dot));








                //pernoume ta stoixia pou xreiazomaste gia kathe arxeio
                //kai ta bazoume stis antoistoixes arraylists
                //wste me to pou ginei complete to pattern na apothikeutoun
                String timestamp= String.valueOf(SystemClock.elapsedRealtimeNanos());
                sm.registerListener(Locker.this, my_guro,SensorManager.SENSOR_DELAY_GAME);
                sm.registerListener(Locker.this, my_acc,SensorManager.SENSOR_DELAY_GAME);
                sm.registerListener(Locker.this, my_li_acc,SensorManager.SENSOR_DELAY_GAME);
                String xpoint=String.valueOf(PatternLockUtils.getX(mPatternLockView));
                String ypoint=String.valueOf(PatternLockUtils.getY(mPatternLockView));
                Xpoints.add(PatternLockUtils.getX(mPatternLockView));
                Ypoints.add(PatternLockUtils.getY(mPatternLockView));
                String number_of_activated_point=String.valueOf(dot);
                String pressure=String.valueOf(PatternLockUtils.getPre(mPatternLockView));
                calc_Low(PatternLockUtils.getPre(mPatternLockView));
                calc_Max(PatternLockUtils.getPre(mPatternLockView));
                pres_count++;
                press_sum+=PatternLockUtils.getPre(mPatternLockView);

                Raw_Pat.add(number_of_activated_point);
                Raw_Pat.add(timestamp);
                Raw_Pat.add(xpoint);
                Raw_Pat.add(ypoint);
                Raw_Pat.add(pressure);
               Sensor_Readings.add(timestamp);
               Sensor_Readings.add(String.valueOf(guro_x));
                Sensor_Readings.add(String.valueOf(guro_y));
                Sensor_Readings.add(String.valueOf(String.valueOf(guro_z)));
                Sensor_Readings.add(String.valueOf(String.valueOf(acc_x)));
                Sensor_Readings.add(String.valueOf(String.valueOf(acc_y)));
                Sensor_Readings.add(String.valueOf(String.valueOf(acc_z)));
                Sensor_Readings.add(String.valueOf(String.valueOf(li_acc_x)));
                Sensor_Readings.add(String.valueOf(String.valueOf(li_acc_y)));
                Sensor_Readings.add(String.valueOf(String.valueOf(li_acc_z)));










                dot_count++;



            }

            //klasi i opoia kanei trigger se kathe oloklirwsi tou kathe patter
             //diladi sto ACTION_UP

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                pat = PatternLockUtils.patternToString(mPatternLockView, pattern);
                sm.unregisterListener(Locker.this);

                //ginontai kapoio elegxoi wste na eimaste sta plaisia tis askisis
                //diladi na eisagxoun 26 pattern
                //kai kathe 10 na prepei o xristis na eisagei 3 pou thumatai
                if (check_count26(count26)) {

                    //an den exei eisagei 10 patterns sinexizei kanonika
                    if (!check_count10(count10)) {

                        if (check_count3(count3)) {

                            //an o xristis kanei lathos se ena apo ta 3 pou prepei na thumatai
                            //i efarmogi termatizetai
                            if(check_for_same(pat))
                            { Toast.makeText(Locker.this, "Lathos", Toast.LENGTH_SHORT).show();
                                count10=1;
                                count3=1;
                                count26=1;
                                paterns10.clear();
                                paterns26.clear();
                                finish();

                            }


                            count3++;
                            count26++;
                            my_pat="";
                        }
                        //edw o kwdikas ekteleite otan exei balei ta tria pattern pou prepei na thumatai
                        else {
                            Toast.makeText(Locker.this, "You entered the 3", Toast.LENGTH_SHORT).show();
                            count10 = 1;
                            count3 = 1;
                            count26++;
                            paterns26.addAll(paterns10);
                            paterns10.removeAll(paterns10);
                            my_pat="";
                        }



                    }

                    else
                    {
                        if(check_for_same(pat)&&pattern.size()>=4) {

                            paterns10.add(pat);
                            file.initialize_files(count26);

                            file.Save_Raw(Raw_Pat);
                            Raw_Pat.clear();
                            file.Save_Sensor(Sensor_Readings);
                            Sensor_Readings.clear();
                            Xpoints=PatternLockUtils.getXpoints(mPatternLockView);
                            Ypoints=PatternLockUtils.getYpoints(mPatternLockView);
                            eucl_distance=Euclidean_Distance();
                            avg_sp=avg_speed();
                            avg_pre=String.valueOf(press_sum/pres_count);
                            m_p=String.valueOf(max_press);
                            l_p=String.valueOf(l_p);
                            String pat_len=String.valueOf(pattern.size());
                            String t_t_c=String.valueOf(PatternLockUtils.getTime(mPatternLockView));
                            file.Save_Metadata(String.valueOf(count26),my_pat,pat_len,t_t_c,eucl_distance,avg_sp,avg_pre,m_p,l_p,hand_n,finger_n);
                            Toast.makeText(Locker.this, "You have enterned the : "+(count26)+"patern", Toast.LENGTH_SHORT).show();


                            count10++;
                            count26++;
                            my_pat="";


                            file.read_file(mPatternLockView,resolution);



                        }
                        else if(pattern.size()<4)
                        {Toast.makeText(Locker.this, "Mikro megethos pattern", Toast.LENGTH_SHORT).show();}
                        else
                        {Toast.makeText(Locker.this, "Same patern try again", Toast.LENGTH_SHORT).show();}
                    }

                }


                else
                { Toast.makeText(Locker.this, "Finished!", Toast.LENGTH_SHORT).show();
                    Results.setVisibility(Results.VISIBLE);

                   }


                if(!check_count10(count10))
                {
                    Toast.makeText(Locker.this, "Bale:"+(4-count3)+" patterns pou thumasai", Toast.LENGTH_SHORT).show();
                    paterns10.add(pat);
                    file.initialize_files(count26);
                    Toast.makeText(Locker.this, "Raw Pat size:  "+Raw_Pat.size(), Toast.LENGTH_SHORT).show();
                    file.Save_Raw(Raw_Pat);
                    Raw_Pat.clear();
                    Toast.makeText(Locker.this, "Sensor  size:  "+Sensor_Readings.size(), Toast.LENGTH_SHORT).show();

                    file.Save_Sensor(Sensor_Readings);
                    Sensor_Readings.clear();
                    Xpoints=PatternLockUtils.getXpoints(mPatternLockView);
                    Ypoints=PatternLockUtils.getYpoints(mPatternLockView);
                    eucl_distance=Euclidean_Distance();
                    avg_sp=avg_speed();
                    avg_pre=String.valueOf(press_sum/pres_count);
                    m_p=String.valueOf(max_press);
                    l_p=String.valueOf(l_p);
                    String pat_len=String.valueOf(pattern.size());
                    String t_t_c=String.valueOf(PatternLockUtils.getTime(mPatternLockView));
                    file.Save_Metadata(String.valueOf(count26),my_pat,pat_len,t_t_c,eucl_distance,avg_sp,avg_pre,m_p,l_p,hand_n,finger_n);



                }



                Log.d("Locker","Counter 26:"+count26+"\n"+"Counter 10: "+count10);
                dot_count=0;
                Xpoints.clear();
                Ypoints.clear();
                press_sum=1;
                pres_count=1;
                max_press=-1;
                low_press=2;

                mPatternLockView.clearPattern();









            }

            @Override
            public void onCleared() {
                pat="";
            }
        };

        mPatternLockView.removePatternLockListener(mPatternLockViewListener);




        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.make_dirs();
                file.initialize_meta();
               mPatternLockView.addPatternLockListener(mPatternLockViewListener);
                mPatternLockView.setInStealthMode(false);

            }
        });

        Stop.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v)
            {
                count10=1;
                count3=1;
                count26=1;
                paterns10.clear();
                paterns26.clear();

              mPatternLockView.removePatternLockListener(mPatternLockViewListener);
                mPatternLockView.setInStealthMode(true);


            }
        });

    }



//methodos i opoia tsekarei mesa stin arraylist10 i opoia periexei ta 10 prwta pattern
    //kathws epitrepetai an 10 pattern na uparxei 1 idio pattern diladi sunolika 2 idia sta 26
    public boolean check_for_same(String s)
    {   boolean key=true;
        if(paterns10.size()!=0)
        {
        for(int i=0;i<paterns10.size();i++)

    {
        if (paterns10.get(i).equals(s)) {
            key=false;
        }
        }
        }
        return key;
    }
//metrites pou elegxoun posa pattern exoun eisaxthei gia elegxo tou programmatos
    public boolean check_count10(int count)
    {   boolean key=true;
        if(count<=10)
        {key=true;}
        else
        {key=false;}
        return key;}
    public boolean check_count26(int count)
    {   boolean key=true;
        if(count<26)
        {key=true;}
        else
        {key=false;}
        return key;}
    public boolean check_count3(int count)
    {   boolean key=true;
        if(count<3)
        {key=true;}
        else
        {key=false;}
        return key;}




    //ipologismos apo ta dedomena pou pairnoume apo ta pattern tis megistis piesis
    //i opoia einai statheri kathws den allaei i timi tis piesis pou pairnoume logo
    //tou oti xrisimopoioume virtual device kai oxi kanoniko kinito
    public static void calc_Max(float p)
    {
        if(p>max_press)
        {
            max_press=p;
        }
    }
    public static void calc_Low(float p)
    {
        if(p<low_press)
        {
            low_press=p;
        }
    }

    public  void  convert_the_arrays_()
    {

        //pairnoume to 60% ton simeiwn
        //arxika dimiourgoume prosorines arraylists

        ArrayList<Float> tempx =new ArrayList<Float>();
        ArrayList<Float> tempy=new ArrayList<Float>();
        int i=0;
        int array_size=this.Xpoints.size();

        //se periptwsi pou to megethos tou pinaka einai 1
        //to pososto na mhn midenizei
        int percentage=1;
        //pairnoume to pososto
        if(array_size>1) {
             percentage = (int) (0.6 * array_size);
        }



        //briskoume to bima me to opoio tha prepei na
        //pairnontai ta stoixeia wste na isapexoun
        //ta sunolika stoixeia dia to posa exoume

        int step=array_size/percentage;


        while(i< Xpoints.size()&& i<Ypoints.size())
        {

            tempx.add(Xpoints.get(i));
            tempy.add(Ypoints.get(i));

            i=i+step;


        }
        //bazoume kai ta teleutaia simeia
        //twn duo pinakwn stous prwsorinous
        tempx.add(Xpoints.get(Xpoints.size()-1));
        tempy.add(Ypoints.get(Ypoints.size()-1));
        Xpoints=tempx;
        Ypoints=tempy;

    }

    public String Euclidean_Distance()
    {
        convert_the_arrays_();
        float eu_distance = 0;
        float x,y,sub,cube,root;
        for(int i=0;i<Xpoints.size();i++)
        {
            x=Xpoints.get(i);
            y=Ypoints.get(i);
            sub=x-y;
            cube= (float) Math.pow(sub,2);
            root=(float)Math.sqrt(cube);
            eu_distance=eu_distance+root;
        }
        return String.valueOf(eu_distance);

    }
    public String avg_speed()
    {
        long avg_sp;
       float Euc_d=Float.valueOf(eucl_distance);

        avg_sp=(long)Euc_d/PatternLockUtils.getTime(mPatternLockView);

        return String.valueOf(avg_sp);

    }
    //listeners gia tous sensores
    //den leitourgoun sto virtual device giauto oi times einai miden
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor s=event.sensor;

        if(s.getType()==Sensor.TYPE_ACCELEROMETER)
        {

            acc_x=event.values[0];
            acc_y=event.values[1];
            acc_z=event.values[2];


        }
        if(s.getType()==Sensor.TYPE_LINEAR_ACCELERATION)
        {
            li_acc_x=event.values[0];
            li_acc_y=event.values[1];
            li_acc_z=event.values[2];


        }
        if(s.getType()==Sensor.TYPE_GYROSCOPE)
        {
            guro_x=event.values[0];
            guro_y=event.values[1];
            guro_z=event.values[2];


        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
