package com.boshfish.controller;

import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.boshfish.common.RestResult;
import com.boshfish.dto.*;
import com.boshfish.model.File;
import com.boshfish.model.User;
import com.boshfish.model.UserFile;
import com.boshfish.service.FileService;
import com.boshfish.service.UserFileService;
import com.boshfish.service.UserService;
import com.boshfish.util.DateUtil;
import com.boshfish.vo.TreeNodeVO;
import com.boshfish.vo.UserFileListVO;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;


@Tag(name = "file", description = "该接口为文件接口，主要用来做一些文件的基本操作，如创建目录，删除，移动，复制等。")
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    @Resource
    FileService fileService;
    @Resource
    UserService userService;
    @Resource
    UserFileService userFileService;

    @Operation(summary = "创建文件", description = "目录(文件夹)的创建", tags = {"file"})
    @PostMapping(value = "/createfile")
    @ResponseBody
    public RestResult<String> createFile(@RequestBody CreateFileDTO createFileDTO, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            RestResult.fail().message("token认证失败");
        }
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileName, "").eq(UserFile::getFilePath, "").eq(UserFile::getUserId, 0);
        List<UserFile> userFiles = userFileService.list(lambdaQueryWrapper);
        if (!userFiles.isEmpty()) {
            RestResult.fail().message("同目录下文件名重复");
        }

        UserFile userFile = new UserFile();
        userFile.setUserId(sessionUser.getUserId());
        userFile.setFileName(createFileDTO.getFileName());
        userFile.setFilePath(createFileDTO.getFilePath());
        userFile.setIsDir(1);
        userFile.setUploadTime(DateUtil.getCurrentTime());
        userFile.setDeleteFlag(0);
        userFileService.save(userFile);
        return RestResult.success();
    }

    @Operation(summary = "获取文件列表", description = "用来做前台文件列表展示", tags = {"file"})
    @GetMapping(value = "/getfilelist")
    @ResponseBody
    public RestResult<UserFileListVO> getUserFileList(UserFileListDTO userFileListDTO,
                                                      @RequestHeader("token") String token) {


        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");

        }

        List<UserFileListVO> fileList = userFileService.getUserFileByFilePath(
                userFileListDTO.getFilePath(),
                sessionUser.getUserId(),
                userFileListDTO.getCurrentPage(),
                userFileListDTO.getPageCount());

        LambdaQueryWrapper<UserFile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userFileLambdaQueryWrapper.eq(
                UserFile::getUserId,
                sessionUser.getUserId()).eq(UserFile::getFilePath,
                userFileListDTO.getFilePath()).eq(UserFile::getDeleteFlag,
                0);
        int total = Math.toIntExact(userFileService.count(userFileLambdaQueryWrapper));

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", fileList);

        return RestResult.success().data(map);

    }

    @Operation(summary = "通过文件类型选择文件", description = "该接口可以实现文件格式分类查看", tags = {"file"})
    @GetMapping(value = "/selectfilebyfiletype")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> selectFileByFileType(int fileType, Long currentPage, Long pageCount, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);

        long userId = sessionUser.getUserId();

        Map<String, Object> map = userFileService.getUserFileByType(
                fileType,
                currentPage,
                pageCount,
                userId);
        return RestResult.success().data(map);

    }

    @Operation(summary = "删除文件", description = "可以删除文件或者目录", tags = {"file"})
    @RequestMapping(value = "/deletefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteFile(@RequestBody DeleteFileDTO deleteFileDto, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);

        userFileService.deleteUserFile(deleteFileDto.getUserFileId(), sessionUser.getUserId());

        return RestResult.success();

    }

    @Operation(summary = "批量删除文件", description = "批量删除文件", tags = {"file"})
    @RequestMapping(value = "/batchdeletefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> deleteImageByIds(@RequestBody BatchDeleteFileDTO batchDeleteFileDto,
                                               @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);

        List<UserFile> userFiles = JSON.parseArray(batchDeleteFileDto.getFiles(), UserFile.class);
        for (UserFile userFile : userFiles) {
            userFileService.deleteUserFile(userFile.getUserFileId(), sessionUser.getUserId());
        }

        return RestResult.success().message("批量删除文件成功");
    }

    @Operation(summary = "获取文件树", description = "文件移动的时候需要用到该接口，用来展示目录树", tags = {"file"})
    @RequestMapping(value = "/getfiletree", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<TreeNodeVO> getFileTree(@RequestHeader("token") String token) {
        RestResult<TreeNodeVO> result = new RestResult<>();
        UserFile userFile = new UserFile();
        User sessionUser = userService.getUserByToken(token);
        userFile.setUserId(sessionUser.getUserId());

        List<UserFile> filePathList = userFileService.selectFilePathTreeByUserId(sessionUser.getUserId());
        TreeNodeVO resultTreeNode = new TreeNodeVO();
        resultTreeNode.setLabel("/");

        for (UserFile file : filePathList) {
            String filePath = file.getFilePath() + file.getFileName() + "/";

            Queue<String> queue = new LinkedList<>();

            String[] strArr = filePath.split("/");
            for (String s : strArr) {
                if (!"".equals(s) && s != null) {
                    queue.add(s);
                }
            }
            if (queue.size() == 0) {
                continue;
            }
            resultTreeNode = insertTreeNode(resultTreeNode, "/", queue);


        }
        result.setSuccess(true);
        result.setData(resultTreeNode);
        return result;

    }

    public TreeNodeVO insertTreeNode(TreeNodeVO treeNode, String filePath, Queue<String> nodeNameQueue) {

        List<TreeNodeVO> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null) {
            return treeNode;
        }

        Map<String, String> map = new HashMap<>();
        filePath = filePath + currentNodeName + "/";
        map.put("filePath", filePath);

        if (!isExistPath(childrenTreeNodes, currentNodeName)) {  //1、判断有没有该子节点，如果没有则插入
            //插入
            TreeNodeVO resultTreeNode = new TreeNodeVO();

            resultTreeNode.setAttributes(map);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            // resultTreeNode.setId(treeid++);

            childrenTreeNodes.add(resultTreeNode);

        } else {  //2、如果有，则跳过
            nodeNameQueue.poll();
        }

        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                TreeNodeVO childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())) {
                    childrenTreeNode = insertTreeNode(childrenTreeNode, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }
            }
        } else {
            treeNode.setChildren(childrenTreeNodes);
        }
        return treeNode;
    }

    public boolean isExistPath(List<TreeNodeVO> childrenTreeNodes, String path) {
        boolean isExistPath = false;

        try {
            for (TreeNodeVO childrenTreeNode : childrenTreeNodes) {
                if (path.equals(childrenTreeNode.getLabel())) {
                    isExistPath = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isExistPath;
    }

    @Operation(summary = "文件移动", description = "可以移动文件或者目录", tags = {"file"})
    @RequestMapping(value = "/movefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> moveFile(@RequestBody MoveFileDTO moveFileDTO, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        String oldFilePath = moveFileDTO.getOldFilePath();
        String newFilePath = moveFileDTO.getFilePath();
        String fileName = moveFileDTO.getFileName();
        String extendName = moveFileDTO.getExtendName();

        userFileService.updateFilepathByFilepath(oldFilePath, newFilePath, fileName, extendName, sessionUser.getUserId());
        return RestResult.success();

    }

    @Operation(summary = "批量移动文件", description = "可以同时选择移动多个文件或者目录", tags = {"file"})
    @RequestMapping(value = "/batchmovefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> batchMoveFile(@RequestBody BatchMoveFileDTO batchMoveFileDTO,
                                            @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        String files = batchMoveFileDTO.getFiles();
        String newFilePath = batchMoveFileDTO.getFilePath();
        List<UserFile> userFiles = JSON.parseArray(files, UserFile.class);

        for (UserFile userFile : userFiles) {
            userFileService.updateFilepathByFilepath(userFile.getFilePath(), newFilePath, userFile.getFileName(),
                    userFile.getExtendName(), sessionUser.getUserId());
        }

        return RestResult.success().data("批量移动文件成功");

    }

    @Operation(summary = "文件重命名", description = "文件重命名", tags = {"file"})
    @RequestMapping(value = "/renamefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> renameFile(@RequestBody RenameFileDTO renameFileDTO, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        UserFile userFile = userFileService.getById(renameFileDTO.getUserFileId());

        List<UserFile> userFiles = userFileService.selectUserFileByNameAndPath(renameFileDTO.getFileName(), userFile.getFilePath(), sessionUser.getUserId());
        if (userFiles != null && !userFiles.isEmpty()) {
            return RestResult.fail().message("同名文件已存在");

        }
        if (1 == userFile.getIsDir()) {
            LambdaUpdateWrapper<UserFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(UserFile::getFileName, renameFileDTO.getFileName())
                    .set(UserFile::getUploadTime, DateUtil.getCurrentTime())
                    .eq(UserFile::getUserFileId, renameFileDTO.getUserFileId());
            userFileService.update(lambdaUpdateWrapper);
            userFileService.replaceUserFilePath(userFile.getFilePath() + renameFileDTO.getFileName() + "/",
                    userFile.getFilePath() + userFile.getFileName() + "/", sessionUser.getUserId());
        } else {
            File file = fileService.getById(userFile.getFileId());

            LambdaUpdateWrapper<UserFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(UserFile::getFileName, renameFileDTO.getFileName())
                    .set(UserFile::getUploadTime, DateUtil.getCurrentTime())
                    .eq(UserFile::getUserFileId, renameFileDTO.getUserFileId());
            userFileService.update(lambdaUpdateWrapper);

        }

        return RestResult.success();
    }
}