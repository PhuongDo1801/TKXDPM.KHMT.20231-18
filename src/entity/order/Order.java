package entity.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.db.AIMSDB;
import entity.user.User;
import utils.Configs;

public class Order {
    private int id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private int userID;

    private int shippingFees;
    private List lstOrderMedia;
    private HashMap<String, String> deliveryInfo;
    private int price;
    private int quantity;

    public Order(){
        this.lstOrderMedia = new ArrayList<>();
    }

    public Order(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public Order(int id, String name, String email, String address, String phone, int userID, int shippingFees, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.userID = userID;
        this.shippingFees = shippingFees;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void addOrderMedia(OrderMedia om){
        this.lstOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om){
        this.lstOrderMedia.remove(om);
    }

    public List getlstOrderMedia() {
        return this.lstOrderMedia;
    }

    public void setlstOrderMedia(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public HashMap getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(HashMap deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", userID=" + userID +
                ", shippingFees=" + shippingFees +
                ", lstOrderMedia=" + lstOrderMedia +
                ", deliveryInfo=" + deliveryInfo +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    public int getAmount(){
        double amount = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }

    public List getAllOrder() throws SQLException {

        List<Order> orders = new ArrayList<>();

        try {
            Statement stm = AIMSDB.getConnection().createStatement();
//            String sql = "SELECT * FROM Order LEFT JOIN User ON Order.userID = User.id LEFT JOIN OrderMedia ON Order.id = OrderMedia.orderID";
            String sql = "SELECT * FROM \"Order\" " +
                    "LEFT JOIN User ON \"Order\".userID = User.id " +
                    "LEFT JOIN OrderMedia ON \"Order\".id = OrderMedia.orderID";

            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                int orderID = rs.getInt(1);
                String name = rs.getString("name");
                String email = rs.getString(2);
                String address = rs.getString(3);
                String phone = rs.getString(4);
                int userID = rs.getInt(5);
                int shippingFees = rs.getInt(6);
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                Order order = new Order(orderID, name, email, address, phone, userID, shippingFees, price, quantity);
                orders.add(order);
            }

            // Đóng kết nối
//            rs.close();
//            stm.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

}
