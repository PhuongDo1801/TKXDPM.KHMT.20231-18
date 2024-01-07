package entity.operation ;

import entity.db.AIMSDB;
import entity.media.Media;
import entity.order.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Operation  {
    private int id;
    private Date timeOperation;
    private String type;
    private String description;

    private int value;

    
    public Operation (){

    }

    public Operation(int id, Date timeOperation, String type, String description, int value) {
        this.id = id;
        this.timeOperation = timeOperation;
        this.type = type;
        this.description = description;
        this.value = value;
    }

    public List getAllOperation() throws SQLException{
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("select * from Operation");
        ArrayList medium = new ArrayList<>();
        while (res.next()) {
            Operation operation = new Operation()
                    .setId(res.getInt("id"))
                    .setTimeOperation(res.getDate("timeOperation"))
                    .setType(res.getString("type"))
                    .setDescription(res.getString("description"))
                    .setValue(res.getInt("value"));
            medium.add(operation);
        }
        return medium;
    }

    public int checkValidDelete(int value, int idMedia) throws SQLException {
        Date currentTime = new Date();

        java.sql.Date sqlDate = new java.sql.Date(currentTime.getTime());

        Date todayStart = getTodayStart(currentTime);
        Date todayEnd = getTodayEnd(currentTime);

//         Thực hiện truy vấn để đếm số lượng sản phẩm đã xóa trong ngày
        String sumSql = "SELECT SUM(value) FROM Operation WHERE timeOperation >= ? AND timeOperation <= ? AND type = 'DELETE';";
        try (
             PreparedStatement countStatement = AIMSDB.getConnection().prepareStatement(sumSql)) {

            countStatement.setTimestamp(1, new Timestamp(todayStart.getTime()));
            countStatement.setTimestamp(2, new Timestamp(todayEnd.getTime()));

            ResultSet resultSet = countStatement.executeQuery();
            System.out.println(resultSet);
            countStatement.close();
            if (resultSet.next()) {
                int deleteCount = resultSet.getInt(1);
                countStatement.close();
                resultSet.close();
                if (deleteCount < 30 && deleteCount + value <= 30) {
                    insertDeleteOperation(sqlDate, value);
                    return 1;
                } else if (deleteCount < 30 && deleteCount + value > 30) {
                    return 2;
                } else if (deleteCount >= 30) {
                    return 3;
                }
            }

        }
        catch (SQLException e) {
            System.out.println(e);
            System.out.println("co loi 2");
            e.printStackTrace();
            return -1;
        }
        System.out.println("co loi 3");
        return 1;
//        return -1;
    }

    public int checkValidUpdate(int value, int idMedia) throws SQLException {
        Date currentTime = new Date();

        java.sql.Date sqlDate = new java.sql.Date(currentTime.getTime());

        Date todayStart = getTodayStart(currentTime);
        Date todayEnd = getTodayEnd(currentTime);

//         Thực hiện truy vấn để đếm số lượng sản phẩm đã cập nhật trong ngày
        String sumSql = "SELECT SUM(value) FROM Operation WHERE timeOperation >= ? AND timeOperation <= ? AND type = 'UPDATE';";
        Connection connection = AIMSDB.getConnection();
        try (
                PreparedStatement countStatement = connection.prepareStatement(sumSql)) {

            countStatement.setTimestamp(1, new Timestamp(todayStart.getTime()));
            countStatement.setTimestamp(2, new Timestamp(todayEnd.getTime()));

            ResultSet resultSet = countStatement.executeQuery();
            countStatement.close();
            connection.close();
            if (resultSet.next()) {
                int deleteCount = resultSet.getInt(1);
                if (deleteCount < 30 && deleteCount + value <= 30) {
                    insertUpdateOperation(sqlDate, value);
                    return 1;
                } else if (deleteCount < 30 && deleteCount + value > 30) {
                    return 2;
                } else if (deleteCount >= 30) {
                    return 3;
                }
            }
        }

        return -1;
    }

    private void insertDeleteOperation(java.sql.Date sqlDate, int value) throws SQLException {
        String insertSql = "INSERT INTO Operation (timeOperation, type, value) VALUES (?, 'DELETE', ?);";

        try (PreparedStatement insertStatement = AIMSDB.getConnection().prepareStatement(insertSql)) {

            insertStatement.setTimestamp(1, new Timestamp(sqlDate.getTime()));
            insertStatement.setInt(2, value);

            insertStatement.executeUpdate();
        }
    }

    private void insertUpdateOperation(java.sql.Date sqlDate, int value) throws SQLException {
        String insertSql = "INSERT INTO Operation (timeOperation, type, value) VALUES (?, 'UPDATE', ?);";

        try (Connection connection = AIMSDB.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            insertStatement.setTimestamp(1, new Timestamp(sqlDate.getTime()));
            insertStatement.setInt(2, value);

            insertStatement.executeUpdate();
        }
    }


    private boolean hasReachedDeleteLimit(Date currentTime) throws SQLException {
        // Lấy ngày bắt đầu và kết thúc của ngày hiện tại
        Date todayStart = getTodayStart(currentTime);
        Date todayEnd = getTodayEnd(currentTime);

        // Thực hiện truy vấn để đếm số lượng sản phẩm đã xóa trong ngày
//        String countSql = "SELECT COUNT(*) FROM Operation WHERE timeOperation >= ? AND timeOperation <= ? AND type = 'DELETE';";
        String sumSql = "SELECT SUM(value) FROM Operation WHERE timeOperation >= ? AND timeOperation <= ? AND type = 'DELETE';";
        try (Connection connection = AIMSDB.getConnection();
             PreparedStatement countStatement = connection.prepareStatement(sumSql)) {

            countStatement.setTimestamp(1, new Timestamp(todayStart.getTime()));
            countStatement.setTimestamp(2, new Timestamp(todayEnd.getTime()));

            ResultSet resultSet = countStatement.executeQuery();
            if (resultSet.next()) {
                int deleteCount = resultSet.getInt(1);
                return deleteCount < 30;
            }
        }

        return false;
    }

    private Date getTodayStart(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
    }

    private Date getTodayEnd(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), 23, 59, 59);
    }

    public int getId() {
        return id;
    }

    public Operation setId(int id) {
        this.id = id;
        return this;
    }

    public Date getTimeOperation() {
        return timeOperation;
    }

    public Operation setTimeOperation(Date timeOperation) {
        this.timeOperation = timeOperation;
        return this;
    }

    public String getType() {
        return type;
    }

    public Operation setType(String type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Operation setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getValue() {
        return value;
    }

    public Operation setValue(int value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", timeOperation=" + timeOperation +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}
