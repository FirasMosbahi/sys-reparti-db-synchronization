package ho;

import java.io.*;

public class Product implements Serializable {
    public int id;
    public String date;
    public String region;
    public String product;
    public int qty;
    public double cost;
    public double amt;
    public double tax;
    public double total;
    public String senderBo;
    public Product(int id,String date,String region,String product,int qty,double cost,double amt,double tax,double total,String senderBo){
        this.id = id;
        this.date = date;
        this.region = region;
        this.product = product;
        this.qty = qty;
        this.cost = cost;
        this.tax = tax;
        this.amt = amt;
        this.total = total;
        this.senderBo = senderBo;
    }
    public byte[] getByteArray() throws Exception{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(os);
        objOut.writeObject(this);
        return os.toByteArray();
    }
    public static bo.Product deserialize(byte[] byteArray) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        ObjectInputStream is = new ObjectInputStream(in);
        return (bo.Product) is.readObject();
    }
}
