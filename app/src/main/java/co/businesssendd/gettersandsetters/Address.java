package co.businesssendd.gettersandsetters;

import com.activeandroid.annotation.Column;

/**
 * Created by harshkaranpuria on 9/7/15.
 */
public class Address {
    public String addressline1;
    public String addressline2;
    public String city;
    public String state;
    public String pincode;
    public String country;
    public Boolean saveAddress;

    public Boolean getSaveAddress() {
        return saveAddress;
    }

    public void setSaveAddress(Boolean saveAddress) {
        this.saveAddress = saveAddress;
    }


    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
