package com.example.tnb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText unitsEditText, rebateEditText;
    TextView resultTextView;
    Button calculateButton;

    DecimalFormat df = new DecimalFormat("#,###.00");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mytoolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(mytoolbar);

        unitsEditText = findViewById(R.id.unitsEditText);
        rebateEditText = findViewById(R.id.rebateEditText);
        resultTextView = findViewById(R.id.resultTextView);
        calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Error handling: Check if EditText fields are empty before proceeding
                if (unitsEditText.getText().toString().isEmpty() || rebateEditText.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter values in both fields", Toast.LENGTH_SHORT).show();
                } else {
                    calculateBill();
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        } else if (selected == R.id.menuInstructions) {
            Intent intent = new Intent(MainActivity.this, Instructions.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void calculateBill() {
        try {
            // Get user input
            int units = Integer.parseInt(unitsEditText.getText().toString());
            double rebate = Double.parseDouble(rebateEditText.getText().toString());

            // Check if rebate percentage is within the valid range (0% - 5%)
            if (rebate < 0 || rebate > 5) {
                // Display error message if rebate percentage is out of range
                Toast.makeText(MainActivity.this, "Rebate percentage must be between 0% and 5%", Toast.LENGTH_SHORT).show();
                return; // Exit the method early
            }

            // Calculate bill
            double billAmount = calculateBillAmount(units, rebate);

            // Display result
            resultTextView.setText("Electricity Bill: RM" + df.format(billAmount));
        } catch (NumberFormatException e) {
            // Error handling: Catch NumberFormatException if input cannot be parsed to int or double
            Toast.makeText(MainActivity.this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show();
        }
    }


    private double calculateBillAmount(int units, double rebate) {
        double totalBill = 0;

        if (units <= 200) {
            totalBill = units * 0.218;
        } else if (units <= 300) {
            totalBill = (200 * 0.218) + ((units - 200) * 0.334);
        } else if (units <= 600) {
            totalBill = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
        } else if (units > 600) {
            totalBill = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
        }

        // Apply rebate
        totalBill -= (totalBill * rebate / 100.0);

        return totalBill;
    }
}
