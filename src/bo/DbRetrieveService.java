package bo;

import java.sql.*;
import java.util.ArrayList;

public class DbRetrieveService {
    private int boNumber;
    private String url;
    private String user;
    private String password;
    private String query;
    public DbRetrieveService(int boNumber, boolean onlyNotSynchronized){
        this.boNumber = boNumber;
        this.user = "root";
        this.password = "";
        this.url = "jdbc:mysql://localhost:3306/bo"+Integer.toString(this.boNumber);
        this.query = "SELECT * FROM products_sale" + (onlyNotSynchronized ? "WHERE isSynchronized=FALSE" : "");
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
                        resultSet.getString("region"),
                        resultSet.getString("product"),
                        resultSet.getInt("qty"),
                        resultSet.getDouble("cost"),
                        resultSet.getDouble("amt"),
                        resultSet.getDouble("tax"),
                        resultSet.getDouble("total"),
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
