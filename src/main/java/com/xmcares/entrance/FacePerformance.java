package com.xmcares.entrance;


import com.alibaba.fastjson.JSONObject;
import com.xmcares.framework.serviceful.annotation.ServiceBean;
import com.xmcares.framework.serviceful.annotation.ServiceMethod;
import com.xmcares.entity.TMatch;
import com.xmcares.util.OracleJdbcUtils;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 人脸服务器性能测试类 lix
 */
@ServiceBean(path = "/test", title = "服务器性能测试")
public class FacePerformance {
    public static  String[] exts = {"jpg", "jpeg"};
    private static String searchUrl = "http://10.83.2.232:10010/xmcares_fp/recog/search";
    private static String addUrl = "http://10.83.2.232:10010/xmcares_fp/face/add";
    // windows图片路径
    public static String dir = "F:\\rl\\pic";
    // linux图片路径
    //public static String dir = "/home/test/";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ServiceMethod(path = "/addTest", title = "服务器add性能测试", opened = true)
    public void sendFaceAdd(String ip, String port, String name, String pwd, String DBId) throws InterruptedException, IOException {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(10);
        okHttpClient.dispatcher().setMaxRequests(64);
        // 判断人脸底库是否存在 如果存在就删除重新创建
        // 判断人脸底库是否存在 如果不存在就创建
        String createDbUrl = "http://"+ip+":"+port+"/xmcares_fp/db/create";
        String deleteDbUrl = "http://"+ip+":"+port+"/xmcares_fp/db/delete";
        JSONObject addParameters = new JSONObject();
        addParameters.put("DBId", DBId);
        addParameters.put("name", DBId);
        addParameters.put("desc", DBId);
        String namepwd = name+":"+pwd;
        Base64 base = new Base64();
        String base64Sign = base.encodeToString(namepwd.getBytes("UTF-8"));
        RequestBody addPostBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), addParameters.toJSONString());
        final Request addRequest = new Request.Builder().url(createDbUrl).addHeader("Authorization", "Basic "+base64Sign).post(addPostBody).build();
        Call addCall = okHttpClient.newCall(addRequest);
        Response addResponse = addCall.execute();
        // 其中Response对象就是服务器返回的数据，将数据转换成字符串
        String addResponseData = addResponse.body().string();
        if("0".equals(JSONObject.parseObject(addResponseData).getString("result").toString())){
            logger.info("创建底库成功！");
        }else{
            JSONObject delParameters = new JSONObject();
            delParameters.put("DBId", DBId);
            RequestBody delPostBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), delParameters.toJSONString());
            final Request delRequest = new Request.Builder().url(deleteDbUrl).addHeader("Authorization", "Basic "+base64Sign).post(delPostBody).build();
            Call delCall = okHttpClient.newCall(delRequest);
            Response delResponse = delCall.execute();
            // 其中Response对象就是服务器返回的数据，将数据转换成字符串
            String delResponseData = delResponse.body().string();
            if("0".equals(JSONObject.parseObject(delResponseData).getString("result").toString())){
                logger.info("底库删除成功！");
                final Request addRequest1 = new Request.Builder().url(createDbUrl).addHeader("Authorization", "Basic "+base64Sign).post(addPostBody).build();
                Call addCall1 = okHttpClient.newCall(addRequest1);
                Response addResponse1 = addCall1.execute();
                addResponseData = addResponse1.body().string();
                if("0".equals(JSONObject.parseObject(addResponseData).getString("result").toString())){
                    logger.info("创建底库成功！");
                }else{
                    logger.info("创建底库失败！");
                }
            }else{
                logger.info("底库删除失败！");
                logger.info("底库创建失败！");
            }
        }
        long start = System.currentTimeMillis();
        OracleJdbcUtils test = new OracleJdbcUtils();
        //List<TMatch> tMatchs=test.query("select tb_credentials.c_photo,t1.r_photo_cut,t1.r_id,t1.c_id from (select c_id,r_photo_cut,r_id from tb_record where tb_record.r_photo_cut is not null and TB_RECORD.R_PHOTO_CUT_COMP =1 and tb_record.r_flag = 1 and rownum<=10000)  t1 left join tb_credentials on tb_credentials.c_id = t1.c_id", true);
        List<TMatch> tMatchs = null;
        try {
            tMatchs = test.query("select pm_r_id,pm_r_comp,pm_c_path from tb_pic_model where pm_mm_id = '3' and rownum < 10001", true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final CountDownLatch countDownLatch = new CountDownLatch(tMatchs.size());
        addUrl = "http://"+ip+":"+port+"/xmcares_fp/face/add";
        for ( int j = 0; j < tMatchs.size(); j++) {
            final TMatch tMatch = tMatchs.get(j);

            byte imgByte[] = com.xmcares.util.FileUtils.downLoadFcFile(tMatch.getDpsrphotocut(), "facePicCut");
            String picBase64 = Base64.encodeBase64String(imgByte);
            // 保存所有底库图片 图片名称为主键
           /* BufferedImage image = null;
            byte[] imageByte = null;
            try {
                imageByte = DatatypeConverter.parseBase64Binary(picBase64);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                image = ImageIO.read(new ByteArrayInputStream(imageByte));
                bis.close();
                File outputfile = new File("f://rl//addPic//"+tMatch.getDpsrId()+".jpg");
                ImageIO.write(image, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            JSONObject parameters = new JSONObject();
            parameters.put("DBId", DBId);
            parameters.put("faceId", tMatch.getDpsrId());
            parameters.put("img", picBase64.replaceAll(" ", "+"));
            RequestBody postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters.toJSONString());
            final Request request = new Request.Builder().url(addUrl).addHeader("Authorization", "Basic "+base64Sign).post(postBody).build();
            Call call = okHttpClient.newCall(request);
            //Response response = call.execute();
            // 其中Response对象就是服务器返回的数据，将数据转换成字符串
            //String responseData = response.body().string();
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure: ");
                    // 保存插入人脸底库失败图片信息
                    /*BASE64Decoder decoder = new BASE64Decoder();
                    byte[] bytes1 = new byte[0];
                    try {
                        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
                        BufferedImage bi1 = ImageIO.read(bais);
                        File f1 = new File("d://WRONGPIC//"+tMatch.getDpsrId()+".jpg");
                        ImageIO.write(bi1, "jpg", f1);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
*/
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject o = JSONObject.parseObject(response.body().string());
                    System.out.println(o.toJSONString());
                    if(o.getInteger("result") != 0){
                        // 保存插入人脸底库失败图片信息
                    /*BASE64Decoder decoder = new BASE64Decoder();
                    byte[] bytes1 = new byte[0];
                    try {
                        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
                        BufferedImage bi1 = ImageIO.read(bais);
                        File f1 = new File("d://WRONGPIC//"+tMatch.getDpsrId()+".jpg");
                        ImageIO.write(bi1, "jpg", f1);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }*/
                    }
                    long cost = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
                    logger.info("cost............ " + cost);
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        long cost = System.currentTimeMillis() - start;
        double avg = (double) cost / (double) (10000);
        logger.info(10000 + "add , cost:" + cost + "ms, avg " + avg + "ms");
        //System.exit(0);
    }
    public void sendFaceAdd(String ip, String port, String DBId) throws InterruptedException, IOException {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(10);
        okHttpClient.dispatcher().setMaxRequests(64);
        // 判断人脸底库是否存在 如果存在就删除重新创建
        // 判断人脸底库是否存在 如果不存在就创建
        String createDbUrl = "http://"+ip+":"+port+"/xmcares_fp/db/create";
        String deleteDbUrl = "http://"+ip+":"+port+"/xmcares_fp/db/delete";
        JSONObject addParameters = new JSONObject();
        addParameters.put("DBId", DBId);
        addParameters.put("name", DBId);
        addParameters.put("desc", DBId);
        Base64 base = new Base64();
        RequestBody addPostBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), addParameters.toJSONString());
        final Request addRequest = new Request.Builder().url(createDbUrl).post(addPostBody).build();
        Call addCall = okHttpClient.newCall(addRequest);
        Response addResponse = addCall.execute();
        // 其中Response对象就是服务器返回的数据，将数据转换成字符串
        String addResponseData = addResponse.body().string();
        if("0".equals(JSONObject.parseObject(addResponseData).getString("result").toString())){
            logger.info("创建底库成功！");
        }else{
            JSONObject delParameters = new JSONObject();
            delParameters.put("DBId", DBId);
            RequestBody delPostBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), delParameters.toJSONString());
            final Request delRequest = new Request.Builder().url(deleteDbUrl).post(delPostBody).build();
            Call delCall = okHttpClient.newCall(delRequest);
            Response delResponse = delCall.execute();
            // 其中Response对象就是服务器返回的数据，将数据转换成字符串
            String delResponseData = delResponse.body().string();
            if("0".equals(JSONObject.parseObject(delResponseData).getString("result").toString())){
                logger.info("底库删除成功！");
                final Request addRequest1 = new Request.Builder().url(createDbUrl).post(addPostBody).build();
                Call addCall1 = okHttpClient.newCall(addRequest1);
                Response addResponse1 = addCall1.execute();
                addResponseData = addResponse1.body().string();
                if("0".equals(JSONObject.parseObject(addResponseData).getString("result").toString())){
                    logger.info("创建底库成功！");
                }else{
                    logger.info("创建底库失败！");
                }
            }else{
                logger.info("底库删除失败！");
                logger.info("底库创建失败！");
            }
        }
        long start = System.currentTimeMillis();
        OracleJdbcUtils test = new OracleJdbcUtils();
        //List<TMatch> tMatchs=test.query("select tb_credentials.c_photo,t1.r_photo_cut,t1.r_id,t1.c_id from (select c_id,r_photo_cut,r_id from tb_record where tb_record.r_photo_cut is not null and TB_RECORD.R_PHOTO_CUT_COMP =1 and tb_record.r_flag = 1 and rownum<=10000)  t1 left join tb_credentials on tb_credentials.c_id = t1.c_id", true);
        List<TMatch> tMatchs = null;
        try {
            tMatchs = test.query("select pm_r_id,pm_r_comp,pm_c_path from tb_pic_model where pm_mm_id = '3' and rownum < 10001", true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final CountDownLatch countDownLatch = new CountDownLatch(tMatchs.size());
        addUrl = "http://"+ip+":"+port+"/xmcares_fp/face/add";
        for ( int j = 0; j < tMatchs.size(); j++) {
            final TMatch tMatch = tMatchs.get(j);

            byte imgByte[] = com.xmcares.util.FileUtils.downLoadFcFile(tMatch.getDpsrphotocut(), "facePicCut");
            String picBase64 = Base64.encodeBase64String(imgByte);
            // 保存所有底库图片 图片名称为主键
           /* BufferedImage image = null;
            byte[] imageByte = null;
            try {
                imageByte = DatatypeConverter.parseBase64Binary(picBase64);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                image = ImageIO.read(new ByteArrayInputStream(imageByte));
                bis.close();
                File outputfile = new File("f://rl//addPic//"+tMatch.getDpsrId()+".jpg");
                ImageIO.write(image, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            JSONObject parameters = new JSONObject();
            parameters.put("DBId", DBId);
            parameters.put("faceId", tMatch.getDpsrId());
            parameters.put("img", picBase64.replaceAll(" ", "+"));
            RequestBody postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters.toJSONString());
            final Request request = new Request.Builder().url(addUrl).post(postBody).build();
            Call call = okHttpClient.newCall(request);
            //Response response = call.execute();
            // 其中Response对象就是服务器返回的数据，将数据转换成字符串
            //String responseData = response.body().string();
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure: ");
                    // 保存插入人脸底库失败图片信息
                    /*BASE64Decoder decoder = new BASE64Decoder();
                    byte[] bytes1 = new byte[0];
                    try {
                        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
                        BufferedImage bi1 = ImageIO.read(bais);
                        File f1 = new File("d://WRONGPIC//"+tMatch.getDpsrId()+".jpg");
                        ImageIO.write(bi1, "jpg", f1);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
*/
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject o = JSONObject.parseObject(response.body().string());
                    System.out.println(o.toJSONString());
                    if(o.getInteger("result") != 0){
                        // 保存插入人脸底库失败图片信息
                    /*BASE64Decoder decoder = new BASE64Decoder();
                    byte[] bytes1 = new byte[0];
                    try {
                        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
                        BufferedImage bi1 = ImageIO.read(bais);
                        File f1 = new File("d://WRONGPIC//"+tMatch.getDpsrId()+".jpg");
                        ImageIO.write(bi1, "jpg", f1);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }*/
                    }
                    long cost = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
                    logger.info("cost............ " + cost);
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        long cost = System.currentTimeMillis() - start;
        double avg = (double) cost / (double) (10000);
        logger.info(10000 + "add , cost:" + cost + "ms, avg " + avg + "ms");
        //System.exit(0);
    }
    public void sendRecogSearch(String ip, String port, String name, String pwd, String dirPath, String DBId, String picCount) throws InterruptedException, IOException {
        searchUrl = "http://"+ip+":"+port+"/xmcares_fp/recog/search";
        dir = dirPath;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(10);
        okHttpClient.dispatcher().setMaxRequests(64);
        //FileWriter fileWriter = new FileWriter(new File("f:/rl/" + UUID.randomUUID().toString() + ".csv"));
        int i = 0;
        // 图片转base64
        long start = System.currentTimeMillis();
        Collection<File> fileList = FileUtils.listFiles(new File(dir), exts, true);
        int count = Integer.parseInt(picCount);
        if(fileList.size() != count ){
            logger.info("配置图片数量与本地图片数量不等");
            return;
        }
        if(1000%count != 0){
            logger.info("不能整除。。。。有余数");
            return;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1000);
        long baseAll = 0;
        /*File filetxt = new File("/home/test/base64.txt");
        // 读取txt中的base64(linux上使用)
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filetxt))); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            logger.info("base64............." + base64);
            // 单张图片循环
            for (int j = 0; j < 1000; j++) {}*/
        int k = 1000/count;
        for (int j = 0; j < k; j++) {
           // 多张图片循环
           for (final File file : fileList) {
               System.out.println();
                i++;
                final String reqId = UUID.randomUUID().toString();
                JSONObject o = new JSONObject();
                // 计算转base64耗时
                long basestart = System.currentTimeMillis();
                BASE64Encoder encoder = new BASE64Encoder();
                String base64 = encoder.encode(FileUtils.readFileToByteArray(file));
                base64 = base64.replaceAll("\r\n","");
                // 20200805 在linux上测试瑞为服务器时出现问题存在\n的问题 lix
               base64 = base64.replaceAll("\n","");
                long baseend = System.currentTimeMillis();
                long result = baseend - basestart;
                baseAll+=result;
                o.put("topN", 1);
                o.put("DBId", DBId);
                o.put("reqId", reqId);
                o.put("type", 1);
                o.put("img", base64);
               Base64 base = new Base64();
               String namepwd = name+":"+pwd;
               String base64Sign = base.encodeToString(namepwd.getBytes("UTF-8"));
               logger.info(o.toJSONString());
                RequestBody postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), o.toJSONString());
                final Request request = new Request.Builder().url(searchUrl).addHeader("Authorization", "Basic "+base64Sign).post(postBody).build();
                Call call = okHttpClient.newCall(request);
                logger.info("发送结束" + System.currentTimeMillis());
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("onFailure: ");
                        logger.info("onFailure: ");
                        countDownLatch.countDown();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject o = JSONObject.parseObject(response.body().string());
                        logger.info(o.toJSONString());
                        long cost = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
                        System.out.println(cost);
                        logger.info(String.valueOf(cost));
                        String line =   "" + cost;
                        countDownLatch.countDown();
                    }
                });
            }
        }
        countDownLatch.await();
        long cost = System.currentTimeMillis() - start;
        double avg = (double) cost / (double) (fileList.size()*10);
        logger.info("总次数："+k*count + ", cost:" + cost +"ms,"+ "base64Cost" + baseAll+"ms, avg " + avg + "ms;");
        //fileWriter.flush();
        //fileWriter.close();
        //System.in.read();
        //System.exit(0);

    }
    public void sendRecogSearch(String ip, String port, String dirPath, String DBId, String picCount) throws InterruptedException, IOException {
        searchUrl = "http://"+ip+":"+port+"/xmcares_fp/recog/search";
        dir = dirPath;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(10);
        okHttpClient.dispatcher().setMaxRequests(64);
        //FileWriter fileWriter = new FileWriter(new File("f:/rl/" + UUID.randomUUID().toString() + ".csv"));
        int i = 0;
        // 图片转base64
        long start = System.currentTimeMillis();
        Collection<File> fileList = FileUtils.listFiles(new File(dir), exts, true);
        int count = Integer.parseInt(picCount);
        if(fileList.size() != count ){
            logger.info("配置图片数量与本地图片数量不等");
            return;
        }
        if(1000%count != 0){
            logger.info("不能整除。。。。有余数");
            return;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1000);
        long baseAll = 0;
        /*File filetxt = new File("/home/test/base64.txt");
        // 读取txt中的base64(linux上使用)
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filetxt))); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            logger.info("base64............." + base64);
            // 单张图片循环
            for (int j = 0; j < 1000; j++) {}*/
        int k = 1000/count;
        for (int j = 0; j < k; j++) {
            // 多张图片循环
            for (final File file : fileList) {
                System.out.println();
                i++;
                final String reqId = UUID.randomUUID().toString();
                JSONObject o = new JSONObject();
                // 计算转base64耗时
                long basestart = System.currentTimeMillis();
                BASE64Encoder encoder = new BASE64Encoder();
                String base64 = encoder.encode(FileUtils.readFileToByteArray(file));
                long baseend = System.currentTimeMillis();
                long result = baseend - basestart;
                baseAll+=result;
                o.put("topN", 1);
                o.put("DBId", DBId);
                o.put("reqId", reqId);
                o.put("type", 1);
                o.put("img", base64);
                Base64 base = new Base64();
                RequestBody postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), o.toJSONString());
                final Request request = new Request.Builder().url(searchUrl).post(postBody).build();
                Call call = okHttpClient.newCall(request);
                logger.info("发送结束" + System.currentTimeMillis());
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("onFailure: ");
                        logger.info("onFailure: ");
                        countDownLatch.countDown();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject o = JSONObject.parseObject(response.body().string());
                        logger.info(o.toJSONString());
                        long cost = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
                        System.out.println(cost);
                        logger.info(String.valueOf(cost));
                        String line =   "" + cost;
                        countDownLatch.countDown();
                    }
                });
            }
        }
        countDownLatch.await();
        long cost = System.currentTimeMillis() - start;
        double avg = (double) cost / (double) (fileList.size()*10);
        logger.info("总次数："+k*count + ", cost:" + cost +"ms,"+ "base64Cost" + baseAll+"ms, avg " + avg + "ms;");
        //fileWriter.flush();
        //fileWriter.close();
        //System.in.read();
        //System.exit(0);

    }
    public String getBase(){
        String encoding = "GBK";
        String lineTxt = null;
        File file = new File("F:\\rl\\lx_base64.txt");
        if (file.isFile() && file.exists()) { //判断文件是否存在
            InputStreamReader read = null;//考虑到编码格式
            try {
                read = new InputStreamReader(
                        new FileInputStream(file), encoding);

            BufferedReader bufferedReader = new BufferedReader(read);
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if(lineTxt != null){
                    return lineTxt;
                }
                System.out.println(lineTxt);
            }
            read.close();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("找不到指定的文件");
        }
        return lineTxt;
    }
}