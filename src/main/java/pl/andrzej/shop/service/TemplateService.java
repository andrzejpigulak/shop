package pl.andrzej.shop.service;

import pl.andrzej.shop.model.dao.Template;

public interface TemplateService {

    Template save(Template template);

    Template searchByName(String name);
}
