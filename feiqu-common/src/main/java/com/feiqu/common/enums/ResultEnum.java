package com.feiqu.common.enums;

/**
 * @author cwd
 * @create 2017-10-14:09
 **/
public enum  ResultEnum {

    /**
     * 操作成功
     */
    SUCCESS("0000","操作成功"),
    /**
     * 操作失败
     */
    FAIL("0001","操作失败"),
    NICKNAME_EXIST("0002","昵称已经存在"),
    USER_NOT_LOGIN("0003","用户未登陆"),

    USER_ALREADY_LIKE("0004","已经点过赞了哦~"),
    PARAM_NULL("0005","参数不能为空~"),
    TIME_LINE_SAME_DAY("0006","一天只能写一篇总结~"),
    DELETE_NOT_MY("0007","请不要删别人的~"),
    STR_LENGTH_TOO_LONG("0008","字符串长度超过限制了~"),
    USER_NOT_SAME("0009","用户信息不一致~"),
    PASSWORD_LENGTH_ERROR("0010","密码长度必须是6~20位~"),
    USER_NOT_FOUND("0011","用户未查到~"),
    USERNAME_EXIST("0012","账号名称已经存在~"),
    USERNAME_LENGTH_ERROR("0013","账号长度必须是3~20位"),
    FILE_TOO_LARGE("0014","上传文件过大"),
    PASSWORD_TOO_SIMPLE("0015","密码太过简单"),
    PIC_URL_NOT_RIGHT("0016","图片url不正确"),
    FOLLOWED_USER_NOT_EXIST("0017","被关注者不存在"),
    FOLLOW_NOT_EXIST("0018","关注记录不存在"),
    EMAIL_NOT_CORRECT("0019","邮箱格式不正确"),
    VERIFY_CODE_NOT_CORRECT("0020","验证码不正确"),
    PIC_URL_ALREADY_EXIST("0021","图片url已经存在"),
    USER_NOT_SIGNED("0022","用户未签到"),
    LABEL_NULL("0023","标签不能为空"),
    ARTICLE_NOT_EXITS("0025","文章不存在"),
    USERNAME_OR_PASSWORD_ERROR("0026","用户名或密码错误"),
    TIME_EXPIRED("0027","时间已经过期"),
    USER_NOT_AUTHORIZED("0028","用户没有权限"),
    PIC_EXTNAME_NOT_RIGHT("0029","图片后缀名不正确"),
    SIGN_ALREADY_TODAY("0030","今日已经签过到了~"),
    MP3_URL_ALREADY_EXIST("0031","MP3的url已经存在"),
    SONG_URL_NOT_RIGHT("0032","音乐url不正确"),
    POST_THOUGHT_FREQUENCY_OVER_LIMIT("0033","发表想法频率超过限制，请过一分钟再试!"),
    THOUGHT_COMMENT_FREQUENCY_OVER_LIMIT("0034","想法评论频率超过限制，请过一分钟再试!"),
    POST_ARTICLE_FREQUENCY_OVER_LIMIT("0035","发表文章频率超过限制，请过一分钟再试!"),
    PARAM_ERROR("0036","参数有误~"),
    WEBSITE_URL_ERROR("0037","网址URL格式不正确~"),
    WEBSITE_URL_EXISTS("0038","网址URL已经存在~"),
    Lyric_TOO_LONG("0039","音乐歌词长度不能超过3000~"),
    THOUGHT_TOO_LONG("0040","想法长度不能超过400~"),
    PIC_URL_SAME("0041","图片地址不能和之前的一样!"),
    THOUGHT_TOP_EXIST("0042","置顶的想法已经存在!"),
    USER_FROZEN("0043","你的账号已被冻结，请联系官方人员解决！"),
    SYSTEM_ERROR("4000","系统错误！")

    ;

    private String code;
    private String msg;


    ResultEnum(String code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
