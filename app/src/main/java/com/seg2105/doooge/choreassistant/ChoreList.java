package com.seg2105.doooge.choreassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;


/**
 * Created by dustin on 11/22/17.
 */

public class ChoreList extends AppCompatActivity {

    //Stores user information
    private static String currentOperator;
    private PersonRule currentUser;

    //stores the current date
    private int day;
    private int month;
    private int year;
    private Calendar cal;

    DatabaseReference databaseResponsibility = FirebaseDatabase.getInstance().getReference("Responsibility");
    DatabaseReference databaseChore = FirebaseDatabase.getInstance().getReference("Chore");


    /**
     * Displays the a date picker dialog and sets the current date.
     */
    private DatePickerDialog.OnDateSetListener tempListen = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //the selected month (0-11 for compatibility with MONTH)
            updateDate(year, month, dayOfMonth);
            setDate(year, month, dayOfMonth);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_list);

        Intent intent = getIntent();
        currentUser = (PersonRule) intent.getSerializableExtra("currentUser");

        //setting up calendar
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        setDate(year, month, day);

        //FAKE CHORE AND RESPONSIBILITY
        try {


            Chore chore1 = new Chore("Chore1", "Displayed!! 1/2", 123);
            Chore chore2 = new Chore("Chore2", "Displayed!! 2/2", 456);
            Chore chore3 = new Chore("Chore3", "Should not display!!!", 789);
            databaseChore.child(chore1.getChoreIdentification()).setValue(chore1);
            databaseChore.child(chore2.getChoreIdentification()).setValue(chore2);
            databaseChore.child(chore3.getChoreIdentification()).setValue(chore3);


            String a = chore1.getChoreIdentification();
            String b = chore2.getChoreIdentification();
            String c = chore3.getChoreIdentification();
            Responsibility responsibility1 = new Responsibility(605228, a);//Assign to Vison
            Responsibility responsibility2 = new Responsibility(605228, b);//Assign to Vison
            Responsibility responsibility3 = new Responsibility(123456, c);//Should not display
            databaseResponsibility.child(a).setValue(responsibility1);
            databaseResponsibility.child(b).setValue(responsibility2);
            databaseResponsibility.child(c).setValue(responsibility3);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //================================

        databaseResponsibility.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot responsibility : dataSnapshot.getChildren()) {

                    int currentUserID = currentUser.getUserID();
                    long userIDInDB = (long) responsibility.child("userID").getValue();

                    System.out.println("=================================");
                    System.out.println("currentUserID: " + currentUserID);
                    System.out.println("userIDInDB: " + userIDInDB);
                    System.out.println("IF: " + (currentUserID == userIDInDB));
                    System.out.println("=================================");
                    if (currentUserID == userIDInDB) {

                        final String correspondingChoreID = (String) responsibility.child("choreIdentification").getValue();

                        databaseChore.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singleChore : dataSnapshot.getChildren()) {
                                    if (singleChore.child("choreIdentification").getValue().equals(correspondingChoreID)) {
                                        Chore chore = singleChore.getValue(Chore.class);
                                        displayChore(chore);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        // check if admin, if not ,set invisible for add button and edit button


        if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE) != null) {

            if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE).equals("user")) {
                currentOperator = "user";
                Button add = findViewById(R.id.btnAdd);
                add.setVisibility(View.VISIBLE);

            } else if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE).equals("admin")) {
                currentOperator = "admin";
                Button add = findViewById(R.id.btnAdd);
                add.setVisibility(View.VISIBLE);
            }
        } else {
            //choreSubmit = (Chore) intent.getSerializableExtra("SUBMIT");
        }


        //get the PersonRule from Welcomepage
        //testRule = (PersonRule) intent.getSerializableExtra("test");

    }

    public void add_OnClick(View view) {
        Intent intent = new Intent(ChoreList.this, ChoreEdit.class); //switch homepage to edit chore page
        startActivity(intent);
    }

    /**
     * Creates a gridlayout and adds the new layout to choreView
     * the content that will be displayed is based on the chore
     *
     * @param chore the chore that has been assigned to, or created by, the current user.
     */
    private void displayChore (Chore chore){
        //extract Calendar data from chore
        Calendar tempCal        = Calendar.getInstance();
        tempCal.setTimeInMillis( chore.getTimeInMillis() );

        int choreHours          = tempCal.get(Calendar.HOUR);
        int choreMinute         = tempCal.get(Calendar.MINUTE);
        int choreYear           = tempCal.get(Calendar.YEAR);
        int choreMonth          = tempCal.get(Calendar.MONTH);
        int choreDay            = tempCal.get(Calendar.DAY_OF_MONTH);

        //if chores day doesn't match the current displayed day exit
        //if ((choreYear != year) || (choreMonth != month) || (choreDay != day)){
        //    return ;
        //}

        LinearLayout linearView = findViewById(R.id.choreView);

        GridLayout gridLayout   = new GridLayout(this);
        Point point             = new Point();                  //required for to get display size

        //create text views and add text to them
        TextView text1          = textTest(chore.getChoreName(),18);
        TextView text2          = textTest( String.format("%02d:%02d", choreHours, choreMinute), 14);
        TextView text3          = textTest(chore.getDescription(), 14);

        //set the layout
        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(2);
        text1.setGravity(Gravity.LEFT);
        text2.setGravity(Gravity.TOP | Gravity.LEFT);
        text3.setGravity(Gravity.LEFT);
        getWindowManager().getDefaultDisplay().getSize(point);
        text1.setWidth(point.x - 160);
        text2.setWidth( 160 );
        text3.setWidth(point.x - 160);

        text1.setTypeface(Typeface.DEFAULT_BOLD);
        text2.setTypeface(null,Typeface.ITALIC);
        text3.setTypeface(null,Typeface.BOLD_ITALIC);

        text1.setTextColor(Color.BLACK);

        //add text views to new grid layout
        gridLayout.addView(text1);
        gridLayout.addView(text2);
        gridLayout.addView(text3);

        gridLayout.setPadding(0,30,0,30);

        //tag the chore for referencing
        gridLayout.setTag(chore);

        // Create a listener for touch screen events;
        gridLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Toast.makeText(getBaseContext(), " " + view.getTag(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChoreList.this, showDetailDialog.class);
                intent.putExtra("SUBMIT", (Chore) view.getTag());
                startActivity(intent);

            }
        });

        //Create a listener for long touch screen events;
        gridLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (currentOperator.equals("admin")) {
                    Toast.makeText(getBaseContext(), "It is a long click event", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChoreList.this, ChoreEdit.class);
                    intent.putExtra("SUBMIT", (Chore) view.getTag());
                    startActivity(intent);
                }
                return true;

            }
        });

        linearView.addView(gridLayout);

    }


    /**
     * Creates new textviews to display in new gridlayout of choreView
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

    public void imgDateUp_OnClick(View view) {
        buildNewDate(1);
    }

    public void imgDateDown_OnClick(View view) {
        buildNewDate(-1);
    }


    /**
     * Sets the class varibles to this date and, if required, refreshes the ui
     *
     * @param year sets the new year
     * @param month sets the new month
     * @param day sets the new day
     */
    private void updateDate(int year, int month, int day){

        if ((year == this.year) && (month == this.month) && (day == this.day)) {
            return;
        }

        cal.set(year,month,day);

        this.year   = year;
        this.month  = month;
        this.day    = day;

        scrubChoreView();
    }

    /**
     * used to either add or subtract X amount of days from the current day
     *
     * @param upDown amount in days to shift the calendar date by, can be negative or positive
     */
    private void buildNewDate(int upDown) {
        cal.add(Calendar.DATE, upDown);

        year    = cal.get(Calendar.YEAR);
        month   = cal.get(Calendar.MONTH);
        day     = cal.get(Calendar.DAY_OF_MONTH);

        updateDate(year, month, day);
        setDate(year, month, day);
    }


    private void scrubChoreView(){
        LinearLayout choreView = findViewById(R.id.choreView);
        choreView.removeAllViews();
    }

    /**
     * Displays the date as a readable text to textDate
     *
     * @param year the year the calendar is to be set to
     * @param month the month the calendar is to be set to
     * @param day the day the calendar is to be set to
     */
    private void setDate(int year, int month, int day) {
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

        textDate.setText(monthString[month] + " " + day + ", " + year);
    }

    private void datePick() {
        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.show();
    }


}
