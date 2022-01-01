package pl.andrzej.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.andrzej.shop.model.dao.Template;
import pl.andrzej.shop.repository.TemplateRepository;
import pl.andrzej.shop.service.TemplateService;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;

    @Override
    public Template save(Template template) {
        return templateRepository.save(template);
    }

    @Override
    public Template searchByName(String name) {
        return templateRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Template not found"));
    }
}
