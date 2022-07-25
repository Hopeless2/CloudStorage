package com.example.cloudstorage.controller.app;

import com.example.cloudstorage.entity.FileEntity;
import com.example.cloudstorage.exception.FileException;
import com.example.cloudstorage.exception.MyAuthException;
import com.example.cloudstorage.model.ExceptionRequest;
import com.example.cloudstorage.service.app.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@Validated
@Slf4j
public class FileController {
    private static final String FILE = "/file";
    private static final String LIST = "/list";

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping(value = FILE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public void uploadFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename, @RequestBody MultipartFile file) throws IOException {
        fileService.uploadFile(authToken, filename, file);
    }

    @GetMapping(value = FILE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename) {
        FileEntity file = fileService.downloadFile(authToken, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @GetMapping(LIST)
    public List<FileEntity> getFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {
        return fileService.getFiles(authToken, limit);
    }

    @DeleteMapping(FILE)
    public void deleteFile(@RequestHeader("auth-token") String token, @RequestParam("filename") String fileName) {
        fileService.deleteFile(token, fileName);
    }

    @PutMapping(value = FILE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void editFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename, @RequestBody Map<String, String> bodyParams) {
        fileService.renameFile(authToken, filename, bodyParams.get("filename"));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MyAuthException.class)
    ExceptionRequest handleMyAuthException(MyAuthException e) {
        log.error(e.getMessage());
        return new ExceptionRequest(e.getMessage(), 401);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileException.class)
    ExceptionRequest handleFileException(FileException e) {
        log.error(e.getMessage());
        return new ExceptionRequest(e.getMessage(), 400);
    }
}
