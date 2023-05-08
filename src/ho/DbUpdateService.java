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
        this.query = "UPDATE product_sale SET date=? ,region=? ,product=? ,qty=? ,cost=? ,tax=? ,amt=? ,total=? WHERE id=?";
    }
    public void updateDb(Product product) throws SQLException{

        try (
                Connection connection = DriverManager.getConnection(url,user,password);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setString(1,product.date);
            preparedStatement.setString(2, product.region);
            preparedStatement.setString(3, product.product);
            preparedStatement.setInt(4,product.qty);
            preparedStatement.setDouble(5,product.cost);
            preparedStatement.setDouble(6,product.tax);
            preparedStatement.setDouble(7,product.amt);
            preparedStatement.setDouble(8,product.total);
            preparedStatement.setInt(9,product.id);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
