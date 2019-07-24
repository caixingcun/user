package com.test.util;

import java.io.*;

public class FileUtil {
    /**
     *  生成文件
     *  @param fileName
     */
    public static boolean createFile(File fileName){
        if(!fileName.exists()){
            try {
                fileName.createNewFile();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * @description 读文件
     * @throws IOException
     */
    public static String readTxtFile(File fileName) throws IOException {
        String result = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        fileReader = new FileReader(fileName);
        bufferedReader = new BufferedReader(fileReader);

        String read = "";
        while((read = bufferedReader.readLine()) != null){
            result =  read + "\r\n";
        }

        if(bufferedReader != null){
            bufferedReader.close();
        }

        if(fileReader != null){
            fileReader.close();
        }

        System.out.println("¶ÁÈ¡ÎÄ¼þµÄÄÚÈÝÊÇ£º " + "\r\n" + result);
        return result;
    }

    /**
     * @description 写文件
     * @param content
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static boolean writeTxtFile(String content,File fileName) throws UnsupportedEncodingException, IOException{
        FileOutputStream o = null;
        o = new FileOutputStream(fileName);
        o.write(content.getBytes("UTF-8"));
        o.close();
        return true;
    }


}


