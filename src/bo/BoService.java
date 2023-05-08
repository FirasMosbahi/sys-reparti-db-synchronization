package bo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP;
import java.sql.SQLException;
import java.util.ArrayList;

public class BoService {
    private final int boNumber;
    private final String exchange_name;
    private final DbCreateService dbCreateService;
    private final DbUpdateService dbUpdateService;
    private final DbDeleteService dbDeleteService;
    private final DbRetrieveService dbRetrieveService;
    private final DbRetrieveService dbNotSynchronizedRetrieveService;
    BoService(int boNumber){
        this.boNumber = boNumber;
        this.exchange_name = "product_sale_exchange";
        this.dbCreateService = new DbCreateService(this.boNumber);
        this.dbDeleteService = new DbDeleteService(this.boNumber);
        this.dbUpdateService = new DbUpdateService(this.boNumber);
        this.dbRetrieveService = new DbRetrieveService(this.boNumber,false);
        this.dbNotSynchronizedRetrieveService = new DbRetrieveService(this.boNumber,true);
    }
    public void synchronizeDb() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT,true);
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).build();
            ArrayList<Product> products = dbNotSynchronizedRetrieveService.getProducts();
            for(Product product : products){
                channel.basicPublish(exchange_name, Integer.toString(boNumber),properties, product.getByteArray());
                dbUpdateService.updateSynchronizedToTrue(product);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateDb(Product product) throws SQLException {
        this.dbUpdateService.updateDb(product);
    }
    public void deleteFromDb(Product product) throws SQLException {
        this.dbDeleteService.deleteProductFromDb(product);
    }
    public void createDbRow(Product product) throws SQLException {
        this.dbCreateService.updateDb(product);
    }
    public void getAllProducts() throws SQLException {
        this.dbRetrieveService.getProducts();
    }
}
