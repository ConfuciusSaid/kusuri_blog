package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.pojo.Desc;

public interface DescService extends IService<Desc> {
    Result listDescByUserId(Long id, Long page, Long limit);
}
