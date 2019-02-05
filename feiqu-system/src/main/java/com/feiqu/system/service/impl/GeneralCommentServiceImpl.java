package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.GeneralCommentMapper;
import com.feiqu.system.model.Article;
import com.feiqu.system.model.GeneralComment;
import com.feiqu.system.model.GeneralCommentExample;
import com.feiqu.system.pojo.response.CommentsWithThoughtResponse;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.service.GeneralCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* GeneralCommentService实现
* Created by cwd on 2017/10/19.
*/
@Service
@Transactional
@BaseService

public class GeneralCommentServiceImpl extends BaseServiceImpl<GeneralCommentMapper, GeneralComment, GeneralCommentExample> implements GeneralCommentService {

    private static Logger _log = LoggerFactory.getLogger(GeneralCommentServiceImpl.class);

    @Resource
    GeneralCommentMapper generalCommentMapper;

    public List<DetailCommentResponse> selectCommentsByTopic(Article article) {
        return generalCommentMapper.selectCommentsByTopic(article);
    }

    public List<DetailCommentResponse> selectUserByExample(GeneralCommentExample commentExample) {
        return generalCommentMapper.selectUserByExample(commentExample);
    }

    @Override
    public List<CommentsWithThoughtResponse> selectCommentsWithThought(Integer id) {
        return generalCommentMapper.selectCommentsWithThought(id);
    }
}