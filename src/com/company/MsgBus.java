package com.company;

public class MsgBus {

    public static void sendQueueMsg(String row, String queue){

        try {
            //new MQConnector();
            //MQConnector.createRecord(row,queue);
            new MQConnectorTopic();
            MQConnectorTopic.createRecord(row,queue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendQueueMsgRaw(String row, String queue){

        try {
            new MQConnector();
            MQConnector.createRecordRaw(row,queue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String receiveQueueMsg(String queue){
        String msg = "";
        try {
            new MQConnector();
            msg = MQConnector.readRecord(queue);
        } catch (Exception e) {
            System.exit(0);
        }
        return msg;
    }

    public static void sendErrMsg(String err, String who){

        try {

            new MQConnector();
            MQConnector.createRecord("Module:"+who,"tes.err");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String listenerTopicMsg(){
        String msg = "";
        try {
            MQConnectorTopic.listenTopic("hive.guest.process.monitor");
        } catch (Exception e) {
            System.exit(0);
        }
        return msg;
    }


}