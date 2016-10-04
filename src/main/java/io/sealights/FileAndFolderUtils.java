package io.sealights;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileAndFolderUtils {

    public static boolean isFolderExists(String folder) {
        File f = new File(folder);
        if (!f.exists()) {
            return false;
        }

        if (!f.isDirectory()) {
            throw new RuntimeException("'" + folder + "' is not a folder.");
        }

        return true;
    }

    public static boolean createFolder(String folder) {
        if (isFolderExists(folder))
            return false;

        File f = new File(folder);
        return f.mkdir();
    }

    public static void verifyFolderExists(String folder) {
        if (!isFolderExists(folder)) {
            createFolder(folder);
        }
    }

    public static File getOrCreateFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    public static void writeAllTextToFile(String text, String fileName) {
        PrintWriter file = null;
        try {
            File fileObj = new File(fileName);
            if (fileObj.exists()) {
                fileObj.delete();
            }
            fileObj.createNewFile();

            file = new PrintWriter(fileObj);
            file.println(text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null)
                file.close();
        }

    }

    public static void writeAllTextToZipFile(String text, String filename) throws IOException {
        FileOutputStream dest = new FileOutputStream(filename);
        GZIPOutputStream gzip = new GZIPOutputStream(dest);
        gzip.write(text.getBytes());
        gzip.close();
    }


    public static String getFileExtension(String file) {
        String extension = "";
        int lastDot = file.lastIndexOf('.');
        if (lastDot > 0) {
            extension = file.substring(lastDot + 1);
        }
        return extension;
    }

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
