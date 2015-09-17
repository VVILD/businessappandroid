package co.businesssendd.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.businesssendd.R;
import co.businesssendd.gettersandsetters.TrackingDataList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by harshkaranpuria on 9/17/15.
 */
public class Activity_parcel_tracking extends AppCompatActivity {
    ProgressDialog pd;
    ArrayList<TrackingDataList> list = new ArrayList<>(1);
    ListView TrackingListView;
    Tracking_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_tracking);
        TrackingListView = (ListView) findViewById(R.id.ItemTrackingList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon_small);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
                    }
                }
        );

        try {
            if (getIntent().getStringExtra("trackingData") != null) {
                JSONArray arr = new JSONArray(getIntent().getStringExtra("trackingData"));
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject c = (JSONObject) arr.get(i);
                    list.add(updateList(c));
                }
            }
            adapter = new Tracking_adapter(Activity_parcel_tracking.this, R.layout.list_item_tracking_item, list);
            TrackingListView.setAdapter(adapter);
         } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public TrackingDataList updateList(JSONObject c) {
        TrackingDataList trackingDataList = new TrackingDataList();
        try {
            trackingDataList.setDate(c.getString("date"));
            trackingDataList.setStatus(c.getString("status"));
            trackingDataList.setLoaction(c.getString("location"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trackingDataList;
    }

    public class Tracking_holder {
        private TextView Date;
        private TextView Status;
        private TextView Location;
    }

    public class Tracking_adapter extends ArrayAdapter<TrackingDataList> {
        private Context c;
        private List<TrackingDataList> trackingList;

        public Tracking_adapter(Context context, int resource, List<TrackingDataList> objects) {
            super(context, resource, objects);
            this.c = context;
            this.trackingList = objects;
        }

        @Override
        public void add(TrackingDataList object) {
            super.add(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
         }

        @Override
        public int getCount() {
            return trackingList.size();
        }

        @Override
        public TrackingDataList getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(TrackingDataList item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Tracking_holder tracking_holder;
            if (convertView == null) {
                tracking_holder = new Tracking_holder();
                LayoutInflater inflater = (LayoutInflater) c
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_tracking_item, parent, false);
                tracking_holder.Date = (TextView) convertView.findViewById(R.id.tvDate1);
                tracking_holder.Status = (TextView) convertView.findViewById(R.id.tvStatus1);
                tracking_holder.Location = (TextView) convertView.findViewById(R.id.tvLocation1);

                convertView.setTag(tracking_holder);
            } else {
                tracking_holder = (Tracking_holder) convertView.getTag();
            }
            tracking_holder.Date.setText(trackingList.get(position).getDate());
            tracking_holder.Status.setText(trackingList.get(position).getStatus());
            tracking_holder.Location.setText(trackingList.get(position).getLoaction());

            return convertView;
        }
    }

}
