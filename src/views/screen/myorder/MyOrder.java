package views.screen.myorder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import common.exception.MediaNotAvailableException;
import common.exception.PlaceOrderException;
import controller.PlaceOrderController;
import controller.MyOrderController;
import controller.UserController;
import entity.cart.CartMedia;
import entity.order.Order;
import entity.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class MyOrder extends BaseScreenHandler implements Initializable {

    private static Logger LOGGER = Utils.getLogger(MyOrder.class.getName());

//    @FXML
//    private TableView<Order> tableView;
//    @FXML
//    private TableColumn<Order, Integer> idCol;
//    @FXML
//    private TableColumn<Order, String> nameCol;
//    @FXML
//    private TableColumn<Order, Integer> quantityCol;
//    @FXML
//    private TableColumn<Order, Integer> priceCol;
//    @FXML
//    private TableColumn<Order, Integer> shippingFeeCol;
//    @FXML
//    private TableColumn<Order, String> statusCol;

    private MyOrderController myOrderController;

    public MyOrder(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.myOrderController = new MyOrderController();
//                try {
//            List<Order> listUser = myOrderController.getAllOrder();
//            for (Order user : listUser) {
//                System.out.println("UserID: " + user.toString());
//            }
//        } catch (Exception e) {
//            // Xử lý ngoại lệ nếu có
//            e.printStackTrace(); // In thông báo lỗi ra console
//        } finally {
//            // Có thể bỏ qua phần này nếu không cần
//        }
//
//        ObservableList<Order> orders = FXCollections.observableArrayList();
//
//        orders.addAll(myOrderController.getAllOrder());
//
//        tableView.setItems(orders);
//
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
//        shippingFeeCol.setCellValueFactory(new PropertyValueFactory<>("shippingFees"));
//        statusCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

}
