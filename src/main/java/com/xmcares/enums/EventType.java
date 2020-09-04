/**
 * @(#)com.xmcares.pro.enums.EventType.java
 *
 * Copyright (c) 2014-2018 厦门民航凯亚有限公司
 *
 */
package com.xmcares.enums;

/**
*
* @author lix
* @version 1.0  20200425
*/
public enum EventType {
	dbadd, 	//人脸库新增
	dbupd, 	//人脸库修改
	dbdel, 	//人脸库删除
	dbsel, 	//人脸库查询
	dballsel, //人脸库查询全部
	dbpicsel, //特征数查询
	picadd, //图片新增
	picdel,	//图片删除
	picalldel, //批量删除
	picsel,	//图片查询
	picfeasel, //提取特征码
	picfacedet, //人脸检测
	picfacecomp, //人脸验证
	sysstatus //引擎状态
}
