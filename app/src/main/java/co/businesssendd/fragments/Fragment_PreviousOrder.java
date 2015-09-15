package co.businesssendd.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.businesssendd.R;
import co.businesssendd.activity.Activity_Main;

public class Fragment_PreviousOrder extends Fragment {
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Activity_Main.mProgress!=null && Activity_Main.mProgress.isShowing()){
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Previous Orders");
        Activity_Main.exit = false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_profile, container, false);

    }
    @Override
    public void onResume(){
        super.onResume();
        if (Activity_Main.mProgress!=null && Activity_Main.mProgress.isShowing()){
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Previous Orders");
        Activity_Main.exit = false;
    }
}
