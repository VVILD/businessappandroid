package co.businesssendd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import co.businesssendd.R;
import co.businesssendd.gettersandsetters.PreviousOrder;

/**
 * Created by harshkaranpuria on 9/16/15.
 */
public class Activity_PrevOrder_details extends AppCompatActivity {
    PreviousOrder mPreviousOrder = new PreviousOrder();
    TextView Customer_Details, Customer_Address, Type_of_shipment, Payment_Method, Date;
    Button ParcelDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevorder_details);
        Customer_Details = (TextView) findViewById(R.id.Customer_Details);
        Customer_Address = (TextView) findViewById(R.id.Customer_Address);
        Type_of_shipment = (TextView) findViewById(R.id.Type_of_shipment);
        Payment_Method = (TextView) findViewById(R.id.Payment_Method);
        Date = (TextView) findViewById(R.id.Date);
        ParcelDetails = (Button) findViewById(R.id.ParcelDetails);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon_small);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Activity_PrevOrder_details.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
                    }
                }
        );
        Gson GS = new Gson();
        mPreviousOrder = GS.fromJson(getIntent().getStringExtra("PreviousOrder"), PreviousOrder.class);
        Customer_Details.setText(mPreviousOrder.getName() + "\n" + mPreviousOrder.getPhone() + "\n" + mPreviousOrder.getEmail());
        Customer_Address.setText(mPreviousOrder.getAddress1() + "\n" + mPreviousOrder.getAddress2() + "\n" + mPreviousOrder.getPincode() + "\n" + mPreviousOrder.getCity() + "\n" + mPreviousOrder.getState());
        if (mPreviousOrder.getMethod().equals("N"))
            Type_of_shipment.setText("Premium");
        else if (mPreviousOrder.getMethod().equals("B"))
            Type_of_shipment.setText("Bulk");
        if(mPreviousOrder.getPmethod().equals("F"))
            Payment_Method.setText("Free Shipping");
        else if(mPreviousOrder.getPmethod().equals("C"))
            Payment_Method.setText("COD");
        Date.setText(mPreviousOrder.getBook_time().substring(0,10));
        ParcelDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(!mPreviousOrder.getProducts().toString().equals("[]")) {
                    Gson GS = new Gson();
                    String PreviousOrder = GS.toJson(mPreviousOrder);
                    Intent i = new Intent(getApplicationContext(), Activity_PrevOrder_parcelDetails.class);
                    i.putExtra("PreviousOrder", PreviousOrder);
                    startActivity(i);
                    overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                }else {
                    Toast.makeText(Activity_PrevOrder_details.this,"No Parcels found",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
