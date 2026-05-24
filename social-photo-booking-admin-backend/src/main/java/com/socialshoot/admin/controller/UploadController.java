package com.socialshoot.admin.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    // 上传图片
    @PostMapping("/image")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        if (file.isEmpty()) {
            result.put("code", 400);
            result.put("message", "请选择要上传的文件");
            return result;
        }

        try {
            // 生成唯一的文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;
            
            // 创建上传目录
            String uploadDir = "C:/social-photo-booking-system/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 保存文件
            String filePath = uploadDir + newFilename;
            file.transferTo(new File(filePath));
            
            // 返回文件访问URL
            String fileUrl = "/uploads/" + newFilename;
            
            Map<String, Object> data = new HashMap<>();
            data.put("url", fileUrl);
            data.put("filename", newFilename);
            
            result.put("code", 200);
            result.put("message", "上传成功");
            result.put("data", data);
            
        } catch (IOException e) {
            result.put("code", 500);
            result.put("message", "上传失败：" + e.getMessage());
        }
        
        return result;
    }
}
