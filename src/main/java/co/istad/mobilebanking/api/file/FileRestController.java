package co.istad.mobilebanking.api.file;

import co.istad.mobilebanking.base.BaseRest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@Slf4j
@RequiredArgsConstructor
public class FileRestController {

    private final FileService fileService;

    @GetMapping("/download/{name}")
    public ResponseEntity<?> download(@PathVariable String name) {
       Resource resource = fileService.download(name);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header("Content-Disposition",
                        "attachment; filename=" + resource.getFilename())
                .body(resource);
    }

    @DeleteMapping("/{name}")
    public void deleteByName(@PathVariable String name) {
        fileService.deleteByName(name);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/{name}")
    public BaseRest<?> findByName(@PathVariable String name) throws IOException {
        FileDto fileDto = fileService.findByName(name);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("File has been found")
                .data(fileDto)
                .build();

    }

    @PostMapping
    public BaseRest<?>  uploadSingle(@RequestPart MultipartFile file) {
      log.info("Uploading file request = {}", file);
      FileDto fileDto = fileService.uploadSingle(file);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("File has been uploaded")
                .data(fileDto)
                .build();

    }

    @PostMapping("/multiple")
    public BaseRest<?> uploadMultiple(@RequestPart List<MultipartFile> files) {
        log.info("Uploading files request = {}", files);
        List<FileDto> filesDto = fileService.uploadMultiple(files);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("File has been uploaded")
                .data(filesDto)
                .build();
    }

}
