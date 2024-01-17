package views.screen.user;

import controller.UserController;
import entity.media.Book;
import entity.media.CD;
import entity.media.DVD;
import entity.media.Media;
import entity.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * lớp này thỏa mãn Single Resposibility Principle - SRP vì các phương thức bên trong đều có chung mục đích
 * thỏa mãn Liskov Substitution Principle - LSP vì MediaHandler nó có thể thay thế được cho FXMLScreenHandler
 */

public class CreateUserHandler extends BaseScreenHandler implements Initializable {

    @FXML
    private TextField userid;

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private TextField address;

    @FXML
    private TextField phone;

    @FXML
    protected Button createBtn;

    private User user;

    private static Logger LOGGER = Utils.getLogger(CreateUserHandler.class.getName());
    private UserController userController;

    public CreateUserHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.userController = new UserController();

        this.createBtn.setOnAction(this::handleCreateButtonClick);

        try {
            this.user = new User();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCreateButtonClick(ActionEvent event) {
        try {
            createUser();
        } catch (Exception exp) {
            LOGGER.severe("handleCreateButtonClick");
            exp.printStackTrace();
        }
    }

    private void createUser() {
        try {
            this.user.setName(name.getText());
            this.user.setEmail(email.getText());
            this.user.setAddress(address.getText());
            this.user.setPhone(phone.getText());
            this.userController.createUser(this.user);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

}
