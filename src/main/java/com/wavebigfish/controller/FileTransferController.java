package com.wavebigfish.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wavebigfish.dto.DownloadFileDTO;
import com.wavebigfish.common.RestResult;
import com.wavebigfish.dto.UploadFileDTO;
import com.wavebigfish.model.File;
import com.wavebigfish.model.User;
import com.wavebigfish.model.UserFile;
import com.wavebigfish.service.FileService;
import com.wavebigfish.service.FileTransferService;
import com.wavebigfish.service.UserFileService;
import com.wavebigfish.service.UserService;
import com.wavebigfish.util.DateUtil;
import com.wavebigfish.util.FileUtil;
import com.wavebigfish.vo.UploadFileVo;

import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "filetransfer", description = "该接口为文件传输接口，主要用来做文件的上传和下载")
@RestController
@RequestMapping("/filetransfer")
public class FileTransferController {

    @Resource
    UserService userService;
    @Resource
    FileService fileService;
    @Resource
    UserFileService userFileService;
    @Resource
    FileTransferService fileTransferService;

    @Operation(summary = "极速上传", description = "校验文件MD5判断文件是否存在，如果存在直接上传成功并返回skipUpload=true，如果不存在返回skipUpload=false需要再次调用该接口的POST方法", tags = {"filetransfer"})
    @GetMapping(value = "/uploadfile")
    @ResponseBody
    public RestResult<UploadFileVo> uploadFileSpeed(UploadFileDTO uploadFileDto, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {

            return RestResult.fail().message("未登录");
        }

        UploadFileVo uploadFileVo = new UploadFileVo();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("identifier", uploadFileDto.getIdentifier());
        synchronized (FileTransferController.class) {
            List<File> list = fileService.listByMap(param);
            if (list != null && !list.isEmpty()) {
                File file = list.get(0);

                UserFile userFile = new UserFile();
                userFile.setFileId(file.getFileId());
                userFile.setUserId(sessionUser.getUserId());
                userFile.setFilePath(uploadFileDto.getFilePath());
                String fileName = uploadFileDto.getFilename();
                userFile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
                userFile.setExtendName(FileUtil.getFileExtendName(fileName));
                userFile.setIsDir(0);
                userFile.setUploadTime(DateUtil.getCurrentTime());
                userFileService.save(userFile);
                uploadFileVo.setSkipUpload(true);

            } else {
                uploadFileVo.setSkipUpload(false);
            }
        }
        return RestResult.success().data(uploadFileVo);
    }

    @Operation(summary = "上传文件", description = "真正的上传文件接口", tags = {"filetransfer"})
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<UploadFileVo> uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("未登录");
        }

        fileTransferService.uploadFile(request, uploadFileDto, sessionUser.getUserId());
        UploadFileVo uploadFileVo = new UploadFileVo();
        return RestResult.success().data(uploadFileVo);
    }

    @Operation(summary = "下载文件", description = "下载文件接口", tags = {"filetransfer"})
    @RequestMapping(value = "/downloadfile", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO) {
        fileTransferService.downloadFile(response, downloadFileDTO);
    }

    @Operation(summary = "获取存储信息", description = "获取存储信息", tags = {"filetransfer"})
    @RequestMapping(value = "/getstorage", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<Long> getStorage(@RequestHeader("token") String token) {

        User sessionUserBean = userService.getUserByToken(token);
        EmbeddedMongoProperties.Storage storageBean = new EmbeddedMongoProperties.Storage();

        Long storageSize = fileTransferService.selectStorageSizeByUserId(sessionUserBean.getUserId());
        return RestResult.success().data(storageSize);
    }
}