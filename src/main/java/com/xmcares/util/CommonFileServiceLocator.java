/**
 * @(#)com.xmcares.ws.filesystem.service.CommonFileServiceLocator.java
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

public class CommonFileServiceLocator extends org.apache.axis.client.Service implements CommonFileService_Service {

    public CommonFileServiceLocator() {
    }


    public CommonFileServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CommonFileServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CommonFileServiceImplPort
    private java.lang.String CommonFileServiceImplPort_address = getIpAndPort();

    private String getIpAndPort() {
        // 性能测试地址 lix 20200130
    	StringBuffer sb = new StringBuffer();
    	sb.append("http://");
    	sb.append("10.83.3.128");
    	sb.append(":");
    	sb.append("8080");
    	sb.append("/xmcares-filesystem/commonFileService");
    	return sb.toString();
    	//return ScimsContext.fsAddress;
    }

    public java.lang.String getCommonFileServiceImplPortAddress() {
        return CommonFileServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CommonFileServiceImplPortWSDDServiceName = "CommonFileServiceImplPort";

    public java.lang.String getCommonFileServiceImplPortWSDDServiceName() {
        return CommonFileServiceImplPortWSDDServiceName;
    }

    public void setCommonFileServiceImplPortWSDDServiceName(java.lang.String name) {
        CommonFileServiceImplPortWSDDServiceName = name;
    }

    public CommonFileService getCommonFileServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CommonFileServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCommonFileServiceImplPort(endpoint);
    }

    public CommonFileService getCommonFileServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            CommonFileServiceSoapBindingStub _stub = new CommonFileServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCommonFileServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCommonFileServiceImplPortEndpointAddress(java.lang.String address) {
        CommonFileServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (CommonFileService.class.isAssignableFrom(serviceEndpointInterface)) {
                CommonFileServiceSoapBindingStub _stub = new CommonFileServiceSoapBindingStub(new java.net.URL(CommonFileServiceImplPort_address), this);
                _stub.setPortName(getCommonFileServiceImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CommonFileServiceImplPort".equals(inputPortName)) {
            return getCommonFileServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://serviceImpl.filesystem.ws.xmcares.com/", "commonFileService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://serviceImpl.filesystem.ws.xmcares.com/", "CommonFileServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

if ("CommonFileServiceImplPort".equals(portName)) {
            setCommonFileServiceImplPortEndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

