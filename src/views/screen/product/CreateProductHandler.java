package views.screen.product;

import common.exception.MediaNotAvailableException;
import controller.ProductController;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Book;
import entity.media.CD;
import entity.media.DVD;
import entity.media.Media;
import entity.order.Order;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.FXMLScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * lớp này thỏa mãn Single Resposibility Principle - SRP vì các phương thức bên trong đều có chung mục đích
 * thỏa mãn Liskov Substitution Principle - LSP vì MediaHandler nó có thể thay thế được cho FXMLScreenHandler
 */

public class CreateProductHandler extends BaseScreenHandler implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextField category;
    @FXML
    private ComboBox<String> type;
    @FXML
    private TextField price;
    @FXML
    private TextField quantity;
    @FXML
    private TextField value;
    @FXML
    private Label name1;
    @FXML
    private Label name2;
    @FXML
    private Label name3;
    @FXML
    private Label name4;
    @FXML
    private Label name5;
    @FXML
    private Label name6;
    @FXML
    private Label name7;
    @FXML
    private Label note1;
    @FXML
    private Label note2;
    @FXML
    private Label note3;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField textField5;
    @FXML
    private TextField textField6;
    @FXML
    private TextField textField7;
    @FXML
    private Group groupSelect;
    @FXML
    protected Button createBtn;
    @FXML
    protected Button uploadImageBtn;
    @FXML
    protected ImageView imageView;

    private static Logger LOGGER = Utils.getLogger(CreateProductHandler.class.getName());
    private String selectedType;
    private ProductController productController;
    private Media media;
    private Book book;
    private DVD dvd;
    private CD cd;
    private Image image;
    private String destinationFilePath="";

//    @FXML
//    private void handleUploadImage() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select Image");
//
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg, *.gif)", "*.png", "*.jpg", "*.jpeg", "*.gif");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        File selectedFile = fileChooser.showOpenDialog(null);
//        if (selectedFile != null) {
//            image = new Image(selectedFile.toURI().toString());
//            imageView.setImage(image);
//            System.out.println(image.getUrl());
//
//        }
//    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg, *.gif)", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Lấy đường dẫn của file được chọn
            String sourcePath = selectedFile.toPath().toString();

            String destinationPath="";

            selectedType = type.getValue();
            if ("BOOK".equals(selectedType)) {
                destinationPath = "assets/images/book/";
            } else if ("DVD".equals(selectedType)) {
                destinationPath = "assets/images/dvd/";
            } else if ("CD".equals(selectedType)) {
                destinationPath = "assets/images/cd/";
            }

            // Tên file mới
            String fileName = selectedFile.getName();

            // Đường dẫn đầy đủ của file mới
