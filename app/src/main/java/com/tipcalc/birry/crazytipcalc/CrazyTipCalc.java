package com.tipcalc.birry.crazytipcalc;

import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class CrazyTipCalc extends ActionBarActivity {

    //defines constants that are saved if shutdown and reopen
    //it is used for onCreate and savedInstance State
    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP = "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    private double billBeforeTip;
    private double tipAmount;
    private double finalBill;

    //editable texts
    EditText billBeforeTipET;
    EditText tipAmountET;
    EditText finalBillET;

    //Initialize Seek Bar
    SeekBar tipSeekBar;

    //Lesson 2

    Spinner problemSpinner;

    //buttons
    Button startChronometerButton;
    Button pauseChronometerButton;
    Button resetChronometerButton;

    private int[] checkListValues = new int [12];
    CheckBox friendlyCheckBox;
    CheckBox specialsCheckBox;
    CheckBox opinionsCheckBox;

    RadioGroup avaliableRadioGroup;
    RadioButton badRadio;
    RadioButton okRadio;
    RadioButton goodRadio;

    Chronometer timeWaitingChronometer;
    long secondsWaiting = 0;

    TextView timeWaitingTextView;

    //When we initialize the code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazy_tip_calc);

        //check if app just started or restored from another state

        //if savedInstanceState == null, means we just got started.
        if (savedInstanceState == null){
            billBeforeTip = 0.0;
            tipAmount = .15;
            finalBill = 0.0;
        }
        //else the restored state
        else
        {
            //current block is found from top to restart with last used data
            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
            tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
            finalBill = savedInstanceState.getDouble(TOTAL_BILL);
        }

        //Intitialize all my edit text boxes (Got from the ID of the mainActivity.xml stuff)
        billBeforeTipET = (EditText) findViewById(R.id.billEditText);
        tipAmountET = (EditText) findViewById(R.id.tipEditText);
        finalBillET = (EditText) findViewById(R.id.finalBillEditText);
        //initialize seek bar
        tipSeekBar = (SeekBar) findViewById(R.id.changeTipSeekBar);

        //change listener (Listener are input events)

        billBeforeTipET.addTextChangedListener(billBeforeTipListener);

        //listener for seek bar
        tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListener);

        //Lesson 2
        //Initializing checkboxes


        friendlyCheckBox = (CheckBox) findViewById(R.id.friendlyCheckBox);
        specialsCheckBox = (CheckBox) findViewById(R.id.specialsCheckBox);
        opinionsCheckBox = (CheckBox) findViewById(R.id.opinionsCheckBox);

        //setup all my listeners
        setUpIntroCheckBoxes();

        avaliableRadioGroup = (RadioGroup) findViewById(R.id.avaliableRadioGroup);
        badRadio = (RadioButton) findViewById(R.id.avaliableBadRadio);
        okRadio = (RadioButton) findViewById(R.id.avaliableRadioOk);
        goodRadio = (RadioButton) findViewById(R.id.avaliableGoodRadio);

        addChangeListenerToRadios();

        problemSpinner = (Spinner) findViewById(R.id.problemsSpinner);

        addItemSelectedListenerSpinner();

        startChronometerButton = (Button) findViewById(R.id.startButton);
        pauseChronometerButton = (Button) findViewById(R.id.pauseButton);
        resetChronometerButton = (Button) findViewById(R.id.resetButton);
        //listener for buttons
        setButtonOnClickListeners();

        timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);

        timeWaitingTextView = (TextView) findViewById (R.id.timeWaitingTextView);

    }

    private TextWatcher billBeforeTipListener = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try{
                billBeforeTip = Double.parseDouble(s.toString());
            }
            catch(NumberFormatException e)
            {
                billBeforeTip = 0.0;
            }

            updateTipAndFinalBill();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateTipAndFinalBill()
    {

        double tipAmount = Double.parseDouble(tipAmountET.getText().toString());

        double finalBill = billBeforeTip + (billBeforeTip * tipAmount);

        finalBillET.setText(String.format("%.02f", finalBill));
    }

    //Lesson 3 Listener Functions
    private void setUpIntroCheckBoxes()
    {
        friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //If it is checked, its 4, else 0
                checkListValues[0] = (friendlyCheckBox.isChecked())?4:0;

                setTipFromWaitressCheckList();

                updateTipAndFinalBill();
            }
        });
        specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //If it is checked, its 4, else 0
                checkListValues[1] = (specialsCheckBox.isChecked()) ? 4 : 0;

                setTipFromWaitressCheckList();

                updateTipAndFinalBill();
            }
        });
        opinionsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //If it is checked, its 4, else 0
                checkListValues[2] = (opinionsCheckBox.isChecked()) ? 2 : 0;

                setTipFromWaitressCheckList();

                updateTipAndFinalBill();
            }
        });
    }
    //Listener for radio
    public void addChangeListenerToRadios()
    {
        avaliableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkListValues[3] = badRadio.isChecked() ? -1 : 0;
                checkListValues[4] = okRadio.isChecked() ? 2 : 0;
                checkListValues[5] = goodRadio.isChecked() ? 4 : 0;
                setTipFromWaitressCheckList(); //Loops through tip value
                updateTipAndFinalBill();       //updates tip amount
            }
        });
    }
    private void addItemSelectedListenerSpinner(){

        problemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkListValues[6] = problemSpinner.getSelectedItem().equals("Bad") ? -1 : 0;
                checkListValues[7] = problemSpinner.getSelectedItem().equals("OK") ? 2 : 0;
                checkListValues[8] = problemSpinner.getSelectedItem().equals("Good") ? 6 : 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //waitress checklist
    private void setTipFromWaitressCheckList()
    {
        int checkListTotal = 0;
        for (int item: checkListValues){
            checkListTotal += item;
        }
        tipAmountET.setText(String.format("%.02f", checkListTotal * .01));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crazy_tip_calc, menu);
        return true;
    }

    //if device changes, or if rotated, etc
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putDouble(TOTAL_BILL, finalBill);
        outState.putDouble(CURRENT_TIP, tipAmount);
        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
    }

    private void setButtonOnClickListeners()
    {
        startChronometerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Hold the total number of milliseconds paused last
                int stoppedMilliseconds = 0;
                // if i want to get text from chronometer
                String chronoText = timeWaitingChronometer.getText().toString();
                String array[] = chronoText.split(":");

                //determining which is for seconds and minutes.
                if (array.length == 2) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 +
                            Integer.parseInt(array[1]) * 1000;
                } else if (array.length == 3) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 +
                            Integer.parseInt(array[1]) * 60 * 1000
                            + Integer.parseInt(array[2]) * 1000;
                }
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);

                secondsWaiting = Long.parseLong(array[1]); //array[1] for seconds, 2 for minutes

                updateTipBasedOnTimeWaited(secondsWaiting);

                timeWaitingChronometer.start();
            }
        });
        pauseChronometerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                timeWaitingChronometer.stop();
            }
        });
        resetChronometerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());

                secondsWaiting = 0;
            }
        });
    }

    //update tip on button
    private void updateTipBasedOnTimeWaited(long secondsWaited)
    {
        checkListValues[9] = (secondsWaited > 25)? -2:2;
        setTipFromWaitressCheckList();
        updateTipAndFinalBill();
    }
    private SeekBar.OnSeekBarChangeListener tipSeekBarListener = new SeekBar.OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            tipAmount = (tipSeekBar.getProgress()) * .01; //turn it into a percent
            tipAmountET.setText(String.format("%.02f",tipAmount));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
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
