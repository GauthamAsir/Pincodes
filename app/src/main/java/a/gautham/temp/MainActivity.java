package a.gautham.temp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText pinCode;
    private static final String TAG = "Details: ";
    Button check_bt;
    TextView result_tv;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinCode = findViewById(R.id.pinCode);
        check_bt = findViewById(R.id.check);
        result_tv = findViewById(R.id.result);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Details");

        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.light_orange),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        check_bt.setOnClickListener(v ->{
            if (getPincode().isEmpty()){
                Toast.makeText(this, "Pincode can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (getPincode().length()!=6){
                Toast.makeText(this, "Enter valid pincode, or create your own city", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();

            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
            Call<PinModel> callAsync;
            PincodeService pincodeService = ServiceGenerator.build().create(PincodeService.class);
            callAsync = pincodeService.getDetails(getPincode());

            callAsync.enqueue(new Callback<PinModel>() {
                @SuppressWarnings("NullableProblems")
                @Override
                public void onResponse(Call<PinModel> call, Response<PinModel> response) {
                    progressDialog.dismiss();
                    if (response.code()==200){
                        PinModel pinModel = response.body();
                        if (pinModel != null && pinModel.getStatus().equals("Error")) {
                            Toast.makeText(MainActivity.this, "No records found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        PostOffice postOffice;
                        if (pinModel != null) {
                            postOffice = pinModel.getPostOffices().get(0);
                            System.out.println(response.body());
                            String res = String.format(Locale.getDefault(),
                                    "Status: %s | State: %s | District: %s",
                                    pinModel.getStatus(), postOffice.getState(), postOffice.getDistrict());
                            result_tv.setText(res);
                        }

                    }else {
                        Toast.makeText(MainActivity.this, "Could not get Details, Connection Problem", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,response.message());
                    }
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public void onFailure(Call<PinModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        });

    }

    private String getPincode(){
        return pinCode.getText().toString();
    }

}