package ho;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class HoService {

    private static String exchange_name= "product_sale_exchange";
    private static DbCreateService dbCreateService= new DbCreateService();
    private DbUpdateService dbUpdateService;
    private DbDeleteService dbDeleteService;
    private DbRetrieveService dbRetrieveService;
    HoService(){
        //HoService.exchange_name = "product_sale_exchange";
        //HoService.dbCreateService = new DbCreateService();
        this.dbDeleteService = new DbDeleteService();
        this.dbUpdateService = new DbUpdateService();
        this.dbRetrieveService = new DbRetrieveService();
    }
    public static void synchronizeDb() throws IOException, TimeoutException {
                //HoService.synchronizeDb();
        
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        
                channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT,true);
               // String queueName = channel.queueDeclare().getQueue();
        
               
                   channel.queueBind("product_sales_queue_bo1", exchange_name, "1");
                   channel.queueBind("product_sales_queue_bo2",exchange_name,"2");
        
                
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
                DeliverCallback deliverCallback2 = (consumerTag,delivery) -> {
                    try {
                        bo.Product receivedProduct = bo.Product.deserialize(delivery.getBody());
                        System.out.println(receivedProduct.id+":bo2");
                        System.out.println("passed");
                        Product product = new Product(
                                Integer.parseInt(("2"+Integer.toString(receivedProduct.id))),
                                receivedProduct.date,
                                receivedProduct.product,
                                receivedProduct.qty,
                                receivedProduct.cost,
                                "bo2"
                        );
                        createDbRow(product);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                DeliverCallback deliverCallback1 = (consumerTag,delivery) -> {
                    try {
                        bo.Product receivedProduct = bo.Product.deserialize(delivery.getBody());
                        System.out.println(receivedProduct.id+":bo1");
                        System.out.println("passed");
                        Product product = new Product(
                                Integer.parseInt(("1"+Integer.toString(receivedProduct.id))),
                                receivedProduct.date,
                                receivedProduct.product,
                                receivedProduct.qty,
                                receivedProduct.cost,
                                "bo1"
                                );
                        createDbRow(product);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                };
                
                channel.basicConsume("product_sales_queue_bo1",true, deliverCallback1,consumerTag -> {});
                channel.basicConsume("product_sales_queue_bo2", true, deliverCallback2, consumerTag -> {});
            
       /*  ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT, true );
           channel.queueDeclare("product_sales_queue_bo1",true,false,false,null);
            channel.queueDeclare("product_sales_queue_bo2",true,false,false,null);
            channel.queueBind("product_sales_queue_bo1",exchange_name,"1");
            channel.queueBind("product_sales_queue_bo2",exchange_name,"2");
            DeliverCallback deliverCallback1 = (consumerTag,delivery) -> {
                try {
                    bo.Product receivedProduct = bo.Product.deserialize(delivery.getBody());
                    System.out.println(receivedProduct.id+":bo1");
                    System.out.println("passed");
                    Product product = new Product(
                            Integer.parseInt(("1"+Integer.toString(receivedProduct.id))),
                            receivedProduct.date,
                            receivedProduct.product,
                            receivedProduct.qty,
                            receivedProduct.cost,
                            "bo1"
                            );
                    createDbRow(product);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            };
            DeliverCallback deliverCallback2 = (consumerTag,delivery) -> {
                try {
                    bo.Product receivedProduct = bo.Product.deserialize(delivery.getBody());
                    System.out.println(receivedProduct.id+":bo2");
                    System.out.println("passed");
                    Product product = new Product(
                            Integer.parseInt(("2"+Integer.toString(receivedProduct.id))),
                            receivedProduct.date,
                            receivedProduct.product,
                            receivedProduct.qty,
                            receivedProduct.cost,
                            "bo2"
                    );
                    createDbRow(product);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            channel.basicConsume("product_sales_queue_bo1",true,deliverCallback1,consumerTag -> {});
            channel.basicConsume("product_sales_queue_bo2",true,deliverCallback2,consumerTag -> {});
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        }catch (Exception e){
        }
        */
    }
    public void updateDb(Product product) throws SQLException {
        this.dbUpdateService.updateDb(product);
    }
    public void deleteFromDb(int id) throws SQLException {
        this.dbDeleteService.deleteProductFromDb(id);
    }
    public static void createDbRow(Product product) throws SQLException {
        dbCreateService.updateDb(product);
    }
    public ArrayList<Product> getAllProducts() throws SQLException {
        return this.dbRetrieveService.getProducts();
    }
    public static void main( String []argv) throws IOException, TimeoutException{
        //HoService.synchronizeDb();
        
       /* ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT,true);
       // String queueName = channel.queueDeclare().getQueue();

       
           channel.queueBind("product_sales_queue_bo1", exchange_name, "1");
           channel.queueBind("product_sales_queue_bo2",exchange_name,"2");

        
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback2 = (consumerTag,delivery) -> {
            try {
                bo.Product receivedProduct = bo.Product.deserialize(delivery.getBody());
                System.out.println(receivedProduct.id+":bo2");
                System.out.println("passed");
                Product product = new Product(
                        Integer.parseInt(("2"+Integer.toString(receivedProduct.id))),
                        receivedProduct.date,
                        receivedProduct.product,
                        receivedProduct.qty,
                        receivedProduct.cost,
                        "bo2"
                );
                createDbRow(product);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        DeliverCallback deliverCallback1 = (consumerTag,delivery) -> {
            try {
                bo.Product receivedProduct = bo.Product.deserialize(delivery.getBody());
                System.out.println(receivedProduct.id+":bo1");
                System.out.println("passed");
                Product product = new Product(
                        Integer.parseInt(("1"+Integer.toString(receivedProduct.id))),
                        receivedProduct.date,
                        receivedProduct.product,
                        receivedProduct.qty,
                        receivedProduct.cost,
                        "bo1"
                        );
                createDbRow(product);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };
        
        channel.basicConsume("product_sales_queue_bo1",true, deliverCallback1,consumerTag -> {});
        channel.basicConsume("product_sales_queue_bo2", true, deliverCallback2, consumerTag -> {});
    */
        
    }
}
