import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
 
public class LoanCalculatorPanel extends JPanel {
 
    private JTextField principalField, rateField, monthsField;
    private JLabel simplePaymentLabel, simpleTotalLabel, simpleInterestLabel;
    private JLabel compoundPaymentLabel, compoundTotalLabel, compoundInterestLabel;
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> scheduleType;
 
    public LoanCalculatorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(650, 550));
 
        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildSummaryPanel(), BorderLayout.CENTER);
        add(buildTablePanel(), BorderLayout.SOUTH);
    }
 
    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Loan Details"));
 
        principalField = new JTextField();
        rateField = new JTextField();
        monthsField = new JTextField();
 
        panel.add(new JLabel("Principal Amount (R):"));
        panel.add(principalField);
        panel.add(new JLabel("Annual Interest Rate (%):"));
        panel.add(rateField);
        panel.add(new JLabel("Loan Term (months):"));
        panel.add(monthsField);
 
        JButton calculateBtn = new JButton("Calculate");
        calculateBtn.addActionListener(e -> calculate());
        panel.add(new JLabel());
        panel.add(calculateBtn);
 
        return panel;
    }
 
    private JPanel buildSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
 
        // Simple interest summary
        JPanel simple = new JPanel(new GridLayout(3, 1));
        simple.setBorder(BorderFactory.createTitledBorder("Simple Interest"));
        simplePaymentLabel  = new JLabel("Monthly Payment: -");
        simpleTotalLabel    = new JLabel("Total Repayment: -");
        simpleInterestLabel = new JLabel("Total Interest: -");
        simple.add(simplePaymentLabel);
        simple.add(simpleTotalLabel);
        simple.add(simpleInterestLabel);
 
        // Compound interest summary
        JPanel compound = new JPanel(new GridLayout(3, 1));
        compound.setBorder(BorderFactory.createTitledBorder("Compound Interest"));
        compoundPaymentLabel  = new JLabel("Monthly Payment: -");
        compoundTotalLabel    = new JLabel("Total Repayment: -");
        compoundInterestLabel = new JLabel("Total Interest: -");
        compound.add(compoundPaymentLabel);
        compound.add(compoundTotalLabel);
        compound.add(compoundInterestLabel);
 
        panel.add(simple);
        panel.add(compound);
        return panel;
    }
 
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Repayment Schedule"));
 
        // Dropdown to switch between simple and compound schedule
        scheduleType = new JComboBox<>(new String[]{"Simple Interest", "Compound Interest"});
        scheduleType.addActionListener(e -> calculate());
        panel.add(scheduleType, BorderLayout.NORTH);
 
        String[] columns = {"Month", "Payment", "Interest", "Remaining Balance"};
        tableModel = new DefaultTableModel(columns, 0);
        scheduleTable = new JTable(tableModel);
        scheduleTable.setEnabled(false);
 
        JScrollPane scroll = new JScrollPane(scheduleTable);
        scroll.setPreferredSize(new Dimension(600, 200));
        panel.add(scroll, BorderLayout.CENTER);
 
        return panel;
    }
 
    private void calculate() {
        try {
            double principal = Double.parseDouble(principalField.getText().trim());
            double rate      = Double.parseDouble(rateField.getText().trim());
            int months       = Integer.parseInt(monthsField.getText().trim());
 
            if (principal <= 0 || rate < 0 || months <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter valid positive values.");
                return;
            }
 
            LoanCalculatorLogic logic = new LoanCalculatorLogic(principal, rate, months);
 
            // Update summary labels
            simplePaymentLabel .setText(String.format("Monthly Payment: R %.2f", logic.simpleMonthlyPayment()));
            simpleTotalLabel   .setText(String.format("Total Repayment: R %.2f", logic.totalSimple()));
            simpleInterestLabel.setText(String.format("Total Interest:  R %.2f", logic.totalSimpleInterest()));
 
            compoundPaymentLabel .setText(String.format("Monthly Payment: R %.2f", logic.compoundMonthlyPayment()));
            compoundTotalLabel   .setText(String.format("Total Repayment: R %.2f", logic.totalCompound()));
            compoundInterestLabel.setText(String.format("Total Interest:  R %.2f", logic.totalCompoundInterest()));
 
            // Load the selected schedule into the table
            tableModel.setRowCount(0);
            Object[][] data = scheduleType.getSelectedIndex() == 0
                    ? logic.simpleSchedule()
                    : logic.compoundSchedule();
 
            for (Object[] row : data) tableModel.addRow(row);
 
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields with numbers.");
        }
    }
}