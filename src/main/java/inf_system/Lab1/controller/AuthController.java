package inf_system.Lab1.controller;

import inf_system.Lab1.services.UserService;
import inf_system.Lab1.helper.CookiesHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth")
    public String authPage(Model model, HttpServletRequest request) {
        String userId = CookiesHelper.getField(request, "userId");
        if (userId != null) {
            return "redirect:main";
        } else {
            return "auth";
        }
    }

    @PostMapping("/auth")
    public String auth(
            @RequestParam("username") String userName,
            @RequestParam("password") String password,
            HttpServletResponse response,
            Model model
    ) {
        Long userId = userService.findOrCreateUser(userName, password);
        System.out.println(userId);
        CookiesHelper.updateField(response, "userId", String.valueOf(userId), 30 * 60); // 30 минут
        CookiesHelper.updateField(response, "userName", userName, 30 * 60); // 30 минут
        return "redirect:main";
    }
}
