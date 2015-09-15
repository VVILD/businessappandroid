package co.businesssendd.databases;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import co.businesssendd.gettersandsetters.Users;
@Table(name = "user_details")
public class DB_UserDetails extends Model {

    @Column(name = "name")
    public String name;
    @Column(name = "resource_uri")
    public String resource_uri;
    @Column(name = "number")
    public String number;
    @Column(name = "pickupaddress")
    public String pickupaddress;
    @Column(name = "pickuptime")
    public String pickuptime;
    @Column(name = "username")
    public String username;

    public void AddToDB(Users user) {
        this.name = user.getName();
        this.resource_uri = user.getBusiness();
        this.number = user.getNumber();
        this.username = user.getUsername();
        this.pickupaddress = user.getPickupadd();
        this.pickuptime = user.getPickuptime();
        this.save();
    }
    public static void update(Users user){
        DB_UserDetails userDB = new Select()
                .from(DB_UserDetails.class)
                .executeSingle();
        if (userDB != null) {
            userDB.number = user.getNumber();
            userDB.pickupaddress = user.getPickupadd();
            userDB.pickuptime = user.getPickuptime();
            userDB.save();
        }

    }

    public static DB_UserDetails getUser(){
        return new Select()
                .from(DB_UserDetails.class)
                .executeSingle();
    }
    public void deleteAllItems(){
        new Delete().from(DB_UserDetails.class).execute();
    }

}