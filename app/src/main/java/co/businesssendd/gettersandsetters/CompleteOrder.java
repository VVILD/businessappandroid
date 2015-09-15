package co.businesssendd.gettersandsetters;


import java.util.ArrayList;

public class CompleteOrder {
    private Drop_Address address;
    private String shippingMethod;
    private String paymentMode;
    private ArrayList<Product> products;

    public Drop_Address getAddress() {
        return address;
    }

    public void setAddress(Drop_Address address) {
        this.address = address;
    }


    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}