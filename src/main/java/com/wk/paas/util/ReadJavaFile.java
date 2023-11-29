package com.wk.paas.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ReadJavaFile {

    public static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static void writeFile(String content, String filePath) {
        File file = new File(filePath); // 创建一个文件对象

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content); // 将字符串写入文件
            writer.flush(); // 刷新缓冲区
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建文件夹
     *
     * @param outputUrl 输出路径
     */
    public static void createFolder(String outputUrl) {
        String dir = outputUrl.substring(0, outputUrl.lastIndexOf("/"));
        File folder = new File(dir);
        //判断文件夹是否存在,不存在则创建
        if (!folder.exists() && !folder.mkdirs()) {
            log.error("创建文件夹失败！");
        }
    }

    public static void main(String[] args) {
        String filePath = "path/to/your/file.java";
        String content = readFile(filePath);
        System.out.println(content);
    }

}