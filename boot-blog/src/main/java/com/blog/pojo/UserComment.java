package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.blog.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@TableName("tb_comment")
public class UserComment {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    String markdown;
    Long targetId;
    Long userId;
//    @JsonFormat(timezone = "GMT+8")
    Date createTime;

    @TableField(exist = false)
    UserDTO user;
}
