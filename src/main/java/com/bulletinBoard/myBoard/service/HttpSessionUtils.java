package com.bulletinBoard.myBoard.service;

import com.bulletinBoard.myBoard.domain.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class HttpSessionUtils {

    public static final String USER_SESSION_KEY ="sessionedUser";

    public static boolean isLoginUser(HttpSession session) {
        Object loggedInUser = session.getAttribute(USER_SESSION_KEY);
        if (loggedInUser == null) {
            return false;
        }
        return true;
    }

    public static User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(USER_SESSION_KEY);
    }

}
