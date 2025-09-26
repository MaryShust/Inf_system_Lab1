package inf_system.Lab1.controller;

import inf_system.Lab1.helper.CookiesHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String startPage(Model model, HttpServletRequest request) {
        String userId = CookiesHelper.getField(request, "userId");
        if (userId != null) {
            return "redirect:main";
        } else {
            return "redirect:auth";
        }
    }
}
