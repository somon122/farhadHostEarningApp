package com.digital.farhad_app.Login;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.os.CountDownTimer;
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
import com.digital.farhad_app.MainActivity;
import com.digital.farhad_app.R;
import com.digital.farhad_app.SaveUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class OTPFragment extends Fragment {


    public OTPFragment() {}


    private EditText otpET;
    private Button sentCodeButton;
    private CardView  verifyingShow;
    private TextView resentTimeShow;
    private  String verificationId;
    private FirebaseAuth auth;

    String authNumber;
    String deviceId;
    String referId;
    String password;
    String name;
    String work;
    String checkBox;

    private static final long START_TIME_IN_MILLIS = 10000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private int value;

    @Override
    public void onResume() {
        super.onResume();

        startTimer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ot, container, false);


        otpET = root.findViewById(R.id.otpCode_id);
        sentCodeButton = root.findViewById(R.id.otpSentButton_id);
        resentTimeShow = root.findViewById(R.id.resentTimeShow_id);
        verifyingShow = root.findViewById(R.id.verifyingOtpCodeSection_id);
        auth = FirebaseAuth.getInstance();
        verifyingShow.setVisibility(View.GONE);
        resentTimeShow.setVisibility(View.GONE);



        Bundle bundle = getArguments();
        if (bundle != null){

            authNumber = bundle.getString("authNumber");
            password = bundle.getString("password");
            name = bundle.getString("userName");
            work = bundle.getString("workDirection");
            referId = bundle.getString("refer");
            deviceId = bundle.getString("deviceId");

        }


        sentCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    phoneAuthentication(authNumber);
                    sentCodeButton.setVisibility(View.GONE);
                    resentTimeShow.setVisibility(View.VISIBLE);
                    startTimer();
            }
        });

        return root;
    }

    private void phoneAuthentication(String number) {
        sentVerificationCode(number);
    }



    private boolean haveNetwork ()
    {
        boolean have_WiFi = false;
        boolean have_Mobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfo){

            if (info.getTypeName().equalsIgnoreCase("WIFI"))
            {
                if (info.isConnected())
                {
                    have_WiFi = true;
                }
            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))

            {
                if (info.isConnected())
                {
                    have_Mobile = true;
                }
            }

        }
        return have_WiFi || have_Mobile;

    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            if (work.equals("login")){
                                SaveUser saveUser = new SaveUser(getContext());
                                saveUser.dataStore(authNumber,password,name);
                                Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(),MainActivity.class));

                            }else{

                                userReferBounce(deviceId,password,authNumber,referId,name);
                            }

                        }else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }



    private void sentVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );


    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

            Toast.makeText(getContext(), "Code sent", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null)
            {
                otpET.setText(code);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verifyCode( String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
        verifyingShow.setVisibility(View.VISIBLE);


    }



    private void userReferBounce(final String deviceId, final String password, final String authNumber, final String referId, final String name) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://demosomon.000webhostapp.com/FarhadApps/get_refer_point.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String main_point = jsonObject.getString("main_point");
                    String last_point = jsonObject.getString("last_point");
                    String refer_point = jsonObject.getString("refer_point");
                    String success = jsonObject.getString("response");

                    if (success.equals("Point available")){
                        referBounce(main_point,last_point,refer_point,deviceId,password,authNumber,referId,name);

                    }else {
                        referCodeAlert();
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

                params.put("refer_code",referId);
                return params;
            }
        };
        queue.add(stringRequest);

    }

    private void referCodeAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!")
                .setMessage("Your enter Refer code could not match! please try again")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), RegisterHostActivity.class));
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private void referBounce(final String main_Point,final String last_Point,final String refer_Point,final String deviceId, final String password, final String authNumber, final String referCode , final String name) {

        String url ="https://demosomon.000webhostapp.com/FarhadApps/bounce_add.php";
        int p = Integer.parseInt(main_Point)+50;
        final String updatePoint = String.valueOf(p);

        int p2 = Integer.parseInt(last_Point)+50;
        final String last_updatePoint = String.valueOf(p2);

        int p3 = Integer.parseInt(refer_Point)+50;
        final String refer_updatePoint = String.valueOf(p3);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String status = jsonObject.getString("response");

                    if (status.equals("bounce_add_success")){

                        signUpMethod(deviceId,password,authNumber,name);
                    }else if (status.equals("device_id_already_taken")){

                      reAccountAlert();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getContext(), "Slow net connection", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), " Connection Error Try again", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params = new HashMap<>();

                params.put("device_id",deviceId);
                params.put("refer_code",referCode);
                params.put("main_point",updatePoint);
                params.put("last_point",last_updatePoint);
                params.put("refer_point",refer_updatePoint);


                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);

    }

    private void reAccountAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert !")
                .setMessage("You have already account! Please Login")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), RegisterHostActivity.class));
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void signUpMethod(final String deviceId, final String password,final String number ,final String name) {

        String url ="https://demosomon.000webhostapp.com/FarhadApps/sign_up.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String status = jsonObject.getString("response");

                    if (status.equals("signUp_success")){

                        Toast.makeText(getContext(), "Register Success", Toast.LENGTH_SHORT).show();
                        SaveUser saveUser = new SaveUser(getContext());
                        saveUser.dataStore(number,password,name);
                        startActivity(new Intent(getContext(),MainActivity.class));



                    }else if (status.equals("refer_already_exists")){
                        Toast.makeText(getContext(), "refer_already_exists", Toast.LENGTH_SHORT).show();

                    }else if (status.equals("username_already_exists")){
                        Toast.makeText(getContext(), "username_already_exists", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getContext(), "net connection problem", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getContext(), "Slow net connection", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> Params = new HashMap<>();
                Params.put("userName",name);
                Params.put("device_id",deviceId);
                Params.put("password",password);
                Params.put("refer_code",number);
                Params.put("main_point","50");
                Params.put("last_point","50");
                Params.put("refer_point","0");
                return Params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);

    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                resentTimeShow.setVisibility(View.GONE);
                sentCodeButton.setVisibility(View.VISIBLE);
                if (value>0){
                    sentCodeButton.setText("Resent code");
                }else {
                    value++;
                }
                resetTimer();

            }
        }.start();

        mTimerRunning = true;
    }


    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();



    }

    private void updateCountDownText() {
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d",seconds);
        resentTimeShow.setText("Resent code remaining : "+timeLeftFormatted);



    }



}