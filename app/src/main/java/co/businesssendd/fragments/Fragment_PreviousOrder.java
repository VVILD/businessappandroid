package co.businesssendd.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import co.businesssendd.R;
import co.businesssendd.activity.Activity_Main;
import co.businesssendd.activity.Activity_PS_Item_details;
import co.businesssendd.activity.Activity_Payment_Summary;
import co.businesssendd.activity.Activity_PrevOrder_details;
import co.businesssendd.databases.DB_UserDetails;
import co.businesssendd.gettersandsetters.Billing;
import co.businesssendd.gettersandsetters.LoginUser;
import co.businesssendd.gettersandsetters.PreviousOrder;
import co.businesssendd.gettersandsetters.PreviousOrder_Products;
import co.businesssendd.gettersandsetters.User_Profile;
import co.businesssendd.gettersandsetters.Users;
import co.businesssendd.helper.NetworkUtils;
import co.businesssendd.helper.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Fragment_PreviousOrder extends Fragment {
    EditText etSearch;
    Button Previous, Next, Search,ClearFilter;
    ListView lvPreviousOrders;
    ArrayList<PreviousOrder> previousOrders = new ArrayList<>();
    BillingList_Adapter madapter;
    String nextUrl = null, prevUrl = null;
    ProgressDialog pd;
    NetworkUtils mnetworkutils;

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((Activity_Main) getActivity()).setActionBarTitle("Previous Orders");
        Activity_Main.exit = false;
        if (Activity_Main.mProgress != null && Activity_Main.mProgress.isShowing()) {
            Activity_Main.mProgress.dismiss();
        }
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        Previous = (Button) view.findViewById(R.id.Previous);
        Next = (Button) view.findViewById(R.id.Next);
        Search = (Button) view.findViewById(R.id.bSearch);
        ClearFilter =(Button)view.findViewById(R.id.clearFilter);
        ClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPreviousOrders();
            }
        });
        lvPreviousOrders = (ListView) view.findViewById(R.id.lvPreviousOrders);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPages(nextUrl);
            }
        });
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPages(prevUrl);
            }
        });
        mnetworkutils = new NetworkUtils(getActivity());
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Loading, Please wait...");
                    pd.setCancelable(false);
                    pd.setIndeterminate(true);
                    if (mnetworkutils.isnetconnected()) {
                        pd.show();
                        Utils mUtils = new Utils(getActivity());
                        mnetworkutils.getapi().getSearchReasult(etSearch.getText().toString(), new Callback<Response>() {
                            @Override
                            public void success(Response user, Response response) {

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
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    Next.setEnabled(false);
                                    Previous.setEnabled(false);
                                    PreviousOrder mPreviousOrder = new PreviousOrder();
                                    mPreviousOrder.setCity(jsonObject.getJSONObject("order").getString("city"));
                                    mPreviousOrder.setAddress1(jsonObject.getJSONObject("order").getString("address1"));
                                    mPreviousOrder.setAddress2(jsonObject.getJSONObject("order").getString("address2"));
                                    mPreviousOrder.setBook_time(jsonObject.getJSONObject("order").getString("book_time"));
                                    mPreviousOrder.setEmail(jsonObject.getJSONObject("order").getString("email"));
                                    mPreviousOrder.setMethod(jsonObject.getJSONObject("order").getString("method"));
                                    mPreviousOrder.setName(jsonObject.getJSONObject("order").getString("name"));
                                    mPreviousOrder.setOrder_no(jsonObject.getJSONObject("order").getString("order_no"));
                                    mPreviousOrder.setPhone(jsonObject.getJSONObject("order").getString("phone"));
                                    mPreviousOrder.setPincode(jsonObject.getJSONObject("order").getString("pincode"));
                                    mPreviousOrder.setPmethod(jsonObject.getJSONObject("order").getString("payment_method"));
                                    mPreviousOrder.setState(jsonObject.getJSONObject("order").getString("state"));
                                    switch (jsonObject.getJSONObject("order").getString("status")) {
                                        case "P":
                                            mPreviousOrder.setStatus("Pending");
                                            break;
                                        case "C":
                                            mPreviousOrder.setStatus("Completed");
                                            break;
                                        case "N":
                                            mPreviousOrder.setStatus("Cancelled");
                                            break;
                                        case "D":
                                            mPreviousOrder.setStatus("In Transit");
                                            break;
                                        case "R":
                                            mPreviousOrder.setStatus("Return");
                                            break;
                                        case "RC":
                                            mPreviousOrder.setStatus("Return/Complete");
                                            break;
                                        case "PU":
                                            mPreviousOrder.setStatus("Picked UP");
                                            break;
                                        case "DI":
                                            mPreviousOrder.setStatus("Dispatched");
                                            break;
                                        default:
                                            mPreviousOrder.setStatus("Pending");
                                            break;
                                    }
                                    ArrayList<PreviousOrder_Products> MPreviousOrder_products = new ArrayList<>();
                                    for (int l = 0; l < jsonObject.getJSONObject("order").getJSONArray("products").length(); l++) {
                                        PreviousOrder_Products previousOrderProducts = new PreviousOrder_Products();
                                        JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("order").getJSONArray("products").getJSONObject(l).toString());
                                        previousOrderProducts.setName(jsonObject1.getString("name"));
                                        previousOrderProducts.setApplied_weight(jsonObject1.getString("applied_weight"));
                                        previousOrderProducts.setPrice(jsonObject1.getString("price"));
                                        previousOrderProducts.setQuantity(jsonObject1.getString("quantity"));
                                        previousOrderProducts.setReal_tracking_no(jsonObject1.getString("real_tracking_no"));
                                        previousOrderProducts.setShipping_cost(jsonObject1.getString("shipping_cost"));
                                        previousOrderProducts.setTracking_data(jsonObject1.getString("tracking_data"));
                                        previousOrderProducts.setWeight(jsonObject1.getString("weight"));
                                        previousOrderProducts.setSku(jsonObject1.getString("sku"));
                                        MPreviousOrder_products.add(previousOrderProducts);
                                    }
                                    mPreviousOrder.setProducts(MPreviousOrder_products);
                                    previousOrders.clear();
                                    previousOrders.add(mPreviousOrder);
                                    madapter = new BillingList_Adapter(getActivity(), R.layout.list_item_previous_business, previousOrders);
                                    lvPreviousOrders.setAdapter(madapter);
                                    madapter.notifyDataSetChanged();
                                    lvPreviousOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Gson GS = new Gson();
                                            String PreviousOrder = GS.toJson(previousOrders.get(position));
                                            Intent i = new Intent(getActivity(), Activity_PrevOrder_details.class);
                                            i.putExtra("PreviousOrder", PreviousOrder);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                        }
                                    });
                                    pd.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pd.dismiss();

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "Error in fetching your profile. Please try again in some time.", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Enter a valid value", Toast.LENGTH_LONG).show();
                }
            }
        });
        LoadPreviousOrders();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_previous_orders, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Activity_Main.mProgress != null && Activity_Main.mProgress.isShowing()) {
            Activity_Main.mProgress.dismiss();
        }
        ((Activity_Main) getActivity()).setActionBarTitle("Previous Orders");
        Activity_Main.exit = false;
    }

    //Address Holder
    public class prevOrder_holder {
        private TextView OrderID;
        private TextView Status;
        private TextView Recipient_name;
        private TextView Number;
        private TextView City;

    }

    //Set up Address Adapter
    public class BillingList_Adapter extends ArrayAdapter<PreviousOrder> {

        private Context c;
        private List<PreviousOrder> PreviousOrder_details;

        public BillingList_Adapter(Context context, int resource, List<PreviousOrder> objects) {
            super(context, resource, objects);
            this.c = context;
            this.PreviousOrder_details = objects;
        }


        @Override
        public void add(PreviousOrder object) {
            super.add(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return PreviousOrder_details.size();
        }

        @Override
        public PreviousOrder getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(PreviousOrder item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            prevOrder_holder prevOrder_holder;
            if (convertView == null) {
                //initialize holder
                prevOrder_holder = new prevOrder_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_previous_business, parent, false);
                prevOrder_holder.OrderID = (TextView) convertView.findViewById(R.id.OrderID);
                prevOrder_holder.Recipient_name = (TextView) convertView.findViewById(R.id.Recipient_name);
                prevOrder_holder.City = (TextView) convertView.findViewById(R.id.City);
                prevOrder_holder.Number = (TextView) convertView.findViewById(R.id.Number);
                prevOrder_holder.Status = (TextView) convertView.findViewById(R.id.Status);
                convertView.setTag(prevOrder_holder);
            } else {
                prevOrder_holder = (prevOrder_holder) convertView.getTag();
            }
            //setup list objects
            prevOrder_holder.OrderID.setText(PreviousOrder_details.get(position).getOrder_no());
            prevOrder_holder.City.setText(PreviousOrder_details.get(position).getCity());
            prevOrder_holder.Recipient_name.setText(PreviousOrder_details.get(position).getName());
            prevOrder_holder.Number.setText(PreviousOrder_details.get(position).getPhone());
            prevOrder_holder.Status.setText(PreviousOrder_details.get(position).getStatus());
            return convertView;
        }
    }

    public void switchPages(String url) {
        final NetworkUtils mnetworkutils = new NetworkUtils(getActivity());
        if (mnetworkutils.isnetconnected()) {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading, Please wait...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            Utils mUtils = new Utils(getActivity());
            pd.show();
            mnetworkutils.getapi().getNewPage(url, new Callback<Response>() {
                @Override
                public void success(Response user, Response response) {
                    previousOrders.clear();
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
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getJSONObject("meta").get("next").toString().equals("null"))
                            Next.setEnabled(false);
                        else
                            Next.setEnabled(true);

                        if (jsonObject.getJSONObject("meta").get("previous").toString().equals("null"))
                            Previous.setEnabled(false);
                        else
                            Previous.setEnabled(true);

                        nextUrl = jsonObject.getJSONObject("meta").get("next").toString();
                        prevUrl = jsonObject.getJSONObject("meta").get("previous").toString();
                        for (int i = 0; i < jsonObject.getJSONArray("objects").length(); i++) {
                            PreviousOrder mPreviousOrder = new PreviousOrder();
                            mPreviousOrder.setCity(jsonObject.getJSONArray("objects").getJSONObject(i).getString("city"));
                            mPreviousOrder.setAddress1(jsonObject.getJSONArray("objects").getJSONObject(i).getString("address1"));
                            mPreviousOrder.setAddress2(jsonObject.getJSONArray("objects").getJSONObject(i).getString("address2"));
                            mPreviousOrder.setBook_time(jsonObject.getJSONArray("objects").getJSONObject(i).getString("book_time"));
                            mPreviousOrder.setEmail(jsonObject.getJSONArray("objects").getJSONObject(i).getString("email"));
                            mPreviousOrder.setMethod(jsonObject.getJSONArray("objects").getJSONObject(i).getString("method"));

                            mPreviousOrder.setName(jsonObject.getJSONArray("objects").getJSONObject(i).getString("name"));
                            mPreviousOrder.setOrder_no(jsonObject.getJSONArray("objects").getJSONObject(i).getString("order_no"));
                            mPreviousOrder.setPhone(jsonObject.getJSONArray("objects").getJSONObject(i).getString("phone"));
                            mPreviousOrder.setPincode(jsonObject.getJSONArray("objects").getJSONObject(i).getString("pincode"));
                            mPreviousOrder.setPmethod(jsonObject.getJSONArray("objects").getJSONObject(i).getString("payment_method"));
                            mPreviousOrder.setState(jsonObject.getJSONArray("objects").getJSONObject(i).getString("state"));
                            switch (jsonObject.getJSONArray("objects").getJSONObject(i).getString("status")) {
                                case "P":
                                    mPreviousOrder.setStatus("Pending");
                                    break;
                                case "C":
                                    mPreviousOrder.setStatus("Completed");
                                    break;
                                case "N":
                                    mPreviousOrder.setStatus("Cancelled");
                                    break;
                                case "D":
                                    mPreviousOrder.setStatus("In Transit");
                                    break;
                                case "R":
                                    mPreviousOrder.setStatus("Return");
                                    break;
                                case "RC":
                                    mPreviousOrder.setStatus("Return/Complete");
                                    break;
                                case "PU":
                                    mPreviousOrder.setStatus("Picked UP");
                                    break;
                                case "DI":
                                    mPreviousOrder.setStatus("Dispatched");
                                    break;
                                default:
                                    mPreviousOrder.setStatus("Pending");
                                    break;
                            }

                            ArrayList<PreviousOrder_Products> MPreviousOrder_products = new ArrayList<>();
                            for (int l = 0; l < jsonObject.getJSONArray("objects").getJSONObject(i).getJSONArray("products").length(); l++) {
                                PreviousOrder_Products previousOrderProducts = new PreviousOrder_Products();
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONArray("objects").getJSONObject(i).getJSONArray("products").getJSONObject(l).toString());
                                previousOrderProducts.setName(jsonObject1.getString("name"));
                                previousOrderProducts.setApplied_weight(jsonObject1.getString("applied_weight"));
                                previousOrderProducts.setPrice(jsonObject1.getString("price"));
                                previousOrderProducts.setQuantity(jsonObject1.getString("quantity"));
                                previousOrderProducts.setReal_tracking_no(jsonObject1.getString("real_tracking_no"));
                                previousOrderProducts.setShipping_cost(jsonObject1.getString("shipping_cost"));
                                previousOrderProducts.setTracking_data(jsonObject1.getString("tracking_data"));
                                previousOrderProducts.setWeight(jsonObject1.getString("weight"));
                                previousOrderProducts.setSku(jsonObject1.getString("sku"));
                                MPreviousOrder_products.add(previousOrderProducts);
                            }
                            mPreviousOrder.setProducts(MPreviousOrder_products);
                            previousOrders.add(mPreviousOrder);

                        }
                        madapter.notifyDataSetChanged();
                        lvPreviousOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Gson GS = new Gson();
                                String PreviousOrder = GS.toJson(previousOrders.get(position));
                                Intent i = new Intent(getActivity(), Activity_PrevOrder_details.class);
                                i.putExtra("PreviousOrder", PreviousOrder);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                            }
                        });
                        pd.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Error in fetching your profile. Please try again in some time.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }

    }

    public void LoadPreviousOrders() {

        previousOrders.clear();
        if (mnetworkutils.isnetconnected()) {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading, Please wait...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
            Utils mUtils = new Utils(getActivity());
            mnetworkutils.getapi().getPreviousOrder(mUtils.getvalue("UserName"), "10", "0", "-book_time", new Callback<Response>() {
                @Override
                public void success(Response user, Response response) {

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
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getJSONObject("meta").get("next").toString().equals("null"))
                            Next.setEnabled(false);
                        else
                            Next.setEnabled(true);

                        if (jsonObject.getJSONObject("meta").get("previous").toString().equals("null"))
                            Previous.setEnabled(false);
                        else
                            Previous.setEnabled(true);

                        nextUrl = jsonObject.getJSONObject("meta").get("next").toString();
                        prevUrl = jsonObject.getJSONObject("meta").get("previous").toString();
                        for (int i = 0; i < jsonObject.getJSONArray("objects").length(); i++) {
                            PreviousOrder mPreviousOrder = new PreviousOrder();
                            mPreviousOrder.setCity(jsonObject.getJSONArray("objects").getJSONObject(i).getString("city"));
                            mPreviousOrder.setAddress1(jsonObject.getJSONArray("objects").getJSONObject(i).getString("address1"));
                            mPreviousOrder.setAddress2(jsonObject.getJSONArray("objects").getJSONObject(i).getString("address2"));
                            mPreviousOrder.setBook_time(jsonObject.getJSONArray("objects").getJSONObject(i).getString("book_time"));
                            mPreviousOrder.setEmail(jsonObject.getJSONArray("objects").getJSONObject(i).getString("email"));
                            mPreviousOrder.setMethod(jsonObject.getJSONArray("objects").getJSONObject(i).getString("method"));

                            mPreviousOrder.setName(jsonObject.getJSONArray("objects").getJSONObject(i).getString("name"));
                            mPreviousOrder.setOrder_no(jsonObject.getJSONArray("objects").getJSONObject(i).getString("order_no"));
                            mPreviousOrder.setPhone(jsonObject.getJSONArray("objects").getJSONObject(i).getString("phone"));
                            mPreviousOrder.setPincode(jsonObject.getJSONArray("objects").getJSONObject(i).getString("pincode"));
                            mPreviousOrder.setPmethod(jsonObject.getJSONArray("objects").getJSONObject(i).getString("payment_method"));
                            mPreviousOrder.setState(jsonObject.getJSONArray("objects").getJSONObject(i).getString("state"));
                            switch (jsonObject.getJSONArray("objects").getJSONObject(i).getString("status")) {
                                case "P":
                                    mPreviousOrder.setStatus("Pending");
                                    break;
                                case "C":
                                    mPreviousOrder.setStatus("Completed");
                                    break;
                                case "N":
                                    mPreviousOrder.setStatus("Cancelled");
                                    break;
                                case "D":
                                    mPreviousOrder.setStatus("In Transit");
                                    break;
                                case "R":
                                    mPreviousOrder.setStatus("Return");
                                    break;
                                case "RC":
                                    mPreviousOrder.setStatus("Return/Complete");
                                    break;
                                case "PU":
                                    mPreviousOrder.setStatus("Picked UP");
                                    break;
                                case "DI":
                                    mPreviousOrder.setStatus("Dispatched");
                                    break;
                                default:
                                    mPreviousOrder.setStatus("Pending");
                                    break;
                            }

                            ArrayList<PreviousOrder_Products> MPreviousOrder_products = new ArrayList<>();
                            for (int l = 0; l < jsonObject.getJSONArray("objects").getJSONObject(i).getJSONArray("products").length(); l++) {
                                PreviousOrder_Products previousOrderProducts = new PreviousOrder_Products();
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONArray("objects").getJSONObject(i).getJSONArray("products").getJSONObject(l).toString());
                                previousOrderProducts.setName(jsonObject1.getString("name"));
                                previousOrderProducts.setApplied_weight(jsonObject1.getString("applied_weight"));
                                previousOrderProducts.setPrice(jsonObject1.getString("price"));
                                previousOrderProducts.setQuantity(jsonObject1.getString("quantity"));
                                previousOrderProducts.setReal_tracking_no(jsonObject1.getString("real_tracking_no"));
                                previousOrderProducts.setShipping_cost(jsonObject1.getString("shipping_cost"));
                                previousOrderProducts.setTracking_data(jsonObject1.getString("tracking_data"));
                                previousOrderProducts.setWeight(jsonObject1.getString("weight"));
                                previousOrderProducts.setSku(jsonObject1.getString("sku"));
                                MPreviousOrder_products.add(previousOrderProducts);
                            }
                            mPreviousOrder.setProducts(MPreviousOrder_products);
                            previousOrders.add(mPreviousOrder);

                        }
                        madapter = new BillingList_Adapter(getActivity(), R.layout.list_item_previous_business, previousOrders);
                        lvPreviousOrders.setAdapter(madapter);
                        madapter.notifyDataSetChanged();
                        lvPreviousOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Gson GS = new Gson();
                                String PreviousOrder = GS.toJson(previousOrders.get(position));
                                Intent i = new Intent(getActivity(), Activity_PrevOrder_details.class);
                                i.putExtra("PreviousOrder", PreviousOrder);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                            }
                        });

                        pd.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                   pd.dismiss();
                    Toast.makeText(getActivity(), "Error in fetching your profile. Please try again in some time.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }
    }
}