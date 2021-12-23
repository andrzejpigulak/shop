package pl.andrzej.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.andrzej.shop.flyweight.generic.GenericFactory;
import pl.andrzej.shop.flyweight.generic.strategy.email.EmailGeneratorStrategy;
import pl.andrzej.shop.flyweight.generic.strategy.file.FileGeneratorStrategy;
import pl.andrzej.shop.flyweight.model.EmailType;
import pl.andrzej.shop.flyweight.model.FileType;
import pl.andrzej.shop.flyweight.standard.GeneratorFactory;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/files")
public class FileController {

    private final GeneratorFactory generatorFactory;
    private final GenericFactory<FileType, FileGeneratorStrategy> genericFactory;
    private final GenericFactory<EmailType, EmailGeneratorStrategy> genericEmailFactory;

    @GetMapping
    public void getFile(@RequestParam FileType fileType) {
        generatorFactory.getByType(fileType).generateFile();
    }

    @GetMapping("/generic")
    public ResponseEntity<byte[]> getGenericFile(@RequestParam FileType fileType) {
        byte[] file = genericFactory.getByType(fileType).generateFile();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=report." + fileType.name().toLowerCase());
        return ResponseEntity.ok().headers(headers).body(file);
    }

    @GetMapping("/email")
    public void getGenericEmail(@RequestParam EmailType emailType) {
        genericEmailFactory.getByType(emailType).generateEmail();
    }


}
