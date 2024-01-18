package controller;

import entity.cart.Cart;
import entity.media.Book;
import entity.media.CD;
import entity.media.DVD;
import entity.media.Media;
import entity.operation.Operation;

import java.sql.SQLException;
import java.util.List;

/**
 * This class controls the flow of events when users view the Cart
 * @author phuongnm
 */
public class ProductController extends BaseController{
    
    /**
     * This method checks the available products in Cart
     * @throws SQLException
     * Data Coupling
     * Phân tích tính Conhesion:
     * - Functional Cohesion: thực hiện một nhiệm vụ cụ thể là kiểm tra xem sản phẩm có trong giỏ hàng hay không. 
     */
    public int createMedia(Media media) {
        try {
            return new Media().createMedia(media);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

//    public boolean createBook(Book book, int idMedia) throws SQLException{
//        return new Book().createBook(book, idMedia);
//    }

    public boolean createBook(Book book, int idMedia) {
        try {
            return new Book().createBook(book, idMedia);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createDVD(DVD dvd, int idMedia) {
        try {
            return new DVD().createDVD(dvd, idMedia);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createCD(CD cd, int idMedia) {
        try {
            return new CD().createCD(cd, idMedia);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        try {
            return new Media().deleteMediaById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProducts(List ids) {
        try {
            return new Media().deleteMediaByIds(ids);
        } catch (SQLException e) {
            System.out.println("loi deleteProducts");
            e.printStackTrace();
            return false;
        }
    }


    public int checkValidDelete(int value) {
        try {
            return new Operation().checkValidDelete(value);
        } catch (SQLException e) {
            System.out.println("co loi checkValidDelete");
            e.printStackTrace();
            return -1;
        }
    }

    public Book getBookById(int idMedia) {
        try {
            return new Book().getBookById(idMedia);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DVD getDVDById(int idMedia) {
        try {
            return new DVD().getDVDById(idMedia);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CD getCDById(int idMedia) {
        try {
            return new CD().getCDById(idMedia);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int checkValidUpdate(int value, int idMedia, boolean same) {
        try {
            return new Operation().checkValidUpdate(value, idMedia, same);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getCountUpdateValue(int idMedia) {
        return new Operation().getCountUpdateValue(idMedia);
    }

    public boolean insertUpdateOperation(int value, int idMedia, int countUpdateValue) {
        try {
            return new Operation().insertUpdateOperation(value, idMedia, countUpdateValue);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMediaById(int idMedia, Media media) {
        try {
            return new Media().updateMediaById(idMedia, media);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBookById(int idMedia, Book book) {
        try {
            if (new Book().getBookById(idMedia) != null) {
                return new Book().updateBookById(idMedia, book);
            } else {
                return createBook(book, idMedia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDVDId(int idMedia, DVD dvd) {
        try {
            if (new DVD().getDVDById(idMedia) != null) {
                return new DVD().updateDVDById(idMedia, dvd);
            } else {
                return createDVD(dvd, idMedia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCDById(int idMedia, CD cd) {
        try {
            if (new CD().getCDById(idMedia) != null) {
                return new CD().updateCDById(idMedia, cd);
            } else {
                return createCD(cd, idMedia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
