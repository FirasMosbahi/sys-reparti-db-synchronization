package bo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbCreateService {
    private int boNumber;
    private String url;
    private String user;
    private String password;
    private String query;
    public DbCreateService(int boNumber){
        this.boNumber = boNumber;
        this.user = "root";
        this.password = "";
        this.url = "jdbc:mysql://localhost:3306/bo"+Integer.toString(this.boNumber);
        this.query = "INSERT INTO product_sale (id,date,product,qty,cost,isSynchronized) VALUES (?,?,?,?,?,?)";
    }
    public void updateDb(Product product) throws SQLException{
        try (
                Connection connection = DriverManager.getConnection(url,user,password);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1,product.id);
            preparedStatement.setString(2,product.date);
            preparedStatement.setString(3, product.product);
            preparedStatement.setInt(4,product.qty);
            preparedStatement.setDouble(5,product.cost);
            preparedStatement.setBoolean(6,false);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
