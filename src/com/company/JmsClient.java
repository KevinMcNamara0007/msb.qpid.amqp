package com.company;

        import javax.jms.*;

        import org.apache.qpid.client.AMQAnyDestination;
        import org.apache.qpid.client.AMQConnection;

        import org.apache.qpid.jms.ListMessage;

        import java.util.Enumeration;
        import java.util.Iterator;

public class JmsClient {

    public static String TOPIC = "hive.guest.process.monitor";
    public static String msg = "";
    public static void start() throws Exception
    {
        Connection connection =  new AMQConnection("amqp://system:manager@clientid/?brokerlist='tcp://10.0.0.240:5672'");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = new AMQAnyDestination("ADDR:message_queue; {create: always}");

        // Create the destination (Topic or Queue)
        Destination destination = session.createTopic(TOPIC);

        // Create a MessageConsumer from the Session to the Topic or Queue
        MessageConsumer consumer = session.createConsumer(destination);

        // Wait for a message
        Message message = consumer.receive();
        MapMessage m = (MapMessage)consumer.receive();
        System.out.println(m);

        consumer.close();
        session.close();
        connection.close();
    }
}