package co.businesssendd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import co.businesssendd.R;
import co.businesssendd.gettersandsetters.Billing;

public class Activity_Payment_Summary extends AppCompatActivity {
    ListView PaymentSummary;
    BillingList_Adapter madapter;
    List<Billing> billings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_summary_list);
        Gson GS = new Gson();
        Toolbar toolbar = (Toolbar) findViewById(R.id.Main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon_small);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Activity_Payment_Summary.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
                    }
                }
        );
        billings = Arrays.asList(GS.fromJson(getIntent().getStringExtra("completeDetails"), Billing[].class));
        PaymentSummary = (ListView) findViewById(R.id.PaymentSummary);
        madapter = new BillingList_Adapter(this, R.layout.list_item_paymentsummary, billings);
        PaymentSummary.setAdapter(madapter);
        PaymentSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), Activity_PS_Item_details.class);
                Gson GS = new Gson();
                String Product = GS.toJson(billings.get(position).getProducts());
                i.putExtra("Products", Product);
                startActivity(i);
                Activity_Payment_Summary.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
            }
        });

    }
    //Get all Saved Addresses in Address_Receiver DB.


    //Address Holder
    public class addresslist_holder {
        private TextView OrderId;
        private TextView City;
        private TextView Name;
        private TextView ShippingCost;
        private TextView Rem_Pending;

    }

    //Set up Address Adapter
    public class BillingList_Adapter extends ArrayAdapter<Billing> {

        private Context c;
        private List<Billing> Billing_details;

        public BillingList_Adapter(Context context, int resource, List<Billing> objects) {
            super(context, resource, objects);
            this.c = context;
            this.Billing_details = objects;
        }


        @Override
        public void add(Billing object) {
            super.add(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return Billing_details.size();
        }

        @Override
        public Billing getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(Billing item) {
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
                convertView = inflater.inflate(R.layout.list_item_paymentsummary, parent, false);
                addresslist_holder.Name = (TextView) convertView.findViewById(R.id.Name);
                addresslist_holder.City = (TextView) convertView.findViewById(R.id.City);
                addresslist_holder.OrderId = (TextView) convertView.findViewById(R.id.OrderID);
                addresslist_holder.ShippingCost = (TextView) convertView.findViewById(R.id.shipping_cost);
                addresslist_holder.Rem_Pending = (TextView) convertView.findViewById(R.id.rem_pending);
                convertView.setTag(addresslist_holder);
            } else {
                addresslist_holder = (addresslist_holder) convertView.getTag();
            }
            //setup list objects
            addresslist_holder.Name.setText(Billing_details.get(position).getReceiver_name());
            addresslist_holder.City.setText(Billing_details.get(position).getDrop_address_city());
            addresslist_holder.OrderId.setText(Billing_details.get(position).getOrderId());
            addresslist_holder.ShippingCost.setText(Billing_details.get(position).getTotal_shipping_cost());
            addresslist_holder.Rem_Pending.setText(Billing_details.get(position).getTotal_remittance_pending());
            return convertView;
        }
    }
}