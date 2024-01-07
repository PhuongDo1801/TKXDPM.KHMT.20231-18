package entity.media;

import entity.db.AIMSDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

public class DVD extends Media {

    String discType;
    String director;
    int runtime;
    String studio;
    String subtitles;
    Date releasedDate;
    String filmType;

    public DVD() throws SQLException{

    }

    public DVD(int id, String title, String category, int price, int quantity, String type, int value,String discType,
            String director, int runtime, String studio, String subtitles, Date releasedDate, String filmType) throws SQLException{
        super(id, title, category, price, quantity, type, value);
        this.discType = discType;
        this.director = director;
        this.runtime = runtime;
        this.studio = studio;
        this.subtitles = subtitles;
        this.releasedDate = releasedDate;
        this.filmType = filmType;
    }

    public String getDiscType() {
        return this.discType;
    }

    public DVD setDiscType(String discType) {
        this.discType = discType;
        return this;
    }

    public String getDirector() {
        return this.director;
    }

    public DVD setDirector(String director) {
        this.director = director;
        return this;
    }

    public int getRuntime() {
        return this.runtime;
    }

    public DVD setRuntime(int runtime) {
        this.runtime = runtime;
        return this;
    }

    public String getStudio() {
        return this.studio;
    }

    public DVD setStudio(String studio) {
        this.studio = studio;
        return this;
    }

    public String getSubtitles() {
        return this.subtitles;
    }

    public DVD setSubtitles(String subtitles) {
        this.subtitles = subtitles;
        return this;
    }

    public Date getReleasedDate() {
        return this.releasedDate;
    }

    public DVD setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
        return this;
    }

    public String getFilmType() {
        return this.filmType;
    }

    public DVD setFilmType(String filmType) {
        this.filmType = filmType;
        return this;
    }

    @Override
    public String toString() {
        return "{" + super.toString() + " discType='" + discType + "'" + ", director='" + director + "'" + ", runtime='"
                + runtime + "'" + ", studio='" + studio + "'" + ", subtitles='" + subtitles + "'" + ", releasedDate='"
                + releasedDate + "'" + ", filmType='" + filmType + "'" + "}";
    }

    @Override
    public Media getMediaById(int id) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        String sql = "SELECT * FROM "+
                     "aims.DVD " +
                     "INNER JOIN aims.Media " +
                     "ON Media.id = DVD.id " +
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

        // from DVD table
        String discType = res.getString("discType");
        String director = res.getString("director");
        int runtime = res.getInt("runtime");
        String studio = res.getString("studio");
        String subtitles = res.getString("subtitle");
        Date releasedDate = res.getDate("releasedDate");
        String filmType = res.getString("filmType");

        stm.close();
        res.close();
        return new DVD(id, title, category, price, quantity, type, value,discType, director, runtime, studio, subtitles, releasedDate, filmType);

        } else {
            throw new SQLException();
        }
    }

    @Override
    public List getAllMedia() {
        return null;
    }

    public boolean createDVD(DVD dvd, int idMedia) throws SQLException {
        String sql = "INSERT INTO DVD (discType, director, runtime, releasedDate,studio, subtitle, filmType, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            java.sql.Date sqlReleasedDate = new java.sql.Date(dvd.getReleasedDate().getTime());
            preparedStatement.setString(1, dvd.getDiscType());
            preparedStatement.setString(2, dvd.getDirector());
            preparedStatement.setInt(3, dvd.getRuntime());
            preparedStatement.setDate(4, sqlReleasedDate);
            preparedStatement.setString(5, dvd.getStudio());
            preparedStatement.setString(6, dvd.getSubtitles());
            preparedStatement.setString(7, dvd.getFilmType());
            preparedStatement.setInt(8, idMedia);

            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();
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

    public DVD getDVDById(int idMedia) throws SQLException {
        String sql = "SELECT * FROM DVD WHERE id = ?;";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, idMedia);
            ResultSet res = preparedStatement.executeQuery();
            preparedStatement.close();
            res.close();
            if(res.next()) {
                return new DVD()
                        .setDiscType(res.getString("discType"))
                        .setDirector(res.getString("director"))
                        .setRuntime(res.getInt("runtime"))
                        .setReleasedDate(res.getDate("releasedDate"))
                        .setStudio(res.getString("studio"))
                        .setSubtitles(res.getString("subtitle"))
                        .setFilmType(res.getString("filmType"));
            }
        }

        return null;
    }

    public boolean updateDVDById(int idMedia, DVD dvd) throws SQLException {
        String updateSql = "UPDATE DVD SET discType = ?, director = ?, runtime = ?, releasedDate = ?, studio = ?, subtitle = ?, filmType = ? WHERE id = ?;";
        try (PreparedStatement updateStatement = AIMSDB.getConnection().prepareStatement(updateSql)) {
            updateStatement.setString(1, dvd.getDiscType());
            updateStatement.setString(2, dvd.getDirector());
            updateStatement.setInt(3, dvd.getRuntime());
            java.util.Date utilDate = dvd.getReleasedDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            updateStatement.setDate(4, sqlDate);
            updateStatement.setString(5, dvd.getStudio());
            updateStatement.setString(6, dvd.getSubtitles());
            updateStatement.setString(7, dvd.getFilmType());
            updateStatement.setInt(8, idMedia);

            int rowsAffected = updateStatement.executeUpdate();
            updateStatement.close();
            return rowsAffected > 0;
        }

    }
}
