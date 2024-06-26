package calculator;

import javax.swing.*;
import java.awt.*;

class CalculatorButton {
    private final String title;
    private final String value;
    private final Color backgroundColor;
    public boolean isActionButton = false;

    public final JButton jButton = new JButton();

    private final Dimension defaultSize = new Dimension(130, 70);
    private Dimension size;

    public CalculatorButton(String title, String value, Color backgroundColor) {
        this.title = title;
        this.value = value;
        this.backgroundColor = backgroundColor;
        setButton();
    }
    public CalculatorButton(String title, String value, Color backgroundColor, boolean isActionButton) {
        this.title = title;
        this.value = value;
        this.backgroundColor = backgroundColor;
        this.isActionButton = isActionButton;
        setButton();
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
    public void setButton()
    {
        jButton.setText(this.title);
        jButton.setBackground(getBackgroundColor());
    }
    public void setSize(){
        setSize(defaultSize);
    }
    public void setSize(Dimension size){
        this.size = size;
        jButton.setPreferredSize(size);
    }

    public JButton getButton() { return jButton; }
}