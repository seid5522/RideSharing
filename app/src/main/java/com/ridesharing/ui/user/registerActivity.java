package com.ridesharing.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.*;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultType;
import com.ridesharing.Entity.User;
import com.ridesharing.R;
import com.ridesharing.Service.UserService;
import com.ridesharing.ui.ActionBarBaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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


/**
 * @Package com.ridesharing.ui.user
 * @Author Nathan
 * @Date 2014/11/8.
 */
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
    @ViewById(R.id.driverSwitch)
    Switch driverSwitch;
    @ViewById(R.id.driverTitle)
    TextView driverTitle;
    @ViewById(R.id.driverMake)
    TextView MakeHeader;
    @ViewById(R.id.driverModel)
    TextView ModelHeader;
    @ViewById(R.id.driverSeats)
    TextView SeatsHeader;
    @ViewById(R.id.driverYear)
    TextView YearHeader;
    @ViewById(R.id.reg_carMake)
    EditText carMake;
    @ViewById(R.id.reg_carModel)
    EditText carModel;
    @ViewById(R.id.reg_numSeats)
    EditText numOfSeats;
    @ViewById(R.id.reg_carYear)
    EditText carYear;


    @Inject
    UserService userService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.activity_register);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
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
                            getDateFromDatePicker(birthday), address.getText().toString(), address2.getText().toString(),
                            city.getText().toString(),state.getText().toString(), zip.getText().toString(), phone.getText().toString(), ""
                            );
                    register(user);

                    User driver = new User(username.getText().toString(), password.getText().toString(), email.getText().toString(), firstname.getText().toString(), lastname.getText().toString(),
                            getDateFromDatePicker(birthday), address.getText().toString(), address2.getText().toString(),
                            city.getText().toString(),state.getText().toString(), zip.getText().toString(), phone.getText().toString(), ""
                    );
                    register(driver);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.e("Error", e.getMessage());
                }


            }
        });

        driverSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchDriver(isChecked);
            }
        });


    }

    private void switchDriver( boolean on){
        if(on){

            driverTitle.setVisibility(View.VISIBLE);
            MakeHeader.setVisibility(View.VISIBLE);
            ModelHeader.setVisibility(View.VISIBLE);
            SeatsHeader.setVisibility(View.VISIBLE);
            YearHeader.setVisibility(View.VISIBLE);

            carMake.setVisibility(View.VISIBLE);
            carModel.setVisibility(View.VISIBLE);
            numOfSeats.setVisibility(View.VISIBLE);
            carYear.setVisibility(View.VISIBLE);



        }else{
            driverTitle.setVisibility(View.GONE);
            MakeHeader.setVisibility(View.GONE);
            ModelHeader.setVisibility(View.GONE);
            SeatsHeader.setVisibility(View.GONE);
            YearHeader.setVisibility(View.GONE);

            carMake.setVisibility(View.GONE);
            carModel.setVisibility(View.GONE);
            numOfSeats.setVisibility(View.GONE);
            carYear.setVisibility(View.GONE);
        }
    }

    public void onSwitchClicked(View view) {

        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();
        switchDriver(on);

    }



    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        int month2 = Calendar.JANUARY;


        switch(month){
            case 1:
                month2 = Calendar.JANUARY;
                break;
            case 2:
                month2 = Calendar.FEBRUARY;
                break;
            case 3:
                month2 = Calendar.MARCH;
                break;
            case 4:
                month2 = Calendar.APRIL;
                break;
            case 5:
                month2 = Calendar.MAY;
                break;
            case 6:
                month2 = Calendar.JUNE;
                break;
            case 7:
                month2 = Calendar.JULY;
                break;
            case 8:
                month2 = Calendar.AUGUST;
                break;
            case 9:
                month2 = Calendar.SEPTEMBER;
                break;
            case 10:
                month2 = Calendar.OCTOBER;
                break;
            case 11:
                month2 = Calendar.NOVEMBER;
                break;
            case 12:
                month2 = Calendar.DECEMBER;
                break;
        }


        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month2, day);

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
