package com.example.dimitris_admin.pattern_locker.file_utils;
//klasi stin opoio ektelountai oi diadikasies epikoinwnias me ta arxeia


import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.example.dimitris_admin.pattern_locker.PatternLockView;
import com.example.dimitris_admin.pattern_locker.utils.PatternLockUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.util.ArrayList;
import java.util.HashMap;


public class file_io {
    private static final String COMMA_DELIMITER = "; ";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String RAW_HEADER = "<number_of_activation_point; timestamp; xpoint; ypoint; pressure>";
    static final String SENSOR_HEADER = "<timestamp; accel_x; accel_y; accel_z; gyro_x; gyro_y; gyro_z; laccel_x; laccel_y; laccel_z>";
    static final String METADATA_HEADER = "<Username; Attempt_number; Sequence; Seq_length; Time_to_complete; PatternLength; Avg_speed; Avg_pressure; Highest_pressure; Lowest_pressure; HandNum; FingerNum>";
    static final String PAIR_METATADA_HEADER = "< Username; Attempt_number; Screen_resolution; Pattern_number_A; Pattern_number_B; Xcoord_of_central_Point_of_A; Ycoord_of_central_Point_of_A; Xcoord_of_central_Point_of_B; Ycoord_of_central_Point_of_B; First_Xcoord_of_A; First_Ycoord_of_A; Last_ Xcoord_of_B; Last_Ycoord_of_B; Distance_AB; Intertime_AB; Avg_speeadAB; Avg_pressure >";


    private File user_file;
    private File raw_file_dir;
    private File sensor_file_dir;
    private File metadata_file_dir;
    private File pair_metadata_dir;
    private String user_name;
    private File raw_file, sensor_file, metadata_file, pair_metadata_file;
    private String first_button, second_button, t1,p1,p2, t2, x1, y1, x2, y2, time, A_C_X, A_C_Y, B_C_X, B_C_Y, eu_d, speed_a_b;
    private ArrayList<Float> pointsx = new ArrayList<Float>();
    private ArrayList<Float> pointsy = new ArrayList<Float>();
    private ArrayList<Float> pressure = new ArrayList<Float>();
    private String avg_pres;
    private int attempt_number;
    private PatternLockView patternLockView;

    public file_io(String name, File f) {
        this.user_name = name;
        this.user_file = f;
    }


