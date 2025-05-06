package co.istad.mobilebanking.ulti;

import co.istad.mobilebanking.api.file.FileDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Component
@Getter
public class FileUtil {

    @Value("${file.server-path}")
    public String fileServerPath;

    @Value("${file.base-url}")
    public String fileBaseUrl;

    @Value("${file.download-url}")
    private String fileDownloadUrl;

    public void deleteByName(String name){
        Path path = Paths.get(fileServerPath + name);
        try {
            boolean isDeleted = Files.deleteIfExists(path);
            if (!isDeleted){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "File not found");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File is filed to delete");
        }
    }

    public Resource findByName(String name) {
        Path path = Paths.get(fileServerPath + name);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File not found or not readable: " + name
                );
            }
            return resource;

        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Malformed file URL: " + name,
                    e);
        }
    }

    public FileDto upload(MultipartFile file) {

//        String extension = getExtension(file.getOriginalFilename());

        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));

        long size = file.getSize();

        String name = String.format("%s.%s", UUID.randomUUID(), extension);

        String url = String.format("%s%s", fileBaseUrl, name);

        Path path = Paths.get(fileServerPath + name);

        try {
            Files.copy(file.getInputStream(),path);
            return FileDto.builder()
                    .name(name)
                    .url(url)
                    .downloadUrl (String.format("%s%s", fileDownloadUrl, name))
                    .extension(extension)
                    .size(size)
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Upload file failed...!");
        }

    }

    public String getExtension(String fileName){

        int lastDotIndex = fileName.lastIndexOf(".");

        return fileName.substring(lastDotIndex + 1);

    }

}
