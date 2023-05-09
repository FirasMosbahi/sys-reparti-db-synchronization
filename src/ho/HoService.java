package ho;


import com.rabbitmq.client.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class HoService {

    private String exchange_name;
    private DbCreateService dbCreateService;
    private DbUpdateService dbUpdateService;
    private DbDeleteService dbDeleteService;
    private DbRetrieveService dbRetrieveService;
    HoService(){
        this.exchange_name = "product_sale_exchange";
        this.dbCreateService = new DbCreateService();
        this.dbDeleteService = new DbDeleteService();
        this.dbUpdateService = new DbUpdateService();
        this.dbRetrieveService = new DbRetrieveService();
    }
    public void synchronizeDb() {
        ConnectionFactory factory = new ConnectionFactory();
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
                    System.out.println(receivedProduct.id);
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
        }catch (Exception e){
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
