package controller;

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

    public boolean createUser(User user) {
        try {
            return new User().createUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}