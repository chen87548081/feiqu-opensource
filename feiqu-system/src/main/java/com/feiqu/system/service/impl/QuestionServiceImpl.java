package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.QuestionMapper;
import com.feiqu.system.model.Question;
import com.feiqu.system.model.QuestionExample;
import com.feiqu.system.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* QuestionService实现
* Created by cwd on 2017/10/13.
*/
@Service
@Transactional
@BaseService

public class QuestionServiceImpl extends BaseServiceImpl<QuestionMapper, Question, QuestionExample> implements QuestionService {

    private static Logger _log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    QuestionMapper questionMapper;

    public List selectWithUserByExample(QuestionExample example) {
        return questionMapper.selectWithUserByExample(example);
    }
}