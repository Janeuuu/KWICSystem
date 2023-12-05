package rabbitmq;

import com.rabbitmq.client.*;
import rabbitmq.Consumer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Consumer  {
    public static String rabbitMqRes = "";

    private static final String QUEUE_NAME = "q_test_01";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 5672; //RabbitMQ 服务端默认端口号为 5672
    private static final String USER_NAME = "guest";
    private static final String PASSWORD = "guest";

    public static void getMessage() throws IOException, TimeoutException,InterruptedException
    {
        String res;
        Address[] addresses = new Address[]{new Address(IP_ADDRESS, PORT)};
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        Connection connection = factory.newConnection(addresses); //创建连接
        final Channel channel = connection.createChannel(); //创建信道
        channel.exchangeDeclare("amqp.fanout","fanout");
        channel.queueBind(QUEUE_NAME, "amqp.fanout","");
        channel.basicQos(1); //设置客户端最多接收未被ack的消息的个数
        DefaultConsumer consumer = new DefaultConsumer(channel)
        {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                rabbitMqRes = new String(body);
                System.out.println("接收消息队列 'q_test_01' 中的信息：" + new String(body));
                try
                {
                    TimeUnit.SECONDS.sleep(1);
                }
                catch (InterruptedException ie)
                {
                    ie.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(QUEUE_NAME, consumer);
        //等待回调函数执行完毕之后，关闭资源
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }

}
