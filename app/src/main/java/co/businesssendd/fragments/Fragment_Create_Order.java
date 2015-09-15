package co.businesssendd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import co.businesssendd.R;
import co.businesssendd.activity.Activity_AddProduct;
import co.businesssendd.activity.Activity_Main;
import co.businesssendd.gettersandsetters.Drop_Address;
import co.businesssendd.gettersandsetters.Drop_User;


public class Fragment_Create_Order extends Fragment implements TextWatcher {
    Button NextPage;
    EditText etName, etEmail, etPhone;
    RadioButton bulk, premium, cod, freecheckout;

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Activity_Main.mProgress != null && Activity_Main.mProgress.isShowing()) {
            Activity_Main.mProgress.dismiss();
        }
        Activity_Main.exit = true;
        etName = (EditText) view.findViewById(R.id.etName);
        etName.addTextChangedListener(this);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etEmail.addTextChangedListener(this);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etPhone.addTextChangedListener(this);
        bulk = (RadioButton) view.findViewById(R.id.radio3);
        premium = (RadioButton) view.findViewById(R.id.radio4);
        cod = (RadioButton) view.findViewById(R.id.radio2);
        freecheckout = (RadioButton) view.findViewById(R.id.radio1);
        ((Activity_Main) getActivity()).setActionBarTitle("Create Order");
        NextPage = (Button) view.findViewById(R.id.NextPage);
        Fragment_New_Addresses New_Addresses = new Fragment_New_Addresses();
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.frame_container1, New_Addresses, "Fragment_New_Addresses")
                .commit();

        NextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etName.getText().toString())) {
                    if (!TextUtils.isEmpty(etPhone.getText().toString())) {
                        if (etPhone.getText().length() == 10) {
                            Fragment_New_Addresses New_Addresses = (Fragment_New_Addresses) getFragmentManager().findFragmentByTag("Fragment_New_Addresses");
                            if (New_Addresses.getAddress() != null) {
                                Intent i = new Intent(getActivity(), Activity_AddProduct.class);
                                Drop_User dropUser = new Drop_User();
                                dropUser.setEmail(etEmail.getText().toString());
                                dropUser.setName(etName.getText().toString());
                                dropUser.setPhone(etPhone.getText().toString());
                                Gson GS = new Gson();
                                Drop_Address dropAddress = new Drop_Address();
                                dropAddress.setAddressline1(New_Addresses.getAddress().getAddressline1());
                                dropAddress.setAddressline2(New_Addresses.getAddress().getAddressline2());
                                dropAddress.setCity(New_Addresses.getAddress().getCity());
                                dropAddress.setState(New_Addresses.getAddress().getState());
                                dropAddress.setCountry(New_Addresses.getAddress().getCountry());
                                dropAddress.setPincode(New_Addresses.getAddress().getPincode());
                                dropAddress.setSaveAddress(New_Addresses.getAddress().getSaveAddress());
                                dropAddress.setDropUser(dropUser);
                                String Drop_Address_Details = GS.toJson(dropAddress);
                                i.putExtra("Drop_Address_Details", Drop_Address_Details);
                                if (bulk.isChecked()) {
                                    i.putExtra("Shipping_Mode", "B");
                                } else {
                                    i.putExtra("Shipping_Mode", "N");
                                }
                                if (cod.isChecked()) {
                                    i.putExtra("payment_method", "C");
                                } else {
                                    i.putExtra("payment_method", "F");
                                }
                                startActivity(i);
                                getActivity().overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                            }else {
                                Toast.makeText(getActivity(),"NO Address FOunfd",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            etPhone.setError("* Enter a 10 digit Number ");
                        }
                    } else {
                        etPhone.setError("* Enter Number ");
                    }

                } else {
                    etName.setError("* Enter Name ");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_create_order, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        Activity_Main.exit = true;

        if (Activity_Main.mProgress != null && Activity_Main.mProgress.isShowing()) {
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Create Order");

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!etName.getText().toString().isEmpty()) etName.setError(null, null);
        if (!etEmail.getText().toString().isEmpty()) etEmail.setError(null, null);
        if (!etPhone.getText().toString().isEmpty()) etPhone.setError(null, null);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}