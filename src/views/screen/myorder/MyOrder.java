package views.screen.myorder;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import common.exception.MediaNotAvailableException;
import common.exception.PlaceOrderException;
import controller.PlaceOrderController;
import controller.MyOrderController;
import entity.cart.CartMedia;
import entity.order.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.shipping.ShippingScreenHandler;

/**
 * lớp này thỏa mãn Single Resposibility Principle - SRP vì các phương thức bên trong đều có chung mục đích về Cart
 * thỏa mãn Liskov Substitution Principle - LSP vì CartScreenHandler nó có thể thay thế được cho BaseScreenHandler
 */

public class MyOrder extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(MyOrder.class.getName());

    @FXML
    private Button btnOrder;

    public MyOrder(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);

        // on mouse clicked, we start processing place order usecase
//        btnOrder.setOnMouseClicked(e -> {
//            LOGGER.info("Place Order button clicked");
//            try {
//                requestToPlaceOrder();
//            } catch (SQLException | IOException exp) {
//                LOGGER.severe("Cannot place the order, see the logs");
//                exp.printStackTrace();
//                throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
//            }
//
//        });
    }

    public void requestToPlaceOrder() throws SQLException, IOException {
        try {
            // create placeOrderController and process the order
            PlaceOrderController placeOrderController = new PlaceOrderController();
            if (placeOrderController.getListCartMedia().size() == 0){
                PopupScreen.error("You don't have anything to place");
                return;
            }

            placeOrderController.placeOrder();

            // display available media

            // create order
            Order order = placeOrderController.createOrder();

            // display shipping form
            ShippingScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, order);
            ShippingScreenHandler.setPreviousScreen(this);
            ShippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
            ShippingScreenHandler.setScreenTitle("Shipping Screen");
            ShippingScreenHandler.setBController(placeOrderController);
            ShippingScreenHandler.show();

        } catch (MediaNotAvailableException e) {
            // if some media are not available then display cart and break usecase Place Order
        }
    }

}