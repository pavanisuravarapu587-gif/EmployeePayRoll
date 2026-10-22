package payroll.model;

/** Model class (JavaBean) holding employee data and the salary rules. */
public class Employee {
    private int id;
    private String name, department, designation, gender;
    private double basicSalary;
    private boolean bonusEligible;
    private int bonusPercent;

    // ---- salary rules ----
    public double getHra()   { return basicSalary * 0.20; }   // House rent allowance 20%
    public double getDa()    { return basicSalary * 0.10; }   // Dearness allowance 10%
    public double getBonus() { return bonusEligible ? basicSalary * bonusPercent / 100.0 : 0; }
    public double getGross() { return basicSalary + getHra() + getDa() + getBonus(); }
    public double getPf()    { return basicSalary * 0.12; }   // Provident fund 12% of basic
    public double getTax() {
        double g = getGross();
        if (g > 50000) return g * 0.10;
        if (g > 25000) return g * 0.05;
        return 0;
    }
    public double getNetSalary() { return getGross() - getPf() - getTax(); }

    public String getPayslip() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== PAYSLIP ==========\n");
        sb.append(String.format("Employee ID  : %d%n", id));
        sb.append(String.format("Name         : %s%n", name));
        sb.append(String.format("Department   : %s%n", department));
        sb.append(String.format("Designation  : %s%n", designation));
        sb.append("-----------------------------\n");
        sb.append(String.format("Basic        : %10.2f%n", basicSalary));
        sb.append(String.format("HRA (20%%)    : %10.2f%n", getHra()));
        sb.append(String.format("DA (10%%)     : %10.2f%n", getDa()));
        sb.append(String.format("Bonus (%d%%)   : %10.2f%n", bonusEligible ? bonusPercent : 0, getBonus()));
        sb.append(String.format("GROSS        : %10.2f%n", getGross()));
        sb.append("-----------------------------\n");
        sb.append(String.format("PF (12%%)     : %10.2f%n", getPf()));
        sb.append(String.format("Income Tax   : %10.2f%n", getTax()));
        sb.append("-----------------------------\n");
        sb.append(String.format("NET PAY      : %10.2f%n", getNetSalary()));
        return sb.toString();
    }

    // ---- getters / setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public boolean isBonusEligible() { return bonusEligible; }
    public void setBonusEligible(boolean bonusEligible) { this.bonusEligible = bonusEligible; }
    public int getBonusPercent() { return bonusPercent; }
    public void setBonusPercent(int bonusPercent) { this.bonusPercent = bonusPercent; }
}
