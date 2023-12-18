package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    /** 
     * Không thỏa mãn Single Responsibility Principle - SRP bởi vì trong đoạn mã, hàm placeOrder thực hiện nhiều công việc, bao gồm kiểm tra sự khả dụng của sản phẩm, tạo đơn đặt hàng, tạo hóa đơn và thực hiện các bước khác trong quá trình đặt hàng. Điều này có thể làm tăng độ phức tạp của phương thức và làm cho nó khó hiểu và khó bảo trì.
     * Giải pháp: tách các công việc này thành các phương thức riêng biệt ví dụ:
     * createOrder thực hiện công việc tạo đơn đặt hàng
     * createInvoice thực hiện công việc tạo hóa đơn.
     * processOrder thực hiện các bước trong quá trình đặt hàng.
     */
    /* Control Coupling */
    /* cohesion chức năng */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
      /* Data Coupling */
     /* cohesion chức năng */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
      /* Control Coupling */
    /* cohesion tuần tự */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */

      /* Control Coupling */
    /* cohesion truyền thông */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
   * The method validates the info
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
      /* Control Coupling */
       /* cohesion truyền thông */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
    	
    }
       /* Control Coupling */
       /* cohesion truyền thông */
    public boolean validatePhoneNumber(String phoneNumber) {
    	// TODO: your work
    	return false;
    }
       /* Control Coupling */
       /* cohesion truyền thông */
    public boolean validateName(String name) {
    	// TODO: your work
    	return false;
    }
       /* Control Coupling */
       /* cohesion truyền thông */
    public boolean validateAddress(String address) {
    	// TODO: your work
    	return false;
    }
    

    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
      /* Data Coupling */
    /* cohesion thủ tục */
    public int calculateShippingFee(Order order){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
