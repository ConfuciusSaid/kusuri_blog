package com.blog.controller;

import com.blog.dto.Result;
import com.blog.dto.UserDTO;
import com.blog.service.UserService;
import com.blog.settings.CacheConsts;
import com.blog.settings.DataSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/")
    public Result listImage(@RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        File folder = new File(DataSettings.IMAGE_PATH + user.getId());
        return Result.ok(folder.list());
    }

    @PostMapping("/upload")
    public Result uploadImage(@RequestParam("file") MultipartFile multipartFile,
                              @RequestParam("kusuri_token") String token) {
        if (multipartFile == null) {
            return Result.fail("文件为空");
        }
        if (multipartFile.getSize() > DataSettings.IMAGE_MAX) {
            return Result.fail("请勿上传超过" + DataSettings.IMAGE_MAX / 1024 / 1024 + "MB的图片");
        }
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        String key = CacheConsts.IMAGE_PREFIX + user.getId();
        String value = redisTemplate.opsForValue().get(key);
        if (value != null && Integer.parseInt(value) >= DataSettings.IMAGE_COUNT) {
            return Result.fail("该用户上传图片超过上限");
        }
        File folder = new File(DataSettings.IMAGE_PATH + user.getId());
        if (!folder.isDirectory()) {
            if (!folder.mkdirs()) {
                return Result.fail("创建图片目录失败");
            }
        }

        String originalName = multipartFile.getOriginalFilename();
        assert originalName != null;
        String fileName = UUID.randomUUID() +
                originalName.substring(originalName.lastIndexOf("."));
        try {
            File image = new File(folder, fileName);
            multipartFile.transferTo(image);
            redisTemplate.opsForValue().increment(key);
            return Result.ok(DataSettings.SERVER_IMAGE_PATH + user.getId() + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail("上传失败");
    }
}
