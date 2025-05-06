package co.istad.mobilebanking.api.file;

import co.istad.mobilebanking.ulti.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${file.download-url}")
    private String fileDownloadUrl;

    private FileUtil fileUtil;

    @Autowired
    public void setFileUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }


    @Override
    public Resource download(String name) {
        return fileUtil.findByName(name);
    }

    @Override
    public void deleteByName(String name) {
         fileUtil.deleteByName(name);
    }

    @Override
    public FileDto findByName(String name) throws IOException {
        Resource resource = fileUtil.findByName(name);
        return FileDto.builder()
                .name(resource.getFilename())
                .extension(fileUtil.getExtension(Objects.requireNonNull(resource.getFilename())))
                .url(String.format("%s%s", fileUtil.getFileBaseUrl(), resource.getFilename()))
                .downloadUrl (String.format("%s%s", fileDownloadUrl, name))
                .size(resource.contentLength())
                .build();
    }

    @Override
    public FileDto uploadSingle(MultipartFile file) {

       return fileUtil.upload(file);

    }

    @Override
    public List<FileDto> uploadMultiple(List<MultipartFile> files) {

        List<FileDto> filesDto = new ArrayList<>();

      for (MultipartFile file : files) {
        filesDto.add(fileUtil.upload(file));
      }
      return filesDto;
    }
}
