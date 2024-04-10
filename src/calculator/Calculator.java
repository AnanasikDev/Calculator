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
    private JPanel keyboardPanel; // includes all keys of calculator, exluding ANS
    private JPanel buttonPanel; // includes keyboard panel and ansPanel
    private JPanel historyPanel;
    private int historyFontSize = 25;
    private final String fontName = "Arial";
    private final int keyboardPadding = 2;

    private List<HistoryToken> history = new ArrayList<>();

    private List<CalculatorButton> calculatorButtons = new ArrayList<>();

    private final int columns = 4;
    private int getRows() { return (int)Math.ceil(calculatorButtons.size() / (float)columns);};

    private void UpdateOnWindowResize(){
        System.out.println(mainPanel.getWidth());
        if (mainPanel.getWidth() < 500){
            historyPanel.setPreferredSize(new Dimension(0, mainPanel.getHeight()));
            historyPanel.setVisible(false);
        }
        else{
            historyPanel.setVisible(true);
        }

        for (CalculatorButton jButton : calculatorButtons){
            int newSize = (int) (Math.pow(mainPanel.getWidth() + mainPanel.getHeight(), 0.45));
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
        textField.setHorizontalAlignment(0);
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
    private void InitButtonPanel(){
        keyboardPanel = new JPanel();

        // MAIN PANEL grid layout settings
        //keyboardPanel.setLayout(new GridLayout(getRows(), columns, keyboardPadding, keyboardPadding));
        keyboardPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        //constraints.gridx = 500;
        //constraints.gridy = 500;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill =  GridBagConstraints.CENTER;
        //constraints.fill = GridBagConstraints.HORIZONTAL;
        /*constraints.weightx = 0.1;
        constraints.weighty = 0.1;*/

        List<CalculatorButton> buttons = getCalculatorButtons();
        int i = 0;
        int columns = 5;
        for (CalculatorButton button : buttons) {
            button.setButton();
            JButton jButton = button.getButton();
            button.setSize();
            calculatorButtons.add(button);
            jButton.setFont(new Font(fontName, Font.PLAIN, 16));

            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textField.requestFocus();
                }
            });

            jButton.addActionListener(new ButtonClickListener(button, button.isActionButton));
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 0.5;
            constraints.gridx = i % columns;
            constraints.gridy = i / columns;
            constraints.ipadx = 0;
            keyboardPanel.add(jButton, constraints);

            i++;
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

        solver = new Solver();

        mainPanel = new JPanel();
        InitTextField();

        buttonPanel = new JPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        InitButtonPanel();

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
        buttons.add(new CalculatorButton("=", "", new Color(217, 137, 91), true));
        buttons.getLast().jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evaluate();
            }
        });

        buttons.add(new CalculatorButton("C", "", new Color(117, 117, 117), true));
        buttons.getLast().jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
            }
        });

        buttons.add(new CalculatorButton("del", "", new Color(117, 117, 117), true));
        buttons.getLast().jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText(textField.getText().isEmpty() ? "" : textField.getText().substring(0, textField.getText().length()-1) );
            }
        });

        buttons.add(new CalculatorButton("ans", "", new Color(117, 117, 117), true));
        buttons.getLast().jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText(textField.getText() + (history.isEmpty() ? "" : history.get(history.size()-1).getValue()));
            }
        });

        buttons.add(new CalculatorButton("last", "", new Color(117, 117, 117), true));
        buttons.getLast().jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText(textField.getText() + (history.isEmpty() ? "" : history.get(history.size()-1).getExpression()));
            }
        });

        buttons.add(new CalculatorButton(solver.degMod ? "deg" : "rad", "", new Color(117, 117, 117), true));
        buttons.getLast().jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solver.setDegMod(!solver.degMod);
                ((JButton)(e.getSource())).setText(solver.degMod ? "deg" : "rad");
            }
        });

        buttons.add(new CalculatorButton("1", "1", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("2", "2", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("3", "3", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("+", "+", new Color(150, 136, 186)));

        buttons.add(new CalculatorButton("(", "(", new Color(200, 200, 200)));
        buttons.add(new CalculatorButton("4", "4", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("5", "5", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("6", "6", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("-", "-", new Color(150, 136, 186)));

        buttons.add(new CalculatorButton(")", ")", new Color(200, 200, 200)));
        buttons.add(new CalculatorButton("7", "7", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("8", "8", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("9", "9", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("×", "*", new Color(150, 136, 186)));

        buttons.add(new CalculatorButton("!", "!", new Color(200, 200, 200)));
        buttons.add(new CalculatorButton(".", ".", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("0", "0", new Color(135, 222, 184)));
        buttons.add(new CalculatorButton("-", "", new Color(135, 222, 184), true));
        buttons.getLast().jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("-(" + textField.getText() + ")");
            }
        });

        buttons.add(new CalculatorButton("/", "/", new Color(150, 136, 186)));

        buttons.add(new CalculatorButton("π", "pi",new Color(245, 135, 219)));
        buttons.add(new CalculatorButton("x^y", "^",     new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("x²", "^2",     new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("sqrt", "sqrt(", new Color(150, 136, 186)));
        buttons.add(new CalculatorButton("root", "^(1/", new Color(150, 136, 186)));

        buttons.add(new CalculatorButton("e", "e",       new Color(245, 135, 219)));
        buttons.add(new CalculatorButton("sin", "sin",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("cos", "cos",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("tan", "tan",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("ctg", "ctg", new Color(239, 245, 135)));

        buttons.add(new CalculatorButton("g", "[g]",       new Color(245, 135, 219)));
        buttons.add(new CalculatorButton("asin", "asin",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("acos", "acos",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("atan", "atan",   new Color(239, 245, 135)));
        buttons.add(new CalculatorButton("actg", "actg", new Color(239, 245, 135)));

        buttons.add(new CalculatorButton("φ", "[phi]",       new Color(245, 135, 219)));
        buttons.add(new CalculatorButton("ln", "ln", new Color(135, 245, 179)));
        buttons.add(new CalculatorButton("log10", "log10", new Color(135, 245, 179)));
        buttons.add(new CalculatorButton("log2", "log2", new Color(135, 245, 179)));
        buttons.add(new CalculatorButton("log", "log(_, _)", new Color(135, 245, 179)));

        return buttons;
    }

    private class ButtonClickListener implements ActionListener {
        private final CalculatorButton button;
        public boolean isActionButton = false;

        public ButtonClickListener(CalculatorButton button, boolean isActionButton) {
            this.button = button;
            this.isActionButton = isActionButton;
        }

        public void actionPerformed(ActionEvent e) {
            String command = button.getValue();
            if (!isActionButton)
                textField.setText(textField.getText() + command);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
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