package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import entity.media.Media;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.user.User;
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

    public List getAllOrder() {
        try {
            return new Order().getAllOrder();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
