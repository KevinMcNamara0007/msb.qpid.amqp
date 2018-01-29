package com.company;

public class Main {

    public static void main(String[] args) {
        //MsgBus.listenerTopicMsg();


        try {
            JmsClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
