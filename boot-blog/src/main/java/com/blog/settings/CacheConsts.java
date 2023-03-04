package com.blog.settings;

public class CacheConsts {

    public static final Long EMPTY_EXPIRED_TIME = 10L;

    // blog content

    public static final String TAG_PREFIX = "content:tag:";
    public static final Long TAG_EXPIRED_TIME = -1L;
    public static final String BLOG_INFO_PREFIX = "content:info:";
    public static final Long BLOG_INFO_EXPIRED_TIME = 120L;
    public static final String CONTENT_PREFIX = "content:content:";
    public static final Long CONTENT_EXPIRED_TIME = 120L;
    public static final String BLOG_PREFIX = "content:blog:";
    public static final Long BLOG_EXPIRED_TIME = 120L;

    public static final String BLOGTMP_PREFIX = "content:review:";
    public static final Long BLOGTMP_EXPIRED_TIME = -1L;

    // blog image

    public static final String IMAGE_PREFIX = "user:image:";

    // blog home

    public static final String DESC_PREFIX = "home:desc:";
    public static final Long DESC_EXPIRED_TIME = 60L;

    public static final String USER_DESC_PREFIX = "home:desc:user:";

    public static final String INFO_PREFIX = "home:info:";
    public static final Long INFO_EXPIRED_TIME = 60L;

    // forum user

    public static final String CODE_PREFIX = "user:code:";
    public static final Long CODE_EXPIRED_TIME = 240L;

    public static final String TOKEN_PREFIX = "user:token:";
    public static final Long TOKEN_EXPIRED_TIME = 259200L;

    public static final String USER_PREFIX = "user:";
    public static final Long USER_EXPIRED_TIME = 60L;

    public static final String ADMIN_PREFIX = "user:admin:";

    // user comment

    public static final String USER_CMT_PREFIX = "user:comment:";
    public static final Long USER_CMT_EXPIRED_TIME = 7200L;
}
