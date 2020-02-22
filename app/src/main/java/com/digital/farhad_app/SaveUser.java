package com.digital.farhad_app;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveUser {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SaveUser(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void dataStore (String number,String password,String userName){

        editor.putString("number",number);
        editor.putString("password",password);
        editor.putString("userName",userName);
        editor.commit();

    }

    public String getNumber (){

        String number = sharedPreferences.getString("number","");
        return number;
    }

    public String getUserName (){

        String userName = sharedPreferences.getString("userName","");
        return userName;
    }
    public String getpassword (){

        String password = sharedPreferences.getString("password","");
        return password;
    }

    public void delete (){

        editor.clear();
        editor.commit();

    }


}
