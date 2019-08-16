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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class PicController {
    //    public static final String PICTURE_PATH = "D:\\picsoup\\picture\\";
//    public static final String PICTURE_PATH = "D:\\picsoup\\picture\\";
    public static final String PICTURE_PATH = "/pic/";


    /**
     * 获取图片子文件夹
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping("/api/pics/dirs")
    public String pic_sub_dir() {
        File file = new File(PICTURE_PATH);
        File[] files = file.listFiles();
        List<String> childDirNames = Stream.of(files)
                .map(file1 -> file1.getName())
                .collect(Collectors.toList());
        return new Gson().toJson(childDirNames);
    }


    /**
     * 获取子文件夹图片
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping("/api/pics/dir/{dir}")
    public String pics(@PathVariable("dir") String dir) throws ParseException {
        File file = new File(PICTURE_PATH + dir);
        File[] files = file.listFiles();
        List<String> childDirNames = Stream.of(files)
                .map(file1 -> file1.getName())
                .collect(Collectors.toList());
        return new Gson().toJson(childDirNames);
    }


    @RequestMapping(path = "/api/pic/{pic_name}", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> exportPic(@PathVariable("pic_name") String fileName) throws Exception {
        List<String> picList = getPicList();
        System.out.println(fileName);
        List<String> pics = picList.stream()
                .filter(s -> {
                    System.out.println(s);
                    return s.contains(fileName);
                })
                .collect(Collectors.toList());

        if (pics.size() == 0) {
            throw new BadRequestException("当前图片路径不存在1");
        }


        File imgFile = new File(pics.get(0));
        if (!imgFile.exists()) {
            throw new BadRequestException("当前图片路径不存在2");
        }
        return export(imgFile);
    }

    private List<String> getPicList() {
        File file = new File(PICTURE_PATH);
        File[] dirs = file.listFiles();
        List<String> pics = new ArrayList<>();
        for (File dir : dirs) {
            File[] picsTemplate = dir.listFiles();
            List<String> picsPath = Stream.of(picsTemplate)
                    .map(file1 -> file1.getAbsolutePath())
                    .collect(Collectors.toList());
            pics.addAll(picsPath);
        }
        return pics;
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
