package com.blog.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.Result;
import com.blog.mapper.DescMapper;
import com.blog.pojo.Desc;
import com.blog.service.DescService;
import com.blog.settings.CacheConsts;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@MapperScan("com.blogmain.mapper")
public class DescServiceImpl extends ServiceImpl<DescMapper, Desc> implements DescService {
    @Autowired
    DescMapper descMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    public Desc getDescById(String id) {
        String key = CacheConsts.DESC_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            Desc desc = getById(id);
            if (desc == null) {
                redisTemplate.opsForValue().set(key, "", CacheConsts.EMPTY_EXPIRED_TIME, TimeUnit.SECONDS);
                return null;
            } else {
                redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(desc), CacheConsts.DESC_EXPIRED_TIME,
                        TimeUnit.SECONDS);
            }
            return desc;
        }
        if (value.equals("")) {
            return null;
        }
        return JSONUtil.toBean(value, Desc.class);
    }

//    @Override
//    public Result getDescByUserId(Long id, Long page, Long limit) {
//        String key = CacheConsts.USER_DESC_PREFIX + id;
//        long start = (page - 1) * limit;
//        long end = page * limit - 1;
//        Set<String> ids = redisTemplate.opsForZSet().range(key, start, end);
//        if (ids == null) {
//            return Result.ok();
//        }
//        Long count = redisTemplate.opsForZSet().zCard(key);
//        List<Desc> descList = new ArrayList<>();
//        for (String idStr : ids) {
//            Desc desc = getDescById(idStr);
//            if (desc == null) {
//                redisTemplate.opsForZSet().remove(key, idStr);
//                return getDescByUserId(id, page, limit);
//            } else {
//                descList.add(desc);
//            }
//        }
//        Page<Desc> producePage = new Page<>(page, limit);
//        producePage.setRecords(descList);
//        producePage.setTotal(count);
//        return Result.ok(producePage);
//    }

    @Override
    public Result listDescByUserId(Long id, Long page, Long limit) {
        Page<Desc> producePage = new Page<>(page, limit);
        LambdaQueryWrapper<Desc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Desc::getUserId, id);
        descMapper.selectPage(producePage, wrapper);
        return Result.ok(producePage);
    }
}
