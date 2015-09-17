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

import java.util.List;

import co.businesssendd.R;
import co.businesssendd.gettersandsetters.PreviousOrder;
import co.businesssendd.gettersandsetters.PreviousOrder_Products;

/**
 * Created by harshkaranpuria on 9/16/15.
 */
public class Activity_PrevOrder_parcelDetails extends AppCompatActivity{
    ListView lvParcels;
    PreviousOrder mPreviousOrder = new PreviousOrder();
    PrevOrderProduct_Adapter madapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevorder_parceldetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon_small);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Activity_PrevOrder_parcelDetails.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
                    }
                }
        );
        lvParcels = (ListView)findViewById(R.id.lvParcels);
        Gson GS = new Gson();
        mPreviousOrder = GS.fromJson(getIntent().getStringExtra("PreviousOrder"), PreviousOrder.class);
        madapter = new PrevOrderProduct_Adapter(Activity_PrevOrder_parcelDetails.this, R.layout.list_item_product_list, mPreviousOrder.getProducts());
        lvParcels.setAdapter(madapter);
        lvParcels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),Activity_parcel_tracking.class);
                i.putExtra("trackingData", mPreviousOrder.getProducts().get(position).getTracking_data());
                startActivity(i);
                overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
            }
        });
    }


    //Address Holder
    public class prevOrderProduct_holder {
        private TextView TrackingId;
        private TextView SKU;
        private TextView inputwt;
        private TextView totalprice;
        private TextView name;
        private TextView qty;
        private TextView chargewt;
        private TextView shippingcost;

    }

    //Set up Address Adapter
    public class PrevOrderProduct_Adapter extends ArrayAdapter<PreviousOrder_Products> {

        private Context c;
        private List<PreviousOrder_Products> PreviousOrderProduct_details;

        public PrevOrderProduct_Adapter(Context context, int resource, List<PreviousOrder_Products> objects) {
            super(context, resource, objects);
            this.c = context;
            this.PreviousOrderProduct_details = objects;
        }


        @Override
        public void add(PreviousOrder_Products object) {
            super.add(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return PreviousOrderProduct_details.size();
        }

        @Override
        public PreviousOrder_Products getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(PreviousOrder_Products item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            prevOrderProduct_holder prevOrderProduct_holder;
            if (convertView == null) {
                //initialize holder
                prevOrderProduct_holder = new prevOrderProduct_holder();
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_product_list, parent, false);
                prevOrderProduct_holder.chargewt = (TextView) convertView.findViewById(R.id.chargewt);
                prevOrderProduct_holder.inputwt = (TextView) convertView.findViewById(R.id.inputwt);
                prevOrderProduct_holder.name = (TextView) convertView.findViewById(R.id.name);
                prevOrderProduct_holder.qty = (TextView) convertView.findViewById(R.id.qty);
                prevOrderProduct_holder.shippingcost = (TextView) convertView.findViewById(R.id.shippingcost);
                prevOrderProduct_holder.SKU = (TextView) convertView.findViewById(R.id.SKU);
                prevOrderProduct_holder.totalprice = (TextView) convertView.findViewById(R.id.totalprice);
                prevOrderProduct_holder.TrackingId = (TextView) convertView.findViewById(R.id.TrackingId);
                convertView.setTag(prevOrderProduct_holder);
            } else {
                prevOrderProduct_holder = (prevOrderProduct_holder) convertView.getTag();
            }
            //setup list objects
            prevOrderProduct_holder.chargewt.setText("Charged Wt.: "+PreviousOrderProduct_details.get(position).getApplied_weight());
            prevOrderProduct_holder.inputwt.setText("Input Wt.: "+PreviousOrderProduct_details.get(position).getWeight());
            prevOrderProduct_holder.name.setText(PreviousOrderProduct_details.get(position).getName());
            prevOrderProduct_holder.qty.setText("Quantity: " + PreviousOrderProduct_details.get(position).getQuantity());
            prevOrderProduct_holder.shippingcost.setText("Shipping Cost: "+PreviousOrderProduct_details.get(position).getShipping_cost());
            prevOrderProduct_holder.SKU.setText("SKU: "+PreviousOrderProduct_details.get(position).getSku());
            prevOrderProduct_holder.totalprice.setText("Total Price: "+PreviousOrderProduct_details.get(position).getPrice());
            prevOrderProduct_holder.TrackingId.setText(PreviousOrderProduct_details.get(position).getReal_tracking_no());
            return convertView;
        }
    }
}
