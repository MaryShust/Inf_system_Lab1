package inf_system.Lab1.services;

import inf_system.Lab1.helper.CookiesHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public boolean isUserAuthenticated(HttpServletRequest request) {
        String userId = getUserId(request);
        return userId != null;
    }

    public void authenticateUser(String userName, String password, HttpServletResponse response) {
        Long userId = userService.findOrCreateUser(userName, password);
        CookiesHelper.updateField(response, "userId", String.valueOf(userId), 30 * 60); // 30 минут
        CookiesHelper.updateField(response, "userName", userName, 30 * 60); // 30 минут
    }

    public void clearAuthCookies(HttpServletResponse response) {
        CookiesHelper.deleteField(response, "userId");
        CookiesHelper.deleteField(response, "userName");
    }

    public String getUserId(HttpServletRequest request) {
        return CookiesHelper.getField(request, "userId");
    }

    public String getUserName(HttpServletRequest request) {
        return CookiesHelper.getField(request, "userName");
    }
}