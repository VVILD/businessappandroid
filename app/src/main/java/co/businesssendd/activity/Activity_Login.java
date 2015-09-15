package co.businesssendd.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.businesssendd.R;
import co.businesssendd.databases.DB_UserDetails;
import co.businesssendd.gettersandsetters.LoginUser;
import co.businesssendd.gettersandsetters.Users;
import co.businesssendd.helper.NetworkUtils;
import co.businesssendd.helper.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Activity_Login extends BaseActivity {
    EditText Username,Password;
    Button Login;
    ProgressDialog pd;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        utils = new Utils(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
        Username = (EditText) findViewById(R.id.LoginUserName);
        Password = (EditText) findViewById(R.id.LoginPassword);
        Login = (Button) findViewById(R.id.LoginButton);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(Activity_Login.this);
                pd.setMessage("Loading, Please wait...");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                if (!TextUtils.isEmpty(Username.getText().toString())) {
                    if (!TextUtils.isEmpty(Password.getText().toString())) {
                        final NetworkUtils mnetworkutils = new NetworkUtils(Activity_Login.this);
                        if (mnetworkutils.isnetconnected()) {
                            LoginUser loginUser = new LoginUser();
                            loginUser.setUsername(Username.getText().toString());
                            loginUser.setPassword(Password.getText().toString());
                            pd.show();
                            mnetworkutils.getapi().LoginUser(loginUser, new Callback<Users>() {
                                @Override
                                public void success(Users user, Response response) {
                                    pd.dismiss();
                                    switch (user.getMsg()) {
                                        case "success":
                                            utils.setvalue("LoggedIn", "true");
                                            utils.setvalue("UserName", user.getUsername());
                                            Users addUser = new Users();
                                            addUser.setUsername(user.getUsername());
                                            addUser.setName(user.getName());
                                            addUser.setBusiness(user.getBusiness());
                                            DB_UserDetails dbUserDetails = new DB_UserDetails();
                                            dbUserDetails.AddToDB(addUser);

                                            Intent i = new Intent(getApplicationContext(), Activity_Main.class);
                                            startActivity(i);
                                            overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                            finish();
                                            break;
                                        case "wrongpassword":
                                            Toast.makeText(Activity_Login.this, "Incorrect Password. Please try again", Toast.LENGTH_LONG).show();
                                            break;
                                        case "notregistered":
                                            Toast.makeText(Activity_Login.this, "Please register to continue.", Toast.LENGTH_LONG).show();
                                            break;
                                        default:

                                            break;
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                        pd.dismiss();
                                        Log.i("Error:", error.toString());
                                        Toast.makeText(Activity_Login.this, "Error in registering your phone number. Please try again in some time.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(Activity_Login.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Activity_Login.this,"Please Enter Password",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Activity_Login.this,"Please Enter Username",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}