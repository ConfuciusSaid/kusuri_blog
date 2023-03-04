package com.blog.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.Result;
import com.blog.mapper.InfoMapper;
import com.blog.pojo.Info;
import com.blog.service.InfoService;
import com.blog.settings.CacheConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Result listInfoByUserId(Long id) {
        String key = CacheConsts.INFO_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            List<Info> list = query().eq("user_id", id).list();
            if (list.isEmpty()) {
                redisTemplate.opsForValue().set(key, "", CacheConsts.EMPTY_EXPIRED_TIME, TimeUnit.SECONDS);
                return Result.ok(list);
            } else {
                redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(list), CacheConsts.INFO_EXPIRED_TIME,
                        TimeUnit.SECONDS);
            }
            return Result.ok(list);
        }
        if (value.equals("")) {
            return Result.ok(Collections.emptyList());
        }
        return Result.ok(JSONUtil.toList(value, Info.class));
    }

    @Override
    public void deleteInfoCache(Long id) {
        String key = CacheConsts.BLOG_INFO_PREFIX + id;
        redisTemplate.delete(key);
    }
}
