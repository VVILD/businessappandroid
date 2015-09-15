package co.businesssendd.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.businesssendd.R;
import co.businesssendd.activity.Activity_Main;
import co.businesssendd.databases.DB_DropAddresses;
import co.businesssendd.gettersandsetters.Drop_Address;

public class Fragment_Saved_Addresses extends Fragment {
    BackPressListener mCallback;
    ListItemClickedListener listItemClickedListener;
    Button  BackButton;
    ListView lvSavedAddresses;
    private ArrayList<Drop_Address> Address_list;
    AddressList_Adapter mAdapter;
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Activity_Main.mProgress!=null && Activity_Main.mProgress.isShowing()){
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Create Order");
        Activity_Main.exit = false;
        BackButton =(Button)view.findViewById(R.id.backButton);
        lvSavedAddresses =(ListView)view.findViewById(R.id.lvAddresses);
        mAdapter = new AddressList_Adapter(getActivity(), R.layout.list_item_saved_addresses, ShowAddressToList());
        lvSavedAddresses.setAdapter(mAdapter);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onBackButtonPressed();
            }
        });
        lvSavedAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItemClickedListener.onListItemClicked(Address_list.get(position).getAddressline1(),Address_list.get(position).getAddressline2(),Address_list.get(position).getCity(),Address_list.get(position).getState(),Address_list.get(position).getPincode(),Address_list.get(position).getCountry());
            }
        });
    }
    public interface BackPressListener {
        void onBackButtonPressed();
    }
    public interface ListItemClickedListener {
        void onListItemClicked(String addline1,String addline2,String city,String state,String pincode,String country);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_saved_addresses, container, false);
    }
    @Override
    public void onResume(){
        super.onResume();
        if (Activity_Main.mProgress!=null && Activity_Main.mProgress.isShowing()){
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Create Order");
        Activity_Main.exit = false;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (BackPressListener) activity;
            listItemClickedListener = (ListItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        listItemClickedListener = null;
    }

    //Address Holder
    public class addresslist_holder {
        private TextView name;
        private TextView address;
    }
    //Get all Saved Addresses in Address_Receiver DB.
    public ArrayList<Drop_Address>   ShowAddressToList() {
        Address_list = new ArrayList<>();
        List<DB_DropAddresses> list = DB_DropAddresses.getAllAddress();
        for (int i = 0; i < DB_DropAddresses.getAllAddress().size(); i++) {

            DB_DropAddresses addDBReceiver = list.get(i);
            if(addDBReceiver.saveAddress) {
                Drop_Address po = new Drop_Address();
                po.setAddressline1(addDBReceiver.addressline1);
                po.setAddressline2(addDBReceiver.addressline2);
                po.setCity(addDBReceiver.city);
                po.setState(addDBReceiver.state);
                po.setCountry(addDBReceiver.country);
                po.setPincode(addDBReceiver.pincode);
                Address_list.add(po);
            }
        }
        return Address_list;
    }


    //Set up Address Adapter
    public class AddressList_Adapter extends ArrayAdapter<Drop_Address> {

        private Context c;
        private List<Drop_Address> address_list;

        public AddressList_Adapter(Context context, int resource, List<Drop_Address> objects) {
            super(context, resource, objects);
            this.c = context;
            this.address_list = objects;
        }


        @Override
        public void add(Drop_Address object) {
            super.add(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return address_list.size();
        }

        @Override
        public Drop_Address getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(Drop_Address item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            addresslist_holder addresslist_holder;
            if (convertView == null) {
                //initialize holder
                addresslist_holder = new addresslist_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_saved_addresses, parent, false);
                addresslist_holder.name = (TextView) convertView.findViewById(R.id.tvList_Item_Address_Name);
                addresslist_holder.address = (TextView) convertView.findViewById(R.id.ivList_item_Address_AddressLine);
                convertView.setTag(addresslist_holder);
            } else {
                addresslist_holder = (addresslist_holder) convertView.getTag();
            }
            addresslist_holder.name.setText(address_list.get(position).getAddressline1());
            addresslist_holder.address.setText(address_list.get(position).getAddressline1()+address_list.get(position).getAddressline2());
            return convertView;
        }
    }


}