package com.example.dell.attendence_mgt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.System.in;

public class PostAttendance extends AppCompatActivity implements View.OnClickListener{

    String day,month,year;
    Button today_att,custom_att,pick_date;
    EditText cust_date;
    int mYear, mMonth, mDay;
    Intent intent;
    List<String> supplierNames = new ArrayList<>();
    DatabaseReference reference;
    int a,b,c2;
    int a1,b1,c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_attendence);

        intent = new Intent(PostAttendance.this,GetAttendance.class);

        today_att = (Button)findViewById(R.id.button2);
        custom_att = (Button)findViewById(R.id.button3);
        pick_date = (Button)findViewById(R.id.button6);
        cust_date = (EditText)findViewById(R.id.editText);

        custom_att.setOnClickListener(this);
        today_att.setOnClickListener(this);
        pick_date.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        a = calendar.get(Calendar.YEAR);
        b = calendar.get(Calendar.MONTH);
        c2 = calendar.get(Calendar.DAY_OF_MONTH);

        year = String.valueOf(a);
        month = String.valueOf(b+1);
        day = String.valueOf(c2);

        supplierNames = Arrays.asList("sup1", "sup2", "sup3", "sup4", "sup5");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        reference = db.getReference();

        int i;


        for(i=0;i<supplierNames.size();i++) {
            AttendanceModel model = new AttendanceModel(String.valueOf(i),supplierNames.get(i), "Present");

            //reference.child("Attendance").child(year).child(month).child(day).child(String.valueOf(1));
            reference.child("Attendance").child(year).child(month).child(day).child(String.valueOf(i)).setValue(model);
            reference.child("Students").setValue(supplierNames);
        }

    }

    @Override
    public void onClick(View v) {

        if(v==today_att){
            intent.putExtra("day",day);
            intent.putExtra("month",month);
            intent.putExtra("year",year);
            startActivity(intent);
        }

        if(v==custom_att){
            if(cust_date.getText().toString().equals("")){
                Toast.makeText(getBaseContext(),"Please choose a valid date to post attendance",Toast.LENGTH_SHORT).show();
            }
            else{

                int i;
                for(i=0;i<supplierNames.size();i++) {
                    AttendanceModel model = new AttendanceModel(String.valueOf(i),supplierNames.get(i), "Present");

                    //reference.child("Attendance").child(year).child(month).child(day).child(String.valueOf(1));
                    reference.child("Attendance").child(a1+"").child((b1+1)+"").child(c1+"").child(String.valueOf(i)).setValue(model);
                    reference.child("Students").setValue(supplierNames);
                }

//                intent.putExtra("day",mDay+"");
//                intent.putExtra("month",mMonth+"");
//                intent.putExtra("year",mYear+"");
                startActivity(intent);

            }
        }

        if(v==pick_date){
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            //Toast.makeText(getBaseContext(),"Chal raha hai",Toast.LENGTH_LONG).show();
//            cust_date.isFocusableInTouchMode(true);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            intent.putExtra("day",dayOfMonth+"");
                            intent.putExtra("month",(monthOfYear+1)+"");
                            intent.putExtra("year",year+"");

                            a1 = year;
                            b1 = monthOfYear;
                            c1 = dayOfMonth;

                            cust_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, a, b, c2);

            datePickerDialog.show();

        }


    }

}
