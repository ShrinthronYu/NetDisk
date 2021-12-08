package com.boshfish.operation.download;

import com.boshfish.operation.download.domain.DownloadFile;

import javax.servlet.http.HttpServletResponse;


public abstract class Downloader {
    public abstract void download(HttpServletResponse httpServletResponse, DownloadFile uploadFile);
}