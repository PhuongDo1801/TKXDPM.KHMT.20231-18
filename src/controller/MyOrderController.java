package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import entity.media.Media;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.order.OrderMedia;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * This class controls the flow of events when users view the Cart
 * @author nguyenlm
 */
public class MyOrderController extends BaseController{

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

    @FXML
    private TableView<OrderMedia> tableView;

    @FXML
    private TableColumn<OrderMedia, Media> mediaColumn;

    @FXML
    private TableColumn<OrderMedia, Integer> quantityColumn;

    @FXML
    private TableColumn<OrderMedia, Integer> priceColumn;

    private ObservableList<OrderMedia> orderMediaList;

    @FXML
    public void initialize() {
        // Khởi tạo cột và dữ liệu
        mediaColumn.setCellValueFactory(cellData -> cellData.getValue().mediaProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        // Khởi tạo dữ liệu mẫu (có thể thay thế bằng dữ liệu thực từ cơ sở dữ liệu)
        orderMediaList = FXCollections.observableArrayList();
        orderMediaList.add(new OrderMedia(new Media("Tên Media 1"), 2, 100));
        orderMediaList.add(new OrderMedia(new Media("Tên Media 2"), 1, 50));

        // Đặt dữ liệu vào TableView
        tableView.setItems(orderMediaList);
    }
}
