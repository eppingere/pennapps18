package com.pennapps18;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;


public class HelpActivity extends Activity implements VerticalStepperForm {
    private String number;
    private VerticalStepperFormLayout verticalStepperForm;

    // Form elements
    ToggleButton q0;
    ToggleButton q1;
    ToggleButton q2;
    ToggleButton q3;
    EditText q4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        String[] mySteps = {"Is your life immediately threatened?", "Are you in a safe location with shelter?",
                "Are you or someone near you injured?", "Do you have food, water, and supplies for the next 12 hrs?", "What else do we need to know?"};
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        // Finding the view
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);

        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, mySteps, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true) // It is true by default, so in this case this line is not necessary
                .init();

        Intent i = getIntent();
        number = i.getStringExtra("Phone#");
    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                view = createThreatStep();
                break;
            case 1:
                view = createLocStep();
                break;
            case 2:
                view = createInjuryStep();
                break;
            case 3:
                view = createSupplyStep();
                break;
            case 4:
                view = createExtraStep();
                break;
        }
        return view;
    }

    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case 0:
                checkThreat();
                break;
            case 1:
                checkLoc();
                break;
            case 2:
                checkInjury();
                break;
            case 3:
                checkSupply();
                break;
            case 4:
                verticalStepperForm.setStepAsCompleted(4);
                break;
        }
    }

    @Override
    public void sendData() {
        int urgency = 0;
        String message = "I need help!";

        if (q0.isChecked()) {
            urgency += 5;
        }
        if (!q1.isChecked()) {
            urgency += 5;
        }
        if (q2.isChecked()) {
            urgency += 5;
        }
        if (!q3.isChecked()) {
            urgency += 3;
        }
        if (q4.getText().toString() != null) {
            urgency++;
        }
        if (urgency > 5) { urgency = 5; }

        Intent myIntent = new Intent(HelpActivity.this,
                MessagingActivity.class);
        myIntent.putExtra ("Phone#", number);
        myIntent.putExtra("Urg", urgency);
        message += q0.isChecked() ? " My life is in immediate danger." : "";
        message += q1.isChecked() ? " I am in a safe location, and I have shelter." :
                " I am not in a safe location and I cannot find shelter.";
        message += q2.isChecked() ? " Me or someone near me is seriously injured." : "" +
                " No one near me is seriously injured.";
        message += q3.isChecked() ? " We have enough food, water, and supplies to last the next 12 hrs" :
                " We are missing food, water, and/or supplies for the next 12 hrs";
        if (q4.getText().toString() != null) {
            message += q4.getText().toString();
        }
        myIntent.putExtra("Body", message);
        startActivity(myIntent);
    }

    private View createThreatStep() {
        q0 = new ToggleButton(HelpActivity.this);
        q0.setTextOff("No");
        q0.setTextOn("Yes");
        return q0;
    }

    private View createLocStep() {
        q1 = new ToggleButton(HelpActivity.this);
        q1.setTextOff("No");
        q1.setTextOn("Yes");
        return q1;
    }

    private View createInjuryStep() {
        q2 = new ToggleButton(HelpActivity.this);
        q2.setTextOff("No");
        q2.setTextOn("Yes");
        return q2;
    }

    private View createSupplyStep() {
        q3 = new ToggleButton(HelpActivity.this);
        q3.setTextOff("No");
        q3.setTextOn("Yes");
        return q3;
    }

    private View createExtraStep() {
        q4 = new EditText(this);
        q4.setHint("Type any more necessary information here.");
        q4.setSingleLine(false);
        return q4;
    }

    private void checkThreat() {
        verticalStepperForm.setActiveStepAsCompleted();
    }

    private void checkLoc() {
        verticalStepperForm.setActiveStepAsCompleted();
    }

    private void checkInjury() {
        verticalStepperForm.setActiveStepAsCompleted();
    }

    private void checkSupply() {
        verticalStepperForm.setActiveStepAsCompleted();
    }
}
