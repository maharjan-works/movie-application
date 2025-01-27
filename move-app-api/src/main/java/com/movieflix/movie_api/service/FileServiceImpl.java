package com.movieflix.movie_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService{

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        logger.info("uploadFile() initiated.");
        //get name of the file
        String fileName = file.getOriginalFilename();

        //get the file path
        String filePath = path + File.separator + fileName;  // posters/*.img

        //create a file object
        File f = new File(path);
        logger.info("File Object Name: "+ f.getName());
        if(!f.exists()){
            f.mkdir();
        }

        //copy the file or upload the file to the filePath
        Files.copy(file.getInputStream(), Paths.get(filePath));

        logger.info("uploadFile() finished");
        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }
}
