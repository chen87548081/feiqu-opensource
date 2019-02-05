package com.feiqu.system.mapper;

import com.feiqu.system.model.FqNotice;
import com.feiqu.system.model.FqNoticeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqNoticeMapper {
    long countByExample(FqNoticeExample example);

    int deleteByExample(FqNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqNotice record);

    int insertSelective(FqNotice record);

    List<FqNotice> selectByExampleWithBLOBs(FqNoticeExample example);

    List<FqNotice> selectByExample(FqNoticeExample example);

    FqNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqNotice record, @Param("example") FqNoticeExample example);

    int updateByExampleWithBLOBs(@Param("record") FqNotice record, @Param("example") FqNoticeExample example);

    int updateByExample(@Param("record") FqNotice record, @Param("example") FqNoticeExample example);

    int updateByPrimaryKeySelective(FqNotice record);

    int updateByPrimaryKeyWithBLOBs(FqNotice record);

    int updateByPrimaryKey(FqNotice record);
}