package com.seg2105.doooge.choreassistant;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2017/11/22.
 */

public class showDetailDialog extends AppCompatActivity {

    DatabaseReference databaseChore = FirebaseDatabase.getInstance().getReference("Chore");
    private Chore choreSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choredetail_dialog);
        Intent intent = getIntent();
        choreSubmit = (Chore) intent.getSerializableExtra("SUBMIT");
        initialData(choreSubmit);

        final ImageButton finish_button = findViewById(R.id.finishButton);
        finish_button.setOnClickListener(new View.OnClickListener() {
            boolean click = false;

            @Override
            public void onClick(View view) {
                click = !click;
                if (click) {


                    showDialog(finish_button);

                }
            }
        });


    }

    // intial all the informatioin of chore
    public void initialData(Chore chore) {
        TextView choreName = findViewById(R.id.printChoreName);
        choreName.setText("Clean the Garage");
        TextView time = findViewById(R.id.printTime);
        time.setText("2017/11/22" + "\n" + "18:00");
        TextView description = findViewById(R.id.printDescription);
        description.setText("Please be careful !!!");
        TextView reward = findViewById(R.id.printReward);
        reward.setText("You will get 10 dollars!");
        Intent intent = getIntent();
        choreSubmit = (Chore) intent.getSerializableExtra("SUBMIT");

        if (choreSubmit != null) {

            choreName.setText(choreSubmit.getChoreName());
            description.setText(choreSubmit.getDescription());

            Calendar tempCal = Calendar.getInstance();
            long millis = choreSubmit.getTimeInMillis();

            tempCal.setTimeInMillis(millis);

            int calYear = tempCal.get(Calendar.YEAR);
            int calMonth = tempCal.get(Calendar.MONTH);
            int calDay = tempCal.get(Calendar.DAY_OF_MONTH);
            int calHour = tempCal.get(Calendar.HOUR);
            int calMinute = tempCal.get(Calendar.MINUTE);

            String Date = setDate(calYear, calMonth, calDay);
            String times = setTime(calHour, calMinute);

            time.setText(Date + "\n" + times);

        }
    }

    /**
     * Set the text of the textDate field.
     *
     * @param year  calendar year
     * @param month calendar month
     * @param day   calendar day
     */
    private String setDate(int year, int month, int day) {
        TextView textDate = findViewById(R.id.textDate);


        String[] monthString = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"};


        return (monthString[month] + " " + day + ", " + year);
    }

    /**
     * Set the text of the textTime field.
     *
     * @param hour   time in hours
     * @param minute time in minutes
     */
    private String setTime(int hour, int minute) {
        TextView textTime = findViewById(R.id.textTime);

        return (String.format("%02d:%02d", hour, minute));
    }



    public void closeDialog(View view) throws Exception {
        try {
            finish();
        } catch (Exception e) {
        }
    }

    public void showDialog(final ImageButton button) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.choredetail_warn, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Confirmation :");

        final Button login = dialogView.findViewById(R.id.yes_button);
        final Button exist = dialogView.findViewById(R.id.no_button);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseChore.child(choreSubmit.getChoreName()).child("complete").setValue(true);
                Toast.makeText(getApplicationContext(), "Congraduation, You finish !!!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                button.setBackgroundResource(R.drawable.finish_button);
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        finish();


                        t.cancel();
                    }
                }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.


            }
        });
        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


}
