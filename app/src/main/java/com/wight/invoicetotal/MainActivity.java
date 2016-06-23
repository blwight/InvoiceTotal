package com.wight.invoicetotal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity 
implements TextView.OnEditorActionListener {

    // define instance variables
    private EditText subtotalEditText;
    private TextView percentTextView;
    private TextView discountAmountTextView;
    private TextView totalTextView;

    private String subtotalString = "";
    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      // get references to the widgets
        subtotalEditText = (EditText) findViewById(R.id.subtotalEditText);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        discountAmountTextView = (TextView) findViewById(R.id.amountTextView);
        totalTextView = (TextView) findViewById(R.id.totalValueTextView);

        //set the listener
        subtotalEditText.setOnEditorActionListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        
        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            
            calculateAndDisplay();
            
        }
        return false;
    }

    private void calculateAndDisplay() {

        subtotalString = subtotalEditText.getText().toString();
        float subtotal;
        if(subtotalString.equals("")) {
            subtotal = 0;
        }   else {
            subtotal = Float.parseFloat(subtotalString);

        }

        // get discount  percent
        float discountPercent = 0;
        if(subtotal >= 200){
            discountPercent = .2f;

        } else if (subtotal >= 100) {
            discountPercent = .1f;
        } else {
                discountPercent = 0;
            }

        // calculate discount

        float discountAmount = subtotal * discountPercent;
        float total = subtotal - discountAmount;

        NumberFormat percent =  NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(discountPercent));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        discountAmountTextView.setText(currency.format(discountAmount));
        totalTextView.setText(currency.format(total));

    }

    @Override
    protected void onPause() {

        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalString);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        subtotalString = savedValues.getString("subtotalString", "");
        subtotalEditText.setText(subtotalString);

        calculateAndDisplay();
    }
}
