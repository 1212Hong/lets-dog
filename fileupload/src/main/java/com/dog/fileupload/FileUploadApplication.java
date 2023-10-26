package com.dog.fileupload;

import com.dog.fileupload.service.FileStorageService;
import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcAuditing
@EnableR2dbcRepositories
@SpringBootApplication
public class FileUploadApplication implements CommandLineRunner {

    @Resource
    FileStorageService fileStorageService;

    public static void main(String[] args) {
        SpringApplication.run(FileUploadApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        fileStorageService.init();
    }
}
