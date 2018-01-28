package com.company;

public class GLOBALS {


    //SEED Locations
    public static String Version = "0001.01272018";
    public static String PWD ="/export/ramdisk/";
    public static String LOCATIONList ="/export/ramdisk/tb_u_locations)";
    public static int    queueDepth =1000;
    public static int 	locationCount = 29427;
    public static int 	loopCount = locationCount/queueDepth;

    //Timers
    public static int 	 PERFIteration = 1;  //Number of Times to sample data
    public static int    GlobalSLEEP = 5*100; //Sleep Timer in seconds

    //Mail
    public static String MAILTO = "staff@hiveio.com";
    public static String MAILTO1 = "kevin@hiveio.com";
    public static String SMSTO = "6464672143@mobile.mycingular.com";
    public static String SUBJECT = "Cloud Engineering Functional Regression Testing Report";
    public static String SMTPSOURCE2 = "outbound.cisco.com";
    public static String SMTPSOURCE1 = "smtpout.secureserver.net";
    public static String SMTPSOURCE = "msp-mail2.uk.jpmorgan.com";
    public static String SMTPPWD = "fearless";
    public static String MAILLIST = "kevin.j.mcnamara@jpmchase.com";

    //QPID Servers
    public static String AMQ2 = "tcp://10.0.0.240:5672";

    public static String fingerprint = "";
}