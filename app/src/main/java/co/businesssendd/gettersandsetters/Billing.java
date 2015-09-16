package co.businesssendd.gettersandsetters;

import java.util.ArrayList;

/**
 * Created by harshkaranpuria on 9/15/15.
 */
public class Billing {


    private String orderId;
    private String drop_address_address;
    private String drop_address_city;
    private String drop_address_pincode;
    private String drop_address_state;
    private ArrayList<Billing_Product> products;
    private String receiver_name;
    private String total_cod_remittance;
    private String total_remittance_pending;
    private String total_shipping_cost;

    public String getDrop_address_address() {
        return drop_address_address;
    }

    public void setDrop_address_address(String drop_address_address) {
        this.drop_address_address = drop_address_address;
    }

    public String getDrop_address_city() {
        return drop_address_city;
    }

    public void setDrop_address_city(String drop_address_city) {
        this.drop_address_city = drop_address_city;
    }

    public String getDrop_address_pincode() {
        return drop_address_pincode;
    }

    public void setDrop_address_pincode(String drop_address_pincode) {
        this.drop_address_pincode = drop_address_pincode;
    }

    public String getDrop_address_state() {
        return drop_address_state;
    }

    public void setDrop_address_state(String drop_address_state) {
        this.drop_address_state = drop_address_state;
    }

    public ArrayList<Billing_Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Billing_Product> products) {
        this.products = products;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getTotal_cod_remittance() {
        return total_cod_remittance;
    }

    public void setTotal_cod_remittance(String total_cod_remittance) {
        this.total_cod_remittance = total_cod_remittance;
    }

    public String getTotal_remittance_pending() {
        return total_remittance_pending;
    }

    public void setTotal_remittance_pending(String total_remittance_pending) {
        this.total_remittance_pending = total_remittance_pending;
    }

    public String getTotal_shipping_cost() {
        return total_shipping_cost;
    }

    public void setTotal_shipping_cost(String total_shipping_cost) {
        this.total_shipping_cost = total_shipping_cost;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
