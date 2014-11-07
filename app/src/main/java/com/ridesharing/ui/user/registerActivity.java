package com.ridesharing.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultType;
import com.ridesharing.Entity.User;
import com.ridesharing.R;
import com.ridesharing.Service.UserService;
import com.ridesharing.ui.ActionBarBaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

@EActivity(R.layout.activity_register)
public class registerActivity extends ActionBarBaseActivity {

    @ViewById(R.id.btnRegister)
    Button btnRegister;
    @ViewById(R.id.link_to_login)
    TextView loginScreen;
    @ViewById(R.id.reg_firstname)
    EditText firstname;
    @ViewById(R.id.reg_lastname)
    EditText lastname;
    @ViewById(R.id.reg_address)
    EditText address;
    @ViewById(R.id.reg_address2)
    EditText address2;
    @ViewById(R.id.reg_city)
    EditText city;
    @ViewById(R.id.reg_state)
    EditText state;
    @ViewById(R.id.reg_zip)
    EditText zip;
    @ViewById(R.id.reg_phone)
    EditText phone;
    @ViewById(R.id.reg_username)
    EditText username;
    @ViewById(R.id.reg_password)
    EditText password;
    @ViewById(R.id.reg_email)
    EditText email;
    @ViewById(R.id.reg_birthday)
    DatePicker birthday;

    @Inject
    UserService userService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.activity_register);
    }

    @AfterViews
    void initView(){
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    User user = new User(username.getText().toString(), password.getText().toString(), email.getText().toString(), firstname.getText().toString(), lastname.getText().toString(),
                            getDateFromDatePicket(birthday), address.getText().toString(), address2.getText().toString(),
                            city.getText().toString(),state.getText().toString(), zip.getText().toString(), phone.getText().toString(), ""
                            );
                    register(user);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.e("Error", e.getMessage());
                }

            }
        });
    }

    public static Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Background
    public void register(User user){
       Result result =  userService.Register(user);
       if(result.getType() == ResultType.Success){
           finish();
       }else{
           showError(result.getMessage());
       }
    }

    @UiThread
    public void showError(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
