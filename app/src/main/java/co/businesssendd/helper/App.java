package co.businesssendd.helper;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import java.io.File;

import co.businesssendd.databases.DB_DropAddresses;
import co.businesssendd.databases.DB_UserDetails;
import co.businesssendd.databases.DB_complete_order;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (i < children.length) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
                i++;
            }
        }
        return dir.delete();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDB();
        instance = this;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(DB_UserDetails.class);
        configurationBuilder.addModelClasses(DB_DropAddresses.class);
        configurationBuilder.addModelClasses(DB_complete_order.class);

        ActiveAndroid.initialize(configurationBuilder.create());
    }
}