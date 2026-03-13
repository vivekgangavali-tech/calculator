package com.example.calc_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    TextView tvResult, tvExpression;
    String currentInput = "";
    String operator = "";
    double firstNumber = 0;
    boolean justCalculated = false;
    ArrayList<String> history = new ArrayList<>();
    TextView tvHistory;
    LinearLayout historyPanel;
    boolean historyVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);
        tvHistory = findViewById(R.id.tvHistory);
        historyPanel = findViewById(R.id.historyPanel);

// Toggle history panel
        findViewById(R.id.btnHistory).setOnClickListener(v -> {
            historyVisible = !historyVisible;
            historyPanel.setVisibility(historyVisible ? View.VISIBLE : View.GONE);
        });

// Clear history
        findViewById(R.id.btnClearHistory).setOnClickListener(v -> {
            history.clear();
            tvHistory.setText("");
        });

        // Number buttons
        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(v -> {
                String digit = ((Button) v).getText().toString();
                if (justCalculated) {
                    currentInput = "";
                    justCalculated = false;
                }
                currentInput += digit;
                tvResult.setText(currentInput);
                tvExpression.setText("");
            });
        }

        // Decimal point
        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (!currentInput.contains(".")) {
                if (currentInput.isEmpty()) currentInput = "0";
                currentInput += ".";
                tvResult.setText(currentInput);
            }
        });

        // Operators: + - × ÷
        View.OnClickListener operatorListener = v -> {
            String op = ((Button) v).getText().toString();
            if (!currentInput.isEmpty()) {
                firstNumber = Double.parseDouble(currentInput);
            }
            operator = op;
            tvExpression.setText(formatNumber(firstNumber) + " " + op);
            currentInput = "";
            justCalculated = false;
        };

        findViewById(R.id.btnPlus).setOnClickListener(operatorListener);
        findViewById(R.id.btnMinus).setOnClickListener(operatorListener);
        findViewById(R.id.btnMultiply).setOnClickListener(operatorListener);
        findViewById(R.id.btnDivide).setOnClickListener(operatorListener);

        // Equals
        findViewById(R.id.btnEquals).setOnClickListener(v -> {
            if (currentInput.isEmpty() || operator.isEmpty()) return;
            double secondNumber = Double.parseDouble(currentInput);
            double result = 0;

            tvExpression.setText(formatNumber(firstNumber) + " " + operator + " " + formatNumber(secondNumber) + " =");

            String operator_symbol = operator;

            switch (operator) {
                case "+": result = firstNumber + secondNumber; break;
                case "−": result = firstNumber - secondNumber; break;
                case "×": result = firstNumber * secondNumber; break;
                case "÷":
                    if (secondNumber == 0) {
                        tvResult.setText("Error");
                        currentInput = "";
                        operator = "";
                        return;
                    }
                    result = firstNumber / secondNumber;
                    break;
            }

            String resultStr = formatNumber(result);
            tvResult.setText(resultStr);
            currentInput = resultStr;
            operator = "";
            justCalculated = true;

// Save to history (keep last 5 only)
            String entry = formatNumber(firstNumber) + " " + operator_symbol + " " + formatNumber(secondNumber) + " = " + resultStr;
            history.add(0, entry);
            if (history.size() > 5) history.remove(history.size() - 1);

// Update history display
            StringBuilder sb = new StringBuilder();
            for (String h : history) sb.append(h).append("\n");
            tvHistory.setText(sb.toString().trim());
        });

        // AC - All Clear
        findViewById(R.id.btnAC).setOnClickListener(v -> {
            currentInput = "";
            operator = "";
            firstNumber = 0;
            justCalculated = false;
            tvResult.setText("0");
            tvExpression.setText("");
        });

        // DEL - Delete last character
        findViewById(R.id.btnDel).setOnClickListener(v -> {
            if (!currentInput.isEmpty()) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                tvResult.setText(currentInput.isEmpty() ? "0" : currentInput);
            }
        });

        // % - Percentage
        findViewById(R.id.btnPercent).setOnClickListener(v -> {
            if (!currentInput.isEmpty()) {
                double val = Double.parseDouble(currentInput) / 100;
                currentInput = formatNumber(val);
                tvResult.setText(currentInput);
            }
        });

        // +/- Toggle sign
        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> {
            if (!currentInput.isEmpty() && !currentInput.equals("0")) {
                if (currentInput.startsWith("-")) {
                    currentInput = currentInput.substring(1);
                } else {
                    currentInput = "-" + currentInput;
                }
                tvResult.setText(currentInput);
            }
        });
    }

    // Formats number: removes .0 for whole numbers
    private String formatNumber(double num) {
        if (num == (long) num) {
            return String.valueOf((long) num);
        } else {
            return String.valueOf(num);
        }
    }
}
