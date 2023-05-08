package ho;

import ho.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbCreateService {
    private String url;
    private String user;
    private String password;
    private String query;
    public DbCreateService(){
        this.user = "root";
        this.password = "";
        this.url = "jdbc:mysql://localhost:3306/ho";
        this.query = "INSERT INTO product_sale (id,date,region,product,qty,cost,tax,amt,total,senderBo) VALUES (?,?,?,?,?,?,?,?,?,?)";
    }
    public void updateDb(Product product) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(url,user,password);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1,product.id);
            preparedStatement.setString(2,product.date);
            preparedStatement.setString(3, product.region);
            preparedStatement.setString(4, product.product);
            preparedStatement.setInt(5,product.qty);
            preparedStatement.setDouble(6,product.cost);
            preparedStatement.setDouble(7,product.tax);
            preparedStatement.setDouble(8,product.amt);
            preparedStatement.setDouble(9,product.total);
            preparedStatement.setString(10, product.senderBo);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
