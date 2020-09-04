/**
 * @(#)com.xmcares.pro.domain.TMatch.java
 *
 * Copyright (c) 2014-2018 厦门民航凯亚有限公司
 *
 */
package com.xmcares.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author zws 旅客信息表
 * @version 1.0 2018-2-5
 * @modified zhengkr 2018-2-5 <修改log>
 */
@Entity
@Table(name = "T_MATCH")
public class TMatch implements Serializable {

	private static final long serialVersionUID = 1L;

	// 主键ID
	private String pkId;

	// 人脸ID用于删除人脸
	private String faceId;

	private String dpsrSource;

	// 旅客ID
	private String dpsrId;

	// 航班号
	private String fltNo;

	// 航班日期
	private Date fltDate;

	private Date fltDeptime;
	private String fltDep;
	private String fltArr;

	// 座位号
	private String dpsrSeat;

	// 登机口
	private String dpsrBoard;

	// 证件类型
	private String dpsrCardType;

	// 证件号
	private String dpsrCard;

	private String dpscid;
	private String dpscfield;
	private String dpscdevice;

	// 入库时间
	private Date createTime;

	// 人脸库
	private String dbCode;

	@Transient
	private String baseImage;
	// 相似度
	@Transient
	private String similarity;

	@Transient
	private String staticDBId;
	// 用户id
	@Transient
	private String userId;
	// 特征码
	@Transient
	private String facefeature;
	// 证件照
	@Transient
	private String cardphoto;
	// 旅客生日
	@Transient
	private String cardbirthday;
	// 旅客英文名
	@Transient
	private String dpsrengname;
	// 旅客姓名
	private String dpsrchnname;
	// 裁剪图
	@Transient
	private String dpsrphotocut;

	// 图片路径
	private String dpscimagePath;

	// 旅客等级
	private String dpscrank;

	// 抓拍时间
	private Date dpscgrabtime;
	
	//人脸库名称
	private String dbName;
	
	//文件服务器图片存储路径
	@Transient
	private String photoMetHod;
	
	//人脸照 base64
	@Transient
	private String faceBase64;
	
	// 裁剪图
	@Transient
	private Date saveTime;

	// 性别
	private String dpsrGender;

	@Column(name = "dpsr_gender", length = 10)
	public String getDpsrGender() {
		return dpsrGender;
	}

	public void setDpsrGender(String dpsrGender) {
		this.dpsrGender = dpsrGender;
	}

	@Transient
	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	@Transient
	public String getFaceBase64() {
		return faceBase64;
	}

	public void setFaceBase64(String faceBase64) {
		this.faceBase64 = faceBase64;
	}

	@Column(name = "dbname", length = 36)
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Transient
	public String getPhotoMetHod() {
		return photoMetHod;
	}

