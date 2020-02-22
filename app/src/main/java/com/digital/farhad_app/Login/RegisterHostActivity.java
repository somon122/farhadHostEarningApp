package com.digital.farhad_app.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.digital.farhad_app.R;

public class RegisterHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_register);


        LogInFragment logInFragment = new LogInFragment();
        fragment(logInFragment);



    }

    private void fragment(Fragment fragment){

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.hostRegister_id,fragment)
                .commit();

    }
}
