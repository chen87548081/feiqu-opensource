package com.feiqu.framwork.support.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 * @author cwd
 * @version V2exDTO, v 0.1 2018/11/29 cwd 1049766
 */
@TargetUrl("https://www.v2ex.com/t/\\w+#\\w+")
@HelpUrl("https://www.v2ex.com/?tab=\\w+")
public class V2exDTO implements AfterExtractor {
    @ExtractBy("//h1/text()")
    private String title;
    @ExtractBy("//div[@class='markdown_body']/outerHtml()")
    private String content;
    @ExtractBy("//small[@class='gray']/a/text()")
    private String author;
    @ExtractBy("//div[@class='header']/div/a/img/@src")
    private String authorIcon;
    @ExtractBy("//span[@class='gray']/text()")
    private String comment;
    @ExtractBy("//small[@class='gray']/text()")
    private String time;
    @ExtractBy("//div[@class='header']/a[2]/text()")
    private String type;
    @ExtractBy("//div[@class='reply_content']/html()")
    private List<String> reply;
    @ExtractBy("//div[@class='topic_content']/text()")
    private List<String> topicContent;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<String> getReply() {
        return reply;
    }

    public void setReply(List<String> reply) {
        this.reply = reply;
    }

    public List<String> getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(List<String> topicContent) {
        this.topicContent = topicContent;
    }

    @Override
    public void afterProcess(Page page) {

    }

    @Override
    public String toString() {
        return "V2exDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", authorIcon='" + authorIcon + '\'' +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
