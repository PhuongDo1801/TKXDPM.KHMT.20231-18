package controller;

import java.sql.SQLException;
import java.util.List;

import entity.media.Media;
import entity.cart.Cart;
import entity.cart.CartMedia;

/**
 * This class controls the flow of events when users view the Cart
 * @author nguyenlm
 */
public class ViewCartController extends BaseController{
    
    /**
     * This method checks the available products in Cart
     * @throws SQLException
     * Data Coupling
     * Phân tích tính Conhesion:
     * - Functional Cohesion: thực hiện một nhiệm vụ cụ thể là kiểm tra xem sản phẩm có trong giỏ hàng hay không. 
     */
    public void checkAvailabilityOfProduct() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method calculates the cart subtotal
     * @return subtotal
     * Data Coupling
     * Phân tích tính Conhesion:
     * - Functional Cohesion: thực hiện chức năng tính toán giỏ hàng.
    * - Informational Cohesion: Trả về số lượng sản phẩm trong giỏ hàng.
     */
    public int getCartSubtotal(){
        int subtotal = Cart.getCart().calSubtotal();
        return subtotal;
    }

}
