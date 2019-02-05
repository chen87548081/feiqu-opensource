package com.feiqu.system.mapper;

import com.feiqu.system.model.Article;
import com.feiqu.system.model.GeneralComment;
import com.feiqu.system.model.GeneralCommentExample;
import com.feiqu.system.pojo.response.CommentsWithThoughtResponse;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GeneralCommentMapper {
    long countByExample(GeneralCommentExample example);

    int deleteByExample(GeneralCommentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GeneralComment record);

    int insertSelective(GeneralComment record);

    List<GeneralComment> selectByExample(GeneralCommentExample example);

    GeneralComment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GeneralComment record, @Param("example") GeneralCommentExample example);

    int updateByExample(@Param("record") GeneralComment record, @Param("example") GeneralCommentExample example);

    int updateByPrimaryKeySelective(GeneralComment record);

    int updateByPrimaryKey(GeneralComment record);

    List<DetailCommentResponse> selectCommentsByTopic(Article article);

    List<DetailCommentResponse> selectUserByExample(GeneralCommentExample commentExample);

    List<CommentsWithThoughtResponse> selectCommentsWithThought(Integer postUserId);
}