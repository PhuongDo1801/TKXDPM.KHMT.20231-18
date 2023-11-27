package controller;

import java.util.List;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;

/**
 * This class is the base controller for our AIMS project
 * @author nguyenlm
 */
public class BaseController {
    
    /**
     * The method checks whether the Media in Cart, if it were in, we will return the CartMedia else return null
     * @param media
     * @return CartMedia or null
     * Phương thức này truyền đối tượng Media vào giỏ hàng và kiểm soát việc kiểm tra xem nó có tồn tại trong giỏ hàng hay không. 
     * Nó phụ thuộc vào cách Cart xử lý kiểm tra và trả về.
     * --> Control Coupling
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
     */
    public List getListCartMedia(){
        return Cart.getCart().getListMedia();
    }
}
