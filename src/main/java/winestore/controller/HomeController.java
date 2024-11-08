package winestore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        System.out.println("HOME! w/ thymleaf ");
        return "index"; // This resolves to index.html in src/main/resources/templates
    }
}
