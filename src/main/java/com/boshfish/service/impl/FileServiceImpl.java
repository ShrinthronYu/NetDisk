package com.boshfish.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.boshfish.mapper.FileMapper;
import com.boshfish.model.File;
import com.boshfish.service.FileService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

}