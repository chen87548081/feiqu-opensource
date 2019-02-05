package com.feiqu.system.service;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.Thought;
import com.feiqu.system.model.ThoughtExample;
import com.feiqu.system.pojo.response.ThoughtWithUser;

import java.util.List;

/**
* ThoughtService接口
*/
public interface ThoughtService extends BaseService<Thought, ThoughtExample> {

    List<ThoughtWithUser> getThoughtWithUser(ThoughtExample thoughtExample);

    ThoughtWithUser getByIdWithUser(Integer thoughtId);
}