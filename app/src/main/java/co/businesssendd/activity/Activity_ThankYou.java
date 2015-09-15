package co.businesssendd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.businesssendd.R;
import co.businesssendd.helper.Utils;


public class Activity_ThankYou extends Activity {
    Button newBooking;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        Utils utils = new Utils(this);
        utils.updateOrderId();
        text = (TextView) findViewById(R.id.tvThankyouText2);
        text.setText("Booking relieved successfully. Sit back and relax.");
        newBooking = (Button) findViewById(R.id.bBookAnother);
        newBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_ThankYou.this, Activity_Main.class);
                startActivity(i);
                Activity_ThankYou.this.overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);

            }
        });
    }
}
