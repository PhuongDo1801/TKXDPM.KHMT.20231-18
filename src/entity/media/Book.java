package entity.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import entity.db.AIMSDB;

public class Book extends Media {

    String author;
    String coverType;
    String publisher;
    Date publishDate;
    int numOfPages;
    String language;
    String bookCategory;

    public Book() throws SQLException{

    }

    public Book(int id, String title, String category, int price, int quantity, String type, int value,String author,
            String coverType, String publisher, Date publishDate, int numOfPages, String language,
            String bookCategory) throws SQLException{
        super(id, title, category, price, quantity, type, value);
        this.author = author;
        this.coverType = coverType;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.numOfPages = numOfPages;
        this.language = language;
        this.bookCategory = bookCategory;
    }

    // getter and setter
    public int getId() {
        return this.id;
    }

    public String getAuthor() {
        return this.author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getCoverType() {
        return this.coverType;
    }

    public Book setCoverType(String coverType) {
        this.coverType = coverType;
        return this;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public Book setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public Book setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public int getNumOfPages() {
        return this.numOfPages;
    }

    public Book setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
        return this;
    }

    public String getLanguage() {
        return this.language;
    }

    public Book setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getBookCategory() {
        return this.bookCategory;
    }

    public Book setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
        return this;
    }

    @Override
    public Media getMediaById(int id) throws SQLException {
        String sql = "SELECT * FROM "+
                     "aims.Book " +
                     "INNER JOIN aims.Media " +
                     "ON Media.id = Book.id " +
                     "where Media.id = " + id + ";";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
		if(res.next()) {

            // from Media table
            String title = "";
            String type = res.getString("type");
            int price = res.getInt("price");
            String category = res.getString("category");
            int quantity = res.getInt("quantity");
            int value = res.getInt("value");

            // from Book table
            String author = res.getString("author");
            String coverType = res.getString("coverType");
            String publisher = res.getString("publisher");
            Date publishDate = res.getDate("publishDate");
            int numOfPages = res.getInt("numOfPages");
            String language = res.getString("language");
            String bookCategory = res.getString("bookCategory");
            
            return new Book(id, title, category, price, quantity, type, value,
                            author, coverType, publisher, publishDate, numOfPages, language, bookCategory);
            
		} else {
			throw new SQLException();
		}
    }

    @Override
    public List getAllMedia() {
        return null;
    }

    public Book getBookById(int idMedia) throws SQLException {
        String sql = "SELECT * FROM Book WHERE id = ?;";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, idMedia);
            ResultSet res = preparedStatement.executeQuery();

            if(res.next()) {
                return new Book()
                        .setAuthor(res.getString("author"))
                        .setCoverType(res.getString("coverType"))
                        .setPublisher(res.getString("publisher"))
                        .setPublishDate(res.getDate("publishDate"))
                        .setNumOfPages(res.getInt("numOfPages"))
                        .setLanguage(res.getString("language"))
                        .setBookCategory(res.getString("bookCategory"));
            }
        }

        return null;
    }

    public boolean updateBookById(int idMedia, Book book) throws SQLException {
        String updateSql = "UPDATE Book SET author = ?, coverType = ?, publisher = ?, publishDate = ?, numOfPages = ?, language = ?, bookCategory = ? WHERE id = ?;";
        try (PreparedStatement updateStatement = AIMSDB.getConnection().prepareStatement(updateSql)) {
            updateStatement.setString(1, book.getAuthor());
            updateStatement.setString(2, book.getCoverType());
            updateStatement.setString(3, book.getPublisher());
            java.util.Date utilDate = book.getPublishDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            updateStatement.setDate(4, sqlDate);
            updateStatement.setInt(5, book.getNumOfPages());
            updateStatement.setString(6, book.getLanguage());
            updateStatement.setString(7, book.getBookCategory());
            updateStatement.setInt(8, idMedia);

            int rowsAffected = updateStatement.executeUpdate();
            return rowsAffected > 0;
        }

    }

//    public boolean updateBookById(int idMedia, Book book) throws SQLException {
//        // Kiểm tra xem Book có tồn tại không
//        String selectSql = "SELECT * FROM Book WHERE id = ?;";
//        try (PreparedStatement selectStatement = AIMSDB.getConnection().prepareStatement(selectSql)) {
//            selectStatement.setInt(1, idMedia);
//            ResultSet rs = selectStatement.executeQuery();
//            if (!rs.next()) {
//                // Nếu không có Book, tạo một Book mới
//                String insertSql = "INSERT INTO Book (id, author, coverType, publisher, publishDate, numOfPages, language, bookCategory) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
//                try (PreparedStatement insertStatement = AIMSDB.getConnection().prepareStatement(insertSql)) {
//                    insertStatement.setInt(1, idMedia);
//                    insertStatement.setString(2, book.getAuthor());
//                    insertStatement.setString(3, book.getCoverType());
//                    insertStatement.setString(4, book.getPublisher());
//                    java.util.Date utilDate = book.getPublishDate();
//                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//                    insertStatement.setDate(5, sqlDate);
//                    insertStatement.setInt(6, book.getNumOfPages());
//                    insertStatement.setString(7, book.getLanguage());
//                    insertStatement.setString(8, book.getBookCategory());
//
//                    int rowsAffected = insertStatement.executeUpdate();
//                    return rowsAffected > 0;
//                }
//            }
//        }
//
//        // Nếu có Book, tiếp tục cập nhật
//        String updateSql = "UPDATE Book SET author = ?, coverType = ?, publisher = ?, publishDate = ?, numOfPages = ?, language = ?, bookCategory = ? WHERE id = ?;";
//        try (PreparedStatement updateStatement = AIMSDB.getConnection().prepareStatement(updateSql)) {
//            updateStatement.setString(1, book.getAuthor());
//            updateStatement.setString(2, book.getCoverType());
//            updateStatement.setString(3, book.getPublisher());
//            java.util.Date utilDate = book.getPublishDate();
//            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//            updateStatement.setDate(4, sqlDate);
//            updateStatement.setInt(5, book.getNumOfPages());
//            updateStatement.setString(6, book.getLanguage());
//            updateStatement.setString(7, book.getBookCategory());
//            updateStatement.setInt(8, idMedia);
//
//            int rowsAffected = updateStatement.executeUpdate();
//            return rowsAffected > 0;
//        }
//    }



    public boolean createBook(Book book, int idMedia) throws SQLException {
        String sql = "INSERT INTO Book (author, coverType, publisher, publishDate, numOfPages, language, bookCategory, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            java.sql.Date sqlpublishDate = new java.sql.Date(book.getPublishDate().getTime());
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getCoverType());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setDate(4, sqlpublishDate);
            preparedStatement.setInt(5, book.getNumOfPages());
            preparedStatement.setString(6, book.getLanguage());
            preparedStatement.setString(7, book.getBookCategory());
            preparedStatement.setInt(8, idMedia);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "{" +
            super.toString() +
            " author='" + author + "'" +
            ", coverType='" + coverType + "'" +
            ", publisher='" + publisher + "'" +
            ", publishDate='" + publishDate + "'" +
            ", numOfPages='" + numOfPages + "'" +
            ", language='" + language + "'" +
            ", bookCategory='" + bookCategory + "'" +
            "}";
    }
}
