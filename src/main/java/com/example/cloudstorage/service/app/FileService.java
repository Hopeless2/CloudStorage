package com.example.cloudstorage.service.app;

import com.example.cloudstorage.entity.FileEntity;
import com.example.cloudstorage.exception.*;
import com.example.cloudstorage.repository.CloudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final CloudRepository cloudRepository;

    @Autowired
    public FileService(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public FileEntity downloadFile(String authToken, String fileName) {
        return cloudRepository.downloadFile(authToken, fileName).orElseThrow(() -> new DownloadFileException("Error download file " + fileName));
    }

    public List<FileEntity> getFiles(String authToken, int limit) {
        return cloudRepository.getFiles(authToken, limit).orElseThrow(() -> new ListFileException("Error getting file list"));
    }

    public void uploadFile(String authToken, String fileName, MultipartFile file) throws IOException {
        FileEntity cloudFilePOJO = new FileEntity(fileName, file.getContentType(), file.getBytes(), file.getSize());
        cloudRepository.uploadFile(cloudFilePOJO, authToken).orElseThrow(() -> new UploadFileException("Couldn't save the file " + fileName));
    }

    public void deleteFile(String authToken, String fileName) {
        cloudRepository.deleteFile(authToken, fileName);
    }

    public void renameFile(String authToken, String fileName, String newFileName) {
        cloudRepository.renameFile(authToken, fileName, newFileName).orElseThrow(() -> new RenameFileException("Error edit file " + fileName));
    }

}