    public void make_dirs() {

        raw_file_dir = new File(user_file, "Raw pattern");
        sensor_file_dir = new File(user_file, "Sensor data");
        metadata_file_dir = new File(user_file, "Pattern metadata");
        pair_metadata_dir = new File(user_file, "Pair metadata");
        //dimiourgoume gia kathe xristi enan fakelo me to onoma tou


// if the directory does not exist, create it
        if (!this.raw_file_dir.exists() && !this.pair_metadata_dir.exists() && !this.metadata_file_dir.exists() && !this.sensor_file_dir.exists())
        {


            try {
                this.raw_file_dir.mkdir();
                this.sensor_file_dir.mkdir();
                this.metadata_file_dir.mkdir();
                this.pair_metadata_dir.mkdir();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //kanoume initialize to meta arxeio
    //epeidi dimiourgeite mia fora ginetai ksexwrista me ta alla


    public void initialize_meta() {
        FileWriter fw = null;
        this.metadata_file = new File(this.metadata_file_dir, this.user_name + "_metadata");

        try {
            fw = new FileWriter(metadata_file);

            fw.append(METADATA_HEADER);
            fw.append(NEW_LINE_SEPARATOR);




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();



            } catch (IOException e) {
                e.printStackTrace();

            }
        }


    }
    //klasi i opoia diabazei to raw arxeio wste na dimiourgithei to pair metadata

    public void read_file(PatternLockView patternLockView, String s) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        String[] metrics = null;
        HashMap map = new HashMap();
        map = PatternLockUtils.getMap(patternLockView);
        ArrayList<Float> spots = new ArrayList<Float>();
        ArrayList<String> statistics=new ArrayList<String>();

        this.patternLockView = patternLockView;

        try {

            br = new BufferedReader(new FileReader(this.raw_file));

            //skip line
            //wste na min diabazontai ta headers
            br.readLine();
            while ((line = br.readLine()) != null) {
                // use comma as separator

                //an to metrics einai null
                //pare tin prwti grammmi
                //kai these ta arxika

                if (metrics == null) {
                    metrics = line.split(cvsSplitBy);


                }
                //an oxi to first tha einai ta proigoume
                //apo ton pinaka metrics
                else {

                    first_button = metrics[0];
                    t1 = metrics[1];
                    x1 = metrics[2];


                    y1 = metrics[3];
                    p1=metrics[4];

                    spots = (ArrayList) map.get(Integer.valueOf(first_button));
                    A_C_X = String.valueOf(spots.get(0));
                    A_C_Y = String.valueOf(spots.get(1));

                    //pairnoume tin nea grammi
                    //kai thetoume ta epomena
                    metrics = line.split(cvsSplitBy);
                    second_button = metrics[0];
                    t2 = metrics[1];
                    x2 = metrics[2];

                    y2 = metrics[3];
                    p2=metrics[4];
                    this.calc_time(t1, t2);
                    spots = (ArrayList) map.get(Integer.valueOf(second_button));

                    B_C_X = String.valueOf(spots.get(0));
                    B_C_Y = String.valueOf(spots.get(1));

                    calc_Eucl_Distance();
                    find_averege_press();



                  //  statistics.add(first_button+","+second_button+","+A_C_X+","+A_C_Y+","+B_C_X+","+B_C_Y);

                    this.Save_Pair_Metadata(s);


                }



            }
            for(int i=0;i<statistics.size();i++)
            {Log.d("File I/O",statistics.get(i));}

            Statistics a=new Statistics(statistics);


            a.generate_statistics();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //methodos i opoia apothikeuei ta stoixeia gia to pai metadata

    public void Save_Pair_Metadata(String s) {

        FileWriter fw = null;


        try {

            fw = new FileWriter(this.pair_metadata_file, true);

            fw.append(user_name);
            fw.append(COMMA_DELIMITER);

            fw.append(String.valueOf(attempt_number));
            fw.append(COMMA_DELIMITER);

            fw.append(s);
            fw.append(COMMA_DELIMITER);

            fw.append(first_button);
            fw.append(COMMA_DELIMITER);

            fw.append(second_button);
            fw.append(COMMA_DELIMITER);

            fw.append(A_C_X);
            fw.append(COMMA_DELIMITER);

            fw.append(A_C_Y);
            fw.append(COMMA_DELIMITER);

            fw.append(B_C_X);
            fw.append(COMMA_DELIMITER);

            fw.append(B_C_Y);
            fw.append(COMMA_DELIMITER);


            fw.append(x1);
            fw.append(COMMA_DELIMITER);

            fw.append(y1);
            fw.append(COMMA_DELIMITER);

            fw.append(x2);
            fw.append(COMMA_DELIMITER);

            fw.append(y2);
            fw.append(COMMA_DELIMITER);

            fw.append(eu_d);
            fw.append(COMMA_DELIMITER);

            fw.append(time);
            fw.append(COMMA_DELIMITER);

            fw.append(speed_a_b);
            fw.append(COMMA_DELIMITER);

            fw.append(avg_pres);
            fw.append(NEW_LINE_SEPARATOR);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
                Log.d("IO message:", "Pair metadata saveda"+attempt_number+" file");
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

    public void delete_user_file()
    {

        try {
            this.user_file.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

    }



//arxikopoiisi twn arxeiwn

    public void initialize_files(int c) {

        attempt_number = c;

        FileWriter fw = null;
        FileWriter fw1 = null;
        FileWriter fw2 = null;


        this.raw_file = new File(this.raw_file_dir, this.user_name + "_" + attempt_number);
        this.sensor_file = new File(this.sensor_file_dir, this.user_name + "_" + attempt_number);
        this.pair_metadata_file = new File(this.pair_metadata_dir, this.user_name + "_" + attempt_number + "_pairs");


        try {
            fw = new FileWriter(raw_file);
            fw1 = new FileWriter(sensor_file);
            fw2 = new FileWriter(pair_metadata_file);


            fw.append(RAW_HEADER);
            fw.append(NEW_LINE_SEPARATOR);

            fw1.append(SENSOR_HEADER);
            fw1.append(NEW_LINE_SEPARATOR);

            fw2.append(PAIR_METATADA_HEADER);
            fw2.append(NEW_LINE_SEPARATOR);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
                fw1.flush();
                fw1.close();
                fw2.flush();
                fw2.close();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    public void Save_Sensor(ArrayList<String> a) {

        FileWriter fw = null;
        int j = 0;

        try {

            fw = new FileWriter(this.sensor_file, true);

            for (int i = 0; i < a.size(); i++) {
                fw.append(a.get(i));
                fw.append(COMMA_DELIMITER);
                j++;
                if (j == 10) {
                    fw.append(NEW_LINE_SEPARATOR);
                    j = 0;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
                Log.d("IO message:", "Sensor saved "+attempt_number+" file");;
            } catch (IOException e) {
                e.printStackTrace();

            }
        }


    }

    public void Save_Raw(ArrayList<String> a) {
        FileWriter fw = null;
        int j = 0;


        try {

            fw = new FileWriter(this.raw_file, true);


            for (int i = 0; i < a.size(); i++) {
                fw.append(a.get(i));
                fw.append(COMMA_DELIMITER);
                j++;
                if (j == 5) {
                    fw.append(NEW_LINE_SEPARATOR);
                    j = 0;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
                Log.d("IO message:", "Raw saved "+attempt_number+" file");

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

    public void Save_Metadata(String at_num, String seq, String seq_l, String ti_t_c, String pat_l,
                              String avg_s, String avg_p, String high_pre, String low_pre, String h_n, String f_n) {

        FileWriter fw = null;


        try {

            fw = new FileWriter(this.metadata_file, true);

            fw.append(user_name);
            fw.append(COMMA_DELIMITER);

            fw.append(at_num);
            fw.append(COMMA_DELIMITER);


            fw.append(seq);
            fw.append(COMMA_DELIMITER);

            fw.append(seq_l);
            fw.append(COMMA_DELIMITER);

            fw.append(ti_t_c);
            fw.append(COMMA_DELIMITER);

            fw.append(pat_l);
            fw.append(COMMA_DELIMITER);

            fw.append(avg_s);
            fw.append(COMMA_DELIMITER);

            fw.append(avg_p);
            fw.append(COMMA_DELIMITER);

            fw.append(high_pre);
            fw.append(COMMA_DELIMITER);

            fw.append(low_pre);
            fw.append(COMMA_DELIMITER);

            fw.append(h_n);
            fw.append(COMMA_DELIMITER);

            fw.append(f_n);
            fw.append(NEW_LINE_SEPARATOR);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
                Log.d("IO message:", "Metadata saved "+attempt_number+" file");
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }
    //methodos gia upologismo tis eukleidias apostasis
    public void calc_Eucl_Distance()
    {
        find_arraysX();
        find_arraysY();
        float eu_distance = 0;
        float x,y,sub,cube,root;

        int i=0;
        while(i<pointsy.size()&&i<pointsx.size())
        {
            x=pointsx.get(i);
            y=pointsy.get(i);

            sub=x-y;
            cube= (float) Math.pow(sub,2);
            root=(float)Math.sqrt(cube);
            eu_distance=eu_distance+root;
            i++;


        }
        this.eu_d=String.valueOf(eu_distance);

        Float f_spead=eu_distance/Float.valueOf(time);
        this.speed_a_b=String.valueOf(f_spead);



    }

    //methodos i opoia upologizei ton xrono

    public void calc_time(String t1, String t2) {
        Float time1, time2, total;
        time1 = Float.valueOf(t1);
        time2 = Float.valueOf(t2);
        total = Math.abs(time1 - time2);
        this.time = String.valueOf(total);


    }
    //methodos i opoia briskei ta simeia x anamesa omws sta duo koumpia pou anaferontai sto pair metadata arxeio

    public void find_arraysX() {
        int i,j;

        //exwntas ton sunologiko pinaka me ta stoixeia apo tin etoimi klasi PatternLocker
        //pairnoume gia to kathe stoixeio pou briskontai mesa ston pinaka me ta stoixeia
        //(arxiko kai teliko) kai stin sunexeia pairnoume ola ta stoixeia sto euros auto
        i=PatternLockUtils.getXpoints(patternLockView).indexOf(Float.valueOf(x1));
        j=PatternLockUtils.getXpoints(patternLockView).indexOf(Float.valueOf(x2));


        for(int a=i;a<=j;a++)
        {
            pointsx.add(PatternLockUtils.getXpoints(patternLockView).get(a));

        }


    }
    //edw ginetai to idio gia ta y stoixeia
    public void find_arraysY() {
        int i,j;
        i=PatternLockUtils.getYpoints(patternLockView).indexOf(Float.valueOf(y1));
        j=PatternLockUtils.getYpoints(patternLockView).indexOf(Float.valueOf(y2));




        for(int a=i;a<=j;a++)
        {
            pointsy.add(PatternLockUtils.getYpoints(patternLockView).get(a));

        }


    }
    //edw ginetai to idio gia tis pieseis kathws kai autes oi times uparxoun sugkentrwtika
    //alla oxi gia kathe koumpi pou patietai
    public void find_pressure_arrays() {
        int i,j;
        i=PatternLockUtils.getArrayPre(patternLockView).indexOf(Float.valueOf(p1));
        j=PatternLockUtils.getArrayPre(patternLockView).indexOf(Float.valueOf(p2));




        for(int a=i;a<=j;a++)
        {
            pressure.add(PatternLockUtils.getArrayPre(patternLockView).get(a));

        }


    }
//methodos i opoia upologizei tin mesi piesi
    public void find_averege_press()
    {

        find_pressure_arrays();

        float sum=0;
        for(int i=0;i<pressure.size();i++)
        {
            sum=sum+pressure.get(i);

        }

        avg_pres=String.valueOf(sum/pressure.size());

    }
}


