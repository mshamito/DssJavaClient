package ru.cryptopro.support.DssJavaClient.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class SwaggerRedirectController {
    @RequestMapping("/swagger")
    @Hidden
    public void redirect (HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/index.html");
    }
}