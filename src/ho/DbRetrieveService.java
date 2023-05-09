package ho;

import ho.Product;

import java.sql.*;
import java.util.ArrayList;

public class DbRetrieveService {
    private String url;
    private String user;
    private String password;
    private String query;
    public DbRetrieveService(){
        this.user = "root";
        this.password = "";
        this.url = "jdbc:mysql://localhost:3306/ho";
        this.query = "SELECT * FROM product_sale";
    }
    public ArrayList<Product> getProducts() throws SQLException {
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
                        resultSet.getString("senderBo")
                );
                productList.add(product);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return productList;
    }
}
