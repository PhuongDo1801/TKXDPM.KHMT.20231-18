package controller;

import entity.media.Book;
import entity.media.CD;
import entity.media.DVD;
import entity.media.Media;
import entity.operation.Operation;
import entity.user.User;

import java.sql.SQLException;
import java.util.List;


public class UserController extends BaseController{
    public List getAllUser() {
        try {
            return new User().getAllUser();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
