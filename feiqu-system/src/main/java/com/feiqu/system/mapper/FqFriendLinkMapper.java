package com.feiqu.system.mapper;

import com.feiqu.system.model.FqFriendLink;
import com.feiqu.system.model.FqFriendLinkExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqFriendLinkMapper {
    long countByExample(FqFriendLinkExample example);

    int deleteByExample(FqFriendLinkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqFriendLink record);

    int insertSelective(FqFriendLink record);

    List<FqFriendLink> selectByExample(FqFriendLinkExample example);

    FqFriendLink selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqFriendLink record, @Param("example") FqFriendLinkExample example);

    int updateByExample(@Param("record") FqFriendLink record, @Param("example") FqFriendLinkExample example);

    int updateByPrimaryKeySelective(FqFriendLink record);

    int updateByPrimaryKey(FqFriendLink record);
}