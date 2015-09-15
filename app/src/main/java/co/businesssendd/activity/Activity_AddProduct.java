package co.businesssendd.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import co.businesssendd.R;
import co.businesssendd.databases.DB_DropAddresses;
import co.businesssendd.databases.DB_complete_order;
import co.businesssendd.gettersandsetters.CompleteOrder;
import co.businesssendd.gettersandsetters.Drop_Address;
import co.businesssendd.gettersandsetters.Order;
import co.businesssendd.gettersandsetters.Product;
import co.businesssendd.helper.NetworkUtils;
import co.businesssendd.helper.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Activity_AddProduct extends AppCompatActivity implements TextWatcher {
    EditText etName, etSKU, etQuantity, etUnit_Price, etPrice, etWeight;
    Button CreateOrder, addProduct, removeProduct;
    Utils mUtils;
    ArrayList<Product> ProductArray = new ArrayList<>();
    int Counter = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon_small);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Activity_AddProduct.this.overridePendingTransition(R.animator.pull_in_left, R.animator.push_out_right);
                    }
                }
        );
        mUtils = new Utils(Activity_AddProduct.this);
        etName = (EditText) findViewById(R.id.etItemName);
        etName.addTextChangedListener(this);

        etSKU = (EditText) findViewById(R.id.etSKU);
        etSKU.addTextChangedListener(this);

        etQuantity = (EditText) findViewById(R.id.etQuantity);
        etQuantity.addTextChangedListener(this);

        etUnit_Price = (EditText) findViewById(R.id.etUnit_Price);
        etUnit_Price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etQuantity.getText().toString())) {
                    etPrice.setText(String.valueOf(Integer.parseInt(etQuantity.getText().toString()) * Integer.parseInt(etUnit_Price.getText().toString())));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPrice = (EditText) findViewById(R.id.etPrice);
        etPrice.addTextChangedListener(this);

        etWeight = (EditText) findViewById(R.id.etWeight);
        etWeight.addTextChangedListener(this);

        CreateOrder = (Button) findViewById(R.id.CreateOrder);
        removeProduct = (Button) findViewById(R.id.removeProduct);
        addProduct = (Button) findViewById(R.id.addProduct);

        Gson GS = new Gson();
        String Drop_Address_Details = getIntent().getStringExtra("Drop_Address_Details");
        final Drop_Address dropAddress = GS.fromJson(Drop_Address_Details, Drop_Address.class);
        final String Shipping_Mode = getIntent().getStringExtra("Shipping_Mode");
        final String payment_method = getIntent().getStringExtra("payment_method");

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setName(etName.getText().toString());
                product.setPrice(etPrice.getText().toString());
                product.setQuantity(etQuantity.getText().toString());
                product.setSku(etSKU.getText().toString());
                product.setWeight(etWeight.getText().toString());
                addProduct(product);
                clearTextFields();

            }
        });
        removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProduct();
            }
        });
        CreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etName.getText().toString())) {
                    if (!TextUtils.isEmpty(etUnit_Price.getText().toString())) {
                        if (!TextUtils.isEmpty(etQuantity.getText().toString())) {
                            if (!TextUtils.isEmpty(etSKU.getText().toString())) {
                                if (!TextUtils.isEmpty(etWeight.getText().toString())) {
                                    pd = new ProgressDialog(Activity_AddProduct.this);
                                    pd.setMessage("Loading, Please wait...");
                                    pd.setCancelable(false);
                                    pd.setIndeterminate(true);
                                    pd.show();
                                    Product product = new Product();
                                    product.setName(etName.getText().toString());
                                    product.setPrice(etPrice.getText().toString());
                                    product.setQuantity(etQuantity.getText().toString());
                                    product.setSku(etSKU.getText().toString());
                                    product.setWeight(etWeight.getText().toString());
                                    ProductArray.add(product);

                                    final Order mOrder = new Order();
                                    mOrder.setName(dropAddress.getDropUser().getName());
                                    mOrder.setCity(dropAddress.getCity());
                                    mOrder.setUsername(mUtils.getvalue("UserName"));
                                    mOrder.setAddress1(dropAddress.getAddressline1());
                                    mOrder.setAddress2(dropAddress.getAddressline2());
                                    mOrder.setConfirmed(true);
                                    mOrder.setCountry(dropAddress.getCountry());
                                    mOrder.setState(dropAddress.getState());
                                    mOrder.setEmail(dropAddress.getDropUser().getEmail());
                                    mOrder.setPhone(dropAddress.getDropUser().getPhone());
                                    mOrder.setPincode(dropAddress.getPincode());
                                    mOrder.setPayment_method(payment_method);
                                    mOrder.setMethod(Shipping_Mode);
                                    mOrder.setProducts(ProductArray);
                                    final NetworkUtils mnetworkutils = new NetworkUtils(Activity_AddProduct.this);
                                    if (mnetworkutils.isnetconnected()) {
                                        mnetworkutils.getapi().CreateOrder(mOrder, new Callback<Order>() {
                                            @Override
                                            public void success(Order user, Response response) {
                                                pd.dismiss();
                                                DB_DropAddresses db_dropAddresses = new DB_DropAddresses();
                                                db_dropAddresses.AddToDB(dropAddress);
                                                CompleteOrder co = new CompleteOrder();
                                                co.setProducts(ProductArray);
                                                co.setAddress(dropAddress);
                                                co.setPaymentMode(payment_method);
                                                co.setShippingMethod(Shipping_Mode);
                                                DB_complete_order complete_order = new DB_complete_order();
                                                complete_order.AddToDB(co);
                                                Intent i = new Intent(getApplicationContext(), Activity_ThankYou.class);
                                                startActivity(i);
                                                overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                                                finish();
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                pd.dismiss();
                                                Log.i("Error", error.toString());
                                                Toast.makeText(Activity_AddProduct.this, "Error in Placing your Order. Please try again in some time.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(Activity_AddProduct.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    etWeight.setError("* Enter total weight of product ");
                                }
                            } else {
                                etSKU.setError("* Enter SKU");
                            }
                        } else {
                            etQuantity.setError("* Enter Total Quantity");
                        }
                    } else {
                        etUnit_Price.setError("* Enter Unit Price of product");
                    }
                } else {
                    etName.setError("* Enter product name");
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!etName.getText().toString().isEmpty()) etName.setError(null, null);
        if (!etSKU.getText().toString().isEmpty()) etSKU.setError(null, null);
        if (!etQuantity.getText().toString().isEmpty()) etQuantity.setError(null, null);
        if (!etWeight.getText().toString().isEmpty()) etWeight.setError(null, null);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    public void addProduct(Product product) {
        ProductArray.add(product);
        Counter++;
    }

    public void removeProduct() {
        if (Counter > 0) {
            etPrice.setText(ProductArray.get(Counter - 1).getPrice());
            etQuantity.setText(ProductArray.get(Counter - 1).getQuantity());
            etName.setText(ProductArray.get(Counter - 1).getName());
            etUnit_Price.setText(String.valueOf(Integer.parseInt(ProductArray.get(Counter - 1).getPrice()) / Integer.parseInt(ProductArray.get(Counter - 1).getQuantity())));
            etSKU.setText(ProductArray.get(Counter - 1).getSku());
            etWeight.setText(ProductArray.get(Counter - 1).getWeight());
            ProductArray.remove(Counter - 1);
            Counter--;
        } else if (Counter == 0) {
            clearTextFields();
        } else {
            Toast.makeText(Activity_AddProduct.this, "NO Products to remove", Toast.LENGTH_LONG).show();
        }
    }

    public void clearTextFields() {
        etPrice.setText("");
        etQuantity.setText("");
        etName.setText("");
        etSKU.setText("");
        etUnit_Price.setText("");
        etWeight.setText("");
    }
}