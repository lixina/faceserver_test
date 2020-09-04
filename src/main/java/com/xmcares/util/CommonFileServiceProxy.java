/**
 * @(#)com.xmcares.ws.filesystem.service.CommonFileServiceProxy.java
 *
 * Copyright (c) 2014-2018 crimps
 *
 */
package com.xmcares.util;

import java.rmi.RemoteException;

/**
 * 文件服务器
 *
 * @author crimps
 * @create 2017-11-03 9:58
 **/
public class CommonFileServiceProxy implements CommonFileService {
  private String _endpoint = null;
  private CommonFileService commonFileService_PortType = null;
  
  public CommonFileServiceProxy() {
    _initCommonFileServiceProxy();
  }
  
  public CommonFileServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCommonFileServiceProxy();
  }
  
  private void _initCommonFileServiceProxy() {
    try {
      commonFileService_PortType = (new CommonFileServiceLocator()).getCommonFileServiceImplPort();
      if (commonFileService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)commonFileService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)commonFileService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (commonFileService_PortType != null)
      ((javax.xml.rpc.Stub)commonFileService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public CommonFileService getCommonFileService_PortType() {
    if (commonFileService_PortType == null)
      _initCommonFileServiceProxy();
    return commonFileService_PortType;
  }

@Override
public String upload(String arg0, String arg1, String arg2, byte[] arg3)
		throws RemoteException {
	return commonFileService_PortType.upload(arg0, arg1, arg2, arg3);
}

@Override
public byte[] download(String arg0, String arg1, String arg2)
		throws RemoteException {
	// TODO Auto-generated method stub
	return commonFileService_PortType.download(arg0, arg1, arg2)
			;
}
  
  
}
