package inf_system.Lab1.controller;

import inf_system.Lab1.helper.CookiesHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String mainPage(
            Model model,
            HttpServletRequest request
    ) {
        String userId = CookiesHelper.getField(request, "userId");
        String userName = CookiesHelper.getField(request, "userName");
        if (userId != null) {
            model.addAttribute("userId", userId);
            model.addAttribute("userName", userName);
            return "main";
        } else {
            return "redirect:auth";
        }
    }
}
