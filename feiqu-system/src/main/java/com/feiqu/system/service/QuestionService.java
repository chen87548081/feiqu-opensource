package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.Question;
import com.feiqu.system.model.QuestionExample;

import java.util.List;

/**
* QuestionService接口
* created by cwd on 2017/10/13.
*/
public interface QuestionService extends BaseService<Question, QuestionExample> {

    List selectWithUserByExample(QuestionExample example);
}