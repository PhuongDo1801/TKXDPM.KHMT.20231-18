package views.screen.product;

import controller.ProductController;
import entity.media.Book;
import entity.media.CD;
import entity.media.DVD;
import entity.media.Media;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * lớp này thỏa mãn Single Resposibility Principle - SRP vì các phương thức bên trong đều có chung mục đích
 * thỏa mãn Liskov Substitution Principle - LSP vì MediaHandler nó có thể thay thế được cho FXMLScreenHandler
 */

public class UpdateProductHandler extends BaseScreenHandler implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextField category;
    @FXML
    private Label type;
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
    protected Button updateBtn;
    @FXML
    protected Button uploadImageBtn;
    @FXML
    protected ImageView imageView;

    private static Logger LOGGER = Utils.getLogger(UpdateProductHandler.class.getName());
    private String selectedType;
    private ProductController productController;
    private Media media;

    private Book book;
    private DVD dvd;
    private CD cd;
    private Image image;
    private int idMedia;
    private String destinationFilePath="";
    private int value_old;

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

            selectedType = type.getText().toUpperCase();
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

    public UpdateProductHandler(Stage stage, String screenPath, Media media) throws IOException {
        super(stage, screenPath);
        setMedia(media);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.productController = new ProductController();

        datePicker.setEditable(false);

        updateBtn.setOnAction(this::handleUpdateButtonClick);

        try {
            this.media = new Media();
            this.book = new Book();
            this.dvd = new DVD();
            this.cd = new CD();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setMedia(Media media) {
        System.out.println(media.toString());
        this.idMedia = media.getId();
        File file = new File(media.getImageURL());
        Image image = new Image(file.toURI().toString());
        System.out.println(image);
        destinationFilePath = media.getImageURL();
        try {
            title.setText(media.getTitle());
            category.setText(media.getCategory());
            price.setText(String.valueOf(media.getPrice()));
            quantity.setText(String.valueOf(media.getQuantity()));
            value.setText(String.valueOf(media.getValue()));
            imageView.setImage(image);
            type.setText(media.getType());
            value_old = (media.getValue());
        } catch (SQLException exp) {
            exp.printStackTrace();
        }

        selectedType = media.getType().toUpperCase();
        try {
            switch (selectedType) {
                case "BOOK":
                    setLabelsVisibleBook(productController.getBookById(idMedia));
//                    System.out.println(productController.getBookById(99).toString());;
                    break;
                case "DVD":
                    setLabelsVisibleDVD(productController.getDVDById(idMedia));
                    break;
                case "CD":
                    setLabelsVisibleCD(productController.getCDById(idMedia));
                    break;
                default:
                    setLabelsVisible();
                    break;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    private void handleUpdateButtonClick(ActionEvent event) {
        selectedType = type.getText().toUpperCase();
        try {
            switch (selectedType) {
                case "BOOK":
                    System.out.println("update book");
                    updateBook();
                    break;
                case "DVD":
                    System.out.println("update DVD");
                    updateDVD();
                    break;
                case "CD":
                    System.out.println("update cd");
                    updateCD();
                    break;
                default:
//                    setLabelsVisible();
                    PopupScreen.error("There was an error while updating");
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

    private void setLabelsVisibleBook(Book book) {
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

        if (book != null) {
            textField1.setText(book.getAuthor());
            textField2.setText(book.getCoverType());
            textField3.setText(book.getPublisher());
            java.util.Date utilDate = book.getPublishDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            LocalDate localDate = sqlDate.toLocalDate();
            datePicker.setValue(localDate);
            textField5.setText(String.valueOf(book.getNumOfPages()));
            textField6.setText(book.getLanguage());
            textField7.setText(book.getBookCategory());
        }

    }

    private void setLabelsVisibleDVD(DVD dvd) {
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

        if(dvd != null) {
            textField1.setText(dvd.getDiscType());
            textField2.setText(dvd.getDirector());
            textField3.setText(String.valueOf(dvd.getRuntime()));
            java.util.Date utilDate = dvd.getReleasedDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            LocalDate localDate = sqlDate.toLocalDate();
            datePicker.setValue(localDate);
            textField5.setText(dvd.getStudio());
            textField6.setText(dvd.getSubtitles());
            textField7.setText(dvd.getFilmType());
        }
    }

    private void setLabelsVisibleCD(CD cd) {
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

        if(cd != null) {
            textField1.setText(cd.getArtist());
            textField2.setText(cd.getRecordLabel());
            textField3.setText(cd.getMusicType());
            java.util.Date utilDate = cd.getReleasedDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            LocalDate localDate = sqlDate.toLocalDate();
            datePicker.setValue(localDate);
        }
    }

    private void updateBook() {
        try {
//            int checkCanEdit = productController.checkValidUpdate(1, media.getId());
            int checkCanEdit = 1;
            if (checkCanEdit == 1) {
                if(!validateMedia("book")) {
                    PopupScreen.error("Media data form error");
                } else if(!validateBook()) {
                    PopupScreen.error("Book data form error");
                } else if (validateMedia("book")  && validateBook() ) {
                    if (productController.updateMediaById(idMedia, media) && productController.updateBookById(idMedia, book)) {
                        PopupScreen.success("Update book success");
                    } else {
                        PopupScreen.error("Book update error");
                    }
                }

            }else if (checkCanEdit == 2){
                PopupScreen.error("Only a maximum of 30 update operations per day can be performed");
                LOGGER.info("Error update on update btn checkCanEdit == 2");
            } else if (checkCanEdit == 3) {
                PopupScreen.error("You have update 30 products today");
                LOGGER.info("Error update on delete btn checkCanEdit == 3");
            } else if (checkCanEdit == -1) {
                PopupScreen.error("An error occurred while update");
                LOGGER.info("Error update on update btn checkCanEdit == -1");
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void updateDVD() {
        try {
            int checkCanEdit = 1;
            //int checkCanEdit = productController.checkValidUpdate(1, media.getId());
            if (checkCanEdit == 1) {
                if(!validateMedia("dvd") ) {
                    PopupScreen.error("Media data form error");
                } else if(validateDVD() == null) {
                    PopupScreen.error("DVD data form error");
                } else if (validateMedia("dvd")  && validateDVD() != null) {
                    if (productController.updateMediaById(idMedia, media) && productController.updateDVDId(idMedia, dvd)) {
                        PopupScreen.success("Update dvd success");
                    } else {
                        PopupScreen.error("DVD update error");
                    }
                }

            }else if (checkCanEdit == 2){
                PopupScreen.error("Only a maximum of 30 update operations per day can be performed");
                LOGGER.info("Error update on update btn checkCanEdit == 2");
            } else if (checkCanEdit == 3) {
                PopupScreen.error("You have update 30 products today");
                LOGGER.info("Error update on delete btn checkCanEdit == 3");
            } else if (checkCanEdit == -1) {
                PopupScreen.error("An error occurred while update");
                LOGGER.info("Error update on update btn checkCanEdit == -1");
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void updateCD() {
        try {
            int checkCanEdit = 1;
            // int checkCanEdit = productController.checkValidUpdate(1, media.getId());
            if (checkCanEdit == 1) {
                if(!validateMedia("cd")) {
                    PopupScreen.error("Media data form error");
                } else if(validateCD() == null) {
                    PopupScreen.error("CD data form error");
                } else if (validateMedia("cd")  && validateCD() != null) {
                    if (productController.updateMediaById(idMedia, media) && productController.updateCDById(idMedia, cd)) {
                        PopupScreen.success("Update cd success");
                    } else {
                        PopupScreen.error("CD update error");
                    }
                }

            }else if (checkCanEdit == 2){
                PopupScreen.error("Only a maximum of 30 update operations per day can be performed");
                LOGGER.info("Error update on update btn checkCanEdit == 2");
            } else if (checkCanEdit == 3) {
                PopupScreen.error("You have update 30 products today");
                LOGGER.info("Error update on delete btn checkCanEdit == 3");
            } else if (checkCanEdit == -1) {
                PopupScreen.error("An error occurred while update");
                LOGGER.info("Error update on update btn checkCanEdit == -1");
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    private boolean validateMedia(String type) {
        try {
            if (title.getText().length() <= 45 && isNonEmptyString(title.getText()) && category.getText().length() <= 45 && isNonEmptyString(category.getText())
                    && isValidInteger(price.getText())  && validPrice(Integer.parseInt(price.getText()), Integer.parseInt(value.getText())) && isValidInteger(quantity.getText()) && isValidInteger(value.getText()) && imageView != null ) {
                media.setTitle(title.getText());
                media.setCategory(category.getText());
                media.setType(type);
                media.setPrice(Integer.parseInt(price.getText()));
                media.setQuantity(Integer.parseInt(quantity.getText()));
//                media.setMediaURL(imageView.getImage().getUrl());
                media.setMediaURL(destinationFilePath);
                media.setValue(Integer.parseInt(value.getText()));

                return true;
            } else {
                return false;
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return false;
    }

    private boolean validateBook() {
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
                return true;
            } else {
                return false;
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return false;
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

    private boolean validPrice(int price, int value) {
        try {
            if(price >= 0.3 * value && price <= 1.5 * value) {
                return true;
            } else {
                PopupScreen.error("The product price is in error because the price is " + price + " and the value is " + value);
                return false;
            }


        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return false;
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
