package com.wk.paas.util;

import java.io.File;

public class FilePathUtils {
    public static String getParentDirectory(String filePath) {
        if (filePath == null) {
            return null;
        }
        int lastSeparatorIndex = filePath.lastIndexOf(File.separator);
        if (lastSeparatorIndex != -1) {
            return filePath.substring(0, lastSeparatorIndex);
        }
        return filePath;
    }
}