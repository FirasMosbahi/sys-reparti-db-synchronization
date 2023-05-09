package ho;

import ho.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbUpdateService {
    private String url;
    private String user;
    private String password;
    private String query;
    public DbUpdateService(){
        this.user = "root";
        this.password = "";
        this.url = "jdbc:mysql://localhost:3306/ho";
        this.query = "UPDATE product_sale SET date=? ,product=? ,qty=? ,cost=? , WHERE id=?";
    }
    public void updateDb(Product product) throws SQLException{

        try (
                Connection connection = DriverManager.getConnection(url,user,password);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setString(1,product.date);
            preparedStatement.setString(2, product.product);
            preparedStatement.setInt(3,product.qty);
            preparedStatement.setDouble(4,product.cost);
            preparedStatement.setInt(5,product.id);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
