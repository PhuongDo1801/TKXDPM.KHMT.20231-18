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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * lớp này thỏa mãn Single Resposibility Principle - SRP vì các phương thức bên trong đều có chung mục đích
 * thỏa mãn Liskov Substitution Principle - LSP vì MediaHandler nó có thể thay thế được cho FXMLScreenHandler
 */

public class DetailProductHandler extends BaseScreenHandler implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label category;
    @FXML
    private Label type;
    @FXML
    private Label price;
    @FXML
    private Label quantity;
    @FXML
    private Label value;
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
    private Label textField1;
    @FXML
    private Label textField2;
    @FXML
    private Label textField3;
    @FXML
    private Label textField4;
//    @FXML
//    private DatePicker datePicker;
    @FXML
    private Label textField5;
    @FXML
    private Label textField6;
    @FXML
    private Label textField7;

    @FXML
    protected ImageView imageView;

    private static Logger LOGGER = Utils.getLogger(DetailProductHandler.class.getName());
    private String selectedType;
    private ProductController productController;
    private Media media;

    private Book book;
    private DVD dvd;
    private CD cd;
    private Image image;
    private int idMedia;


    public DetailProductHandler(Stage stage, String screenPath, Media media) throws IOException {
        super(stage, screenPath);
        setMedia(media);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.productController = new ProductController();

//        datePicker.setEditable(false);


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
        try {
            title.setText(media.getTitle());
            category.setText(media.getCategory());
            price.setText(String.valueOf(media.getPrice()));
            quantity.setText(String.valueOf(media.getQuantity()));
            value.setText(String.valueOf(media.getValue()));
            imageView.setImage(image);
            type.setText(media.getType());
        } catch (SQLException exp) {
            exp.printStackTrace();
        }

        selectedType = media.getType().toUpperCase();
        try {
            switch (selectedType) {
                case "BOOK":
                    setLabelsVisibleBook(productController.getBookById(idMedia));
                    break;
                case "DVD":
                    setLabelsVisibleDVD(productController.getDVDById(idMedia));
                    break;
                case "CD":
                    setLabelsVisibleCD(productController.getCDById(idMedia));
                    break;
                default:
                    break;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }



    private void setLabelsVisibleBook(Book book) {
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
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = targetFormat.format(utilDate);
            textField4.setText(formattedDate);
            textField5.setText(String.valueOf(book.getNumOfPages()));
            textField6.setText(book.getLanguage());
            textField7.setText(book.getBookCategory());
        }

    }

    private void setLabelsVisibleDVD(DVD dvd) {
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
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = targetFormat.format(utilDate);
            textField4.setText(formattedDate);
            textField5.setText(dvd.getStudio());
            textField6.setText(dvd.getSubtitles());
            textField7.setText(dvd.getFilmType());
        }
    }

    private void setLabelsVisibleCD(CD cd) {
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
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = targetFormat.format(utilDate);
            textField4.setText(formattedDate);
        }
    }



}
