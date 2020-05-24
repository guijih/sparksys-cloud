package com.sparksys.gateway.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;


/**
 * description: 常用Controller
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:18:02
 */
@Controller
public class GeneratorController {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @GetMapping("/gate/doc.html")
    public Rendering doc() {
        String uri = String.format("%s/doc.html", contextPath);
        return Rendering.redirectTo(uri).build();
    }
}
