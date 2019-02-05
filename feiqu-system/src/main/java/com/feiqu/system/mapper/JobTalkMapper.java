package com.feiqu.system.mapper;

import com.feiqu.system.model.JobTalk;
import com.feiqu.system.model.JobTalkExample;
import com.feiqu.system.pojo.response.JobTalkUserDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobTalkMapper {
    long countByExample(JobTalkExample example);

    int deleteByExample(JobTalkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(JobTalk record);

    int insertSelective(JobTalk record);

    List<JobTalk> selectByExampleWithBLOBs(JobTalkExample example);

    List<JobTalk> selectByExample(JobTalkExample example);

    JobTalk selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") JobTalk record, @Param("example") JobTalkExample example);

    int updateByExampleWithBLOBs(@Param("record") JobTalk record, @Param("example") JobTalkExample example);

    int updateByExample(@Param("record") JobTalk record, @Param("example") JobTalkExample example);

    int updateByPrimaryKeySelective(JobTalk record);

    int updateByPrimaryKeyWithBLOBs(JobTalk record);

    int updateByPrimaryKey(JobTalk record);

    List<JobTalkUserDetail> selectWithUserByExample(JobTalkExample example);
}