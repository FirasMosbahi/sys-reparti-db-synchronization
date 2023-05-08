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
        this.query = "UPDATE product_sale SET date=? ,region=? ,product=? ,qty=? ,cost=? ,tax=? ,amt=? ,total=? ,isSynchronized=? WHERE id=?";
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
            preparedStatement.setString(2, product.region);
            preparedStatement.setString(3, product.product);
            preparedStatement.setInt(4,product.qty);
            preparedStatement.setDouble(5,product.cost);
            preparedStatement.setDouble(6,product.tax);
            preparedStatement.setDouble(7,product.amt);
            preparedStatement.setDouble(8,product.total);
            preparedStatement.setBoolean(9,false);
            preparedStatement.setInt(10,product.id);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
