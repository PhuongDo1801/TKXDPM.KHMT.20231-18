package controller;

import java.util.List;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;

/**
 * This class is the base controller for our AIMS project
 * @author phuongdo
 * 
 * 
 * Không thỏa mãn Single Responsibility Principle - SRP vì trong BaseController có 2 hàm với 2 mục địch khác nhau
 * 
 */

 
public class BaseController {
    
    /**
     * The method checks whether the Media in Cart, if it were in, we will return the CartMedia else return null
     * @param media
     * @return CartMedia or null
     * Phương thức này truyền đối tượng Media vào giỏ hàng và kiểm soát việc kiểm tra xem nó có tồn tại trong giỏ hàng hay không. 
     * Nó phụ thuộc vào cách Cart xử lý kiểm tra và trả về.
     * --> Control Coupling
     * Phân tích tính Conhesion:
     * - Functional Cohesion: thực hiện một nhiệm vụ cụ thể là kiểm tra xem Media có trong giỏ hàng hay không. 
     * - Informational Cohesion: Trả về CartMedia khi kiểm tra thoả mãn
     */
    public CartMedia checkMediaInCart(Media media){
        return Cart.getCart().checkMediaInCart(media);
    }

    /**
     * This method gets the list of items in cart
     * @return List[CartMedia]
     * Phương thức này chỉ yêu cầu danh sách các đối tượng CartMedia 
     * từ giỏ hàng mà không kiểm soát hoặc chi phối quá trình.
     * Data Coupling
     * Phân tích tính Conhesion:
     * - Functional Cohesion: hàm thực hiện chức năng cụ thể
     * - Communicational Cohesion: lấy ra danh sách Media từ Cart
     */
    public List getListCartMedia(){
        return Cart.getCart().getListMedia();
    }
}
