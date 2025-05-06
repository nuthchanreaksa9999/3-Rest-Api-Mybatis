package co.istad.mobilebanking.api.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface FileService {

    Resource download(String name);

    void deleteByName(String name);

    /**
     * use to find file by name
     * @param name find file by name
     * @return FileDto
     */
    FileDto findByName(String name) throws IOException;

    /**
     * uses to upload single file
     * @param file request form data from client
     * @return FileDto
     */
    FileDto uploadSingle(MultipartFile file);

    /**
     * uses to upload multiple files
     * @param files request form data from client
     * @return FileDto
     */
    List<FileDto> uploadMultiple(List<MultipartFile> files);

}
