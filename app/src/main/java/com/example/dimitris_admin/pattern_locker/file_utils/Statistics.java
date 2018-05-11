package com.example.dimitris_admin.pattern_locker.file_utils;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

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

    public Statistics()
    {}

}