	public void setPhotoMetHod(String photoMetHod) {
		this.photoMetHod = photoMetHod;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 23)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "dbcode", nullable = false, length = 40)
	public String getDbCode() {
		return dbCode;
	}

	public void setDbCode(String dbCode) {
		this.dbCode = dbCode;
	}

	/**
	 * @return the pkId
	 */
	@Id
	@Column(name = "pk_id", unique = true, nullable = false, length = 36)
	public String getPkId() {
		return pkId;
	}

	/**
	 * @param pkId
	 *            the pkId to set
	 */
	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	/**
	 * @return the faceId
	 */
	@Column(name = "face_id", length = 50)
	public String getFaceId() {
		return faceId;
	}

	/**
	 * @param faceId
	 *            the faceId to set
	 */
	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}

	/**
	 * @return the faceId
	 */
	@Column(name = "dpsc_id", length = 40)
	public String getDpscid() {
		return dpscid;
	}

	public void setDpscid(String dpscid) {
		this.dpscid = dpscid;
	}

	/**
	 * @return the faceId
	 */
	@Column(name = "dpsc_field", length = 20)
	public String getDpscfield() {
		return dpscfield;
	}

	public void setDpscfield(String dpscfield) {
		this.dpscfield = dpscfield;
	}

	/**
	 * @return the faceId
	 */
	@Column(name = "dpsc_device", length = 20)
	public String getDpscdevice() {
		return dpscdevice;
	}

	public void setDpscdevice(String dpscdevice) {
		this.dpscdevice = dpscdevice;
	}

	/**
	 * @return the dpsrSource
	 */
	@Column(name = "dpsr_source", length = 10)
	public String getDpsrSource() {
		return dpsrSource;
	}

	/**
	 * @param dpsrSource
	 *            the dpsrSource to set
	 */
	public void setDpsrSource(String dpsrSource) {
		this.dpsrSource = dpsrSource;
	}

	/**
	 * @return the dpsrId
	 */
	@Column(name = "dpsr_id", nullable = false, length = 36)
	public String getDpsrId() {
		return dpsrId;
	}

	/**
	 * @param dpsrId
	 *            the dpsrId to set
	 */
	public void setDpsrId(String dpsrId) {
		this.dpsrId = dpsrId;
	}

	/**
	 * @return the fltNo
	 */
	@Column(name = "flt_no", length = 10)
	public String getFltNo() {
		return fltNo;
	}

	/**
	 * @param fltNo
	 *            the fltNo to set
	 */
	public void setFltNo(String fltNo) {
		this.fltNo = fltNo;
	}

	/**
	 * @return the fltDate
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "flt_date", length = 7)
	public Date getFltDate() {
		return fltDate;
	}

	/**
	 * @param fltDate
	 *            the fltDate to set
	 */
	public void setFltDate(Date fltDate) {
		this.fltDate = fltDate;
	}

	/**
	 * @return the fltDeptime
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "flt_deptime", length = 7)
	public Date getFltDeptime() {
		return fltDeptime;
	}

	/**
	 * @param fltDeptime
	 *            the fltDeptime to set
	 */
	public void setFltDeptime(Date fltDeptime) {
		this.fltDeptime = fltDeptime;
	}

	/**
	 * @return the fltDep
	 */
	@Column(name = "flt_dep", length = 10)
	public String getFltDep() {
		return fltDep;
	}

	/**
	 * @param fltDep
	 *            the fltDep to set
	 */
	public void setFltDep(String fltDep) {
		this.fltDep = fltDep;
	}

	/**
	 * @return the fltArr
	 */
	@Column(name = "flt_arr", length = 10)
	public String getFltArr() {
		return fltArr;
	}

	/**
	 * @param fltArr
	 *            the fltArr to set
	 */
	public void setFltArr(String fltArr) {
		this.fltArr = fltArr;
	}

	/**
	 * @return the dpsrSeat
	 */
	@Column(name = "dpsr_seat", length = 10)
	public String getDpsrSeat() {
		return dpsrSeat;
	}

	/**
	 * @param dpsrSeat
	 *            the dpsrSeat to set
	 */
	public void setDpsrSeat(String dpsrSeat) {
		this.dpsrSeat = dpsrSeat;
	}

	/**
	 * @return the dpsrBoard
	 */
	@Column(name = "dpsr_board", length = 10)
	public String getDpsrBoard() {
		return dpsrBoard;
	}

	/**
	 * @param dpsrBoard
	 *            the dpsrBoard to set
	 */
	public void setDpsrBoard(String dpsrBoard) {
		this.dpsrBoard = dpsrBoard;
	}

	/**
	 * @return the dpsrCardType
	 */
	@Column(name = "dpsr_card_type", length = 10)
	public String getDpsrCardType() {
		return dpsrCardType;
	}

	/**
	 * @param dpsrCardType
	 *            the dpsrCardType to set
	 */
	public void setDpsrCardType(String dpsrCardType) {
		this.dpsrCardType = dpsrCardType;
	}

	/**
	 * @return the dpsrCard
	 */
	@Column(name = "dpsr_card", length = 20)
	public String getDpsrCard() {
		return dpsrCard;
	}

	/**
	 * @param dpsrCard
	 *            the dpsrCard to set
	 */
	public void setDpsrCard(String dpsrCard) {
		this.dpsrCard = dpsrCard;
	}

	@Transient
	public String getBaseImage() {
		return baseImage;
	}

	public void setBaseImage(String baseImage) {
		this.baseImage = baseImage;
	}

	@Transient
	public String getSimilarity() {
		return similarity;
	}

	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}

	@Transient
	public String getStaticDBId() {
		return staticDBId;
	}

	public void setStaticDBId(String staticDBId) {
		this.staticDBId = staticDBId;
	}

	@Transient
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Transient
	public String getFacefeature() {
		return facefeature;
	}

	public void setFacefeature(String facefeature) {
		this.facefeature = facefeature;
	}

	@Transient
	public String getCardphoto() {
		return cardphoto;
	}

	public void setCardphoto(String cardphoto) {
		this.cardphoto = cardphoto;
	}

	@Transient
	public String getCardbirthday() {
		return cardbirthday;
	}

	public void setCardbirthday(String cardbirthday) {
		this.cardbirthday = cardbirthday;
	}

	@Transient
	public String getDpsrengname() {
		return dpsrengname;
	}

	public void setDpsrengname(String dpsrengname) {
		this.dpsrengname = dpsrengname;
	}

	@Column(name = "dpsc_chn_name", length = 40)
	public String getDpsrchnname() {
		return dpsrchnname;
	}

	public void setDpsrchnname(String dpsrchnname) {
		this.dpsrchnname = dpsrchnname;
	}

	@Transient
	public String getDpsrphotocut() {
		return dpsrphotocut;
	}

	public void setDpsrphotocut(String dpsrphotocut) {
		this.dpsrphotocut = dpsrphotocut;
	}

	@Column(name = "dpsc_image_path", length = 40)
	public String getDpscimagePath() {
		return dpscimagePath;
	}

	public void setDpscimagePath(String dpscimagePath) {
		this.dpscimagePath = dpscimagePath;
	}

	@Column(name = "dpsc_rank", length = 10)
	public String getDpscrank() {
		return dpscrank;
	}

	public void setDpscrank(String dpscrank) {
		this.dpscrank = dpscrank;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dpsc_grab_time", length = 23)
	public Date getDpscgrabtime() {
		return dpscgrabtime;
	}

	public void setDpscgrabtime(Date dpscgrabtime) {
		this.dpscgrabtime = dpscgrabtime;
	}

}
