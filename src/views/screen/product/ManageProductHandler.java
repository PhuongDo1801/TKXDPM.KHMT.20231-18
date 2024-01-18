package views.screen.product;

import common.exception.MediaNotAvailableException;
import common.exception.ViewCartException;
import controller.ProductController;
import controller.ViewCartController;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;
import entity.operation.Operation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.FXMLScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * lớp này thỏa mãn Single Resposibility Principle - SRP vì các phương thức bên trong đều có chung mục đích
 * thỏa mãn Liskov Substitution Principle - LSP vì MediaHandler nó có thể thay thế được cho FXMLScreenHandler
 */

public class ManageProductHandler extends FXMLScreenHandler{

    @FXML
    protected ImageView mediaImage;

    @FXML
    protected Label mediaTitle;

    @FXML
    protected Label mediaPrice;

    @FXML
    protected Label mediaAvail;

    @FXML
    protected Button deleteBtn;

    @FXML
    protected Button updateBtn;

    @FXML
    protected Button detailBtn;

    @FXML
    protected CheckBox checkBox;

    private static Logger LOGGER = Utils.getLogger(ManageProductHandler.class.getName());
    private Media media;
    private Operation operation;
    private ManageProductScreenHandler home;
    private ProductController productController;
    private CheckboxIdManager checkboxIdManager;

    public ManageProductHandler(String screenPath, Media media, ManageProductScreenHandler home) throws SQLException, IOException{
        super(screenPath);
        this.media = media;
        this.home = home;
        this.operation = new Operation();
        this.productController = new ProductController();
        this.checkboxIdManager = CheckboxIdManager.getInstance();

        deleteBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa không?");

            ButtonType confirmButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(cancelButton) == confirmButton) {
                System.out.println("Đã xác nhận xóa");
                try {
                    int checkCanEdit = productController.checkValidDelete(1);
                    if (checkCanEdit == 1) {
                        productController.deleteProduct(media.getId());
                        showAlert("Xóa thành công", "Phần tử đã được xóa thành công.", Alert.AlertType.INFORMATION);
                    } else if (checkCanEdit == 2){
                        PopupScreen.error("Only a maximum of 30 deletion operations per day can be performed");
                        LOGGER.info("Error delete on delete btn checkCanEdit == 2");
                    } else if (checkCanEdit == 3) {
                        PopupScreen.error("You have removed 30 products today");
                        LOGGER.info("Error delete on delete btn checkCanEdit == 3");
                    } else if (checkCanEdit == -1) {
                        PopupScreen.error("An error occurred while deleting");
                        LOGGER.info("Error delete on delete btn checkCanEdit == -1");
                    }
                } catch (Exception exp) {
                    LOGGER.severe("Cannot delete");
                    exp.printStackTrace();
                }
            } else {
                System.out.println("Đã hủy xác nhận xóa");
            }
        });


        updateBtn.setOnMouseClicked(event -> {
            try {
                ProductController productController = new ProductController();
                UpdateProductHandler updateProductHandler = new UpdateProductHandler(new Stage(), Configs.UPDATE_PRODUCT_PATH, media);
                updateProductHandler.setScreenTitle("Update Product Screen");
                updateProductHandler.setBController(productController);
                updateProductHandler.show();
            } catch (Exception exp) {
                LOGGER.severe("updateBtn error");
                exp.printStackTrace();
            }
        });

        detailBtn.setOnMouseClicked(event -> {
            try {
                ProductController productController = new ProductController();
                DetailProductHandler detailProductHandler = new DetailProductHandler(new Stage(), Configs.DETAIL_PRODUCT_PATH, media);
                detailProductHandler.setScreenTitle("Detail Product Screen");
                detailProductHandler.setBController(productController);
                detailProductHandler.show();
            } catch (Exception exp) {
                LOGGER.severe("detailBtn error");
                exp.printStackTrace();
            }
        });

        checkBox.setOnAction(event -> {
            int mediaId = media.getId();

            if (checkBox.isSelected() && checkboxIdManager.getSizeCheckboxId() < 10) {
                checkboxIdManager.addSelectedCheckboxId(mediaId);
                System.out.println("Added id: " + mediaId);
            } else if (!checkBox.isSelected()){
                checkboxIdManager.removeSelectedCheckboxId(mediaId);
                System.out.println("Removed id: " + mediaId);
            }
        });


        setMediaInfo();
    }

    public static void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.getButtonTypes().setAll(ButtonType.OK);

        alert.showAndWait();
    }

    public Media getMedia(){
        return media;
    }

    private void setMediaInfo() throws SQLException {
        // set the cover image of media
        File file = new File(media.getImageURL());
        Image image = new Image(file.toURI().toString());
        mediaImage.setFitHeight(160);
        mediaImage.setFitWidth(152);
        mediaImage.setImage(image);

        mediaTitle.setText(media.getTitle());
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
        mediaAvail.setText(Integer.toString(media.getQuantity()));
//        spinnerChangeNumber.setValueFactory(
//            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1)
//        );

        setImage(mediaImage, media.getImageURL());
    }

}
