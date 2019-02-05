package com.feiqu.framwork.support.spider;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.feiqu.common.enums.SpiderSourceEnum;
import com.feiqu.common.utils.EmojiUtils;
import com.feiqu.system.mapper.FqTopicMapper;
import com.feiqu.system.mapper.FqTopicReplyMapper;
import com.feiqu.system.model.FqTopic;
import com.feiqu.system.model.FqTopicExample;
import com.feiqu.system.model.FqTopicReply;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.Resource;
import java.util.Date;
import java.util.StringJoiner;

/**
 * @author cwd
 * @version TopicInfoPipeline, v 0.1 2018/11/29 cwd 1049766
 */
@Component
public class TopicInfoPipeline implements PageModelPipeline<V2exDTO> {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TopicInfoPipeline.class);

    @Resource
    private FqTopicMapper fqTopicMapper;
    @Resource
    private FqTopicReplyMapper fqTopicReplyMapper;

    @Override
    public void process(V2exDTO v2exDTO, Task task) {
        if(StringUtils.isEmpty(v2exDTO.getTitle())){
            return;
        }
        Date now = new Date();
        FqTopicExample topicExample = new FqTopicExample();
        topicExample.createCriteria().andGmtCreateGreaterThan(DateUtil.offsetHour(now,-5));
        long count = fqTopicMapper.countByExample(topicExample);
        if(count >= 50){
            OOSpider ooSpider = (OOSpider)task;
            ooSpider.stop();
        }
        topicExample.clear();
        topicExample.createCriteria().andTitleEqualTo(v2exDTO.getTitle()).andAuthorEqualTo(v2exDTO.getAuthor());
        count = fqTopicMapper.countByExample(topicExample);
        if(count > 0){
            return;
        }
        FqTopic fqTopic = DTO2DO(v2exDTO);
        fqTopic.setContent(EmojiUtils.toAliases(fqTopic.getContent()));
        fqTopicMapper.insert(fqTopic);
        if(CollectionUtil.isNotEmpty(v2exDTO.getReply())){
            v2exDTO.getReply().forEach(reply->{
                if(StringUtils.isEmpty(reply)){
                    return;
                }
                if(reply.length() > 500){
                    reply = reply.substring(0,480);
                }
                reply = EmojiUtils.toAliases(reply);
                FqTopicReply fqTopicReply = new FqTopicReply();
                fqTopicReply.setGmtCreate(now);
                fqTopicReply.setContent(reply);
                fqTopicReply.setTopicId(fqTopic.getId());
                fqTopicReplyMapper.insert(fqTopicReply);
            });
        }
    }

    private FqTopic DTO2DO(V2exDTO v2exDTO) {
        if(v2exDTO == null){
            return null;
        }
        FqTopic fqTopic = new FqTopic();
        fqTopic.setAuthor(v2exDTO.getAuthor());
        fqTopic.setAuthorIcon(v2exDTO.getAuthorIcon());
        if(StringUtils.isEmpty(v2exDTO.getContent())){
            if(CollectionUtils.isEmpty(v2exDTO.getTopicContent())){
                v2exDTO.setContent(v2exDTO.getTitle());
            }else {
                StringJoiner ss = new StringJoiner(",");
                for(String t : v2exDTO.getTopicContent()){
                    ss.add(t);
                }
                v2exDTO.setContent(ss.toString());
            }
        }
        fqTopic.setContent(v2exDTO.getContent() == null?"":v2exDTO.getContent());
        fqTopic.setGmtCreate(new Date());
        fqTopic.setSource(SpiderSourceEnum.V2EX.getValue());
        fqTopic.setType(v2exDTO.getType());
        fqTopic.setTitle(v2exDTO.getTitle()== null?"":v2exDTO.getTitle());
        if(fqTopic.getTitle().length() > 100){
            fqTopic.setTitle(fqTopic.getTitle().substring(0,98));
        }
        String comment = v2exDTO.getComment();
        comment = comment == null?"":comment;
        int index = StringUtils.indexOf(comment,"回复");
        if(index != -1){
            comment = StringUtils.substring(comment,0,index);
            comment = comment.trim();
            fqTopic.setCommentCount(Integer.valueOf(comment));
        }else {
            fqTopic.setCommentCount(0);
        }

        return fqTopic;
    }
}
