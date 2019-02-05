package com.feiqu.system.mapper;

import com.feiqu.system.model.Thought;
import com.feiqu.system.model.ThoughtExample;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ThoughtMapper {
    long countByExample(ThoughtExample example);

    int deleteByExample(ThoughtExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Thought record);

    int insertSelective(Thought record);

    List<Thought> selectByExample(ThoughtExample example);

    Thought selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Thought record, @Param("example") ThoughtExample example);

    int updateByExample(@Param("record") Thought record, @Param("example") ThoughtExample example);

    int updateByPrimaryKeySelective(Thought record);

    int updateByPrimaryKey(Thought record);

    List<ThoughtWithUser> selectByExampleWithUser(ThoughtExample example);
    ThoughtWithUser getByIdWithUser(Integer id);
}