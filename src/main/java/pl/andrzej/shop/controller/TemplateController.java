package pl.andrzej.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.andrzej.shop.model.dao.Template;
import pl.andrzej.shop.service.TemplateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/template")
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping
    void saveTemplate(@RequestBody Template template) {
        templateService.save(template);
    }
}
