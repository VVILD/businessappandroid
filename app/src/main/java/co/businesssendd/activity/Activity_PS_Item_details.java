package co.businesssendd.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import co.businesssendd.R;
import co.businesssendd.gettersandsetters.Billing;
import co.businesssendd.gettersandsetters.Billing_Product;

/**
 * Created by harshkaranpuria on 9/16/15.
 */
public class Activity_PS_Item_details extends AppCompatActivity {
    ListView ProductDetailsList;
    List<Billing_Product> products;
    ProductList_Adapter madapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps_item_details);
        ProductDetailsList = (ListView)findViewById(R.id.ProductDetailsList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon_small);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Activity_PS_Item_details.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
                    }
                }
        );
        Gson GS = new Gson();
        try {
            products = Arrays.asList(GS.fromJson(getIntent().getStringExtra("Products"), Billing_Product[].class));
            madapter = new ProductList_Adapter(this, R.layout.list_item_paymentsummary, products);
            ProductDetailsList.setAdapter(madapter);
        }catch (NullPointerException i){

        }
    }

    //Address Holder
    public class Productlist_holder {
        private TextView DateVal;
        private TextView Name;
        private TextView Price;
        private TextView Remittance;
        private TextView shipping_cost;
        private TextView weight;
        private TextView cod_cost;
        private TextView return_cost;

    }

    //Set up Address Adapter
    public class ProductList_Adapter extends ArrayAdapter<Billing_Product> {

        private Context c;
        private List<Billing_Product> Billing_details;

        public ProductList_Adapter(Context context, int resource, List<Billing_Product> objects) {
            super(context, resource, objects);
            this.c = context;
            this.Billing_details = objects;
        }


        @Override
        public void add(Billing_Product object) {
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
        public Billing_Product getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(Billing_Product item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Productlist_holder addresslist_holder;
            if (convertView == null) {
                 addresslist_holder = new Productlist_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_product_details, parent, false);
                addresslist_holder.Name = (TextView) convertView.findViewById(R.id.Name);
                addresslist_holder.DateVal = (TextView) convertView.findViewById(R.id.DateVal);
                addresslist_holder.Price = (TextView) convertView.findViewById(R.id.Price);
                addresslist_holder.Remittance = (TextView) convertView.findViewById(R.id.Remittance);
                addresslist_holder.shipping_cost = (TextView) convertView.findViewById(R.id.shipping_cost);
                addresslist_holder.weight = (TextView) convertView.findViewById(R.id.weight);
                addresslist_holder.cod_cost = (TextView) convertView.findViewById(R.id.cod_cost);
                addresslist_holder.return_cost = (TextView) convertView.findViewById(R.id.return_cost);
                convertView.setTag(addresslist_holder);
            } else {
                addresslist_holder = (Productlist_holder) convertView.getTag();
            }

            addresslist_holder.Name.setText(Billing_details.get(position).getName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {

                addresslist_holder.DateVal.setText(sdf.format(Billing_details.get(position).getDate()));
                addresslist_holder.Price.setText(Billing_details.get(position).getPrice());
                addresslist_holder.Remittance.setText(Billing_details.get(position).getRemittance());
                addresslist_holder.shipping_cost.setText(Billing_details.get(position).getShipping_cost());
                addresslist_holder.weight.setText(Billing_details.get(position).getApplied_weight());
                addresslist_holder.cod_cost.setText(Billing_details.get(position).getCod_cost());
                addresslist_holder.return_cost.setText(Billing_details.get(position).getCod_cost());

            }catch (IllegalArgumentException e){

            }
            return convertView;
        }
    }
}
