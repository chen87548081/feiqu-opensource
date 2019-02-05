package com.feiqu.web.controller.extra;

import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * MailController
 *
 * @author chenweidong
 * @date 2017/12/26
 */
@Controller
@RequestMapping("mail")
public class MailController extends BaseController{

    private final static Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private JavaMailSenderImpl mailSender;

    @GetMapping("groupSend")
    @ResponseBody
    public Object qunfaMail(String mails){
        BaseResult result = new BaseResult();
        String htmlContent = "<html><head>" +
                "<base target=\"_blank\">" +
                "<style type=\"text/css\">" +
                "::-webkit-scrollbar{ display: none; }" +
                "</style>" +
                "<style id=\"cloudAttachStyle\" type=\"text/css\">" +
                "#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}" +
                "</style>" +
                "</head>" +
                "<body tabindex=\"0\" role=\"listitem\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 600px; border: 1px solid #ddd; border-radius: 3px; color: #555; font-family: 'Helvetica Neue Regular',Helvetica,Arial,Tahoma,'Microsoft YaHei','San Francisco','微软雅黑','Hiragino Sans GB',STHeitiSC-Light; font-size: 12px; height: auto; margin: auto; overflow: hidden; text-align: left; word-break: break-all; word-wrap: break-word;\"> <tbody style=\"margin: 0; padding: 0;\"> <tr style=\"background-color: #393D49; height: 60px; margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 0;\"> <div style=\"color: #5EB576; margin: 0; margin-left: 30px; padding: 0;\"><a style=\"font-size: 14px; margin: 0; padding: 0; color: #5EB576; " +
                "text-decoration: none;\" href=\"http://www.flyfun.site/\" target=\"_blank\">飞趣社区</a></div> </td> </tr> " +
                "<tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> " +
                "你好，<em style=\"font-weight: 700;\"></em><p>飞趣社区 寻找属于你的乐趣</p> " +
                "<div style=\"\"> <a href=\"http://www.flyfun.site/?from=qqmail\" style=\"background-color: #009E94; color: #fff; display: inline-block; height: 32px; line-height: 32px; margin: 0 15px 0 0; padding: 0 15px; text-decoration: none;\" target=\"_blank\">立即前往</a> </div>" +
                "<div style=\"\"></div></td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">系统邮件，请勿直接回复。</td> </tr> </tbody> </table>" +
                "<style type=\"text/css\">" +
                "body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}" +
                "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}" +
                "pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}" +
                "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}" +
                "img{ border:0}" +
                "header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}" +
                "blockquote{margin-right:0px}" +
                "</style>" +
                "<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>" +
                "</body></html>";;
        try {
            String[] mailTos = new String[mails.split(",").length];
                for(int i = 0;i<mails.split(",").length;i++){
                    mailTos[i] = mails.split(",")[i]+"@qq.com";
                }
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
                        helper.setFrom(mailSender.getUsername());
//            helper.setFrom("admin@flyfun.site");
            //邮件主题
            helper.setSubject("飞趣社区 寻找生活的乐趣");
            //邮件接收者的邮箱地址
            helper.setTo(mailTos);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        }
        catch (MessagingException e) {
            logger.error("发送邮件失败 ",e);
        }
        result.setData(mails.split(",").length);
        return result;
    }

    @GetMapping("sendBatch")
    @ResponseBody
    public Object sendBatch(String mails){
        BaseResult result = new BaseResult();
        String htmlContent = "<html><head>" +
                "<base target=\"_blank\">" +
                "<style type=\"text/css\">" +
                "::-webkit-scrollbar{ display: none; }" +
                "</style>" +
                "<style id=\"cloudAttachStyle\" type=\"text/css\">" +
                "#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}" +
                "</style>" +
                "</head>" +
                "<body tabindex=\"0\" role=\"listitem\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 600px; border: 1px solid #ddd; border-radius: 3px; color: #555; font-family: 'Helvetica Neue Regular',Helvetica,Arial,Tahoma,'Microsoft YaHei','San Francisco','微软雅黑','Hiragino Sans GB',STHeitiSC-Light; font-size: 12px; height: auto; margin: auto; overflow: hidden; text-align: left; word-break: break-all; word-wrap: break-word;\"> <tbody style=\"margin: 0; padding: 0;\"> <tr style=\"background-color: #393D49; height: 60px; margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 0;\"> <div style=\"color: #5EB576; margin: 0; margin-left: 30px; padding: 0;\"><a style=\"font-size: 14px; margin: 0; padding: 0; color: #5EB576; " +
                "text-decoration: none;\" href=\"http://www.flyfun.site/\" target=\"_blank\">飞趣社区</a></div> </td> </tr> " +
                "<tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> " +
                "你好，<em style=\"font-weight: 700;\"></em><p>飞趣社区 是真实的社交网络 为了联络你和你周围的朋友,然后交流互相的想法,发表自己的文章,和朋友分享相片、音乐和电影,展示自我,从而帮助大家更好得成长</p><p>飞趣社区欢迎你的体验</p> " +
                "<div style=\"\"> <a href=\"http://www.flyfun.site/?from=qqmail\" style=\"background-color: #009E94; color: #fff; display: inline-block; height: 32px; line-height: 32px; margin: 0 15px 0 0; padding: 0 15px; text-decoration: none;\" target=\"_blank\">立即前往</a> </div>" +
                "<div style=\"\"></div>  </td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">系统邮件，请勿直接回复。</td> </tr> </tbody> </table>" +
                "<style type=\"text/css\">" +
                "body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}" +
                "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}" +
                "pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}" +
                "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}" +
                "img{ border:0}" +
                "header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}" +
                "blockquote{margin-right:0px}" +
                "</style>" +
                "<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>" +
                "</body></html>";;
        try {
            int qq = 758991930;
            String[] qqs = new String[7];
            for(int i = 0;i<6;i++){
                qqs[i] = qq+"@qq.com";
                qq++;
            }
            qqs[6] = "573047307@qq.com";
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
//            helper.setFrom("admin@flyfun.site");
            helper.setFrom(mailSender.getUsername());
            //邮件主题
            helper.setSubject("飞趣社区 寻找生活的乐趣");
            //邮件接收者的邮箱地址
            helper.setTo(qqs);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

            result.setData(qqs);
        }
        catch (MessagingException e) {
            logger.error("发送邮件失败 ",e);
        }
        return result;
    }
}
