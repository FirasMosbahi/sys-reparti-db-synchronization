package bo;

import java.sql.*;
import java.util.ArrayList;

public class DbRetrieveService {
    private int boNumber;
    private String url;
    private String user;
    private String password;
    private String query;
    private String query2;
    public DbRetrieveService(int boNumber){
        this.boNumber = boNumber;
        this.user = "root";
        this.password = "";
        this.url = "jdbc:mysql://localhost:3306/bo"+Integer.toString(this.boNumber);
        this.query = "SELECT * FROM product_sale";
        this.query2 = "SELECT * FROM product_sale WHERE isSynchronized=?";
    }
    public ArrayList<Product> getProducts() throws SQLException{
        ArrayList<Product> productList = new ArrayList<Product>();
        try (
                Connection connection = DriverManager.getConnection(url,user,password);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ){


            while(resultSet.next()){
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("date"),
                        resultSet.getString("product"),
                        resultSet.getInt("qty"),
                        resultSet.getDouble("cost"),
                        resultSet.getBoolean("isSynchronized")
                );
                productList.add(product);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return productList;
    }
    public ArrayList<Product> getNotSyncProducts() throws SQLException{
        ArrayList<Product> productList = new ArrayList<Product>();
        try (
                Connection connection = DriverManager.getConnection(url,user,password);
                PreparedStatement preparedStatement = connection.prepareStatement(query2);
        ){
            preparedStatement.setBoolean(1,false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("date"),
                        resultSet.getString("product"),
                        resultSet.getInt("qty"),
                        resultSet.getDouble("cost"),
                        resultSet.getBoolean("isSynchronized")
                );
                productList.add(product);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return productList;
    }
}
