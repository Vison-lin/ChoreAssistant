package com.seg2105.doooge.choreassistant;

import android.app.DatePickerDialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.Calendar;

import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * Created by dustin on 11/22/17.
 */

public class ChoreList extends AppCompatActivity {

    private int day;
    private int month;
    private int year;
    private Calendar cal;

    private final String CHORE_RED = "#ffff4444";
    private final String CHORE_PURPLE = "#ffaa66cc";
    private final String CHORE_ORANGE = "#ffff8800";
    private final String CHORE_GREEN = "#ff99cc00";
    private final String CHORE_BLUE = "#ff0099cc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_list);

        //setting up calendar
        cal     = Calendar.getInstance();
        day     = cal.get(Calendar.DAY_OF_MONTH);
        month   = cal.get(Calendar.MONTH);
        year    = cal.get(Calendar.YEAR);

        setDate(year,month,day);

        LinearLayout linearView = (LinearLayout) findViewById(R.id.choreView);

        /*
        Drawable[] draw = {
                getResources().getDrawable(R.drawable.back),
        };
        */

        for (int i = 0 ; i < 5; i++){
            GridLayout temp = layoutTest("Chore" + i,"TIME", "Name"); //, draw[i%5]);

            temp.setTag("chore" +i);
            linearView.addView( temp );

        }

    }


    public void add_OnClick(View view) {
//        startActivity(new Intent(this,ChoreEdit.class));
    }

    /**
     *
     * @param name name of chore to be created
     * @param time time that chore is to be completed
     * @param description description of chore
     * @return returns a grid layout with chore added into it
     */
    private GridLayout layoutTest(String name, String time, String description){ //, Drawable colorTemp){

        //Create grid layout and textviews
        GridLayout tempLayout   = new GridLayout(this);
        Point point             = new Point();                  //required for to get display size
        TextView text1          = textTest(name,30);
        TextView text2          = textTest(time, 24);
        TextView text3          = textTest(description, 24);

        //set grid layout size
        tempLayout.setColumnCount(2);
        tempLayout.setRowCount(2);

        //set textview position
        text1.setGravity(Gravity.LEFT);
        text2.setGravity(Gravity.TOP); // | Gravity.RIGHT);
        text3.setGravity(Gravity.LEFT);

        //text1.setTextColor(Color.parseColor(colorTemp));

        //get display size and use it to set textbox size.
        getWindowManager().getDefaultDisplay().getSize(point);
        text1.setWidth(point.x - 100);
        text2.setWidth( 100 );
        text3.setWidth(point.x - 100);

        text1.setTypeface(Typeface.DEFAULT_BOLD);
        text3.setTypeface(Typeface.DEFAULT_BOLD);

        //add the created textview to the grid layout
        tempLayout.addView(text1);
        tempLayout.addView(text2);
        tempLayout.addView(text3);


        //tempLayout.setBackgroundDrawable(colorTemp);
        //tempLayout.setBackgroundColor(Color.parseColor(colorTemp));


        tempLayout.setTag(name);

        // Create a listener for touch screen events;
        tempLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getBaseContext(), " " + view.getTag(), Toast.LENGTH_SHORT).show();
            }

        } );

        return tempLayout;
    }


    /**
     *
     * @param text text to be inserted into textview
     * @param textSize size of the font of the text
     * @return
     */
    private TextView textTest(String text, int textSize){
        TextView tempText = new TextView(this);
        tempText.setTextSize(textSize);
        tempText.setText(text);
        return tempText;
    }


    public void textDate_OnClick(View view) {
        datePick();
    }

    public void imgDateUp_OnClick(View view){
        buildNewDate(1);
    }

    public void imgDateDown_OnClick(View view){
        buildNewDate(-1);
    }

    private void updateDate(int year, int month, int day){
        cal.set(year,month,day);
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private void buildNewDate(int upDown){
        cal.add(Calendar.DATE, upDown);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        setDate(year,month,day);
    }

    private void setDate(int year, int month, int day) {
        TextView textDate = (TextView) findViewById(R.id.textDate);

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
                "December" };

        textDate.setText(monthString[month] + " " + day + ", " + year);
    }

    private void datePick() {

        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.show();
    }


    //https://developer.android.com/reference/android/app/DatePickerDialog.OnDateSetListener.html
    private DatePickerDialog.OnDateSetListener tempListen = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //the selected month (0-11 for compatibility with MONTH)
            updateDate(year,month,dayOfMonth);
            setDate(year, month, dayOfMonth);

        }

    };

}