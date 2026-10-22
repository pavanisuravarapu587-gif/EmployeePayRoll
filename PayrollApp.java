package payroll.gui;

import payroll.dao.EmployeeDAO;
import payroll.model.Employee;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

/**
 * AWT desktop client of the Employee Payroll System.
 *
 * Hierarchy: Object -> Component -> Container -> Window -> Frame (PayrollApp)
 * Event handling: ActionListener, ItemListener, AdjustmentListener (this class),
 *                 MouseAdapter, KeyAdapter, WindowAdapter (anonymous classes).
 */
public class PayrollApp extends Frame implements ActionListener, ItemListener, AdjustmentListener {

    private static final String[] DEPARTMENTS = {"HR", "Finance", "IT", "Sales", "Operations"};

    private final EmployeeDAO dao = new EmployeeDAO();
    private java.util.List<Employee> employees = new java.util.ArrayList<>();

    // Card layout: two "screens" in the centre of the frame
    private final CardLayout cardLayout = new CardLayout();
    private final Panel cardPanel = new Panel(cardLayout);

    // Form components
    private final TextField tfId = new TextField(10);
    private final TextField tfName = new TextField(25);
    private final TextField tfDesig = new TextField(25);
    private final TextField tfBasic = new TextField(15);
    private final Choice chDept = new Choice();
    private final CheckboxGroup cgGender = new CheckboxGroup();
    private final Checkbox rbMale = new Checkbox("Male", cgGender, true);
    private final Checkbox rbFemale = new Checkbox("Female", cgGender, false);
    private final Checkbox cbBonus = new Checkbox("Eligible for bonus");
    private final Scrollbar sbBonus = new Scrollbar(Scrollbar.HORIZONTAL, 10, 5, 0, 35); // value 0..30
    private final Label lblBonus = new Label("10%  ");
    private final Label lblStatus = new Label("Ready");

    private final java.awt.List empList = new java.awt.List(15, false);
    private final TextArea taReport = new TextArea("", 20, 80, TextArea.SCROLLBARS_BOTH);

    public PayrollApp() {
        super("Employee Payroll System");
        setSize(950, 580);
        setLayout(new BorderLayout(8, 8));              // Border layout for the frame
        setBackground(new Color(238, 242, 247));

        setMenuBar(buildMenuBar());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildListPanel(), BorderLayout.WEST);

        cardPanel.add(buildFormCard(), "FORM");
        cardPanel.add(buildReportCard(), "REPORT");
        add(cardPanel, BorderLayout.CENTER);

