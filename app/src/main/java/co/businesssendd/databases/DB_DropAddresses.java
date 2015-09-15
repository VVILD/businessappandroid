package co.businesssendd.databases;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import co.businesssendd.gettersandsetters.Address;
import co.businesssendd.gettersandsetters.Drop_Address;
import co.businesssendd.gettersandsetters.Drop_User;

@Table(name = "drop_address")
public class DB_DropAddresses extends Model {
    @Column(name = "addressline1")
    public String addressline1;
    @Column(name = "addressline2")
    public String addressline2;
    @Column(name = "city")
    public String city;
    @Column(name = "state")
    public String state;
    @Column(name = "pincode")
    public String pincode;
    @Column(name = "country")
    public String country;
    @Column(name = "saveAddress")
    public boolean saveAddress;
    @Column(name = "drop_user")
    public Drop_User drop_user;

    public void AddToDB(Drop_Address address) {

        this.addressline1 = address.getAddressline1();
        this.addressline2 =address.getAddressline2();
        this.city = address.getCity();
        this.state = address.getState();
        this.pincode = address.getPincode();
        this.country = address.getCountry();
        this.saveAddress = address.getSaveAddress();
        this.drop_user = address.getDropUser();
        this.save();
    }

    public static List<DB_DropAddresses> getAllAddress() {
        return new Select()
                .from(DB_DropAddresses.class)
                .execute();
    }

    public void deleteAllItems(){
        new Delete().from(DB_DropAddresses.class).execute();
    }

}