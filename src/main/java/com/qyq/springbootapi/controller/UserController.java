package com.qyq.springbootapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UserController {

    @RequestMapping("/coo")
    public String CooController(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie("name","这是一个cookie");
       System.out.println("..."+cookie.getName()+"...."+cookie.getValue());
       cookie.setMaxAge(24*60*60);
       response.addCookie(cookie);
       return "/caa";
    }

    @RequestMapping("/caa")
    @ResponseBody
    public String CaaController(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("name:"+cookie.getName()+",value:"+cookie.getValue()+",maxAge"+cookie.getMaxAge());
        }
        return request.getMethod();

    }


    @RequestMapping("/file")
    public String fileController(){
        return "index.html";
    }

    @PostMapping("/fileUpload")
    @ResponseBody
    public void upload(@RequestParam(value = "file") MultipartFile file){
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "E://WorkSpace"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
