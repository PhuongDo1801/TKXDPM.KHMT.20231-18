package controller;

import java.sql.SQLException;
import java.util.List;

import entity.cart.Cart;
import entity.media.Media;

/**
 * This class controls the flow of events in homescreen
 * @author phuongdo
 * 
 * Thảo mãn SRP vì có 1 hàm duy nhất là getAllMedia()
 * Thoả mãn LSP
 */
public class HomeController extends BaseController{


    /**
     * this method gets all Media in DB and return back to home to display
     * @return List[Media]
     * @throws SQLException
     * Control Coupling
     * Phân tích tính conhesion:
     * - Funtional conhesion: hàm thực hiện chức năng lấy toàn bộ Media từ csdl
     * - Procedural conhesion: hàm thực hiện lấy dư liệu từ database
     */
    public List getAllMedia() throws SQLException{
        return new Media().getAllMedia();
    }

}
