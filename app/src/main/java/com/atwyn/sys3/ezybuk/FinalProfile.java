package com.atwyn.sys3.ezybuk;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class FinalProfile extends AppCompatActivity {
    EditText ed1, ed2, ed3, ed4;
    String finalprofilname, finalprofileemail, finalprofilemobileno;
    private DatePicker datePicker;
    private Calendar calendar;

    private int year, month, day;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_profile);

        Intent i = this.getIntent();
        finalprofilname = i.getExtras().getString("UseName");
        finalprofileemail = i.getExtras().getString("UseEmail");
        finalprofilemobileno = i.getExtras().getString("UserMobileNo");

        ed1 = (EditText) findViewById(R.id.editText_profilename);
        ed2 = (EditText) findViewById(R.id.editText_user2);
        ed3 = (EditText) findViewById(R.id.editText_user22);
        ed4 = (EditText) findViewById(R.id.editText_user222);
        save = (Button) findViewById(R.id.savefinal);
        final RadioGroup rgroup = (RadioGroup) findViewById(R.id.radioGender);
        final RadioButton male = (RadioButton) findViewById(R.id.genderMale);
        final RadioButton female = (RadioButton) findViewById(R.id.genderFemale);

        ed1.setText(finalprofilname);
        ed2.setText(finalprofilemobileno);
        ed4.setText(finalprofileemail);
    }
}

       /* calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        ed3.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}*/