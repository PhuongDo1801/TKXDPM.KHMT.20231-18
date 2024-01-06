package entity.media;

import entity.db.AIMSDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

public class CD extends Media {

    String artist;
    String recordLabel;
    String musicType;
    Date releasedDate;

    public CD() throws SQLException{

    }

    public CD(int id, String title, String category, int price, int quantity, String type, int value,String artist,
            String recordLabel, String musicType, Date releasedDate) throws SQLException{
        super(id, title, category, price, quantity, type, value);
        this.artist = artist;
        this.recordLabel = recordLabel;
        this.musicType = musicType;
        this.releasedDate = releasedDate;
    }

    public String getArtist() {
        return this.artist;
    }

    public CD setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getRecordLabel() {
        return this.recordLabel;
    }

    public CD setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
        return this;
    }

    public String getMusicType() {
        return this.musicType;
    }

    public CD setMusicType(String musicType) {
        this.musicType = musicType;
        return this;
    }

    public Date getReleasedDate() {
        return this.releasedDate;
    }

    public CD setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
        return this;
    }

    @Override
    public String toString() {
        return "{" + super.toString() + " artist='" + artist + "'" + ", recordLabel='" + recordLabel + "'"
                + "'" + ", musicType='" + musicType + "'" + ", releasedDate='"
                + releasedDate + "'" + "}";
    }

    @Override
    public Media getMediaById(int id) throws SQLException {
        String sql = "SELECT * FROM "+
                     "aims.CD " +
                     "INNER JOIN aims.Media " +
                     "ON Media.id = CD.id " +
                     "where Media.id = " + id + ";";
        ResultSet res = stm.executeQuery(sql);
		if(res.next()) {
            
            // from media table
            String title = "";
            String type = res.getString("type");
            int price = res.getInt("price");
            String category = res.getString("category");
            int quantity = res.getInt("quantity");
            int value = res.getInt("value");

            // from CD table
            String artist = res.getString("artist");
            String recordLabel = res.getString("recordLabel");
            String musicType = res.getString("musicType");
            Date releasedDate = res.getDate("releasedDate");
           
            return new CD(id, title, category, price, quantity, type, value,
                          artist, recordLabel, musicType, releasedDate);
            
		} else {
			throw new SQLException();
		}
    }

    @Override
    public List getAllMedia() {
        return null;
    }

    public boolean createCD(CD cd, int idMedia) throws SQLException {
        String sql = "INSERT INTO CD (artist, recordLabel, musicType, releasedDate, id) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            java.sql.Date sqlReleasedDate = new java.sql.Date(cd.getReleasedDate().getTime());
            preparedStatement.setString(1, cd.getArtist());
            preparedStatement.setString(2, cd.getRecordLabel());
            preparedStatement.setString(3, cd.getMusicType());
            preparedStatement.setDate(4, sqlReleasedDate);
            preparedStatement.setInt(5, idMedia);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
//                    book.set(generatedId);
//                    return true;
                }
                return true;
            }
        }
        return false;
    }

    public CD getCDById(int idMedia) throws SQLException {
        String sql = "SELECT * FROM CD WHERE id = ?;";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, idMedia);
            ResultSet res = preparedStatement.executeQuery();

            if(res.next()) {
                return new CD()
                        .setArtist(res.getString("artist"))
                        .setRecordLabel(res.getString("recordLabel"))
                        .setMusicType(res.getString("musicType"))
                        .setReleasedDate(res.getDate("releasedDate"));
            }
        }

        return null;
    }

    public boolean updateCDById(int idMedia, CD cd) throws SQLException {
        String updateSql = "UPDATE CD SET artist = ?, recordLabel = ?, musicType = ?, releasedDate = ? WHERE id = ?;";
        try (PreparedStatement updateStatement = AIMSDB.getConnection().prepareStatement(updateSql)) {
            updateStatement.setString(1, cd.getArtist());
            updateStatement.setString(2, cd.getRecordLabel());
            updateStatement.setString(3, cd.getMusicType());
            java.util.Date utilDate = cd.getReleasedDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            updateStatement.setDate(4, sqlDate);
            updateStatement.setInt(5, idMedia);

            int rowsAffected = updateStatement.executeUpdate();
            return rowsAffected > 0;
        }

    }

}
