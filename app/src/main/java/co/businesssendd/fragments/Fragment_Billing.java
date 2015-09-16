package co.businesssendd.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import co.businesssendd.R;
import co.businesssendd.activity.Activity_Main;
import co.businesssendd.activity.Activity_Payment_Summary;
import co.businesssendd.gettersandsetters.Billing;
import co.businesssendd.gettersandsetters.Billing_Product;
import co.businesssendd.helper.NetworkUtils;
import co.businesssendd.helper.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Fragment_Billing extends Fragment {
    public static Button Start_date,End_Date,PaymentSummary,GenerateBilling;
    LinearLayout llDateSelector,llOnResult;
    public static boolean a=false,b =false;
    TextView tvBillingtsCost,tvBillingsTax,tvBillingsTotalCost,tvBillingsCODrem,tvBillingsRemPending;
    ProgressDialog pd;
    Utils mUtils;
    ArrayList<Billing> CompleteBillingDetails = new ArrayList<>();
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Activity_Main.mProgress!=null && Activity_Main.mProgress.isShowing()){
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Billing");
        Activity_Main.exit = false;
        mUtils= new Utils(getActivity());
        Start_date = (Button)view.findViewById(R.id.Start_date);
        End_Date = (Button)view.findViewById(R.id.End_Date);
        PaymentSummary = (Button)view.findViewById(R.id.PaymentSummary);
        GenerateBilling = (Button)view.findViewById(R.id.GenerateBilling);
        llDateSelector = (LinearLayout)view.findViewById(R.id.llDateSelector);
        llOnResult = (LinearLayout)view.findViewById(R.id.llOnResult);
        tvBillingtsCost =(TextView)view.findViewById(R.id.tvBillingtsCost);
        tvBillingsTax =(TextView)view.findViewById(R.id.tvBillingsTax);
        tvBillingsTotalCost =(TextView)view.findViewById(R.id.tvBillingsTotalCost);
        tvBillingsCODrem =(TextView)view.findViewById(R.id.tvBillingsCODrem);
        tvBillingsRemPending =(TextView)view.findViewById(R.id.tvBillingsRemPending);

        End_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putBoolean("Start_date",false);
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        Start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putBoolean("Start_date",true);
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        GenerateBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a && b) {
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Generating, Please wait...");
                    pd.setCancelable(false);
                    pd.setIndeterminate(true);
                    final NetworkUtils mnetworkutils = new NetworkUtils(getActivity());
                    if (mnetworkutils.isnetconnected()) {
                        pd.show();
                        mnetworkutils.getapi().getBilling(mUtils.getvalue("UserName"),Start_date.getText().toString(),End_Date.getText().toString(), new Callback<Response>() {
                            @Override
                            public void success(Response user, Response response) {
                                pd.dismiss();

                                BufferedReader reader;
                                StringBuilder sb = new StringBuilder();
                                try {
                                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                                    String line;
                                    try {
                                        while ((line = reader.readLine()) != null) {
                                            sb.append(line);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String result = sb.toString();
                                Log.i("ultresult", result);
                                try {
                                    JSONArray jArray = new JSONArray(result);
                                    for (int j = 0; j < jArray.length(); j++) {
                                        JSONArray jArrayObject = jArray.getJSONArray(j);
                                        Log.i("sdfasdf", jArrayObject.get(1).toString());
                                        Billing mBilling = new Billing();
                                        ArrayList<Billing_Product> productArrayList = new ArrayList<>();
                                        mBilling.setDrop_address_address(jArrayObject.getJSONObject(1).getString("drop_address_address"));
                                        mBilling.setDrop_address_city(jArrayObject.getJSONObject(1).getString("drop_address_city"));
                                        mBilling.setDrop_address_pincode(jArrayObject.getJSONObject(1).getString("drop_address_pincode"));
                                        mBilling.setDrop_address_state(jArrayObject.getJSONObject(1).getString("drop_address_state"));
                                        mBilling.setReceiver_name(jArrayObject.getJSONObject(1).getString("receiver_name"));
                                        mBilling.setTotal_cod_remittance(jArrayObject.getJSONObject(1).getString("total_cod_remittance"));
                                        mBilling.setTotal_remittance_pending(jArrayObject.getJSONObject(1).getString("total_remittance_pending"));
                                        mBilling.setTotal_shipping_cost(jArrayObject.getJSONObject(1).getString("total_shipping_cost"));
                                        mBilling.setOrderId(jArrayObject.get(0).toString());
                                       for(int k = 0;k<jArrayObject.getJSONObject(1).getJSONArray("products").length();k++) {
                                           Billing_Product bp = new Billing_Product();
                                           JSONObject jsonObjectProducts = new JSONObject();
                                           jsonObjectProducts = jArrayObject.getJSONObject(1).getJSONArray("products").getJSONObject(k);
                                           bp.setPrice(jsonObjectProducts.getString("price"));
                                           bp.setName(jsonObjectProducts.getString("name"));
                                           bp.setApplied_weight(jsonObjectProducts.getString("applied_weight"));
                                           bp.setCod_cost(jsonObjectProducts.getString("cod_cost"));
                                           bp.setDate(jsonObjectProducts.getString("date"));
                                           bp.setQuantity(jsonObjectProducts.getString("quantity"));
                                           bp.setRemittance(jsonObjectProducts.getString("remittance"));
                                           bp.setReturn_cost(jsonObjectProducts.getString("return_cost"));
                                           bp.setShipping_cost(jsonObjectProducts.getString("shipping_cost"));
                                           bp.setTracking_id(jsonObjectProducts.getString("tracking_id"));
                                           productArrayList.add(bp);
                                       }
                                        mBilling.setProducts(productArrayList);
                                        CompleteBillingDetails.add(mBilling);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }int total_cod_remittance=0,total_remittance_pending=0,total_shipping_cost=0;
                                double servicetax=0;
                                for(int p =0 ;p<CompleteBillingDetails.size();p++){
                                    total_shipping_cost += Integer.parseInt(CompleteBillingDetails.get(p).getTotal_shipping_cost());
                                    total_remittance_pending += Integer.parseInt(CompleteBillingDetails.get(p).getTotal_remittance_pending());
                                    total_cod_remittance += Integer.parseInt(CompleteBillingDetails.get(p).getTotal_cod_remittance());
                                }
                                servicetax = Math.round(.14 * total_shipping_cost * 100)/100;
                                tvBillingtsCost.setText(String.valueOf(total_shipping_cost));
                                tvBillingsCODrem.setText(String.valueOf(total_cod_remittance));
                                tvBillingsTax.setText(String.valueOf(servicetax));
                                tvBillingsRemPending.setText(String.valueOf(total_remittance_pending));
                                tvBillingsTotalCost.setText(String.valueOf(total_shipping_cost+servicetax));
                                llDateSelector.setVisibility(View.GONE);
                                llOnResult.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                pd.dismiss();
                                Log.i("Error:", error.toString());
                                Toast.makeText(getActivity(), "Error in getting details. Please try again in some time.", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Choose a correct date", Toast.LENGTH_LONG).show();
                }
            }
        });
        PaymentSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson GS = new Gson();
                String completeDetails = GS.toJson(CompleteBillingDetails);
                Intent i = new Intent(getActivity(),Activity_Payment_Summary.class);
                i.putExtra("completeDetails", completeDetails);
                startActivity(i);
                getActivity().overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_billing, container, false);

    }
    @Override
    public void onResume(){
        super.onResume();
        if (Activity_Main.mProgress!=null && Activity_Main.mProgress.isShowing()){
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Billing");
        Activity_Main.exit = false;
        Start_date.setText("Start Date");
        End_Date.setText("End Date");
        a = false;
        b = false;

    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment(){
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            Log.i("month",String.valueOf(month));
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(getArguments() != null){
                Bundle args = getArguments();
                if(args.getBoolean("Start_date")){
                    Start_date.setText(String.valueOf(day) + "-" + String.valueOf(month+1) + "-" + String.valueOf(year));
                    a = true;
                }else{
                    b = true;
                    End_Date.setText(String.valueOf(day) + "-" + String.valueOf(month+1) + "-" + String.valueOf(year));
                }
            }
        }
    }
}