package com.company;

        import javax.jms.Connection;
        import javax.jms.Destination;
        import javax.jms.MapMessage;
        import javax.jms.StreamMessage;
        import javax.jms.MessageConsumer;
        import javax.jms.Session;
        import javax.jms.MessageEOFException;

        import org.apache.qpid.client.AMQAnyDestination;
        import org.apache.qpid.client.AMQConnection;

        import org.apache.qpid.jms.ListMessage;

        import java.util.Enumeration;
        import java.util.Iterator;

public class JmsClient {

    public static void main(String[] args) throws Exception
    {
        Connection connection =  new AMQConnection("amqp://system:manager@hive.guest.process.monitor/?brokerlist='tcp://10.0.0.240:5672'");

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = new AMQAnyDestination("ADDR:message_queue; {create: always}");
        MessageConsumer consumer = session.createConsumer(queue);

        /**if (args[0].equals("-l")) {
            System.out.println("Receiving as ListMessage");
            ListMessage m = (ListMessage)consumer.receive();
            System.out.println(m);
            System.out.println("==========================================");
            System.out.println("Printing list contents:");
            Iterator i = m.iterator();
            while(i.hasNext())
                System.out.println(i.next());
            System.exit(0);
        }
        else if (args[0].equals("-m")) {
            System.out.println("Receiving as MapMessage");
            MapMessage m = (MapMessage)consumer.receive();
            System.out.println(m);
            System.out.println("==========================================");
            System.out.println("Printing map contents:");
            Enumeration keys = m.getMapNames();
            while(keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                System.out.println(key + " => " + m.getObject(key));
            }
        }
        else if (args[0].equals("-s")) {
         **/
            System.out.println("Receiving as StreamMessage");
            StreamMessage m = (StreamMessage)consumer.receive();
            System.out.println(m);
            System.out.println("==========================================");
            System.out.println("Printing stream contents:");
            try {
                while(true)
                    System.out.println(m.readString());
                    //System.out.println(m.readObject());
            }
            catch (MessageEOFException e) {
                // DONE
            }
        //}

        connection.close();
    }
}