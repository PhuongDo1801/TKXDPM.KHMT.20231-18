package views.screen.user;


import controller.UserController;
import entity.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;



public class ManageUserScreenHandler extends BaseScreenHandler implements Initializable{

    public static Logger LOGGER = Utils.getLogger(ManageUserScreenHandler.class.getName());

    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, Integer> userIDCol;
    @FXML
    private TableColumn<User, String> nameCol;
    @FXML
    private TableColumn<User, String> emailCol;
    @FXML
    private TableColumn<User, String> addressCol;
    @FXML
    private TableColumn<User, String> phoneCol;

    @FXML
    protected Button createBtn;

    private UserController userController;

    public void requestToViewManageProduct(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Manage User Screen");
        show();
    }

    public ManageUserScreenHandler(Stage stage, String screenPath) throws IOException{
        super(stage, screenPath);
    }



    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.userController = new UserController();
//        try {
//            List<User> listUser = userController.getAllUser();
//            for (User user : listUser) {
//                System.out.println("UserID: " + user.toString());
//            }
//        } catch (Exception e) {
//            // Xử lý ngoại lệ nếu có
//            e.printStackTrace(); // In thông báo lỗi ra console
//        } finally {
//            // Có thể bỏ qua phần này nếu không cần
//        }

        ObservableList<User> users = FXCollections.observableArrayList();
//        users.add(new User(1, "MP", "m@gmail.com", "HN", "123456789"));

        users.addAll(userController.getAllUser());

        table.setItems(users);

        userIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        createBtn.setOnMouseClicked(event -> {
            try {
                UserController uController = new UserController();
                CreateUserHandler createUserHandler = new CreateUserHandler(new Stage(), Configs.CREATE_USER_PATH);
                createUserHandler.setPreviousScreen(this);
                createUserHandler.setHomeScreenHandler(homeScreenHandler);
                createUserHandler.setScreenTitle("Create User Screen");
                createUserHandler.setBController(uController);
                createUserHandler.show();
            } catch (Exception exp) {
                LOGGER.severe("Cannot delete");
                exp.printStackTrace();
            }
        });
    }




}
