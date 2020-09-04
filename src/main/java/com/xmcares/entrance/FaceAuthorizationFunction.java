/**
 * @(#)com.xmcares.pro.util.face.YCFaceUtils.java
 *
 * Copyright (c) 2014-2018 厦门民航凯亚有限公司
 *
 */
package com.xmcares.entrance;

import com.xmcares.entity.TFaceDB;
import com.xmcares.entity.TMatch;
import com.xmcares.enums.EventType;
import com.xmcares.util.SynAuthorizationHttpClientsTest;
import com.xmcares.util.SynHttpClientsUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
* 人脸服务器接口方法测试类 lix
*/
public class FaceAuthorizationFunction {
	private static Logger logger = LoggerFactory.getLogger(FaceAuthorizationFunction.class);


		/**
		 * 对json字符串格式化输出
		 */
		public String formatJson(String jsonStr) {
			if (null == jsonStr || "".equals(jsonStr)) {
				return "";
			}
			StringBuilder sb = new StringBuilder();
			char last = '\0';
			char current = '\0';
			int indent = 0;
			for (int i = 0; i < jsonStr.length(); i++) {
				last = current;
				current = jsonStr.charAt(i);
				switch (current) {
					case '{':
					case '[':
						sb.append(current);
						sb.append('\n');
						indent++;
						addIndentBlank(sb, indent);
						break;
					case '}':
					case ']':
						sb.append('\n');
						indent--;
						addIndentBlank(sb, indent);
						sb.append(current);
						break;
					case ',':
						sb.append(current);
						if (last != '\\') {
							sb.append('\n');
							addIndentBlank(sb, indent);
						}
						break;
					default:
						sb.append(current);
				}
			}

			return sb.toString();
		}

		/**
		 * 添加space
		 */
		private void addIndentBlank(StringBuilder sb, int indent) {
			for (int i = 0; i < indent; i++) {
				sb.append('\t');
			}
		}

	public static String operation(String ip, String port, String name, String pwd, EventType type, JSONObject parameters)  throws Exception {
		if("dballsel".equals(type.toString()) ||  "sysstatus".equals(type.toString())){

		}else {
			if (parameters == null) {
				return "param is null";
			}
		}
		if(parameters != null){
			logger.info("parameters:"+parameters.toString());
		}
		return getResult(ip, port, name ,pwd,type,parameters);
	}


	public static String getResult(String ip, String port, String name, String pwd, EventType type, JSONObject parameters) throws Exception {
		//获取服务器地址
		String url = getUrl(ip, port, type);
		if (url == null) {
			logger.info("url is null");
			return "url is null";
		}
		try {
			switch (type) {
			case dbadd:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case dbupd:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case dbdel:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case dballsel:
				return SynAuthorizationHttpClientsTest.doPost(url, null);
			case dbsel:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case dbpicsel:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case picadd:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case picdel:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case picalldel:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case picsel:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case picfeasel:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case picfacedet:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case picfacecomp:
				return SynAuthorizationHttpClientsTest.doPost(url, parameters.toString());
			case sysstatus:
				return SynAuthorizationHttpClientsTest.doPost(url, null);
			default:
				return null;
			}
		} catch (Exception e) {
			logger.info("yc getResult is error:" + e.getMessage());
			throw e;
		}
	}

	/**
	 * 人脸库操作URL
	 * @return
	 */
	public static String getUrl(String ip, String port, EventType type)  throws Exception {
		switch (type) {
		case dbadd:
			return "http://" + ip + ":" + port + "/xmcares_fp/db/create";
		case dbupd:
			return "http://" + ip + ":" + port + "/xmcares_fp/db/edit";
		case dbdel:
			return "http://" + ip + ":" + port + "/xmcares_fp/db/delete";
		case dbsel:
			return "http://" + ip + ":" + port + "/xmcares_fp/db/query";
		case dballsel:
			return "http://" + ip + ":" + port + "/xmcares_fp/db/queryAllInfo";
		case dbpicsel:
			return "http://" + ip + ":" + port + "/xmcares_fp/db/queryFeatureCount";
		case picadd:
			return "http://" + ip + ":" + port + "/xmcares_fp/face/add";
		case picdel:
			return "http://" + ip + ":" + port + "/xmcares_fp/face/delete";
		case picalldel:
			return "http://" + ip + ":" + port + "/xmcares_fp/face/deleteBatch";
		case picsel:
			return "http://" + ip + ":" + port + "/xmcares_fp/recog/search";
		case picfeasel:
			return "http://" + ip + ":" + port + "/xmcares_fp/recog/feature";
		case picfacedet:
			return "http://" + ip + ":" + port + "/xmcares_fp/recog/detect";
		case picfacecomp:
			return "http://" + ip + ":" + port + "/xmcares_fp/recog/compare";
		case sysstatus:
			return "http://" + ip + ":" + port + "/xmcares_fp/status";
		default:
			return null;

		}
	}
	
