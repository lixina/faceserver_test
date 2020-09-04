/**
 * @(#)com.xmcares.pro.domain.TFaceDB.java
 *
 * Copyright (c) 2014-2018 厦门民航凯亚有限公司
 *
 */
package com.xmcares.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 
 * @author zws
 * 人脸库信息表
 * @version 1.0 2018-2-5
 * @modified zhengkr 2018-2-5 <修改log>
 */
@Entity
@Table(name = "T_FACEDB")
public class TFaceDB implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//人脸库ID(删除入库用)
	private String id;
	
	//人脸库名称
	private String name;
	
	//人脸库编码
	private String code;
	
	//人脸库ID(主键)
	private String faceId;
	
	//备注
	private String dbdesc;
	
	//人脸库类型：证件、人脸、布控...
	private String dbtype;
	
	//是否启用（保留）
	private String isEnables;
	
	//相似度
	private String dbsimilaty;
	
	//服务编码
	private String companyCode;
	
	//删除数据：单位分钟
	private String logDate;
	
	//返回结果
	private String resultMsg;
	
	
	/**
	 * @return the name
	 */
	@Id
	@Column(name = "faceid", nullable = false,length = 36)
	public String getFaceId() {
		return faceId;
	}
	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}
	/**
	 * @return the name
	 */
	@Column(name = "logdate", length = 6)
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	
	/**
	 * @return the name
	 */
	@Column(name = "resultmsg", length = 36)
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	/**
	 * @return the name
	 */
	@Column(name = "dbsimilaty", length = 6)
	public String getDbsimilaty() {
		return dbsimilaty;
	}
	public void setDbsimilaty(String dbsimilaty) {
		this.dbsimilaty = dbsimilaty;
	}
	/**
	 * @return the name
	 */
	@Column(name = "code", length = 36)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the id
	 */
	
	@Column(name = "id", /*unique = true, */ length = 40)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	/**
	 * @return the name
	 */
	@Column(name = "name", length = 36)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the dbdesc
	 */
	@Column(name = "dbdesc", length = 100)
	public String getDbdesc() {
		return dbdesc;
	}
	public void setDbdesc(String dbdesc) {
		this.dbdesc = dbdesc;
	}
	
	/**
	 * @return the dbtype
	 */
	@Column(name = "dbtype", length = 4)
	public String getDbtype() {
		return dbtype;
	}
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
	/**
	 * @return the is_enable
	 */
	@Column(name = "is_enable", length = 4)
	public String getIsEnables() {
		return isEnables;
	}
	public void setIsEnables(String isEnables) {
		this.isEnables = isEnables;
	}
	
	/**
	 * @return the company_code
	 */
	@Column(name = "company_code", length = 36)
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	
}
