package com.digital.farhad_app;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

import com.digital.farhad_app.AdminPanel.AdminActivity;
import com.digital.farhad_app.Login.RegisterHostActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onStart() {
        super.onStart();

       /* if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, RegisterHostActivity.class));
        }*/

    }

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private CardView notify,refer,wallet,appInstall,account,helpLine,rateUs,privacy,subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
         setTitle("Home");

         initialization();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.nav_home){
                   startActivity(new Intent(MainActivity.this,MainActivity.class));
                   finish();

                }else if (id == R.id.nav_admin_panel){

                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                    finish();

                }else if (id == R.id.nav_privacy){

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
                    }catch (ActivityNotFoundException e){

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +getPackageName())));
                    }

                }else if (id == R.id.nav_logout){
                  logOutAlert();

                }else if (id == R.id.nav_rate_us){

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
                    }catch (ActivityNotFoundException e){

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +getPackageName())));
                    }

                }else if (id == R.id.nav_exits){
                    exitsAlert();

                }else {
                    Toast.makeText(MainActivity.this, "Something Problem\nPlease try Again", Toast.LENGTH_SHORT).show();
                }



                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


                return false;

            }
        });

    }

    private void initialization() {

        notify = findViewById(R.id.notificationShow_id);
        refer = findViewById(R.id.inviteFriend_id);
        wallet = findViewById(R.id.withdraw_id);
        appInstall = findViewById(R.id.installApp_id);
        account = findViewById(R.id.accountsWeb_id);
        helpLine = findViewById(R.id.helpLine_id);
        rateUs = findViewById(R.id.rateUs_id);
        privacy = findViewById(R.id.privacy_policy_id);
        subscribe = findViewById(R.id.subscriber_id);

        notify.setOnClickListener(this);
        refer.setOnClickListener(this);
        wallet.setOnClickListener(this);
        appInstall.setOnClickListener(this);
        account.setOnClickListener(this);
        helpLine.setOnClickListener(this);
        rateUs.setOnClickListener(this);
        privacy.setOnClickListener(this);
        subscribe.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if (id==R.id.videoHourly_id){

           startActivity(new Intent(MainActivity.this,VideoShowActivity.class));


        }else if (id==R.id.dailyCheck_id){

            Toast.makeText(this, "dailyCheck_id", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void exitsAlert() {

        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            exitsAlert();
        } else {
            exitsAlert();
        }

    }

    @Override
    public void onClick(View v) {


        int id = v.getId();

        if (id==R.id.notificationShow_id){

            Toast.makeText(this, "notificationShow_id", Toast.LENGTH_SHORT).show();

        }else if (id==R.id.inviteFriend_id){
           shareApp();

        }else if (id==R.id.withdraw_id){
            Toast.makeText(this, "withdraw_id", Toast.LENGTH_SHORT).show();

        }else if (id==R.id.installApp_id){
            Toast.makeText(this, "installApp_id", Toast.LENGTH_SHORT).show();

        }else if (id==R.id.accountsWeb_id){
            Toast.makeText(this, "accountsWeb_id", Toast.LENGTH_SHORT).show();

        }else if (id==R.id.helpLine_id){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
            }catch (ActivityNotFoundException e){

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +getPackageName())));
            }

        }else if (id==R.id.rateUs_id){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
            }catch (ActivityNotFoundException e){

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +getPackageName())));
            }

        }else if (id==R.id.privacy_policy_id){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
            }catch (ActivityNotFoundException e){

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +getPackageName())));
            }

        }else if (id==R.id.subscriber_id){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
            }catch (ActivityNotFoundException e){

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +getPackageName())));
            }

        }else {

            Toast.makeText(this, "try again..", Toast.LENGTH_SHORT).show();
        }

    }

    private void logOutAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout Alert !")
                .setMessage("Are you sure to logout")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, RegisterHostActivity.class));


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


    private void shareApp() {

        SaveUser saveUser = new SaveUser(this);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = "ReferId: "+saveUser.getNumber()+"AppLink:"+"https://play.google.com/store/apps/details?id=" +getPackageName();
        String shareSub = "Make Money by Android App";
        intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        intent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(intent,"Earning App"));

    }

}
