package co.businesssendd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import co.businesssendd.R;
import co.businesssendd.activity.Activity_Main;
import co.businesssendd.databases.DB_UserDetails;
import co.businesssendd.gettersandsetters.User_Profile;
import co.businesssendd.gettersandsetters.Users;
import co.businesssendd.helper.NetworkUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Fragment_Profile extends Fragment {
    private TextView name, phone, address, time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        ((Activity_Main) getActivity()).setActionBarTitle("Profile");
        Activity_Main.exit = false;

        name = (TextView) view.findViewById(R.id.NameValue);
        phone = (TextView) view.findViewById(R.id.NumberValue);
        address = (TextView) view.findViewById(R.id.OfficeAddressValue);
        time = (TextView) view.findViewById(R.id.PickupTimeValue);

        final NetworkUtils mnetworkutils = new NetworkUtils(getActivity());
        if (mnetworkutils.isnetconnected()) {
            DB_UserDetails dbUserDetails = DB_UserDetails.getUser();
            mnetworkutils.getapi().getProfile(dbUserDetails.resource_uri, new Callback<User_Profile>() {
                @Override
                public void success(User_Profile user, Response response) {
                    if (Activity_Main.mProgress != null && Activity_Main.mProgress.isShowing()) {
                        Activity_Main.mProgress.dismiss();
                    }
                    Users mUsers = new Users();
                    mUsers.setPickupadd(user.getAddress());
                    mUsers.setPickuptime(user.getPickup_time());
                    mUsers.setNumber(user.getContact_mob());
                    DB_UserDetails.update(mUsers);
                    name.setText(user.getName());
                    phone.setText(user.getContact_mob());
                    address.setText(user.getAddress() + ", " + user.getCity() + ", " + user.getState() + ", " + user.getPincode());
                    if (user.getPickup_time() != null) {
                        switch (user.getPickup_time()) {
                            case "E":
                                time.setText("Evening");
                                break;
                            case "M":
                                time.setText("Morning");
                                break;
                            case "A":
                                time.setText("Afternoon");
                                break;
                            default:
                                time.setText("Morning");
                                break;
                        }
                    }else{
                        time.setText("Not Set");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (Activity_Main.mProgress != null && Activity_Main.mProgress.isShowing()) {
                        Activity_Main.mProgress.dismiss();
                    }
                    Toast.makeText(getActivity(), "Error in fetching your profile. Please try again in some time.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }
    }
}