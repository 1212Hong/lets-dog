package com.dog.fileupload.utils;

import java.util.UUID;


public class FileNameConverter {

    private final static String BASE = "base";
    private final static String EXTENSION = "extension";

    public static String convert(String originalName) {
        String base = getBaseOrExtension(originalName, BASE);
        String extension = getBaseOrExtension(originalName, EXTENSION);
        return base + UUID.randomUUID() + extension;
    }

    private static String getBaseOrExtension(String fileName, String part) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        if (part.equals(BASE)) {
            return fileName.substring(0, dotIndex);
        }
        return fileName.substring(dotIndex);
    }
}
