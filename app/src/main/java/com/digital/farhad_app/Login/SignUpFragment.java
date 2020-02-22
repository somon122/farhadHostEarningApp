package com.digital.farhad_app.Login;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.digital.farhad_app.R;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {


    public SignUpFragment() {}

    private TextView gotologin;

    private EditText phoneET,passwordET,confirmPasswordET,referET,nameET;
    private CountryCodePicker countryCodePicker;
    private Button registerButton;

    String deviceId;
    private static final int PHONE_PERMISSION_CODE = 100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_sign_up, container, false);


        gotologin = root.findViewById(R.id.gotoLogin_id);

        phoneET = root.findViewById(R.id.signUpPhoneNumber_id);
        passwordET = root.findViewById(R.id.signUpPassword);
        confirmPasswordET = root.findViewById(R.id.signUpConfirmPassword_id);
        referET = root.findViewById(R.id.signUpReferCode_id);
        nameET = root.findViewById(R.id.signUpFullName_id);

        countryCodePicker = root.findViewById(R.id.signUpcountryCodePicker_Id);
        registerButton = root.findViewById(R.id.signUpButton_id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = telephonyManager.getDeviceId();

            } else {
                requestPhoneStatePermission();
            }
        }else {

            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();

        }





        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
                String number = phoneET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPass = confirmPasswordET.getText().toString();
                String refer = referET.getText().toString();
                String name = nameET.getText().toString();

                if (number.isEmpty()){
                    phoneET.setError("Please enter number");
                }else if (name.isEmpty()){
                    nameET.setError("Please enter name");
                }else if (password.isEmpty()){
                    passwordET.setError("Please enter password");
                }else if (confirmPass.isEmpty()){
                    confirmPasswordET.setError("Please enter confirm password");
                }else if (refer.isEmpty()){
                    referET.setError("Please enter password");
                }else {

                    if (confirmPass.equals(password)){


                        checkNewUser(countryCode,number,confirmPass,refer,name);




                    }else {
                        confirmPasswordET.setError("confirm password could not match");
                    }



                }

            }
        });


        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogInFragment logInFragment = new LogInFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.hostRegister_id,logInFragment)
                        .commit();


            }
        });


        return root;
    }

    private void checkNewUser(final String countryCode,final String number,final String confirmPass,final String refer, final String name) {


        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://demosomon.000webhostapp.com/FarhadApps/register_value_test.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("response");

                    if (success.equals("Welcome")){

                        OTPFragment otpFragment = new OTPFragment();
                        Bundle args = new Bundle();
                        String authNumber = countryCode+number;
                        args.putString("authNumber", authNumber);
                        args.putString("password", confirmPass);
                        args.putString("refer", refer);
                        args.putString("userName", name);
                        args.putString("deviceId", deviceId);
                        otpFragment.setArguments(args);
                        FragmentManager manager = getFragmentManager();
                        manager.beginTransaction().replace(R.id.hostRegister_id,otpFragment)
                                .commit();


                    }else if (success.equals("refer_code is not found")){

                       referET.setError("Please enter valid refer code");


                    }else if (success.equals("number_already_taken")){

                       phoneET.setError("This number already taken");


                    }else if (success.equals("device_id_already_taken")){

                        referCodeAlert();

                    }else {
                        Toast.makeText(getContext(), "Please try again..", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    referCodeAlert();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Connection Error Try again", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params = new HashMap<>();

                String newNumber = countryCode+number;

                params.put("refer_code",refer);
                params.put("device_id",deviceId);
                params.put("number",newNumber);
                return params;
            }
        };
        queue.add(stringRequest);

    }

    private void referCodeAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!")
                .setMessage("You have already account! please Login")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LogInFragment logInFragment = new LogInFragment();
                        FragmentManager manager = getFragmentManager();
                        manager.beginTransaction().replace(R.id.hostRegister_id,logInFragment).commit();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void requestPhoneStatePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_PHONE_STATE)) {

            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for Register or SignUp")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.READ_PHONE_STATE}, PHONE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.READ_PHONE_STATE}, PHONE_PERMISSION_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PHONE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getContext(), "Permission granted and you can access this app", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
