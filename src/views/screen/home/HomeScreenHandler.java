package views.screen.home;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import common.exception.ViewCartException;
import controller.*;
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
import views.screen.product.ManageProductScreenHandler;
import views.screen.myorder.MyOrder;
import views.screen.user.ManageUserScreenHandler;


/**
 * lớp này không thỏa mãn Single Resposibility Principle - SRP vì có các phương thức thực hiện mục đích khác nhau
 * thỏa mãn Liskov Substitution Principle - LSP vì HomeScreenHandler nó có thể thay thế được cho BaseScreenHandler
 */

public class HomeScreenHandler extends BaseScreenHandler implements Initializable{

    public static Logger LOGGER = Utils.getLogger(HomeScreenHandler.class.getName());

    @FXML
    private Label numMediaInCart;

    @FXML
    private ImageView aimsImage;

    @FXML
    private ImageView cartImage;

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

    @FXML
    private Button manageProductBtn;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnOrder;

    @FXML
    private Button manageUserBtn;

    @FXML
    private TextField textSearch;

    private List homeItems;

    public HomeScreenHandler(Stage stage, String screenPath) throws IOException{
        super(stage, screenPath);
    }

    public Label getNumMediaCartLabel(){
        return this.numMediaInCart;
    }

    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    @Override
    public void show() {
        numMediaInCart.setText(String.valueOf(Cart.getCart().getListMedia().size()) + " media");
        super.show();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new HomeController());
        try{
            List medium = getBController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media)object;
                MediaHandler m1 = new MediaHandler(Configs.HOME_MEDIA_PATH, media, this);
                this.homeItems.add(m1);
            }
        }catch (SQLException | IOException e){
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Xử lý sự kiện nhấn Enter trong ô tìm kiếm
        textSearch.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                handleSearchEvent();
            }
        });

        btnSearch.setOnMouseClicked(e -> {
            handleSearchEvent();
        });
            
        aimsImage.setOnMouseClicked(e -> {
            addMediaHome(this.homeItems);
        });
        
        cartImage.setOnMouseClicked(e -> {
            CartScreenHandler cartScreen;
            try {
                LOGGER.info("User clicked to view cart");
                cartScreen = new CartScreenHandler(this.stage, Configs.CART_SCREEN_PATH);
                cartScreen.setHomeScreenHandler(this);
                cartScreen.setBController(new ViewCartController());
                cartScreen.requestToViewCart(this);
            } catch (IOException | SQLException e1) {
                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }
        });

        manageProductBtn.setOnMouseClicked(e -> {
            ManageProductScreenHandler manageProductScreenHandler;
            try {
                LOGGER.info("User clicked to view cart");
                manageProductScreenHandler = new ManageProductScreenHandler(this.stage, Configs.MANAGE_PRODUCT_PATH);
                manageProductScreenHandler.setHomeScreenHandler(this);
                manageProductScreenHandler.setPreviousScreen(this);
                manageProductScreenHandler.setBController(new ProductController());
                manageProductScreenHandler.requestToViewManageProduct(this);
            } catch (IOException | SQLException e1) {
                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }
        });

        btnOrder.setOnMouseClicked(e -> {
            MyOrder orderScreenHandler;
            try {
                LOGGER.info("User clicked to view cart");
                orderScreenHandler = new MyOrder(new Stage(), Configs.MY_ORDER_PATH);
                orderScreenHandler.setHomeScreenHandler(this);
                orderScreenHandler.setPreviousScreen(this);
                orderScreenHandler.setBController(new MyOrderController());
                orderScreenHandler.show();
            } catch (IOException e1) {
                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }
        });

        manageUserBtn.setOnMouseClicked(e -> {
            ManageUserScreenHandler manageUserScreenHandler;
            try {
                manageUserScreenHandler = new ManageUserScreenHandler(new Stage(), Configs.MANAGE_USER_PATH);
                manageUserScreenHandler.setHomeScreenHandler(this);
                manageUserScreenHandler.setPreviousScreen(this);
                manageUserScreenHandler.setBController(new UserController());
                manageUserScreenHandler.requestToViewManageProduct(this);
//                manageUserScreenHandler.show();
            } catch (IOException | SQLException e1) {
                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }
        });

        addMediaHome(this.homeItems);
        addMenuItem(0, "Book", splitMenuBtnSearch);
        addMenuItem(1, "DVD", splitMenuBtnSearch);
        addMenuItem(2, "CD", splitMenuBtnSearch);
    }

    public void setImage(){
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);

        File file2 = new File(Configs.IMAGE_PATH + "/" + "cart.png");
        Image img2 = new Image(file2.toURI().toString());
        cartImage.setImage(img2);
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
                    MediaHandler media = (MediaHandler) mediaItems.get(0);
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
                MediaHandler media = (MediaHandler) me;
                if (media.getMedia().getType().toLowerCase().startsWith(text.toLowerCase())){
                    filteredItems.add(media);
                }
            });

            // fill out the home with filted media as category
            addMediaHome(filteredItems);
        });
        menuButton.getItems().add(position, menuItem);
    }

    private void handleSearchEvent() {
        String keyword = textSearch.getText();
        performSearch(keyword);
    }
    
    private void performSearch(String keyword) {
        List filteredItems = new ArrayList<>();
        homeItems.forEach(item -> {
            MediaHandler media = (MediaHandler) item;
            if (media.getMedia().getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                filteredItems.add(media);
            }
        });
        addMediaHome(filteredItems);
    }
    
    
    
}
