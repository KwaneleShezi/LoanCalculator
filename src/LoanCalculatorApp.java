import javax.swing.*;
 
public class LoanCalculatorApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Loan Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new LoanCalculatorPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
 