//            String destinationFilePath = destinationPath + fileName;
            destinationFilePath = destinationPath + fileName;

            // Sao chép file từ vị trí hiện tại đến thư mục đích
            try {
                Files.copy(Paths.get(sourcePath), Paths.get(destinationFilePath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File saved to: " + destinationFilePath);

                // Cập nhật ImageView
                image = new Image(new File(destinationFilePath).toURI().toString());
                imageView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error copying file!");
            }
        }
    }



    public CreateProductHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.productController = new ProductController();

        datePicker.setEditable(false);

        String[] typeString = {"BOOK", "DVD", "CD"};
        this.type.getItems().addAll(typeString);
        this.type.setValue("Select type");
        type.setOnAction(this::handleTypeSelection);

        createBtn.setOnAction(this::handleCreateButtonClick);

        try {
            this.media = new Media();
            this.book = new Book();
            this.dvd = new DVD();
            this.cd = new CD();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleTypeSelection(ActionEvent event) {
        selectedType = type.getValue();
        switch (selectedType) {
            case "BOOK":
                setLabelsVisibleBook();
                break;
            case "DVD":
                setLabelsVisibleDVD();
                break;
            case "CD":
                setLabelsVisibleCD();
                break;
            default:
                setLabelsVisible();
                break;
        }
    }

    private void handleCreateButtonClick(ActionEvent event) {
        selectedType = type.getValue();
        try {
            switch (selectedType) {
                case "BOOK":
                    System.out.println("create book");
                    createBook();
                    break;
                case "DVD":
                    System.out.println("create DVD");
                    createDVD();
                    break;
                case "CD":
                    System.out.println("create cd");
                    createCD();
                    break;
                default:
//                    setLabelsVisible();
                    PopupScreen.error("need to select media type");
                    break;
            }
        } catch (Exception exp) {
            LOGGER.severe("handleCreateButtonClick");
            exp.printStackTrace();
        }
    }

    private void setLabelsVisible() {
        groupSelect.setVisible(false);
    }

    private void setLabelsVisibleBook() {
        groupSelect.setVisible(true);
        name1.setText("Author");
        name2.setText("Cover type");
        name3.setText("Pulisher");
        name4.setText("Publish date");
        name5.setText("Number of date");
        name6.setText("Language");
        name7.setText("Book category");

        name5.setVisible(true);
        name6.setVisible(true);
        name7.setVisible(true);
        textField5.setVisible(true);
        textField6.setVisible(true);
        textField7.setVisible(true);
        note1.setVisible(true);
        note2.setVisible(true);
        note3.setVisible(true);
    }

    private void setLabelsVisibleDVD() {
        groupSelect.setVisible(true);

        name1.setText("Disc type");
        name2.setText("Director");
        name3.setText("Runtime");
        name4.setText("Release date");
        name5.setText("Studio");
        name6.setText("Subtitle");
        name7.setText("Film type");

        name5.setVisible(true);
        name6.setVisible(true);
        name7.setVisible(true);
        textField5.setVisible(true);
        textField6.setVisible(true);
        textField7.setVisible(true);
        note1.setVisible(true);
        note2.setVisible(true);
        note3.setVisible(true);
    }

    private void setLabelsVisibleCD() {
        groupSelect.setVisible(true);

        name1.setText("Artist");
        name2.setText("Record label");
        name3.setText("Music type");
        name4.setText("Release date");

        name5.setVisible(false);
        name6.setVisible(false);
        name7.setVisible(false);
        textField5.setVisible(false);
        textField6.setVisible(false);
        textField7.setVisible(false);
        note1.setVisible(false);
        note2.setVisible(false);
        note3.setVisible(false);
    }

    private void createBook() {
        try {
            if (validateMedia("book") != null && validateBook() != null) {
                int idMedia = productController.createMedia(media);
                if (idMedia > 0 && productController.createBook(book, idMedia)) {
                    PopupScreen.success("Create book success");
                } else {
                    PopupScreen.error("Book creation error");
                }
            } else if(validateMedia("book") == null) {
                PopupScreen.error("Media data form error");
            } else if(validateBook() == null) {
                PopupScreen.error("Book data form error");
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void createDVD() {
        try {
            if (validateMedia("dvd") != null && validateDVD() != null) {
                int idMedia = productController.createMedia(media);
                if (idMedia > 0 && productController.createDVD(dvd, idMedia)) {
                    PopupScreen.success("Create dvd success");
                } else {
                    PopupScreen.error("DVD creation error");
                }
            } else if(validateMedia("dvd") == null) {
                PopupScreen.error("Media data form error");
            } else if(validateDVD() == null) {
                PopupScreen.error("DVD data form error");
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void createCD() {
        try {
            if (validateMedia("cd") != null && validateCD() != null) {
                int idMedia = productController.createMedia(media);
                if (idMedia > 0 && productController.createCD(cd, idMedia)) {
                    PopupScreen.success("Create cd success");
                } else {
                    PopupScreen.error("CD creation error");
                }
            } else if(validateMedia("cd") == null) {
                PopupScreen.error("Media data form error");
            } else if(validateCD() == null) {
                PopupScreen.error("CD data form error");
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    private Media validateMedia(String type) {
        try {
            if (title.getText().length() <= 45 && isNonEmptyString(title.getText()) && category.getText().length() <= 45 && isNonEmptyString(category.getText())
                    && isValidInteger(price.getText()) && isValidInteger(quantity.getText()) && isValidInteger(value.getText()) && image != null && image.getUrl() != null) {
                media.setTitle(title.getText());
                media.setCategory(category.getText());
                media.setType(type);
                media.setPrice(Integer.parseInt(price.getText()));
                media.setQuantity(Integer.parseInt(quantity.getText()));
//                media.setMediaURL(image.getUrl());
                media.setMediaURL(destinationFilePath);
                media.setValue(Integer.parseInt(value.getText()));
                return media;
            } else {
                return null;
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    private Book validateBook() {
        try {
            if (textField1.getText().length() <= 45 && isNonEmptyString(textField1.getText()) && textField2.getText().length() <= 45 && isNonEmptyString(textField2.getText())
                    && textField3.getText().length() <= 45 && isNonEmptyString(textField3.getText()) && textField5.getText().length() <= 45 && isNonEmptyString(textField5.getText())
                    && textField6.getText().length() <= 45 && isNonEmptyString(textField6.getText()) && textField7.getText().length() <= 45 && isNonEmptyString(textField7.getText())) {
                book.setAuthor(textField1.getText());
                book.setCoverType(textField2.getText());
                book.setPublisher(textField3.getText());
                book.setPublishDate(Date.valueOf(datePicker.getValue()));
                book.setNumOfPages(Integer.parseInt(textField5.getText()));
                book.setLanguage(textField6.getText());
                book.setBookCategory(textField7.getText());
                return book;
            } else {
                return null;
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    private DVD validateDVD() {
        try {
            if (textField1.getText().length() <= 45 && isNonEmptyString(textField1.getText()) && textField2.getText().length() <= 45 && isNonEmptyString(textField2.getText())
                    && textField3.getText().length() <= 45 && isNonEmptyString(textField3.getText()) && textField5.getText().length() <= 45 && isNonEmptyString(textField5.getText())
                    && textField6.getText().length() <= 45 && isNonEmptyString(textField6.getText()) && textField7.getText().length() <= 45 && isNonEmptyString(textField7.getText())) {
                dvd.setDiscType(textField1.getText());
                dvd.setDirector(textField2.getText());
                dvd.setRuntime(Integer.parseInt(textField3.getText()));
                dvd.setReleasedDate(Date.valueOf(datePicker.getValue()));
                dvd.setStudio(textField5.getText());
                dvd.setSubtitles(textField6.getText());
                dvd.setFilmType(textField7.getText());
                return dvd;
            } else {
                return null;
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    private CD validateCD() {
        try {
            if (textField1.getText().length() <= 45 && isNonEmptyString(textField1.getText()) && textField2.getText().length() <= 45 && isNonEmptyString(textField2.getText())
                    && textField3.getText().length() <= 45 && isNonEmptyString(textField3.getText())) {
                cd.setArtist(textField1.getText());
                cd.setRecordLabel(textField2.getText());
                cd.setMusicType(textField3.getText());
                cd.setReleasedDate(Date.valueOf(datePicker.getValue()));
                return cd;
            } else {
                return null;
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isNonEmptyString(String str) {
        return str != null && !str.trim().isEmpty();
    }

}
