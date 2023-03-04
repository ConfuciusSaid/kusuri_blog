package com.blog.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.Result;
import com.blog.mapper.BlogInfoMapper;
import com.blog.pojo.BlogInfo;
import com.blog.service.BlogInfoService;
import com.blog.settings.CacheConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BlogInfoServiceImpl extends ServiceImpl<BlogInfoMapper, BlogInfo> implements BlogInfoService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Result listInfoByBlogId(Long id) {
        String key = CacheConsts.BLOG_INFO_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            List<Long> list = query().eq("blog_id", id).list()
                    .stream().map(BlogInfo::getTagId).collect(Collectors.toList());
            if (list.isEmpty()) {
                redisTemplate.opsForValue().set(key, "", CacheConsts.EMPTY_EXPIRED_TIME, TimeUnit.SECONDS);
                return Result.fail("文章不存在");
            } else {
                redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(list), CacheConsts.BLOG_INFO_EXPIRED_TIME,
                        TimeUnit.SECONDS);
            }
            return Result.ok(list);
        }
        if (value.equals("")) {
            return Result.fail("文章不存在");
        }
        return Result.ok(JSONUtil.toList(value, Long.class));
    }
}
