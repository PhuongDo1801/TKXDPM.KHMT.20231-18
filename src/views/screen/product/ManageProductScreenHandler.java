package views.screen.product;

import common.exception.ViewCartException;
import controller.HomeController;
import controller.ProductController;
import controller.ViewCartController;
import entity.cart.Cart;
import entity.media.Media;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.cart.CartScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.shipping.ShippingScreenHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

/**
 * lớp này không thỏa mãn Single Resposibility Principle - SRP vì có các phương thức thực hiện mục đích khác nhau
 * thỏa mãn Liskov Substitution Principle - LSP vì HomeScreenHandler nó có thể thay thế được cho BaseScreenHandler
 */

public class ManageProductScreenHandler extends BaseScreenHandler implements Initializable{

    public static Logger LOGGER = Utils.getLogger(ManageProductHandler.class.getName());


    @FXML
    private ImageView aimsImage;


    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private HBox hboxMedia;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;

    private List homeItems;

    @FXML
    protected Button createBtn;

    @FXML
    protected Button refreshBtn;

    @FXML
    protected Button deleteSelectBtn;
    private ProductController productController;

//    public static List<Integer> selectedCheckboxIds = new ArrayList<>();
    private CheckboxIdManager checkboxIdManager;

    public ManageProductScreenHandler(Stage stage, String screenPath) throws IOException{
        super(stage, screenPath);
        this.productController = new ProductController();
        this.checkboxIdManager = CheckboxIdManager.getInstance();
    }

//    public Label getNumMediaCartLabel(){
//        return this.numMediaInCart;
//    }


    @Override
    public void show() {
//        numMediaInCart.setText(String.valueOf(Cart.getCart().getListMedia().size()) + " media");
        super.show();
    }

    public void requestToViewManageProduct(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Manage Product Screen");
//        displayCartWithMediaAvailability();
        show();
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
//        setBController(new HomeController());
        try{
            List medium = new HomeController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media)object;
                ManageProductHandler m1 = new ManageProductHandler(Configs.CARD_PRODUCT_PATH, media, this);
                this.homeItems.add(m1);
            }
        }catch (SQLException | IOException e){
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }

        File file1 = new File(Configs.IMAGE_PATH + "/" + "back.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);


        aimsImage.setOnMouseClicked(e -> {
//            addMediaHome(this.homeItems);
            getPreviousScreen().show();
        });


        createBtn.setOnMouseClicked(event -> {
            try {
                ProductController productController = new ProductController();
                CreateProductHandler CreateProductHandler = new CreateProductHandler(new Stage(), Configs.CREATE_PRODUCT_PATH);
                CreateProductHandler.setPreviousScreen(this);
                CreateProductHandler.setHomeScreenHandler(homeScreenHandler);
                CreateProductHandler.setScreenTitle("Create Product Screen");
                CreateProductHandler.setBController(productController);
                CreateProductHandler.show();
            } catch (Exception exp) {
                LOGGER.severe("Cannot delete");
                exp.printStackTrace();
            }
        });

        refreshBtn.setOnMouseClicked(event -> {
            try{
                List medium = new HomeController().getAllMedia();
                this.homeItems = new ArrayList<>();
                for (Object object : medium) {
                    Media media = (Media)object;
                    ManageProductHandler m1 = new ManageProductHandler(Configs.CARD_PRODUCT_PATH, media, this);
                    m1.checkBox.setSelected(false);
                    System.out.println(media.toString());
                    this.homeItems.add(m1);
                }
                addMediaHome(this.homeItems);
            }catch (SQLException | IOException e){
                LOGGER.info("Errors occured: " + e.getMessage());
                e.printStackTrace();
            }
        });


        deleteSelectBtn.setOnMouseClicked(event -> {
            for (Integer id : checkboxIdManager.getSelectedCheckboxIds()) {
                System.out.println(id);
            }

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
                    int checkCanEdit = productController.checkValidDelete(checkboxIdManager.getSizeCheckboxId());
                    if (checkCanEdit == 1) {
                        if(productController.deleteProducts(checkboxIdManager.getSelectedCheckboxIds())) {
                            checkboxIdManager.clearSelectedCheckboxId();
                            showAlert("Xóa thành công", "Phần tử đã được xóa thành công.", Alert.AlertType.INFORMATION);
                        } else {
                            showAlert("Lỗi", "Có lỗi xảy ra khi xóa phần tử.", Alert.AlertType.ERROR);
                        }

                    }else if (checkCanEdit == 2){
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


        addMediaHome(this.homeItems);
        addMenuItem(0, "Book", splitMenuBtnSearch);
        addMenuItem(1, "DVD", splitMenuBtnSearch);
        addMenuItem(2, "CD", splitMenuBtnSearch);
    }

    public static void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.getButtonTypes().setAll(ButtonType.OK);

        alert.showAndWait();
    }

    public void addMediaHome(List items){
        ArrayList mediaItems = (ArrayList)((ArrayList) items).clone();
        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });
        while(!mediaItems.isEmpty()){
            hboxMedia.getChildren().forEach(node -> {
                int vid = hboxMedia.getChildren().indexOf(node);
                VBox vBox = (VBox) node;
                while(vBox.getChildren().size()<3 && !mediaItems.isEmpty()){
                    ManageProductHandler media = (ManageProductHandler) mediaItems.get(0);
                    vBox.getChildren().add(media.getContent());
                    mediaItems.remove(media);
                }
            });
            return;
        }
    }

    private void addMenuItem(int position, String text, MenuButton menuButton){
        MenuItem menuItem = new MenuItem();
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(31));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);
        menuItem.setOnAction(e -> {
            // empty home media
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                vBox.getChildren().clear();
            });

            // filter only media with the choosen category
            List filteredItems = new ArrayList<>();
            homeItems.forEach(me -> {
                ManageProductHandler media = (ManageProductHandler) me;
                if (media.getMedia().getType().toLowerCase().startsWith(text.toLowerCase())){
                    filteredItems.add(media);
                }
            });

            // fill out the home with filted media as category
            addMediaHome(filteredItems);
        });
        menuButton.getItems().add(position, menuItem);
    }



}
