package com.test.controller;

import com.google.gson.Gson;
import com.test.exception.BadRequestException;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class PicController {
    //    public static final String PICTURE_PATH = "D:\\picsoup\\picture\\";
//    public static final String PICTURE_PATH = "D:\\picsoup\\picture\\";
    public static final String PICTURE_PATH = "pic"+File.separator;


    /**
     * 获取图片子文件夹
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping("/pic/dirs")
    public String pic_sub_dir() throws ParseException {
        File file = new File(PICTURE_PATH);
        File[] files = file.listFiles();
        List<String> childDirNames = Stream.of(files)
                .map(file1 -> file1.getName())
                .collect(Collectors.toList());
        return new Gson().toJson(childDirNames);
    }


    /**
     * 获取图片子文件夹
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping("/pic/dir/{dir}}")
    public String pics(@PathVariable("dir") String dir) throws ParseException {
        File file = new File(PICTURE_PATH + dir);
        File[] files = file.listFiles();
        List<String> childDirNames = Stream.of(files)
                .map(file1 -> file1.getName())
                .collect(Collectors.toList());
        return new Gson().toJson(childDirNames);
    }


    @RequestMapping(path = "/pic/dir/pic/{dir}/{file}", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> exportPic(@PathVariable("dir") String dir, @PathVariable("file") String file) throws Exception {

        File imgFile = new File(PICTURE_PATH + dir + File.separator + file);
        if (!imgFile.exists()) {
            throw new BadRequestException("当前图片路径不存在");
        }
        return export(imgFile);
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
