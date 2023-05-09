package ho;

import ho.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbDeleteService {
    private String url;
    private String user;
    private String password;
    private String query;
    public DbDeleteService(){
        this.user = "root";
        this.password = "";
        this.url = "jdbc:mysql://localhost:3306/ho";
        this.query = "DELETE FROM product_sale WHERE id=?";
    }
    public void deleteProductFromDb(int id) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(url,user,password);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
