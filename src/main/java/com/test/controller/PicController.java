package com.test.controller;

import com.google.gson.Gson;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class PicController {
//    public static final String PICTURE_PATH = "D:\\picsoup\\picture\\";
//    public static final String PICTURE_PATH = "D:\\picsoup\\picture\\";
    public static final String PICTURE_PATH = "/pic/";

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
        return "provider: hello"+name;
    }


    @RequestMapping(value = "/dates/{date}",method = RequestMethod.GET)
    public String dates(@PathVariable("date") String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.parse(date);
        List<String> list = new ArrayList<>();
        List<String> abcd = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
        for (String s : abcd) {
            String s1 = date + "-" + s;
            File file = new File(PICTURE_PATH+ s1);
            if (file.exists()) {
                list.add(s1);
            }
        }

        return new Gson().toJson(list);
    }

    @RequestMapping("/pics/{date_a}")
    public String pics(@PathVariable("date_a") String date_a) throws ParseException {
        List<String> list = new ArrayList<>();
        File file = new File(PICTURE_PATH + date_a);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                list.add(file1.getName());
            }
        }

        return new Gson().toJson(list);
    }

    @RequestMapping(path = "pic/{date_a}/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> exportPic(@PathVariable("date_a") String date_a, @PathVariable("fileName") String fileName) throws Exception {
        System.out.println(new File(PICTURE_PATH+ date_a, fileName).getAbsolutePath()+" ---");
        File file = new File(PICTURE_PATH + date_a, fileName.endsWith(".jpg") ? fileName : fileName + ".jpg");
        if (!file.exists()) {
            throw new Exception("当前图片路径不存在");
        }
        return export(file);
    }


    public ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + System.currentTimeMillis() + ".jpg");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_JPEG)
                .body(new FileSystemResource(file));
    }

}
