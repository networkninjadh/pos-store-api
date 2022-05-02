package com.howtech.posstoreapi.services;

import com.howtech.posstoreapi.clients.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StorageService {

    private final StorageClient storageClient;

    public StorageService(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public String uploadStoreLogo(Long storeId, MultipartFile file, String username) throws IOException {

        return storageClient.uploadStoreLogo(storeId, file, username);
    }
}
