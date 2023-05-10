package bo;

import com.rabbitmq.client.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class BoService {
    private final int boNumber;
    private final String exchange_name;
    private final DbCreateService dbCreateService;
    private final DbUpdateService dbUpdateService;
    private final DbDeleteService dbDeleteService;
    private final DbRetrieveService dbRetrieveService;
    BoService(int boNumber){
        this.boNumber = boNumber;
        this.exchange_name = "product_sale_exchange";
        this.dbCreateService = new DbCreateService(this.boNumber);
        this.dbDeleteService = new DbDeleteService(this.boNumber);
        this.dbUpdateService = new DbUpdateService(this.boNumber);
        this.dbRetrieveService = new DbRetrieveService(this.boNumber);
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
            ArrayList<Product> products = dbRetrieveService.getNotSyncProducts();
            for(Product product : products){
                //wait(1000);
                channel.basicPublish(exchange_name, Integer.toString(boNumber),properties, product.getByteArray());
                
                System.out.println(Arrays.toString(product.getByteArray()));
                dbUpdateService.updateSynchronizedToTrue(product);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            

        }
    }
    public void updateDb(Product product) throws SQLException {
        this.dbUpdateService.updateDb(product);
    }
    public void deleteFromDb(int id) throws SQLException {
        this.dbDeleteService.deleteProductFromDb(id);
    }
    public void createDbRow(Product product) throws SQLException {
        this.dbCreateService.updateDb(product);
    }
    public ArrayList<Product> getAllProducts() throws SQLException {
        return this.dbRetrieveService.getProducts();
    }
}
