package com.wavebigfish.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.wavebigfish.model.RecoveryFile;
import com.wavebigfish.vo.RecoveryFileListVO;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecoveryFileMapper extends BaseMapper<RecoveryFile> {
    List<RecoveryFileListVO> selectRecoveryFileList();
}