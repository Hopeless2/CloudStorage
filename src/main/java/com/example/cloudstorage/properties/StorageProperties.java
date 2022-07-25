package com.example.cloudstorage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@Component
public class StorageProperties {
    private String rootLocation;

    public String getRootLocation() {
        return rootLocation;
    }

    public void setRootLocation(String rootLocation) {
        this.rootLocation = rootLocation;
    }
}
