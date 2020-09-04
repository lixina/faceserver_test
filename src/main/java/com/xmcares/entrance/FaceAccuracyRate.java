package com.xmcares.entrance;


import com.alibaba.fastjson.JSONObject;
import com.xmcares.entity.TMatch;
import com.xmcares.util.DateUtils;
import com.xmcares.util.OracleJdbcUtils;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 人脸服务器准确率测试类 lix
 */
public class FaceAccuracyRate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String url2 = "http://10.83.2.232:10010/xmcares_fp/recog/search";

    /**
     *以图搜图准确率测试 lix
     */
    public void searchPicFace(String ip, String port, String DBId, String name, String pwd, String num1, String num2){
        url2 = "http://"+ip+":"+port+"/xmcares_fp/recog/search";
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
            okHttpClient.dispatcher().setMaxRequestsPerHost(10);
            okHttpClient.dispatcher().setMaxRequests(64);
            OracleJdbcUtils test = new OracleJdbcUtils();
            for(int i = 0; i < 2; i++){
                List<TMatch> tMatchs = null;
                if(i == 0){
                    // 关注库
                    tMatchs=test.query("select pm_r_id,pm_r_comp,pm_c_path from tb_pic_model where pm_mm_id = '4' and rownum < 1001", true);
                }else if(i == 1){
                    //非关注库
                    tMatchs=test.query("select pm_r_id,pm_r_comp,pm_c_path from tb_pic_model where pm_mm_id = '5' and rownum < 1001", true);
                }
                if(!tMatchs.isEmpty() && tMatchs.size()>0){
                        String[] base64s = new String[tMatchs.size()];
                    for(int j=0;j<tMatchs.size();j++){
                        byte imgByte[] = com.xmcares.util.FileUtils.downLoadFcFile(tMatchs.get(j).getDpsrphotocut(),
                                "facePicCut");
                        String picBase64 = Base64.encodeBase64String(imgByte);

                        base64s[j]=picBase64;
                        logger.info("picBase64................"+j);
                    }
                    logger.info("查询开始："+ DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    for(int j=0;j<tMatchs.size();j++){
                        final TMatch tMatch = tMatchs.get(j);
                        final String reqId =j+"";
                        long start= System.currentTimeMillis();
                        String picBase64 = base64s[j];
                        JSONObject parameters = new JSONObject();
                        parameters.put("DBId", DBId);
                        parameters.put("topN", "1");
                        parameters.put("reqId", reqId);
                        parameters.put("img",picBase64.replaceAll(" ", "+"));
                        parameters.put("type", "1");
                        RequestBody postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters.toJSONString());
                        Base64 base = new Base64();
                        String namepwd = name+":"+pwd;
                        String base64Sign = base.encodeToString(namepwd.getBytes("UTF-8"));
                        final Request request = new Request.Builder()
                                .url(url2)
                                .addHeader("Authorization", "Basic "+base64Sign)
                                .post(postBody)
                                .build();
                        Call call = okHttpClient.newCall(request);
                        Response response = call.execute();
                        // 其中Response对象就是服务器返回的数据，将数据转换成字符串
                        String responseData = response.body().string();
                        org.json.JSONObject jsonObject = new org.json.JSONObject(responseData);
                        String faceId = jsonObject.getJSONArray("faces").getJSONObject(0).getString("faceId");
                        String similarity =  String.valueOf(jsonObject.getJSONArray("faces").getJSONObject(0).get("score"));
                        // 关注
                        List<TMatch> tMatchTemp = test.query("select * from tb_pic_model where pm_r_id = '"+faceId+"'", true);
                        String cardPhoto = tMatchTemp.get(0).getCardphoto();
                        // 保存图片 图片名称为主键
                        /*BufferedImage image = null;
                        byte[] imageByte = null;
                        try {
                            imageByte = DatatypeConverter.parseBase64Binary(picBase64);
                            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                            image = ImageIO.read(new ByteArrayInputStream(imageByte));
                            bis.close();
                            if(i == 0){
                                File outputfile = new File("f://rl//关注库图片//"+tMatchs.get(j).getDpsrId()+"_"+faceId+".jpg");
                                ImageIO.write(image, "jpg", outputfile);
                            }else{
                                File outputfile = new File("f://rl//非关注库图片//"+tMatchs.get(j).getDpsrId()+"_"+faceId+".jpg");
                                ImageIO.write(image, "jpg", outputfile);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        logger.info("关注库入库开始.......................");
                        if(i == 0){
                            // 关注库
                            //if(faceId.equals(tMatchs.get(j).getDpsrId())){
                            if(cardPhoto.equals(tMatchs.get(j).getCardphoto())){
                                test.query("insert into tb_test_detail(td_id,td_tb_id,td_isperson,td_similarity,td_cost,td_c_path,td_comp_path)" + "values('"+tMatch.getDpsrId()+"'," +
                                        ""+num1+","+1+",'"+similarity+"',"+0+",'"+tMatchs.get(j).getCardphoto()+"'," +"'"+tMatchs.get(j).getDpsrphotocut()+"')");
                                logger.info("关注库入库总数"+j+"个.......................");
                            }else{
                                test.query("insert into tb_test_detail(td_id,td_tb_id,td_isperson,td_similarity,td_cost,td_c_path,td_comp_path) values('"+tMatch.getDpsrId()+"',"+
                                        ""+num1+","+1+",'"+similarity+"',"+1+",'"+tMatchs.get(j).getCardphoto()+"',+'"+tMatchs.get(j).getDpsrphotocut()+"')");
                                logger.info("关注库入库总数"+j+"个......................");
                            }
                        }else if(i == 1){
                            // 非关注库
                            //if(faceId.equals(tMatchs.get(j).getDpsrId())){
                            if(cardPhoto.equals(tMatchs.get(j).getCardphoto())){
                                test.query("insert into tb_test_detail(td_id,td_tb_id,td_isperson,td_similarity,td_cost,td_c_path,td_comp_path)" + "values('"+tMatch.getDpsrId()+"'," +
                                        ""+num2+","+1+",'"+similarity+"',"+0+",'"+tMatchs.get(j).getCardphoto()+"'," +"'"+tMatchs.get(j).getDpsrphotocut()+"')");
                                logger.info("非关注库入库总数"+j+"个.......................");
                            }else{
                                test.query("insert into tb_test_detail(td_id,td_tb_id,td_isperson,td_similarity,td_cost,td_c_path,td_comp_path) values('"+tMatch.getDpsrId()+"',"+num2+","+1+",'"+similarity+"',"+1+",'"+tMatchs.get(j).getCardphoto()+"',+'"+tMatchs.get(j).getDpsrphotocut()+"')");
                                logger.info("非关注库入库总数"+j+"个.......................");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("PerformanceTestException:"+e.getMessage());
        }
    }

    public void searchPicFace(String ip, String port, String DBId, String num1, String num2){
        url2 = "http://"+ip+":"+port+"/xmcares_fp/recog/search";
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
            okHttpClient.dispatcher().setMaxRequestsPerHost(10);
            okHttpClient.dispatcher().setMaxRequests(64);
            OracleJdbcUtils test = new OracleJdbcUtils();
            for(int i = 0; i < 2; i++){
                List<TMatch> tMatchs = null;
                if(i == 0){
                    // 关注库
                    tMatchs=test.query("select pm_r_id,pm_r_comp,pm_c_path from tb_pic_model where pm_mm_id = '4' and rownum < 1001", true);
                }else if(i == 1){
                    //非关注库
                    tMatchs=test.query("select pm_r_id,pm_r_comp,pm_c_path from tb_pic_model where pm_mm_id = '5' and rownum < 1001", true);
                }
                if(!tMatchs.isEmpty() && tMatchs.size()>0){
                    String[] base64s = new String[tMatchs.size()];
                    for(int j=0;j<tMatchs.size();j++){
                        byte imgByte[] = com.xmcares.util.FileUtils.downLoadFcFile(tMatchs.get(j).getDpsrphotocut(),
                                "facePicCut");
                        String picBase64 = Base64.encodeBase64String(imgByte);

                        base64s[j]=picBase64;
                        logger.info("picBase64................"+j);
                    }
                    logger.info("查询开始："+ DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    for(int j=0;j<tMatchs.size();j++){
                        final TMatch tMatch = tMatchs.get(j);
                        final String reqId =j+"";
                        long start= System.currentTimeMillis();
                        String picBase64 = base64s[j];
                        JSONObject parameters = new JSONObject();
                        parameters.put("DBId", DBId);
                        parameters.put("topN", "1");
                        parameters.put("reqId", reqId);
                        parameters.put("img",picBase64.replaceAll(" ", "+"));
                        parameters.put("type", "1");
                        RequestBody postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters.toJSONString());
                        Base64 base = new Base64();
                        final Request request = new Request.Builder()
                                .url(url2)
                                .post(postBody)
                                .build();
                        Call call = okHttpClient.newCall(request);
                        Response response = call.execute();
                        // 其中Response对象就是服务器返回的数据，将数据转换成字符串
                        String responseData = response.body().string();
                        org.json.JSONObject jsonObject = new org.json.JSONObject(responseData);
                        String faceId = jsonObject.getJSONArray("faces").getJSONObject(0).getString("faceId");
                        String similarity =  String.valueOf(jsonObject.getJSONArray("faces").getJSONObject(0).get("score"));
                        // 关注
                        List<TMatch> tMatchTemp = test.query("select * from tb_pic_model where pm_r_id = '"+faceId+"'", true);
                        String cardPhoto = tMatchTemp.get(0).getCardphoto();
                        // 保存图片 图片名称为主键
                        /*BufferedImage image = null;
                        byte[] imageByte = null;
                        try {
                            imageByte = DatatypeConverter.parseBase64Binary(picBase64);
                            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                            image = ImageIO.read(new ByteArrayInputStream(imageByte));
                            bis.close();
                            if(i == 0){
                                File outputfile = new File("f://rl//关注库图片//"+tMatchs.get(j).getDpsrId()+"_"+faceId+".jpg");
                                ImageIO.write(image, "jpg", outputfile);
                            }else{
                                File outputfile = new File("f://rl//非关注库图片//"+tMatchs.get(j).getDpsrId()+"_"+faceId+".jpg");
                                ImageIO.write(image, "jpg", outputfile);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        logger.info("关注库入库开始.......................");
                        if(i == 0){
                            // 关注库
                            //if(faceId.equals(tMatchs.get(j).getDpsrId())){
                            if(cardPhoto.equals(tMatchs.get(j).getCardphoto())){
                                test.query("insert into tb_test_detail(td_id,td_tb_id,td_isperson,td_similarity,td_cost,td_c_path,td_comp_path)" + "values('"+tMatch.getDpsrId()+"'," +
                                        ""+num1+","+1+",'"+similarity+"',"+0+",'"+tMatchs.get(j).getCardphoto()+"'," +"'"+tMatchs.get(j).getDpsrphotocut()+"')");
                                logger.info("关注库入库总数"+j+"个.......................");
                            }else{
                                test.query("insert into tb_test_detail(td_id,td_tb_id,td_isperson,td_similarity,td_cost,td_c_path,td_comp_path) values('"+tMatch.getDpsrId()+"',"+
                                        ""+num1+","+1+",'"+similarity+"',"+1+",'"+tMatchs.get(j).getCardphoto()+"',+'"+tMatchs.get(j).getDpsrphotocut()+"')");
                                logger.info("关注库入库总数"+j+"个......................");
                            }
                        }else if(i == 1){
                            // 非关注库
                            //if(faceId.equals(tMatchs.get(j).getDpsrId())){
                            if(cardPhoto.equals(tMatchs.get(j).getCardphoto())){
                                test.query("insert into tb_test_detail(td_id,td_tb_id,td_isperson,td_similarity,td_cost,td_c_path,td_comp_path)" + "values('"+tMatch.getDpsrId()+"'," +
                                        ""+num2+","+1+",'"+similarity+"',"+0+",'"+tMatchs.get(j).getCardphoto()+"'," +"'"+tMatchs.get(j).getDpsrphotocut()+"')");
                                logger.info("非关注库入库总数"+j+"个.......................");
                            }else{
                                test.query("insert into tb_test_detail(td_id,td_tb_id,td_isperson,td_similarity,td_cost,td_c_path,td_comp_path) values('"+tMatch.getDpsrId()+"',"+num2+","+1+",'"+similarity+"',"+1+",'"+tMatchs.get(j).getCardphoto()+"',+'"+tMatchs.get(j).getDpsrphotocut()+"')");
                                logger.info("非关注库入库总数"+j+"个.......................");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("PerformanceTestException:"+e.getMessage());
        }
    }
    public String calculationAccuracy(String num1, String num2) {
        String result = "";
        try {
            OracleJdbcUtils test = new OracleJdbcUtils();
            // 获取非关注库相似度
            logger.info("准确率计算开始.......................");
            List<TMatch> tMatchs = test.queryCount("select * from tb_test_detail where td_tb_id = "+num1+" and td_cost = '0'", true);
            int count = tMatchs.size();
            System.out.println("静态库查询，准确率为：" + count / 10.00 + "%");
            List<TMatch> tMatchs1 = test.querySimilarity("select * from (select td_similarity,rownum as rn from (" + "select td_similarity from tb_test_detail where 1=1 and td_tb_id = "+num2+" order by td_similarity desc" + ")) where rn=10", true);
            String normalSimalarity1 = tMatchs1.get(0).getSimilarity();
            List<TMatch> tMatchs11 = test.queryCount("select * from tb_test_detail where td_similarity>'" + normalSimalarity1 + "' and td_cost = '0' and td_tb_id = "+num1, true);
            int count1 = tMatchs11.size();
            System.out.println("误识率为1%时，准确率是：" + count1 / 10.00 + "%");
            List<TMatch> tMatchs2 = test.querySimilarity("select * from (select td_similarity,rownum as rn from (" + "select td_similarity from tb_test_detail where 1=1 and td_tb_id = "+num2+" order by td_similarity desc" + ")) where rn=50", true);
            String normalSimalarity2 = tMatchs2.get(0).getSimilarity();
            List<TMatch> tMatchs22 = test.queryCount("select * from tb_test_detail where td_similarity>'" + normalSimalarity2 + "' and td_cost = '0' and td_tb_id = "+num1, true);
            int count2 = tMatchs22.size();
            System.out.println("误识率为5%时，准确率是：" + count2 / 10.00 + "%");
            List<TMatch> tMatchs3 = test.querySimilarity("select * from (select td_similarity,rownum as rn from (" + "select td_similarity from tb_test_detail where 1=1 and td_tb_id = "+num2+" order by td_similarity desc" + ")) where rn=100", true);
            String normalSimalarity3 = tMatchs3.get(0).getSimilarity();
            List<TMatch> tMatchs33 = test.queryCount("select * from tb_test_detail where td_similarity>'" + normalSimalarity3 + "' and td_cost = '0' and td_tb_id = "+num1, true);
            int count3 = tMatchs33.size();
            System.out.println("静态库查询，准确率为"+"误识率为10%时，准确率是：" + count3 / 10.00 + "%");
            result = "误识率为1%时，准确率是：" + count1 / 10.00 + "%; " + "误识率为5%时，准确率是：" + count2 / 10.00 + "%; " + "误识率为10%时，准确率是：" + count3 / 10.00 + "%; ";
            //select * from tb_test_detail where td_tb_id like '2020032101'
            //select count(*) from tb_test_detail where td_tb_id = '20200321' and td_cost = '0'
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据地址获取base64
     * @param path
     */
    public static void download(String path){
        byte imgByte[] = com.xmcares.util.FileUtils.downLoadFcFile(path,
                "facePicCut");
        String picBase64 = Base64.encodeBase64String(imgByte);
        System.out.println(picBase64);
    }

    public static void main(String[] args) {
        download("/NI35/2018/07/09/ea0f1eeabe66642391fad8591866bb02e4c0b89c469a4ca108e82411c3b1a13bb913cc29f9348486dce71629624ea77a.jpg");
    }
}
