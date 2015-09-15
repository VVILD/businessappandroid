package co.businesssendd.databases;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import co.businesssendd.gettersandsetters.CompleteOrder;
import co.businesssendd.gettersandsetters.Drop_Address;
import co.businesssendd.gettersandsetters.Product;
import co.businesssendd.gettersandsetters.User_Profile;


@Table(name = "complete_order")
public class DB_complete_order extends Model {
     @Column(name = "address")
    public Drop_Address address;

    @Column(name = "paymentMode")
    public String paymentMode;
    @Column(name = "shippingMethod")
    public String shippingMethod;
    @Column(name = "products")
    public ArrayList<Product>  products;


    public void AddToDB(CompleteOrder completeOrder) {

        this.address = completeOrder.getAddress();
        this.paymentMode = completeOrder.getPaymentMode();
        this.shippingMethod = completeOrder.getShippingMethod();
        this.products = completeOrder.getProducts();
        this.save();
    }

    public static List<DB_complete_order> getAllOrders() {
        return new Select()
                .from(DB_complete_order.class)
                .execute();
    }

    public void deleteAllOrders(){
        new Delete().from(DB_complete_order.class).execute();
    }


}
