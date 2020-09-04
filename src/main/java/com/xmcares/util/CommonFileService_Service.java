/**
 * @(#)com.xmcares.ws.filesystem.service.CommonFileService_Service.java
 *
 * Copyright (c) 2014-2018 crimps
 *
 */
package com.xmcares.util;

/**
 * 文件服务器
 *
 * @author crimps
 * @create 2017-11-03 9:58
 **/
public interface CommonFileService_Service extends javax.xml.rpc.Service {
    public java.lang.String getCommonFileServiceImplPortAddress();

    public CommonFileService getCommonFileServiceImplPort() throws javax.xml.rpc.ServiceException;

    public CommonFileService getCommonFileServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
