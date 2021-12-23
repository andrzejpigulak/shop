package pl.andrzej.shop.helper;

import org.springframework.stereotype.Component;
import pl.andrzej.shop.function.BiConsumerThrowable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class FileHelper {

    public BiConsumerThrowable<InputStream, Path, IOException> fileCopy() {
        return (inputStream, path) -> Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }

}
