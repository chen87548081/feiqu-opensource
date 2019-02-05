package com.feiqu.system.service;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.Article;
import com.feiqu.system.model.GeneralComment;
import com.feiqu.system.model.GeneralCommentExample;
import com.feiqu.system.pojo.response.CommentsWithThoughtResponse;
import com.feiqu.system.pojo.response.DetailCommentResponse;

import java.util.List;

/**
* GeneralCommentService接口
* created by cwd on 2017/10/19.
*/
public interface GeneralCommentService extends BaseService<GeneralComment, GeneralCommentExample> {

    List<DetailCommentResponse> selectCommentsByTopic(Article article);

    /**
     * 拿到评论伴随用户详情
     * @param commentExample
     * @return
     */
    List<DetailCommentResponse> selectUserByExample(GeneralCommentExample commentExample);

    List<CommentsWithThoughtResponse> selectCommentsWithThought(Integer id);
}