package payroll.dao;

import payroll.db.DBConnection;
import payroll.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Data Access Object: all SQL lives here (Statement types, ResultSet, CRUD). */
public class EmployeeDAO {

    public int add(Employee e) throws SQLException {
        String sql = "INSERT INTO employee (name, department, designation, gender, basic_salary, "
                   + "bonus_eligible, bonus_percent) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, e);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public boolean update(Employee e) throws SQLException {
        String sql = "UPDATE employee SET name=?, department=?, designation=?, gender=?, basic_salary=?, "
                   + "bonus_eligible=?, bonus_percent=? WHERE emp_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            bind(ps, e);
            ps.setInt(8, e.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM employee WHERE emp_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Employee> findAll() throws SQLException {
        List<Employee> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM employee ORDER BY emp_id")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Employee findById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM employee WHERE emp_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private void bind(PreparedStatement ps, Employee e) throws SQLException {
        ps.setString(1, e.getName());
        ps.setString(2, e.getDepartment());
        ps.setString(3, e.getDesignation());
        ps.setString(4, e.getGender());
        ps.setDouble(5, e.getBasicSalary());
        ps.setBoolean(6, e.isBonusEligible());
        ps.setInt(7, e.getBonusPercent());
    }

    private Employee map(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("emp_id"));
        e.setName(rs.getString("name"));
        e.setDepartment(rs.getString("department"));
        e.setDesignation(rs.getString("designation"));
        e.setGender(rs.getString("gender"));
        e.setBasicSalary(rs.getDouble("basic_salary"));
        e.setBonusEligible(rs.getBoolean("bonus_eligible"));
        e.setBonusPercent(rs.getInt("bonus_percent"));
        return e;
    }
}
