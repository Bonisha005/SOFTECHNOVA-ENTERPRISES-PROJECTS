import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class Calculator extends JFrame implements ActionListener {
    private final JTextField display;
    private String currentOperator = "";
    private double firstOperand = 0;
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Scientific Calculator");
        setSize(440, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 5, 8, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttons = {
            "C", "(", ")", "±", "÷",
            "sin", "cos", "tan", "√", "*",
            "log", "ln", "x²", "x³", "-",
            "7", "8", "9", "^", "+",
            "4", "5", "6", "!", "=",
            "1", "2", "3", "pi", "e",
            "0", ".", "", "", ""
        };

        for (String text : buttons) {
            JButton btn;
            if (text.equals("")) {
                btn = new JButton();
                btn.setEnabled(false);
            } else {
                btn = new JButton(text);
                btn.setFont(new Font("Arial", Font.PLAIN, 18));
                btn.setBackground(Color.decode("#f5f5f5"));
                btn.setFocusPainted(false);
                btn.addActionListener(this);
            }
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);
        startNewNumber = true;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("C")) {
            clear();
        } else if ("0123456789.".contains(command)) {
            appendNumber(command);
        } else if (command.equals("±")) {
            toggleSign();
        } else if ("+-*/÷^".contains(command)) {
            processOperator(command);
        } else if (command.equals("=")) {
            calculateResult();
        } else if (command.equals("pi")) {
            appendConstant(Math.PI);
        } else if (command.equals("e")) {
            appendConstant(Math.E);
        } else if (command.equals("sin") ||
                   command.equals("cos") ||
                   command.equals("tan") ||
                   command.equals("log") ||
                   command.equals("ln") ||
                   command.equals("√") ||
                   command.equals("x²") ||
                   command.equals("x³") ||
                   command.equals("!") ) {
            processScientificFunction(command);
        } else if (command.equals("(") || command.equals(")")) {
            appendParenthesis(command);
        }
    }

    private void appendParenthesis(String par) {
        if (startNewNumber) {
            if (par.equals("(")) {
                display.setText(par);
                startNewNumber = false;
            }
            // Do nothing if start new number and par is ")"
        } else {
            display.setText(display.getText() + par);
        }
    }

    private void appendConstant(double constant) {
        if (startNewNumber) {
            display.setText(Double.toString(constant));
            startNewNumber = false;
        } else {
            display.setText(display.getText() + constant);
        }
    }
    
    private void toggleSign() {
        try {
            String text = display.getText();
            if (text.equals("0") || startNewNumber) {
                return;
            }
            double value = Double.parseDouble(text);
            value = -value;
            display.setText(formatNumber(value));
        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNewNumber = true;
        }
    }

    private void clear() {
        display.setText("0");
        currentOperator = "";
        firstOperand = 0;
        startNewNumber = true;
    }

    private void appendNumber(String num) {
        if (startNewNumber) {
            if (num.equals(".")) {
                display.setText("0.");
            } else {
                display.setText(num);
            }
            startNewNumber = false;
        } else {
            if (num.equals(".") && display.getText().contains(".")) {
                return;
            }
            display.setText(display.getText() + num);
        }
    }

    private void processOperator(String operator) {
        if (!currentOperator.isEmpty()) {
            calculateResult();
        }
        try {
            firstOperand = Double.parseDouble(display.getText());
        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNewNumber = true;
            return;
        }
        currentOperator = operator;
        startNewNumber = true;
    }

    private void calculateResult() {
        if (currentOperator.isEmpty()) {
            return;
        }
        double secondOperand;
        try {
            secondOperand = Double.parseDouble(display.getText());
        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNewNumber = true;
            return;
        }
        double result = 0;
        switch (currentOperator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "*":
                result = firstOperand * secondOperand;
                break;
            case "÷":
            case "/":
                if (secondOperand == 0) {
                    display.setText("Cannot divide by zero");
                    startNewNumber = true;
                    currentOperator = "";
                    return;
                } else {
                    result = firstOperand / secondOperand;
                }
                break;
            case "^":
                result = Math.pow(firstOperand, secondOperand);
                break;
            default:
                display.setText("Error");
                startNewNumber = true;
                currentOperator = "";
                return;
        }
        display.setText(formatNumber(result));
        startNewNumber = true;
        currentOperator = "";
        firstOperand = result;
    }

    private void processScientificFunction(String func) {
        double value;
        try {
            value = Double.parseDouble(display.getText());
        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNewNumber = true;
            return;
        }
        double result;
        switch (func) {
            case "sin":
                result = Math.sin(Math.toRadians(value));
                break;
            case "cos":
                result = Math.cos(Math.toRadians(value));
                break;
            case "tan":
                // Handle tan(90) and tan(270) cases with warning
                double mod = value % 180;
                if (Math.abs(mod - 90) < 1e-10) {
                    display.setText("Undefined");
                    startNewNumber = true;
                    return;
                }
                result = Math.tan(Math.toRadians(value));
                break;
            case "log":
                if (value <= 0) {
                    display.setText("Error");
                    startNewNumber = true;
                    return;
                }
                result = Math.log10(value);
                break;
            case "ln":
                if (value <= 0) {
                    display.setText("Error");
                    startNewNumber = true;
                    return;
                }
                result = Math.log(value);
                break;
            case "√":
                if (value < 0) {
                    display.setText("Error");
                    startNewNumber = true;
                    return;
                }
                result = Math.sqrt(value);
                break;
            case "x²":
                result = value * value;
                break;
            case "x³":
                result = value * value * value;
                break;
            case "!":
                if (value < 0 || value != (int) value) {
                    display.setText("Error");
                    startNewNumber = true;
                    return;
                }
                result = factorial((int) value);
                break;
            default:
                display.setText("Error");
                startNewNumber = true;
                return;
        }
        display.setText(formatNumber(result));
        startNewNumber = true;
        currentOperator = "";
        firstOperand = result;
    }

    private long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
            if (fact < 0) { // overflow protection
                return -1;
            }
        }
        return fact;
    }

    private String formatNumber(double num) {
        // Formats number to avoid trailing zeros
        if (Double.isNaN(num) || Double.isInfinite(num)) {
            return "Error";
        }
        DecimalFormat df = new DecimalFormat("0.######");
        return df.format(num);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}

