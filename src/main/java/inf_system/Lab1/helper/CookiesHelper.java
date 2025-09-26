package inf_system.Lab1.helper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookiesHelper {

    public static String getField(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        String userId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }
        return userId;
    }

    public static void updateField(HttpServletResponse response, String key, String value, int durationMS) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(durationMS);
        response.addCookie(cookie);
    }

    public static void deleteField(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
