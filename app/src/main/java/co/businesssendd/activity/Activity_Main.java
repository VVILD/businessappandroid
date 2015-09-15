package co.businesssendd.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.businesssendd.R;
import co.businesssendd.databases.DB_UserDetails;
import co.businesssendd.fragments.Fragment_Billing;
import co.businesssendd.fragments.Fragment_Customer_Support;
import co.businesssendd.fragments.Fragment_New_Addresses;
import co.businesssendd.fragments.Fragment_PreviousOrder;
import co.businesssendd.fragments.Fragment_Create_Order;
import co.businesssendd.fragments.Fragment_Profile;
import co.businesssendd.fragments.Fragment_Saved_Addresses;
import co.businesssendd.gettersandsetters.Drop_Address;
import co.businesssendd.helper.Utils;
import co.businesssendd.navdrawer.Drawer_Item;
import co.businesssendd.navdrawer.NavDrawer_Adapter;


public class Activity_Main extends BaseActivity  implements Fragment_New_Addresses.SavedAddressListner ,Fragment_Saved_Addresses.BackPressListener,Fragment_Saved_Addresses.ListItemClickedListener {
     public static ProgressDialog mProgress, mProgress2;
    public static boolean exit = false;
    public static Toolbar toolbar;
    private TextView toolbar_txtview;
    private CharSequence mTitle;
    private String[] reg_nav_drawer_items;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayList<Drawer_Item> navDrawerItems;
    private Boolean openPreviousBooking = false, openNotifiation = false;
    private NavDrawer_Adapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private Runnable mPendingRunnable;
    private android.os.Handler mHandler = new android.os.Handler();
    private String tag;
    Fragment_New_Addresses newAddresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI(findViewById(R.id.drawer_layout));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.Main_toolbar);
        toolbar_txtview = (TextView) findViewById(R.id.main_activity_toolbar_textview);
        setSupportActionBar(toolbar);
        toolbar_txtview.setMovementMethod(new ScrollingMovementMethod());
         mTitle = getTitle();
        reg_nav_drawer_items = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mProgress = new ProgressDialog(this);
        mProgress2 = new ProgressDialog(this);
        navDrawerItems = new ArrayList<>();
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[0]));
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[1]));
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[2]));
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[3]));
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[4]));

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawer_Adapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                try {
                    getSupportActionBar().setTitle(mTitle);
                } catch (NullPointerException ignored) {

                }
                invalidateOptionsMenu();

                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                    mPendingRunnable = null;
                }
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        if (!openPreviousBooking && !openNotifiation) {
            if (savedInstanceState != null) {
                Fragment_Create_Order orders_fragment = (Fragment_Create_Order) getSupportFragmentManager().getFragment(savedInstanceState, "Fragment_Orders");
                if (orders_fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, orders_fragment, "Fragment_Orders").commit();
            } else {
                displayView(1);
            }
        } else if (openPreviousBooking) {

            Fragment_PreviousOrder history_fragment = new Fragment_PreviousOrder();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, history_fragment, "fragment_history").commit();
            openPreviousBooking = false;
        }

    }


    //On Activity Resume
    @Override
    protected void onResume() {
        super.onResume();
        //checkPlayServices();
        navDrawerItems = new ArrayList<>();
        Utils utils = new Utils(Activity_Main.this);

        reg_nav_drawer_items = getResources().getStringArray(R.array.nav_drawer_items);
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[0]));
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[1]));
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[2]));
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[3]));
        navDrawerItems.add(new Drawer_Item(reg_nav_drawer_items[4]));
         adapter = new NavDrawer_Adapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        if (utils.getvalue("PickupAddress").equals("")) {
            toolbar_txtview.setText("Sendd");
        } else {
            toolbar_txtview.setText(utils.getvalue("PickupAddress"));
        }
        adapter.notifyDataSetChanged();

    }


    //display Fragment
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        Utils utils = new Utils(Activity_Main.this);
        switch (position) {
            case 0:
                fragment = new Fragment_Profile();
                tag = "Fragment_Profile";
                break;
            case 1:
                fragment = new Fragment_Create_Order();
                tag = "Fragment_Orders";
                break;

            case 2:
                fragment = new Fragment_PreviousOrder();
                tag = "fragment_history";
                break;
            case 3:
                fragment = new Fragment_Billing();
                tag = "Fragment_Billing";
                break;
            case 4:
                fragment = new Fragment_Customer_Support();
                tag = "Fragment_Customer_Support";
                break;
            case 5:
                DB_UserDetails userDB = new DB_UserDetails();
                userDB.deleteAllItems();
                utils.clear();
                Activity_Main.mProgress.dismiss();
                Toast.makeText(Activity_Main.this, "Successfully logged out",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Activity_Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                break;

        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("fragment_orders")
                    .replace(R.id.frame_container, fragment, tag).commit();
        }

    }

    public void setActionBarTitle(String title) {
        toolbar_txtview.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        openPreviousBooking = getIntent().getBooleanExtra("OpenPreviousBooking", false);
        openNotifiation = getIntent().getBooleanExtra("OpenNotification", false);

        if (openPreviousBooking) {
            toolbar_txtview.setText("Previous Shipments");
            Fragment_PreviousOrder history_fragment = new Fragment_PreviousOrder();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, history_fragment, "fragment_history").commit();
            openPreviousBooking = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onListItemClicked(String addline1, String addline2, String city, String state, String pincode, String country) {
        Drop_Address da = new Drop_Address();
        da.setAddressline1(addline1);
        da.setAddressline2(addline2);
        da.setCity(city);
        da.setCountry(country);
        da.setPincode(pincode);
        da.setState(state);
        Fragment_New_Addresses New_Addresses = new Fragment_New_Addresses();
        Bundle args = new Bundle();
        args.putString("addline1",addline1);
        args.putString("addline2",addline2);
        args.putString("city",city);
        args.putString("state",state);
        args.putString("country",country);
        args.putString("pincode", pincode);
        New_Addresses.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.animator.pull_in_left, R.animator.push_out_right, R.animator.pull_in_right, R.animator.push_out_left)
                .replace(R.id.frame_container1, New_Addresses, "Fragment_New_Addresses")
                .commit();
     }


    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position,
                                long id) {

            mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    displayView(position);
                }
            };
            mProgress.show();
            mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular));
            mProgress.setContentView(R.layout.spinner_dialog);
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(reg_nav_drawer_items[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
            Activity_Main.this.overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        } else {
            Fragment_Create_Order orders_fragment = new Fragment_Create_Order();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("fragment_orders")
                    .replace(R.id.frame_container, orders_fragment, "fragment_orders").commit();
            exit = true;

        }
    }


    //Fragment_Saved_Addresses INTERFACES
    @Override
    public void onSavedAddressclick() {
        Fragment_Saved_Addresses savedAddressesFragment = new Fragment_Saved_Addresses();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.pull_in_right, R.animator.push_out_left, R.animator.pull_in_left, R.animator.push_out_right)
                .replace(R.id.frame_container1, savedAddressesFragment, "newFragment")
                .addToBackStack("Fragment_New_Addresses")
                .commit();
    }
    @Override
    public void onBackButtonPressed(){
        getSupportFragmentManager().popBackStack();
    }

}