package com.feiqu.system.mapper;

import com.feiqu.system.model.UploadImgRecord;
import com.feiqu.system.model.UploadImgRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UploadImgRecordMapper {
    long countByExample(UploadImgRecordExample example);

    int deleteByExample(UploadImgRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UploadImgRecord record);

    int insertSelective(UploadImgRecord record);

    List<UploadImgRecord> selectByExample(UploadImgRecordExample example);

    UploadImgRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UploadImgRecord record, @Param("example") UploadImgRecordExample example);

    int updateByExample(@Param("record") UploadImgRecord record, @Param("example") UploadImgRecordExample example);

    int updateByPrimaryKeySelective(UploadImgRecord record);

    int updateByPrimaryKey(UploadImgRecord record);
}