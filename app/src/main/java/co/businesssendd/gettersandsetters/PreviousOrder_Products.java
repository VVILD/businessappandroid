package co.businesssendd.gettersandsetters;

/**
 * Created by harshkaranpuria on 9/16/15.
 */
public class PreviousOrder_Products {
    private String real_tracking_no;
    private String name;
    private String sku;
    private String quantity;
    private String weight;
    private String applied_weight;
    private String price;
    private String shipping_cost;
    private String tracking_data;

    public String getReal_tracking_no() {
        return real_tracking_no;
    }

    public void setReal_tracking_no(String real_tracking_no) {
        this.real_tracking_no = real_tracking_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getApplied_weight() {
        return applied_weight;
    }

    public void setApplied_weight(String applied_weight) {
        this.applied_weight = applied_weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getTracking_data() {
        return tracking_data;
    }

    public void setTracking_data(String tracking_data) {
        this.tracking_data = tracking_data;
    }
}
