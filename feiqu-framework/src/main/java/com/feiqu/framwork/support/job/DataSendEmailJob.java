package com.feiqu.framwork.support.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.feiqu.system.model.NginxLogExample;
import com.feiqu.system.service.NginxLogService;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * DataSendEmailJob
 *
 * @author chenweidong
 * @date 2017/11/14
 */
@Component
public class DataSendEmailJob {

    private final static Logger logger = LoggerFactory.getLogger(DataSendEmailJob.class);

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private NginxLogService logService;

    @Scheduled(cron = "0 0 8 * * ? ")
//    @Scheduled(cron = "0 53 15 * * ? ")
    public void work(){
        
        Stopwatch stopwatch = Stopwatch.createStarted();
        Date now = new Date();
//        DateUtil.offsetDay(now, -1).toString("yyyyMMdd");

        Date begin = DateUtils.addHours(now,-8);
        Date end = DateUtils.addHours(now,-6);

        NginxLogExample example = new NginxLogExample();
        example.createCriteria().andCreateTimeBetween(begin,end).andSpiderTypeEqualTo(0);
        Integer pv = logService.countByExample(example);
        long uv = logService.countUvByExample(example);

        //1 百度爬虫 2 google爬虫 3 bing爬虫 4 搜狗
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin,end)
        .andSpiderTypeEqualTo(1);
        long baiduzhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin,end)
                .andSpiderTypeEqualTo(2);
        long googlezhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin,end)
                .andSpiderTypeEqualTo(3);
        long bingzhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin,end)
                .andSpiderTypeEqualTo(4);
        long sougouzhizhu = logService.countByExample(example);

        String htmlContent = getEmailHtml(DateUtil.offsetDay(now, -1).toString("yyyy-MM-dd")+"的PV UV",pv,uv,baiduzhizhu,
                googlezhizhu,bingzhizhu,sougouzhizhu);
        String yesterday = DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd");
        String prefix = "/home/chenweidong/feiqu/logs";
        File logFile = new File(prefix+"/cwd-web.log."+yesterday);
        File errorlogFile = new File(prefix+"/cwd-web.error.log."+yesterday);
        File gcLogFile = new File(prefix+"/gc.log");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
            helper.setFrom(mailSender.getUsername());
            //邮件主题
            helper.setSubject("社区前一天的PVUV");
            String[] tos = {"***@qq.com","***@qq.com","***@qq.com","***@qq.com"};
            //邮件接收者的邮箱地址
            helper.setTo(tos);
            helper.setText(htmlContent, true);
//            File logFile = new File("D:\\logs\\access_20171113.log");

            if(logFile.exists()){
                helper.addAttachment("logfiles",logFile);
            }
            if(errorlogFile.exists()){
                helper.addAttachment("errorlog",errorlogFile);
            }
            if(gcLogFile.exists()){
                helper.addAttachment("gcLogFile",gcLogFile);
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("发送邮件失败 ");
        }finally {
            boolean del = logFile.delete();
            if(!del){
                logger.error("删除日志文件失败，{}",logFile.getPath());
            }
            boolean del2 = errorlogFile.delete();
            if(!del2){
                logger.error("删除日志文件失败，{}",errorlogFile.getPath());
            }
            if(gcLogFile.exists()){
                FileUtil.writeBytes(("每天更新\r\n").getBytes(),
                        gcLogFile);
            }
            /*FileUtil.writeBytes((DateUtil.beginOfDay(now).toString() + "每天更新\r\n").getBytes(),
                    new File("/home/tomcat/apache-tomcat-8.5.8/logs/catalina.out"));*/
        }
        stopwatch.stop();
        long seconds = stopwatch.elapsed(TimeUnit.SECONDS);
        logger.info("nginx日志分析发送邮件,耗时{}秒",seconds);
    }

    private String getEmailHtml(String time,long pv,long uv,long baiduzhizhu,long googlezhizhu,long bingzhizhu,long sougouzhizhu){
        String html = "<html><head>" +
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
                "text-decoration: none;\" href=\"http://www.flyfun.site/\" target=\"_blank\">flyfun社区</a></div> </td> </tr> " +
                "<tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> " +
                "你好，<em style=\"font-weight: 700;\">飞趣社区开发者</em>： </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  "+time+" </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  PV:"+pv+" </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  UV:"+uv+" </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  百度蜘蛛爬取次数:"+baiduzhizhu+" </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  google蜘蛛爬取次数:"+googlezhizhu+" </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  bing蜘蛛爬取次数:"+bingzhizhu+" </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  搜狗蜘蛛爬取次数:"+sougouzhizhu+" </p> " +
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
                "</body></html>";
        return html;
    }

    public static void main(String[] args) {
//        System.out.println(DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd"));

        FileUtil.writeBytes((DateUtil.beginOfDay(new Date()).toString() + "每天更新\r\n").getBytes(),new File("D:\\cheninfo\\music\\catelina.out"));
    }
}
