package com.digital.farhad_app.Login;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digital.farhad_app.MainActivity;
import com.digital.farhad_app.R;
import com.digital.farhad_app.SaveUser;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LogInFragment extends Fragment {


    public LogInFragment() {
    }

    private TextView gotoRegister,forgetPass;
    private CountryCodePicker countryCodePicker;
    private EditText numberET,passwordET;
    private CheckBox checkBox;
    private Button loginButton;
    private CardView verifyingLoginCodeSection;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_log_in, container, false);


        gotoRegister = root.findViewById(R.id.gotoRegister_id);
        forgetPass = root.findViewById(R.id.logInForgetPass_id);
        verifyingLoginCodeSection = root.findViewById(R.id.verifyingLoginCodeSection_id);

        countryCodePicker = root.findViewById(R.id.logInCountryCodePicker_Id);
        numberET = root.findViewById(R.id.loginNumber_id);
        passwordET = root.findViewById(R.id.loginPassword);
        loginButton = root.findViewById(R.id.loginButton);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForgetPassFragment forgetPassFragment = new ForgetPassFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.hostRegister_id,forgetPassFragment)
                        .commit();

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
                String number = numberET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if (number.isEmpty()){
                    numberET.setError("Please Enter number");

                }else if (password.isEmpty()){

                    passwordET.setError("Please Enter password");
                }else {

                    verifyingLoginCodeSection.setVisibility(View.VISIBLE);
                    String fullNumber = countryCode+number;
                    confirm_alert(fullNumber,password);

                }

            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpFragment signUpFragment = new SignUpFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.hostRegister_id,signUpFragment)
                        .commit();


            }
        });


        return root;
    }
    private void logInMethod(final String number, final String password) {

        String url ="https://demosomon.000webhostapp.com/FarhadApps/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String status = jsonObject.getString("response");
                    String number = jsonObject.getString("refer_code");
                    String name = jsonObject.getString("userName");
                    String password = jsonObject.getString("password");

                    if (status.equals("Login success")){

                        verifyingLoginCodeSection.setVisibility(View.GONE);
                        OTPFragment otpFragment = new OTPFragment();
                        Bundle args = new Bundle();
                        args.putString("authNumber", number);
                        args.putString("userName", name);
                        args.putString("password", password);
                        args.putString("workDirection", "login");
                        otpFragment.setArguments(args);
                        FragmentManager manager = getFragmentManager();
                        manager.beginTransaction().replace(R.id.hostRegister_id,otpFragment)
                                .commit();


                    }else  {
                        verifyingLoginCodeSection.setVisibility(View.GONE);
                       loginAlert();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    verifyingLoginCodeSection.setVisibility(View.GONE);
                    loginAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                verifyingLoginCodeSection.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error Try again", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params = new HashMap<>();
                params.put("refer_code",number);
                params.put("password",password);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);


    }

    private void confirm_alert(final String fullNumber,final String password) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Alert !")
                .setMessage("Is country code and number right?\n"+fullNumber)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        logInMethod(fullNumber,password);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loginAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!")
                .setMessage("Please enter valid number and password then try again")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                       dialogInterface.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
