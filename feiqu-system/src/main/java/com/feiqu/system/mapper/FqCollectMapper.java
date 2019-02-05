package com.feiqu.system.mapper;

import com.feiqu.system.model.FqCollect;
import com.feiqu.system.model.FqCollectExample;
import com.feiqu.system.pojo.response.ArticleCollectResponse;
import com.feiqu.system.pojo.response.ThoughtCollectResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqCollectMapper {
    long countByExample(FqCollectExample example);

    int deleteByExample(FqCollectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqCollect record);

    int insertSelective(FqCollect record);

    List<FqCollect> selectByExample(FqCollectExample example);

    FqCollect selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqCollect record, @Param("example") FqCollectExample example);

    int updateByExample(@Param("record") FqCollect record, @Param("example") FqCollectExample example);

    int updateByPrimaryKeySelective(FqCollect record);

    int updateByPrimaryKey(FqCollect record);

    List<ArticleCollectResponse> selectWithArticleByEntity(FqCollect fqCollect);

    List<Integer> selectTopicIdsByTypeAndUid(@Param("type") Integer type, @Param("uid") Integer uid);

    List<ThoughtCollectResponse> selectWithThoughtByEntity(FqCollect fqCollect);
}