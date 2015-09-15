package co.businesssendd.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import co.businesssendd.R;
import co.businesssendd.activity.Activity_Main;
import co.businesssendd.gettersandsetters.Address;
import co.businesssendd.gettersandsetters.Drop_Address;

public class Fragment_New_Addresses extends Fragment implements TextWatcher {
    Button SavedAddresses;
    EditText AddLine1,AddLine2,City,State,Pincode,Country;
    SavedAddressListner mCallback;
    CheckBox isChecked;
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Activity_Main.mProgress!=null && Activity_Main.mProgress.isShowing()){
            Activity_Main.mProgress.dismiss();
        }

        isChecked = (CheckBox)view.findViewById(R.id.check0);

        AddLine1 = (EditText)view.findViewById(R.id.AddressLine1);
        AddLine1.addTextChangedListener(this);

        AddLine2 = (EditText)view.findViewById(R.id.AddressLine2);
        AddLine2.addTextChangedListener(this);

        City = (EditText)view.findViewById(R.id.City);
        City.addTextChangedListener(this);

        State = (EditText)view.findViewById(R.id.State);
        State.addTextChangedListener(this);

        Pincode = (EditText)view.findViewById(R.id.Pincode);
        Pincode.addTextChangedListener(this);

        Country = (EditText)view.findViewById(R.id.Country);
        Country.addTextChangedListener(this);

        ((Activity_Main) getActivity()).setActionBarTitle("Create Order");
        Activity_Main.exit = true;

        SavedAddresses =(Button)view.findViewById(R.id.previousAddress);
        SavedAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSavedAddressclick();
            }
        });
        if(getArguments() != null){
            Bundle args =  getArguments();
            AddLine1.setText(args.getString("addline1"));
            AddLine2.setText(args.getString("addline2"));
            City.setText(args.getString("city"));
            State.setText(args.getString("state"));
            Pincode.setText(args.getString("pincode"));
            Country.setText(args.getString("country"));;
        }
    }
    public void onPause(){
        super.onPause();
        Log.i("Called", "Called");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_new_addresses, container, false);
    }
    public Address getAddress(){
        if(!TextUtils.isEmpty(AddLine1.getText().toString())){
            if(!TextUtils.isEmpty(City.getText().toString())){
                if(!TextUtils.isEmpty(State.getText().toString())){
                    if(!TextUtils.isEmpty(Country.getText().toString())){
                        if(!TextUtils.isEmpty(Pincode.getText().toString())){
                            Address add  = new Address();
                            add.setAddressline1(AddLine1.getText().toString());
                            add.setAddressline2(AddLine2.getText().toString());
                            add.setCity(City.getText().toString());
                            add.setState(State.getText().toString());
                            add.setCountry(Country.getText().toString());
                            add.setPincode(Pincode.getText().toString());
                            add.setSaveAddress(isChecked.isChecked());
                            return add;
                        }else{
                            Pincode.setError("Please Enter Pincode");
                            return null;
                        }
                    }else{
                        Country.setError("Please Enter Country");
                        return null;
                    }
                }else{
                    State.setError("Please Enter State");
                    return null;
                }
            }else{
                City.setError("Please Enter City");
                return null;
            }
        }else{
            AddLine1.setError("Please Enter AddressLine 1");
            return null;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (Activity_Main.mProgress!=null && Activity_Main.mProgress.isShowing()){
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Create Order");
        Activity_Main.exit = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!AddLine1.getText().toString().isEmpty()) AddLine1.setError(null, null);
        if (!AddLine2.getText().toString().isEmpty()) AddLine2.setError(null, null);
        if (!City.getText().toString().isEmpty()) City.setError(null, null);
        if (!State.getText().toString().isEmpty()) State.setError(null, null);
        if (!Pincode.getText().toString().isEmpty()) Pincode.setError(null, null);
        if (!Country.getText().toString().isEmpty()) Country.setError(null, null);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface SavedAddressListner {
        void onSavedAddressclick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (SavedAddressListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

}