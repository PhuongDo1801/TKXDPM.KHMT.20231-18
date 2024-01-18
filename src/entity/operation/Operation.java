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
    private int idMedia;
    private int countUpdateValue;

    public Operation (){
    }

    public Operation(int id, Date timeOperation, String type, String description, int value, int idMedia, int countUpdateValue) {
        this.id = id;
        this.timeOperation = timeOperation;
        this.type = type;
        this.description = description;
        this.value = value;
        this.idMedia = idMedia;
        this.countUpdateValue = countUpdateValue;
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

    public int getCountUpdateValue(int idMedia) {
        Date currentTime = new Date();

        Date todayStart = getTodayStart(currentTime);
        Date todayEnd = getTodayEnd(currentTime);

        String sumSql = "SELECT MAX(countUpdateValue) FROM Operation WHERE timeOperation >= ? AND timeOperation <= ? AND type = 'UPDATE' AND idMedia = ?;";
        Connection connection = AIMSDB.getConnection();
        try (PreparedStatement countStatement = connection.prepareStatement(sumSql)) {

            countStatement.setTimestamp(1, new Timestamp(todayStart.getTime()));
            countStatement.setTimestamp(2, new Timestamp(todayEnd.getTime()));
            countStatement.setInt(3, idMedia);

            try (ResultSet resultSet = countStatement.executeQuery()) {
                resultSet.next();
                int updateCount = resultSet.getInt(1);
                System.out.println("update count " + updateCount);

                return updateCount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int checkValidDelete(int value) throws SQLException {
        Date currentTime = new Date();

        java.sql.Date sqlDate = new java.sql.Date(currentTime.getTime());

        Date todayStart = getTodayStart(currentTime);
        Date todayEnd = getTodayEnd(currentTime);

        // Thực hiện truy vấn để đếm số lượng sản phẩm đã xóa trong ngày
        String sumSql = "SELECT SUM(value) FROM Operation WHERE timeOperation >= ? AND timeOperation <= ? AND type = 'DELETE';";
        try (PreparedStatement countStatement = AIMSDB.getConnection().prepareStatement(sumSql)) {

            countStatement.setTimestamp(1, new Timestamp(todayStart.getTime()));
            countStatement.setTimestamp(2, new Timestamp(todayEnd.getTime()));

            try (ResultSet resultSet = countStatement.executeQuery()) {
                resultSet.next();
                int deleteCount = resultSet.getInt(1);

                if (deleteCount < 30 && deleteCount + value <= 30) {
                    insertDeleteOperation(sqlDate, value);
                    return 1;
                } else if (deleteCount < 30 && deleteCount + value > 30) {
                    return 2;
                } else if (deleteCount >= 30) {
                    return 3;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }


    public int checkValidUpdate(int value, int idMedia, boolean same) throws SQLException {
        Date currentTime = new Date();

        java.sql.Date sqlDate = new java.sql.Date(currentTime.getTime());

        Date todayStart = getTodayStart(currentTime);
        Date todayEnd = getTodayEnd(currentTime);

//         Thực hiện truy vấn để đếm số lượng sản phẩm đã cập nhật trong ngày
        String sumSql = "SELECT SUM(value) FROM Operation WHERE timeOperation >= ? AND timeOperation <= ? AND type = 'UPDATE';";
        Connection connection = AIMSDB.getConnection();
        try (PreparedStatement countStatement = connection.prepareStatement(sumSql)) {

            countStatement.setTimestamp(1, new Timestamp(todayStart.getTime()));
            countStatement.setTimestamp(2, new Timestamp(todayEnd.getTime()));

            try (ResultSet resultSet = countStatement.executeQuery()) {
                resultSet.next();
                int updateCount = resultSet.getInt(1);
                int checkValidUpdateValue = checkValidUpdateValue(idMedia);
                if(same) {
                    checkValidUpdateValue = 1;
                }
                System.out.println("checkvalidupdate value" + checkValidUpdateValue);

                if (updateCount < 30 && updateCount + value <= 30 && checkValidUpdateValue == 1) {
//                    insertUpdateOperation(sqlDate, value, idMedia);
                    return 1;
                } else if (updateCount < 30 && updateCount + value > 30) {
                    return 2;
                } else if (updateCount >= 30) {
                    return 3;
                } else if (checkValidUpdateValue == 2) {
                    return 4;
                }
            }
        }

        return -1;
    }

    public int checkValidUpdateValue(int idMedia) {
        Date currentTime = new Date();

        Date todayStart = getTodayStart(currentTime);
        Date todayEnd = getTodayEnd(currentTime);

        String sumSql = "SELECT SUM(countUpdateValue) FROM Operation WHERE timeOperation >= ? AND timeOperation <= ? AND type = 'UPDATE' AND idMedia = ?;";
        Connection connection = AIMSDB.getConnection();
        try (PreparedStatement countStatement = connection.prepareStatement(sumSql)) {

            countStatement.setTimestamp(1, new Timestamp(todayStart.getTime()));
            countStatement.setTimestamp(2, new Timestamp(todayEnd.getTime()));
            countStatement.setInt(3, idMedia);

            try (ResultSet resultSet = countStatement.executeQuery()) {
                resultSet.next();
                int updateCount = resultSet.getInt(1);
                System.out.println("update count");
                System.out.println(updateCount);
                if (updateCount < 2) {
                    return 1;
                } else {
                    return 2;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void insertDeleteOperation(java.sql.Date sqlDate, int value) throws SQLException {
        String insertSql = "INSERT INTO Operation (timeOperation, type, value) VALUES (?, 'DELETE', ?);";
        try (PreparedStatement insertStatement = AIMSDB.getConnection().prepareStatement(insertSql)) {
            insertStatement.setTimestamp(1, new Timestamp(sqlDate.getTime()));
            insertStatement.setInt(2, value);
            insertStatement.executeUpdate();
        }
    }

    public boolean insertUpdateOperation(int value, int idMedia, int countUpdateValue) throws SQLException {
        Date currentTime = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentTime.getTime());

        String insertSql = "INSERT INTO Operation (timeOperation, type, value, idMedia, countUpdateValue) VALUES (?, 'UPDATE', ?, ?, ?);";
        try (PreparedStatement insertStatement = AIMSDB.getConnection().prepareStatement(insertSql)) {
            insertStatement.setTimestamp(1, new Timestamp(sqlDate.getTime()));
            insertStatement.setInt(2, value);
            insertStatement.setInt(3, idMedia);
            insertStatement.setInt(4, countUpdateValue);
            insertStatement.executeUpdate();
            insertStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

//    private void insertUpdateOperation(java.sql.Date sqlDate, int value, int idMedia) throws SQLException {
//        String insertSql = "INSERT INTO Operation (timeOperation, type, value, idMedia) VALUES (?, 'UPDATE', ?, ?);";
//        System.out.println("chon insert 2");
//        try (Connection connection = AIMSDB.getConnection();
//             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
//            System.out.println("chon insert 3");
//            insertStatement.setTimestamp(1, new Timestamp(sqlDate.getTime()));
//            insertStatement.setInt(2, value);
//            insertStatement.setInt(3, idMedia);
//            System.out.println("chon insert 4 ");
//            insertStatement.executeUpdate();
//        }
//    }


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

    public int getIdMedia() {
        return idMedia;
    }

    public Operation setIdMedia(int idMedia) {
        this.idMedia = idMedia;
        return this;
    }

    public int getCountUpdateValue() {
        return countUpdateValue;
    }

    public Operation setCountUpdateValue(int countUpdateValue) {
        this.countUpdateValue = countUpdateValue;
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
                ", idMedia=" + idMedia +
                ", countUpdateValue=" + countUpdateValue +
                '}';
    }
}
