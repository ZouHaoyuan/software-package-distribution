package com.controller;

import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;

@Controller
public class DownloadController {
    @Autowired
    UserService userService;

    @RequestMapping("hello")
    @ResponseBody
    public String get(){
        return userService.get();
    }

//    @RequestMapping("/down")
//    public ResponseEntity<byte[]> download2(){
//        //获取文件对象
//        try {
//            byte[] bytes = FileUtils.readFileToByteArray(new File("src/main/webapp/file/git.exe"));
//            HttpHeaders headers=new HttpHeaders();
//            headers.set("Content-Disposition","attachment;filename=test2.png");
//            ResponseEntity<byte[]> entity=new ResponseEntity<>(bytes,headers, HttpStatus.OK);
//            return entity;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
