package com.example.dimitris_admin.pattern_locker.file_utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Statistics {
    private String long_runs[]={"123","147","159","258","321","369","357","456","654","741","753","789","852","987","951","963"};
    private String u_turns[]={"1254","1452","2145","2541","2365","2563","3256","3652","4125","4521","4785","4587","5214","5412","5236","5632","5896","5698","6325","6523","6985","6589","7458","7854","8547","8745","8569","8965","9658","9856"};
    private String long_u_turns[]={"147852","258741","258963","369852","123654","456321","456987","987654","321456","654789","789654"};
    private String long_v_turns[]={"14536","15987","35789","36951","74159","78951","95147","98753"};
    private String short_v_turns[]={"142","124","152","215","251","245","235","253","265","263","356","326","362","415","425","451","475","485","457","512","542","548","578","598","568","526","536","562","532","659","689","695","685","748","784","758","857","845","847","986","956","968"};
    private String long_L_turns[]={"14789","12369","36978","32147","74123","78963","98741","963321"};
    private String short_L_turns[]={"145","125","256","236","325","365","412","452","458","478","541","521","523","563","569","589","587","632","652","658","698","745","785","874","896","854","856","965","985"};

    private ArrayList<String> a_long_runs=new ArrayList<String>(Arrays.asList(long_runs));
    private ArrayList<String> a_u_turns=new ArrayList<String>(Arrays.asList(u_turns));
    private ArrayList<String> a_long_u_turns=new ArrayList<String>(Arrays.asList(long_u_turns));
    private ArrayList<String> a_long_v_turns=new ArrayList<String>(Arrays.asList(long_v_turns));
    private ArrayList<String> a_short_v_turns=new ArrayList<String>(Arrays.asList(short_v_turns));
    private ArrayList<String> a_long_L_turns=new ArrayList<String>(Arrays.asList(long_L_turns));
    private ArrayList<String> a_short_L_turns=new ArrayList<String>(Arrays.asList(short_L_turns));

    ArrayList<String> sequences=new ArrayList<String>();
    ArrayList<Integer> statistics=new ArrayList<Integer>();
    HashMap stat_map=new HashMap();

    private int long_run,u_turn,long_u_turn,long_v_turn,short_v_turn,long_L_turn,short_L_turn;

    private File meta_file;
    private HashMap h_m;
    private int sum_leng=1;
    private int count1,count2,count3,count4,count5,count6,count7,count8,count9;



    public   Statistics(File f)
    {
        meta_file=f;

    }

    private void read_metadata()
    {   BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        String[] metrics = null;







        try {

            br = new BufferedReader(new FileReader(this.meta_file));

            //skip line
            //wste na min diabazontai ta headers
            br.readLine();
            while ((line = br.readLine()) != null) {
                // use comma as separator


                metrics = line.split(cvsSplitBy);
                Log.d("IO message:", "Attempt: "+metrics[1]+" sequence: "+metrics[2]);
                sum_leng=sum_leng+Integer.valueOf(metrics[3].trim());
               sequences.add(metrics[2]);





            }


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

    public HashMap generate_Statistics()
    {this.read_metadata();

    //gia kathe sequence sto metadata file
     //elegxoume an periexei kapoia seira apo xaraxtires
        //pou exoume arxikopoiisei pio panw
        //an to sequence perixei kapoia akolouthia
        //auksanete o antistoixos metritis
        //telos epistrefete i arraylist me to sunoliko apotelesma


    for(int i=0;i<sequences.size();i++) {


        String S = sequences.get(i);
        if (sequences.contains("1"))
        {count1++;}
        if (sequences.contains("2"))
        {count2++;}
        if (sequences.contains("3"))
        {count3++;}
        if (sequences.contains("4"))
        {count4++;}
        if (sequences.contains("5"))
        {count5++;}
        if (sequences.contains("6"))
        {count6++;}

        if (sequences.contains("7"))
        {count7++;}
        if (sequences.contains("8"))
        {count8++;}
        if (sequences.contains("9"))
        {count9++;}


        for(int a=0;a<a_long_runs.size();a++)
        {
            String S2=a_long_runs.get(a);

            if(S.contains(S2))
        {long_run++;}

        }

        for(int a=0;a<a_u_turns.size();a++)
        {
            String S2=a_u_turns.get(a);

            if(S.contains(S2))
            {u_turn++;}

        }

        for(int a=0;a<a_long_u_turns.size();a++)
        {
            String S2=a_long_u_turns.get(a);

            if(S.contains(S2))
            {long_u_turn++;}

        }
        for(int a=0;a< a_long_v_turns.size();a++)
        {
            String S2= a_long_v_turns.get(a);

            if(S.contains(S2))
            {long_v_turn++;}

        }
        for(int a=0;a< a_long_v_turns.size();a++)
        {
            String S2= a_long_v_turns.get(a);

            if(S.contains(S2))
            {long_v_turn++;}

        }
        for(int a=0;a< a_short_v_turns.size();a++)
        {
            String S2= a_short_v_turns.get(a);

            if(S.contains(S2))
            {short_v_turn++;}

        }
        for(int a=0;a< a_long_L_turns.size();a++)
        {
            String S2= a_long_L_turns.get(a);

            if(S.contains(S2))
            {long_L_turn++;}

        }

        for(int a=0;a< a_short_L_turns.size();a++)
        {
            String S2= a_short_L_turns.get(a);

            if(S.contains(S2))
            {short_L_turn++;}

    }
        statistics.add(long_run);
        statistics.add(u_turn);
        statistics.add(long_u_turn);
        statistics.add(long_v_turn);
        statistics.add(short_v_turn);
        statistics.add(long_L_turn);
        statistics.add(short_L_turn);

        stat_map.put(i,statistics);


        }



        return stat_map;





    }
    public ArrayList get_number_metrics()
    {
         ArrayList list=new ArrayList<>();

    Log.d("Statistics","Sum "+sum_leng+" counter1: "+count1);

         list.add((float)(count1/sum_leng)*100);
        list.add((float)(count2/sum_leng)*100);
        list.add((float)(count3/sum_leng)*100);
        list.add((float)(count4/sum_leng)*100);
        list.add((float)(count5/sum_leng)*100);
        list.add((float)(count6/sum_leng)*100);
        list.add((float)(count7/sum_leng)*100);
        list.add((float)(count8/sum_leng)*100);
        list.add((float)(count9/sum_leng)*100);

return list;
    }

    }









