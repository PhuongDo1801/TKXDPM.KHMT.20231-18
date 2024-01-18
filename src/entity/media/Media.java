package entity.media;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import entity.db.AIMSDB;
import utils.Utils;

/**
 * The general media class, for another media it can be done by inheriting this class
 * @author nguyenlm
 */
public class Media {

    private static Logger LOGGER = Utils.getLogger(Media.class.getName());

//    protected Statement stm;
    protected int id;
    protected String title;
    protected String category;
    protected int value; // the real price of product (eg: 450)
    protected int price; // the price which will be displayed on browser (eg: 500)
    protected int quantity;
    protected String type;
    protected String imageURL;

    private String mediaName;

    public Media(String mediaName) {
        this.mediaName = mediaName;
    }


    public Media() throws SQLException{
        Statement stm = AIMSDB.getConnection().createStatement();
    }

    public Media (int id, String title, String category, int price, int quantity, String type, int value) throws SQLException{
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.value = value;

        //stm = AIMSDB.getConnection().createStatement();
    }

//    public int getQuantity() throws SQLException{
//        int updated_quantity = getMediaById(id).quantity;
//        this.quantity = updated_quantity;
//        return updated_quantity;
//    }

    public int getQuantity() throws SQLException{
        return this.quantity;
    }

    public Media getMediaById(int id) throws SQLException{
        String sql = "SELECT * FROM Media ;";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
		if(res.next()) {

            return new Media()
                .setId(res.getInt("id"))
                .setTitle(res.getString("title"))
                .setQuantity(res.getInt("quantity"))
                .setCategory(res.getString("category"))
                .setMediaURL(res.getString("imageUrl"))
                .setPrice(res.getInt("price"))
                .setType(res.getString("type"));
        }
        return null;
    }

    public List getAllMedia() throws SQLException{
        ArrayList medium = new ArrayList<>();
        String sql = "select * from Media";
        try(PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql)) {
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                Media media = new Media()
                        .setId(res.getInt("id"))
                        .setTitle(res.getString("title"))
                        .setQuantity(res.getInt("quantity"))
                        .setCategory(res.getString("category"))
                        .setMediaURL(res.getString("imageUrl"))
                        .setPrice(res.getInt("price"))
                        .setType(res.getString("type"))
                        .setValue(res.getInt("value"));
                medium.add(media);
            }
            res.close();
            preparedStatement.close();
        }
        return medium;
    }

    public void updateMediaFieldById(String tbname, int id, String field, Object value) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        if (value instanceof String){
            value = "\"" + value + "\"";
        }
        stm.executeUpdate(" update " + tbname + " set" + " " 
                          + field + "=" + value + " " 
                          + "where id=" + id + ";");
    }

    public int createMedia(Media media) throws SQLException {
        String sql = "INSERT INTO Media (title, quantity, category, imageUrl, price, type, value) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, media.getTitle());
            preparedStatement.setInt(2, media.getQuantity());
            preparedStatement.setString(3, media.getCategory());
            preparedStatement.setString(4, media.getImageURL());
            preparedStatement.setInt(5, media.getPrice());
            preparedStatement.setString(6, media.getType());
            preparedStatement.setInt(7, media.getValue());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    media.setId(generatedId);
                    return generatedId;
                }
            }
        }
        return -1;
    }

//    public boolean deleteMediaById(int id) throws SQLException {
//        String sql = "DELETE FROM Media WHERE id = ?;";
//        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql)) {
//            preparedStatement.setInt(1, id);
//            int rowsAffected = preparedStatement.executeUpdate();
//
//            if (rowsAffected > 0) {
//                return true;
//            }
//        }
//        return false;
//    }


    public boolean deleteMediaById(int id) throws SQLException {
        String sql = "DELETE FROM Media WHERE id = ?;";
        try(PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteMediaByIds(List<Integer> ids) {
        // Tạo chuỗi tham số có độ dài bằng với số lượng phần tử trong danh sách ids
        String params = String.join(",", Collections.nCopies(ids.size(), "?"));

        String sql = "DELETE FROM Media WHERE id IN (" + params + ");";

        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql)) {

            // Thiết lập giá trị cho từng tham số
            for (int i = 0; i < ids.size(); i++) {
                preparedStatement.setInt(i + 1, ids.get(i));
            }

            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean updateMediaById(int id, Media updatedMedia) throws SQLException {
        String sql = "UPDATE Media SET title=?, quantity=?, category=?, imageUrl=?, price=?, type=?, value=? WHERE id=?;";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, updatedMedia.getTitle());
            preparedStatement.setInt(2, updatedMedia.getQuantity());
            preparedStatement.setString(3, updatedMedia.getCategory());
            preparedStatement.setString(4, updatedMedia.getImageURL());
            preparedStatement.setInt(5, updatedMedia.getPrice());
            preparedStatement.setString(6, updatedMedia.getType());
            preparedStatement.setInt(7, updatedMedia.getValue());
            preparedStatement.setInt(8, id);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected > 0;
        }
    }

    // getter and setter 
    public int getId() {
        return this.id;
    }

    private Media setId(int id){
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Media setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    public Media setCategory(String category) {
        this.category = category;
        return this;
    }

    public int getPrice() {
        return this.price;
    }

    public Media setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getImageURL(){
        return this.imageURL;
    }

    public Media setMediaURL(String url){
        this.imageURL = url;
        return this;
    }

    public Media setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Media setType(String type) {
        this.type = type;
        return this;
    }

    public int getValue() {
        return value;
    }

    public Media setValue(int value) {
        this.value = value;
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Media{" +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", price=" + price +
                ", quantity=" + quantity +
                ", type='" + type + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

}