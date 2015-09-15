package co.businesssendd.gettersandsetters;

/**
 * Created by harshkaranpuria on 9/7/15.
 */
public class Drop_Address {
    private String addressline1;
    private String addressline2;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private Boolean saveAddress;
    private Drop_User dropUser;

    public Drop_User getDropUser() {
        return dropUser;
    }

    public void setDropUser(Drop_User dropUser) {
        this.dropUser = dropUser;
    }

    public Boolean getSaveAddress() {
        return saveAddress;
    }

    public void setSaveAddress(Boolean saveAddress) {
        this.saveAddress = saveAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

}
