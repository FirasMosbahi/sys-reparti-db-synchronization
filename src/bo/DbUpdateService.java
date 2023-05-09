package bo;

import java.sql.*;
public class DbUpdateService {
    private int boNumber;
    private String url;
    private String user;
    private String password;
    private String query;
    public DbUpdateService(int boNumber){
        this.boNumber = boNumber;
        this.user = "root";
        this.password = "";
        this.url = "jdbc:mysql://localhost:3306/bo"+Integer.toString(this.boNumber);
        this.query = "UPDATE product_sale SET date=? ,product=? ,qty=? ,cost=? WHERE id=? AND isSynchronized=?";
    }
    public void updateSynchronizedToTrue(Product product) throws SQLException{
        try (
                Connection connection = DriverManager.getConnection(url,user,password);
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE product_sale SET isSynchronized=? WHERE id=?");
        ){
            preparedStatement.setBoolean(1,true);
            preparedStatement.setInt(2,product.id);
            preparedStatement.executeUpdate();
        }
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
            preparedStatement.setInt(6,product.id);
            preparedStatement.setBoolean(7,false);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
