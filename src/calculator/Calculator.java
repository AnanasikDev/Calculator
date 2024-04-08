package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends JFrame {
    private JTextField textField;
    private Solver solver;
    private JPanel mainPanel;
    private JPanel ansPanel; // includes only button ANS
    private JButton ansButton;
    private JPanel keyboardPanel; // includes all keys of calculator, exluding ANS
    private JPanel buttonPanel; // includes keyboard panel and ansPanel
    private JPanel historyPanel;
    private int historyFontSize = 25;
    private final String fontName = "Arial";
    private final int keyboardPadding = 2;

    private List<HistoryToken> history = new ArrayList<>();

    private List<CalculatorButton> calculatorButtons = new ArrayList<>();

    private void UpdateOnWindowResize(){
        ansPanel.setPreferredSize(new Dimension(keyboardPanel.getWidth() / 5 - keyboardPadding, keyboardPanel.getHeight() - keyboardPadding));
        int ansFont = (int) (mainPanel.getWidth() * 0.025);
        ansButton.setPreferredSize(new Dimension(ansPanel.getWidth(), ansPanel.getHeight()));
        ansButton.setFont(new Font(fontName, Font.PLAIN, ansFont));

        for (CalculatorButton jButton : calculatorButtons){
            int newSize = (int) (mainPanel.getWidth() * 0.06);
            jButton.getButton().setFont(new Font(fontName, Font.PLAIN, newSize));
        }

        textField.requestFocus();
        adjustHistoryPanel();
    }
    private void UpdateOnHistoryResize(){
        UpdateOnWindowResize();
    }
    private void InitTextField(){
        textField = new JTextField();
        textField.setEditable(true);
        mainPanel.add(textField);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    evaluate();
                }
            }
        });

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                textField.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight() / 5));
                int newSize = (int) (mainPanel.getWidth() * 0.06);
                textField.setFont(new Font(fontName, Font.PLAIN, newSize));
            }
        });
    }
    private void InitAnsPanel(){
        ansPanel = new JPanel(new GridLayout(1, 1));
        buttonPanel.add(ansPanel, BorderLayout.WEST);
        ansButton = new JButton("ANS");
        ansButton.setBackground(new Color(100, 150, 200));
        ansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.requestFocus();
            }
        });

        ansPanel.setBackground(Color.black);
        ansPanel.add(ansButton);
    }
    private void InitButtonPanel(){
        keyboardPanel = new JPanel();

        // MAIN PANEL grid layout settings
        keyboardPanel.setLayout(new GridLayout(7, 4, keyboardPadding, keyboardPadding));

        List<CalculatorButton> buttons = getCalculatorButtons();
        for (CalculatorButton button : buttons) {
            JButton jButton = new JButton(button.getTitle());
            button.setButton(jButton);
            calculatorButtons.add(button);
            jButton.setFont(new Font(fontName, Font.PLAIN, 20));

            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textField.requestFocus();
                }
            });

            jButton.setBackground(button.getBackgroundColor());
            jButton.addActionListener(new ButtonClickListener(button));
            keyboardPanel.add(jButton);
        }
        buttonPanel.add(keyboardPanel, BorderLayout.EAST);
    }
    private void InitHistory(){
        historyPanel = new JPanel();
        historyPanel.setBackground(Color.LIGHT_GRAY);
        historyPanel.setPreferredSize(new Dimension(50, getHeight()));
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));

        historyPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { UpdateOnHistoryResize(); }
        });
    }
    public Calculator() {
        setTitle("Calculator 3000");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        InitTextField();

        buttonPanel = new JPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        InitAnsPanel();

        InitButtonPanel();

        ansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText(textField.getText() + (history.isEmpty() ? "" : history.get(history.size()-1).getValue()));
            }
        });

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                UpdateOnWindowResize();}
        });
        add(mainPanel);

        InitHistory();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, historyPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(getWidth() - 50);
        splitPane.setDividerSize(6);

        mainPanel.setPreferredSize(new Dimension(750, 600));
        historyPanel.setPreferredSize(new Dimension(250, 600));

        add(splitPane);

        pack();

        solver = new Solver();

        UpdateOnWindowResize();
    }

    private void evaluate() {
        String expression = textField.getText();
        String value = solver.Calculate(expression);

        AddToHistory(expression, value);

        textField.setText(value);
    }

    private void AddToHistory(String expression, String value) {
        String text = expression + " = " + value;
        JLabel label = new JLabel(text);
        label.setFont(new Font(fontName, Font.PLAIN, historyFontSize));
        historyPanel.add(label);
        historyPanel.updateUI();
        HistoryToken token = new HistoryToken(label, expression, value, text);
        history.add(token);
    }
    private void ClearHistory(){
        for (HistoryToken token : history){
            mainPanel.remove(token.getLabel());
        }
        history.clear();
    }
    private void adjustHistoryPanel(){
        historyFontSize = (int) (mainPanel.getHeight() * 0.0475);
        for (HistoryToken token : history){
            token.getLabel().setFont(new Font(fontName, Font.PLAIN, historyFontSize));
        }
    }

    private List<CalculatorButton> getCalculatorButtons() {
        List<CalculatorButton> buttons = new ArrayList<>();
        buttons.add(new CalculatorButton("1", "1", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("2", "2", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("3", "3", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("+", "+", new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("4", "4", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("5", "5", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("6", "6", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("-", "-", new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("7", "7", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("8", "8", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("9", "9", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("×", "*", new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("C", "C", new Color(117, 117, 117)));
        buttons.add(new CalculatorButton("0", "0", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("=", "=", new Color(217, 137, 91)));
        buttons.add(new CalculatorButton("/", "/", new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("x^y", "^",     new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("x²", "^2",     new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("sqrt", "sqrt", new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("root", "^(1/", new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("(", "(",       new Color(200, 200, 200)));
        buttons.add(new CalculatorButton(")", ")",       new Color(200, 200, 200)));
        buttons.add(new CalculatorButton("π", "pi",      new Color(245, 135, 219)));
        buttons.add(new CalculatorButton("e", "e",       new Color(245, 135, 219)));
        buttons.add(new CalculatorButton("sin", "sin",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("cos", "cos",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("tan", "tan",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("atan", "atan", new Color(239, 245, 135)));
        return buttons;
    }

    private class ButtonClickListener implements ActionListener {
        private final CalculatorButton button;

        public ButtonClickListener(CalculatorButton button) {
            this.button = button;
        }

        public void actionPerformed(ActionEvent e) {
            String command = button.getValue();
            if ("=".equals(command)) {
                evaluate();
            } else if ("C".equals(command)) {
                textField.setText("");
            } else {
                textField.setText(textField.getText() + command);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}

class CalculatorButton {
    private final String title;
    private final String value;
    private final Color backgroundColor;

    private JButton button;

    public CalculatorButton(String title, String value, Color backgroundColor) {
        this.title = title;
        this.value = value;
        this.backgroundColor = backgroundColor;
    }

    public String getTitle() {
        return title;
    }
    public String getValue() {
        return value;
    }
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public void setButton(JButton button) { this.button = button; }
    public JButton getButton() { return button; }
}

class HistoryToken{
    private final JLabel label;
    private final String text;
    private final String value;
    private final String expression;

    public HistoryToken(JLabel label, String expression, String value, String text) {
        this.label = label;
        this.text = text;
        this.value = value;
        this.expression = expression;
    }

    public String getText() { return text; }
    public String getValue() { return value; }
    public String getExpression() { return expression; }
    public JLabel getLabel() { return label; }
}