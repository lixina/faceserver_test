/**
 * @(#)com.xmcares.ws.filesystem.service.CommonFileService.java
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
public interface CommonFileService extends java.rmi.Remote {
    public java.lang.String upload(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, byte[] arg3) throws java.rmi.RemoteException;
    public byte[] download(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException;
}
