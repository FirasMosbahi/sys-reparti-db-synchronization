package ho;

import java.io.*;

public class Product implements Serializable {
    public int id;
    public String date;
    public String product;
    public int qty;
    public double cost;
    public double tax;
    public String senderBo;
    public Product(int id,String date,String product,int qty,double cost,String senderBo){
        this.id = id;
        this.date = date;
        this.product = product;
        this.qty = qty;
        this.cost = cost;
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
