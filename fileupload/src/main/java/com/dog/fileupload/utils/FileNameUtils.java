package com.dog.fileupload.utils;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

@Slf4j
public class FileNameUtils {

    public final static String BASE = "base";
    public final static String EXTENSION = "extension";

    public static String convert(String originalName) {
        String base = getBaseOrExtension(originalName, BASE);
        String extension = getBaseOrExtension(originalName, EXTENSION);
        return base + UUID.randomUUID() + extension;
    }

    public static String getBaseOrExtension(String fileName, String part) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        if (part.equals(BASE)) {
            return fileName.substring(0, dotIndex);
        }
        return fileName.substring(dotIndex);
    }

    public static MediaType getMediaTypeForFileName(String extension) {
        if (".mp4".equalsIgnoreCase(extension)) {
            return MediaType.valueOf("video/mp4");
        } else if (".jpg".equalsIgnoreCase(extension) || ".jpeg".equalsIgnoreCase(extension)) {
            return MediaType.IMAGE_JPEG;
        }

        return MediaType.APPLICATION_OCTET_STREAM;

    }

}
