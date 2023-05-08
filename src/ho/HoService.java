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
    HoService(int boNumber){
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
                    Product product = new Product(
                            Integer.parseInt(("1"+Integer.toString(receivedProduct.id))),
                            receivedProduct.date,
                            receivedProduct.region,
                            receivedProduct.product,
                            receivedProduct.qty,
                            receivedProduct.cost,
                            receivedProduct.amt,
                            receivedProduct.tax,
                            receivedProduct.total,
                            "bo1"
                            );
                    createDbRow(product);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            DeliverCallback deliverCallback2 = (consumerTag,delivery) -> {
                try {
                    bo.Product receivedProduct = bo.Product.deserialize(delivery.getBody());
                    Product product = new Product(
                            Integer.parseInt(("2"+Integer.toString(receivedProduct.id))),
                            receivedProduct.date,
                            receivedProduct.region,
                            receivedProduct.product,
                            receivedProduct.qty,
                            receivedProduct.cost,
                            receivedProduct.amt,
                            receivedProduct.tax,
                            receivedProduct.total,
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
