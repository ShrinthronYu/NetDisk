package com.wavebigfish.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.wavebigfish.model.UserFile;
import com.wavebigfish.vo.UserFileListVO;

import java.util.List;
import java.util.Map;

public interface UserFileService extends IService<UserFile> {
    List<UserFileListVO> getUserFileList(UserFile userFile, Long currentPage, Long pageCount);

    List<UserFileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount);

    Map<String, Object> getUserFileByType(int fileType, Long currentPage, Long pageCount, Long userId);

    void deleteUserFile(Long userFileId, Long sessionUserId);

    List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId);

    List<UserFile> selectFilePathTreeByUserId(Long userId);

    void updateFilepathByFilepath(String oldfilePath, String newfilePath, String fileName, String extendName, Long userId);

    List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, Long userId);

    void replaceUserFilePath(String filePath, String oldFilePath, Long userId);
}