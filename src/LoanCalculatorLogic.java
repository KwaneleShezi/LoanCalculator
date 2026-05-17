public class LoanCalculatorLogic {
 
    private double principal;
    private double annualRate;
    private int months;
 
    public LoanCalculatorLogic(double principal, double annualRate, int months) {
        this.principal = principal;
        this.annualRate = annualRate;
        this.months = months;
    }
 
    // Simple interest monthly payment (equal split, interest on original principal)
    public double simpleMonthlyPayment() {
        double totalInterest = principal * (annualRate / 100) * (months / 12.0);
        return (principal + totalInterest) / months;
    }
 
    // Compound interest monthly payment using standard amortisation formula
    public double compoundMonthlyPayment() {
        double r = annualRate / 100 / 12;
        if (r == 0) return principal / months;
        return principal * (r * Math.pow(1 + r, months)) / (Math.pow(1 + r, months) - 1);
    }
 
    // Returns a 2D array for the simple interest schedule
    // Columns: Month, Payment, Interest, Balance
    public Object[][] simpleSchedule() {
        Object[][] data = new Object[months][4];
        double monthlyInterest = principal * (annualRate / 100) * (months / 12.0) / months;
        double payment = simpleMonthlyPayment();
        double balance = principal + principal * (annualRate / 100) * (months / 12.0);
 
        for (int i = 0; i < months; i++) {
            balance -= payment;
            data[i][0] = i + 1;
            data[i][1] = String.format("R %.2f", payment);
            data[i][2] = String.format("R %.2f", monthlyInterest);
            data[i][3] = String.format("R %.2f", Math.max(balance, 0));
        }
        return data;
    }
 
    // Returns a 2D array for the compound interest schedule
    public Object[][] compoundSchedule() {
        Object[][] data = new Object[months][4];
        double r = annualRate / 100 / 12;
        double payment = compoundMonthlyPayment();
        double balance = principal;
 
        for (int i = 0; i < months; i++) {
            double interest = balance * r;
            balance = balance - (payment - interest);
            data[i][0] = i + 1;
            data[i][1] = String.format("R %.2f", payment);
            data[i][2] = String.format("R %.2f", interest);
            data[i][3] = String.format("R %.2f", Math.max(balance, 0));
        }
        return data;
    }
 
    public double totalSimple() {
        return simpleMonthlyPayment() * months;
    }
 
    public double totalCompound() {
        return compoundMonthlyPayment() * months;
    }
 
    public double totalSimpleInterest() {
        return totalSimple() - principal;
    }
 
    public double totalCompoundInterest() {
        return totalCompound() - principal;
    }
}