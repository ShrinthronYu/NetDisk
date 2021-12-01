package com.wavebigfish.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wavebigfish.mapper.FileMapper;
import com.wavebigfish.model.File;
import com.wavebigfish.service.FileService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

}