package com.company;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import static java.lang.Thread.*;


public class MQConnectorTopic {
    public static int fails = 0;
    public static String MSG="";
    public static String TOPIC = "hive.guest.process.monitor";

    // Create a ConnectionFactory
    public static ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","manager",GLOBALS.AMQ2);

    public static void createRecord(String msg, String Topic) throws Exception {
        MSG = msg.toString();
        TOPIC = Topic.toString();
        thread(new Producer(), false);
    }

    public static void listenTopic(String topic){
        TOPIC = topic;
        //while(true) {
            try {
            thread(new Consumer(),false);
            readRecord(TOPIC);
            sleep(100);
            } catch (Exception e) {
            e.printStackTrace();
            }
        //}
    }

    public static String readRecord(String TOPIC) throws Exception {
        TOPIC = TOPIC.toString();
        //thread(new Consumer(), false);
        String msg = ConsumerSynchronous();
        System.out.println("MSG="+msg);
        return msg;
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class Producer implements Runnable {
        public void run() {

            try {
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createTopic(TOPIC);

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                // Create a messages
                String text =  MSG;
                TextMessage message = session.createTextMessage(text);

                // Tell the producer to send the message
                //System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
                System.out.println("SENT---->"+text);
                producer.send(message);

                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                fails++;
                System.out.println("Caught: " + fails);
                System.out.println("Caught: " + e.toString());
                //e.printStackTrace();
            }
        }
    }

    public static class Consumer implements Runnable, ExceptionListener {
        public void run() {
            try {

                System.out.println("The TOPIC is:::" + TOPIC);
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                //System.exit(0);
                connection.start();

                connection.setExceptionListener(this);

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createTopic(TOPIC);

                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);

                // Wait for a message
                Message message = consumer.receive();

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("MSG: " + text);
                } else {
                    System.out.println("Received: " + message);
                    //thread(new Consumer(), false);
                }

                //consumer.close();
                //session.close();
                connection.close();
            } catch (Exception e) {
                //System.exit(0);
                //fails++;
                //System.out.println("Caught: " + e);
            }
        }

        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }

    public static String ConsumerSynchronous() {
        String msg = "";
        try {

            //System.out.println("The Queue is:::" + QUEUE);
            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            //connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(TOPIC);

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);

            // Wait for a message
            Message message = consumer.receive(100);

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                msg = textMessage.getText();
                //System.out.println("Received: " + msg);
            } else {
                System.out.println("Error: " + message);
            }
            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
            //System.exit(0);
        }
        return msg;
    }

    public static class setupAllQueues implements Runnable {
        public void run() {

            try {
                AMQSetupNonPersistent("info.queue.np");
                AMQSetupPersistent("broker.queue.p");
                AMQSetupNonPersistent("broker.topic.np");
                AMQSetupPersistent("upm.topic.p");
                AMQSetupPersistent("inventory.queue.p");
                AMQSetupPersistent("error.topic.p");
                AMQSetupPersistent("perf.topic.p");
                AMQSetupPersistent("security.topic.p");
            }
            catch (Exception e) {
                fails++;
                //System.out.println("Caught: " + fails);
                //e.printStackTrace();
            }
        }
    }

    public static void AMQSetupNonPersistent(String qName) {

        try {
            // Create a ConnectionFactory
            //ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","manager",GLOBALS.AMQ1);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(TOPIC);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            String text = "--start--";
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            System.out.println("SENT---->"+text);
            producer.send(message);

            // Clean up
            session.close();
            session.unsubscribe("0");
            connection.close();
            connection = null;
        }
        catch (Exception e) {
            fails++;
            System.out.println("Caught: " + fails);
            //e.printStackTrace();
        }
    }

    public static void AMQSetupPersistent(String qName) {

        try {
            // Create a ConnectionFactory
            //ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","manager",GLOBALS.AMQ1);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(TOPIC);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            // Create a messages
            String text = "--start--";
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            System.out.println("SENT---->"+text);
            producer.send(message);

            // Clean up
            session.close();
            session.unsubscribe("0");
            connection.close();
            connection = null;

        }
        catch (Exception e) {
            fails++;
            System.out.println("Caught: " + fails);
            //e.printStackTrace();
        }
    }

}