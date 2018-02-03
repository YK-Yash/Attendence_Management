package com.example.dell.attendence_mgt;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class GetAttendance extends AppCompatActivity {

    RecyclerView recycle;
    FirebaseDatabase db;
    List<AttendanceModel> list = new ArrayList<>();
    AttendanceModel pojo;
    DatabaseReference reference;

    String year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_attendance);

        recycle = (RecyclerView) findViewById(R.id.recycle);
        db = FirebaseDatabase.getInstance();

        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        day = getIntent().getStringExtra("day");

        Toast.makeText(getBaseContext(),year+" "+month+" "+day,Toast.LENGTH_LONG).show();

        String URL = "/Attendance/"+year+"/"+month+"/"+day;

        reference = db.getReference(URL);//"/Attendance/2017/12/6");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    AttendanceModel value = dataSnapshot1.getValue(AttendanceModel.class);
                    AttendanceModel fire = new AttendanceModel();
                    String name = value.getName();
                    String status = value.getAtt();
                    fire.setName(name);
                    fire.setAtt(status);
                    list.add(fire);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(list,GetAttendance.this);
                RecyclerView.LayoutManager recyce = new GridLayoutManager(GetAttendance.this,1);
                recycle.setLayoutManager(recyce);
                recycle.setItemAnimator( new DefaultItemAnimator());
                recycle.setAdapter(recyclerAdapter);

            }
        },5000);


            recycle.addOnItemTouchListener(new RecyclerTouchListener(getBaseContext(),
                    recycle, new ClickListener() {
                @Override
                public void onClick(View view, final int position) {


                    pojo = list.get(position);
                    Toast.makeText(getBaseContext(),pojo.getName(),Toast.LENGTH_LONG).show();
                    Toast.makeText(getBaseContext(),pojo.getAtt(),Toast.LENGTH_LONG).show();

                    pojo.att = "Present";

                    reference.child(String.valueOf(position)).child("att").setValue("Absent");

                    TextView tx = (TextView) view.findViewById(R.id.vCrop);
                    tx.setText("Absent");
                }

                @Override
                public void onLongClick(View view, int position) {
                    Toast.makeText(GetAttendance.this, "",
                            Toast.LENGTH_LONG).show();
                }
            }));





    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



}