	/**
	 * 3.1.1 新增人脸库参数
	 * @param faceDB
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getsaveFace(TFaceDB faceDB) throws Exception {
		System.out.println("id:"+faceDB.getId());
		JSONObject parameters = new JSONObject();
		parameters.put("DBId", faceDB.getId());
		parameters.put("name", faceDB.getCode());
		parameters.put("desc", faceDB.getDbdesc());
		return parameters;
	}
	
	/**
	 * 3.1.2删除人脸库参数
	 * @throws JSONException
	 */
	public static JSONObject getdelFace(TFaceDB faceDB) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("DBId", faceDB.getId());
		return parameters;
	}
	
	/**
	 * 3.1.3 查询人脸库参数
	 * @param faceDB
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getSelFace(TFaceDB faceDB) throws Exception {
		JSONObject parameters = new JSONObject();
		System.out.println("id:"+faceDB.getId());
		parameters.put("DBId", faceDB.getId());
		parameters.put("name", faceDB.getCode());
		return parameters;
	}
	
	/**
	 * 3.1.4 更新人脸库参数
	 * @param faceDB
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getUpdateFace(TFaceDB faceDB) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("DBId", faceDB.getId());
		parameters.put("name", faceDB.getCode());
		parameters.put("desc", faceDB.getDbdesc());
		return parameters;
	}

	/**
	 * 3.1.6查询人脸库特征码参数
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getSelFeaFace(TFaceDB faceDB) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("DBId", faceDB.getId());
		return parameters;
	}
	
	/**
	 * 新增、修改、删除人脸库结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void delorupFaceDBYC(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		logger.info("saveFaceDBYC获取结果result:" + retData.getInt("result"));
		logger.info("saveFaceDBYC获取结果extInfo:" + retData.getString("extInfo"));
		/*if (0 != result) {
			logger.info("saveFaceDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			logger.info("saveFaceDBYC获取结果DBId:" + retData.getString("DBId"));
			logger.info("saveFaceDBYC获取结果name:" + retData.getString("name"));
			logger.info("saveFaceDBYC获取结果desc:" + retData.getString("desc"));
			logger.info("saveFaceDBYC获取结果extInfo:" + retData.getString("extInfo"));
		}*/
	}
	
	

	/**
	 * 查询人脸库结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static List<TFaceDB> getFaceDBYC(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("getFaceDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		}
		JSONArray jsonArrayTargets = retData.getJSONArray("list");
		List<TFaceDB> faceDBs = new ArrayList<TFaceDB>();
		for (int i = 0; i < jsonArrayTargets.length(); i++) {
			JSONObject singleTargets = jsonArrayTargets.getJSONObject(i);
			TFaceDB faceDB = new TFaceDB();
			faceDB.setId(singleTargets.getString("DBId"));
			faceDB.setCode(singleTargets.getString("name"));
			faceDB.setDbdesc(singleTargets.getString("desc"));
			faceDBs.add(faceDB);
		}
		return faceDBs;
	}


	/**
	 * 查询人脸库特征数结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void getFeaFaceDBYC(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("delorupFaceDBYC获取失败:" +  retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			logger.info("saveFaceDBYC获取结果faceCount:" + retData.getLong("faceCount"));
			logger.info("saveFaceDBYC获取结果loadCount:" + retData.getLong("loadCount"));
		}
	}
	
	/**
	 * 3.2.1保存图片参数
	 * @param code
	 * @param tMatch
	 * @param picBase64
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getsavePicFace(String code, TMatch tMatch, String picBase64) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("DBId", code);
		parameters.put("faceId", tMatch.getDpsrId());
		parameters.put("img", picBase64.replaceAll(" ", "+"));
		return parameters;
	}
	
	/**
	 * 3.2.2删除图片参数
	 * @param code
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getdelPicFace(String code, String id) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("DBId", code);
		parameters.put("faceId", id);
		return parameters;
	}
	
	/**
	 * 3.2.3批量删除图片参数
	 * @param code
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getdelAllPicFace(String code, List<String> facIds) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("DBId", code);
		JSONArray faces=new JSONArray();
		for(String id:facIds){
			JSONObject faceId = new JSONObject();
			faceId.put("faceId", id);
			faces.put(faceId);
		}
		parameters.put("faces",faces);
		return parameters;
	}
	
	/**
	 * 保存图片结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void savePicYC(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("savePicDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			logger.info("savePicDBYC获取结果extInfo:" + retData.getString("extInfo"));
			logger.info("savePicDBYC获取结果DBId:" + retData.getString("DBId"));
			logger.info("savePicDBYC获取结果qualityScore:" + retData.getString("qualityScore"));
		}
	}
	
	/**
	 * 删除图片结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void delPicYC(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("savePicDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			logger.info("savePicDBYC获取结果extInfo:" + retData.getString("extInfo"));
			logger.info("savePicDBYC获取结果DBId:" + retData.getString("DBId"));
		}
	}
	
	/**
	 *批量删除图片结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void delAllPicYC(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("savePicDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			logger.info("savePicDBYC获取结果extInfo:" + retData.getString("extInfo"));
			logger.info("savePicDBYC获取结果allResponse:" + retData.getString("allResponse"));
		}
	}

	/**
	 * 3.3.1 以图搜图参数
	 * @param code
	 * @param picBase64
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getsearchPicFace(String code, String picBase64, String reqId) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("DBId", code);
		parameters.put("topN", "1");
		parameters.put("reqId", reqId);
		parameters.put("img",picBase64.replaceAll(" ", "+"));
		parameters.put("type", "1");
		return parameters;
	}
	
	/**
	 * 以图搜图人脸库结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void searchFaceDBYC(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("searchFaceDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			JSONArray jsonArray = retData.getJSONArray("faces");
			JSONObject row = jsonArray.getJSONObject(0); 
			logger.info("searchFaceDBYC获取结果score:" + row.getDouble("score"));
			logger.info("searchFaceDBYC获取结果DBId:" + row.getString("DBId"));
			logger.info("searchFaceDBYC获取结果faceId:" + row.getString("faceId"));
			/*if(row!=null && row.getDouble("score")>simility){
				tMatch=matchDao.findByPkId(row.getString("userId"));
			}
			tMatch.setUserId(row.getString("userId"));
			tMatch.setSimilarity(row.getDouble("score")+"");
			tMatch.setStaticDBId(row.getString("staticDBId"));
			return tMatch;*/
		}
	}
	
	/**
	 * 3.3.2提取特征参数
	 * @param picBase64
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getSearchFaceFea(String picBase64) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("img",picBase64.replaceAll(" ", "+"));
		return parameters;
	}
	
	/**
	 * 提取特征结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void searchFaceFea(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("searchFaceDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			logger.info("searchFaceDBYC获取结果feature:" + retData.getString("feature"));
			/*JSONArray jsonArray = retData.getJSONArray("faces");
			JSONObject row = jsonArray.getJSONObject(0); */
		}
	}
	
	/**
	 * 3.3.3人脸检测参数
	 * @param picBase64
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getSearchFaceDet(String picBase64) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("img",picBase64.replaceAll(" ", "+"));
		return parameters;
	}
	
	/**
	 * 人脸检测结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void searchFaceDet(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("searchFaceDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			JSONArray jsonArray = retData.getJSONArray("faces");
			JSONObject row = jsonArray.getJSONObject(0); 
			logger.info("searchFaceDBYC获取结果img:" + retData.getString("img"));
			logger.info("searchFaceDBYC获取结果x:" + retData.getInt("x"));
			logger.info("searchFaceDBYC获取结果y:" + retData.getInt("y"));
			logger.info("searchFaceDBYC获取结果w:" + retData.getInt("w"));
			logger.info("searchFaceDBYC获取结果h:" + retData.getInt("h"));
		}
	}
	
	/**
	 * 3.3.4 人脸验证参数
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getSearchFaceComp(String imageBase64A, String imageBase64B) throws Exception {
		JSONObject parameters = new JSONObject();
		parameters.put("imgA", imageBase64A);
		parameters.put("imgB", imageBase64B);
		return parameters;
	}
	
	/**
	 * 人脸验证结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void searchFaceComp(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("searchFaceDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			logger.info("searchFaceDBYC获取结果:" + retData.getString("score"));
		}
	}
	
	
	
	
	/**
	 * 引擎状态结果(云从)
	 * @param resultStr
	 * @return
	 * @throws JSONException
	 */
	public static void getSysStatusResult(String resultStr) throws Exception {
		JSONObject retData = new JSONObject(resultStr);
		int result = retData.getInt("result");
		if (0 != result) {
			logger.info("searchFaceDBYC获取失败:" + retData.getString("extInfo"));
			throw new NullPointerException();
		} else {
			JSONArray jsonArray = retData.getJSONArray("machine_list");
			JSONObject row = jsonArray.getJSONObject(0); 
			logger.info("searchFaceDBYC获取结果:" + row.getString("name"));
			logger.info("searchFaceDBYC获取结果:" + retData.getString("version"));
			logger.info("searchFaceDBYC获取结果:" + retData.getString("status"));
			logger.info("searchFaceDBYC获取结果:" + retData.getString("cpu"));
			logger.info("searchFaceDBYC获取结果:" + retData.getString("disk"));
			logger.info("searchFaceDBYC获取结果:" + retData.getString("mem"));
			logger.info("searchFaceDBYC获取结果:" + retData.getString("alive"));
		}
	}


	
	
	


	
	
	



	
	

	
	
}