        lblStatus.setBackground(new Color(220, 228, 238));
        add(lblStatus, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { exit(); }
        });

        refreshList();
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------ UI building

    private MenuBar buildMenuBar() {
        MenuBar mb = new MenuBar();

        Menu file = new Menu("File");
        file.add(item("Refresh"));
        file.addSeparator();
        file.add(item("Exit"));

        Menu emp = new Menu("Employee");
        emp.add(item("Add"));
        emp.add(item("Update"));
        emp.add(item("Delete"));
        emp.add(item("Payslip"));

        Menu view = new Menu("View");
        view.add(item("Employee Form"));
        view.add(item("Payroll Report"));

        Menu help = new Menu("Help");
        help.add(item("About"));

        mb.add(file);
        mb.add(emp);
        mb.add(view);
        mb.add(help);
        return mb;
    }

    private MenuItem item(String label) {
        MenuItem m = new MenuItem(label);
        m.setActionCommand(label);
        m.addActionListener(this);
        return m;
    }

    private Panel buildHeader() {
        Panel p = new Panel(new FlowLayout(FlowLayout.LEFT, 12, 8));   // Flow layout
        Label title = new Label("EMPLOYEE PAYROLL SYSTEM");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        p.add(title);
        p.add(button("Employee Form"));
        p.add(button("Payroll Report"));
        return p;
    }

    private Panel buildListPanel() {
        Panel p = new Panel(new BorderLayout(0, 4));
        p.add(new Label("Employees (double-click to edit)"), BorderLayout.NORTH);
        p.add(empList, BorderLayout.CENTER);

        // Mouse event: double-click loads the selected employee into the form
        empList.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) loadSelected();
            }
        });
        return p;
    }

    private Component buildFormCard() {
        Panel grid = new Panel(new GridLayout(0, 2, 10, 12));          // Grid layout

        tfId.setEditable(false);
        for (String d : DEPARTMENTS) chDept.add(d);
        chDept.addItemListener(this);
        cbBonus.addItemListener(this);
        sbBonus.addAdjustmentListener(this);
        sbBonus.setEnabled(false);

        // Keyboard events
        tfBasic.addKeyListener(new KeyAdapter() {                     // digits and '.' only
            @Override public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) e.consume();
            }
        });
        tfName.addKeyListener(new KeyAdapter() {                      // Enter jumps to next field
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) tfDesig.requestFocus();
            }
        });

        Panel genderPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.add(rbMale);
        genderPanel.add(rbFemale);

        Panel bonusPanel = new Panel(new BorderLayout(8, 0));
        bonusPanel.add(sbBonus, BorderLayout.CENTER);
        bonusPanel.add(lblBonus, BorderLayout.EAST);

        grid.add(new Label("Employee ID:"));        grid.add(tfId);
        grid.add(new Label("Name:"));               grid.add(tfName);
        grid.add(new Label("Department:"));         grid.add(chDept);
        grid.add(new Label("Designation:"));        grid.add(tfDesig);
        grid.add(new Label("Gender:"));             grid.add(genderPanel);
        grid.add(new Label("Basic Salary (Rs):"));  grid.add(tfBasic);
        grid.add(new Label("Bonus:"));              grid.add(cbBonus);
        grid.add(new Label("Bonus %:"));            grid.add(bonusPanel);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttons.add(button("Add"));
        buttons.add(button("Update"));
        buttons.add(button("Delete"));
        buttons.add(button("Clear"));
        buttons.add(button("Payslip"));

        Panel wrapper = new Panel(new BorderLayout(0, 20)) {
            @Override public Insets getInsets() { return new Insets(15, 15, 15, 15); }
        };
        wrapper.add(grid, BorderLayout.NORTH);
        wrapper.add(buttons, BorderLayout.CENTER);

        ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);   // Scroll pane
        sp.add(wrapper);
        return sp;
    }

    private Panel buildReportCard() {
        Panel p = new Panel(new BorderLayout(0, 6));
        p.add(new Label("Payroll Report (all employees)"), BorderLayout.NORTH);
        taReport.setEditable(false);
        taReport.setFont(new Font("Monospaced", Font.PLAIN, 12));
        p.add(taReport, BorderLayout.CENTER);
        return p;
    }

    private Button button(String label) {
        final Button b = new Button(label);
        b.setActionCommand(label);
        b.addActionListener(this);
        final Color normal = b.getBackground();
        b.addMouseListener(new MouseAdapter() {                       // Mouse hover effect
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(new Color(173, 216, 230)); }
            @Override public void mouseExited(MouseEvent e)  { b.setBackground(normal); }
        });
        return b;
    }

    // ------------------------------------------------------------------ event handling

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Add":            addEmployee();    break;
            case "Update":         updateEmployee(); break;
            case "Delete":         deleteEmployee(); break;
            case "Clear":          clearForm();      break;
            case "Payslip":        showPayslip();    break;
            case "Refresh":        refreshList(); status("List refreshed"); break;
            case "Employee Form":  cardLayout.show(cardPanel, "FORM"); break;
            case "Payroll Report": buildReport(); cardLayout.show(cardPanel, "REPORT"); break;
            case "About":
                showText("About", "Employee Payroll System\n\nAWT + Event Handling + JDBC\n"
                        + "Web side: Servlets + JSP");
                break;
            case "Exit":           exit(); break;
            default: break;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        if (src == cbBonus) {
            boolean on = cbBonus.getState();
            sbBonus.setEnabled(on);
            status(on ? "Bonus enabled - use the scrollbar to set %" : "Bonus disabled");
        } else if (src == chDept) {
            status("Department: " + chDept.getSelectedItem());
        }
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        lblBonus.setText(sbBonus.getValue() + "%  ");
    }

    // ------------------------------------------------------------------ actions

    private void addEmployee() {
        Employee emp = readForm();
        if (emp == null) return;
        try {
            int id = dao.add(emp);
            refreshList();
            clearForm();
            status("Employee added with ID " + id);
        } catch (SQLException ex) { dbError(ex); }
    }

    private void updateEmployee() {
        if (tfId.getText().isEmpty()) { showMessage("Update", "Double-click an employee in the list first."); return; }
        Employee emp = readForm();
        if (emp == null) return;
        try {
            dao.update(emp);
            refreshList();
            status("Employee " + emp.getId() + " updated");
        } catch (SQLException ex) { dbError(ex); }
    }

    private void deleteEmployee() {
        Employee emp = selected();
        if (emp == null) { showMessage("Delete", "Select an employee in the list first."); return; }
        if (!confirm("Delete " + emp.getName() + " (ID " + emp.getId() + ")?")) return;
        try {
            dao.delete(emp.getId());
            refreshList();
            clearForm();
            status("Employee deleted");
        } catch (SQLException ex) { dbError(ex); }
    }

    private void showPayslip() {
        Employee emp = selected();
        if (emp == null) { showMessage("Payslip", "Select an employee in the list first."); return; }
        showText("Payslip - " + emp.getName(), emp.getPayslip());
    }

    private void refreshList() {
        try {
            employees = dao.findAll();
            empList.removeAll();
            for (Employee e : employees) empList.add(e.getId() + " - " + e.getName());
            status(employees.size() + " employee(s) loaded");
        } catch (SQLException ex) { dbError(ex); }
    }

    private void buildReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-20s %-12s %11s %10s %10s %11s%n",
                "ID", "Name", "Dept", "Gross", "PF", "Tax", "Net Pay"));
        sb.append("-".repeat(86)).append('\n');
        double total = 0;
        for (Employee e : employees) {
            sb.append(String.format("%-4d %-20s %-12s %11.2f %10.2f %10.2f %11.2f%n",
                    e.getId(), e.getName(), e.getDepartment(),
                    e.getGross(), e.getPf(), e.getTax(), e.getNetSalary()));
            total += e.getNetSalary();
        }
        sb.append("-".repeat(86)).append('\n');
        sb.append(String.format("Total net payout: %.2f%n", total));
        taReport.setText(sb.toString());
    }

    // ------------------------------------------------------------------ form helpers

    private Employee selected() {
        int idx = empList.getSelectedIndex();
        return idx < 0 ? null : employees.get(idx);
    }

    private void loadSelected() {
        Employee e = selected();
        if (e == null) return;
        tfId.setText(String.valueOf(e.getId()));
        tfName.setText(e.getName());
        tfDesig.setText(e.getDesignation() == null ? "" : e.getDesignation());
        tfBasic.setText(String.valueOf(e.getBasicSalary()));
        chDept.select(e.getDepartment());
        cgGender.setSelectedCheckbox("Female".equals(e.getGender()) ? rbFemale : rbMale);
        cbBonus.setState(e.isBonusEligible());
        sbBonus.setEnabled(e.isBonusEligible());
        sbBonus.setValue(e.getBonusPercent());
        lblBonus.setText(e.getBonusPercent() + "%  ");
        cardLayout.show(cardPanel, "FORM");
        status("Loaded " + e.getName());
    }

    private Employee readForm() {
        String name = tfName.getText().trim();
        if (name.isEmpty()) { showMessage("Validation", "Name is required."); return null; }

        double basic;
        try {
            basic = Double.parseDouble(tfBasic.getText().trim());
            if (basic <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showMessage("Validation", "Enter a valid basic salary.");
            return null;
        }

        Employee e = new Employee();
        e.setName(name);
        e.setDepartment(chDept.getSelectedItem());
        e.setDesignation(tfDesig.getText().trim());
        e.setGender(cgGender.getSelectedCheckbox().getLabel());
        e.setBasicSalary(basic);
        e.setBonusEligible(cbBonus.getState());
        e.setBonusPercent(cbBonus.getState() ? sbBonus.getValue() : 0);
        if (!tfId.getText().isEmpty()) e.setId(Integer.parseInt(tfId.getText()));
        return e;
    }

    private void clearForm() {
        tfId.setText("");
        tfName.setText("");
        tfDesig.setText("");
        tfBasic.setText("");
        chDept.select(0);
        cgGender.setSelectedCheckbox(rbMale);
        cbBonus.setState(false);
        sbBonus.setValue(10);
        sbBonus.setEnabled(false);
        lblBonus.setText("10%  ");
        tfName.requestFocus();
        status("Form cleared");
    }

    private void status(String msg) { lblStatus.setText(" " + msg); }

    private void dbError(SQLException ex) {
        status("Database error");
        showText("Database Error", ex.getMessage() + "\n\nCheck MySQL is running, schema.sql is loaded,\n"
                + "and the password in DBConnection.java is correct.");
    }

    private void exit() { dispose(); System.exit(0); }

    // ------------------------------------------------------------------ dialogs

    /** Simple modal message dialog. */
    private void showMessage(String title, String msg) {
        final Dialog d = new Dialog(this, title, true);
        d.setLayout(new BorderLayout(10, 10));
        d.add(new Label(msg, Label.CENTER), BorderLayout.CENTER);
        Button ok = new Button("OK");
        ok.addActionListener(ev -> d.dispose());
        Panel p = new Panel(new FlowLayout());
        p.add(ok);
        d.add(p, BorderLayout.SOUTH);
        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { d.dispose(); }
        });
        d.setSize(380, 130);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    /** Modal dialog with multi-line text (payslip, about, errors). */
    private void showText(String title, String text) {
        final Dialog d = new Dialog(this, title, true);
        d.setLayout(new BorderLayout(8, 8));
        TextArea ta = new TextArea(text, 14, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        d.add(ta, BorderLayout.CENTER);
        Button ok = new Button("Close");
        ok.addActionListener(ev -> d.dispose());
        Panel p = new Panel(new FlowLayout());
        p.add(ok);
        d.add(p, BorderLayout.SOUTH);
        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { d.dispose(); }
        });
        d.setSize(430, 380);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    /** Modal Yes/No dialog. */
    private boolean confirm(String msg) {
        final boolean[] result = {false};
        final Dialog d = new Dialog(this, "Confirm", true);
        d.setLayout(new BorderLayout(10, 10));
        d.add(new Label(msg, Label.CENTER), BorderLayout.CENTER);
        Button yes = new Button("Yes");
        Button no = new Button("No");
        yes.addActionListener(ev -> { result[0] = true; d.dispose(); });
        no.addActionListener(ev -> d.dispose());
        Panel p = new Panel(new FlowLayout());
        p.add(yes);
        p.add(no);
        d.add(p, BorderLayout.SOUTH);
        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { d.dispose(); }
        });
        d.setSize(380, 130);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
        return result[0];
    }

    public static void main(String[] args) {
        new PayrollApp().setVisible(true);
    }
}
