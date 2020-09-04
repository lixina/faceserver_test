/**
 * @(#)com.xmcares.bmsplus.util.FileUtils.java
 *
 * Copyright (c) 2014-2018 厦门民航凯亚有限公司
 *
 */
package com.xmcares.util;

import com.xmcares.util.CommonFileServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件处理类
 *
 * @author liaofh
 * @version 1.0  2016/6/22
 * @modified liaofh  2016/6/22  <创建>
 */
public class FileUtils {
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	/**
     * 文件名
     */
    private static final String FILE_NAME = "fileName";

    /**
     * 路径名
     */
    private static final String PATH_NAME = "pathName";
    
    /**
     * 将文件完整路径分离为目录路径、文件名
     * @param filePath 文件完整路径
     * @return FILE_NAME : 文件名
     *         PATH_NAME : 目录路径
     */
    public static Map<String, String> getFilePathAndFileName(String filePath) {
        Map<String, String> fileMap = new HashMap<>();
        if(null != filePath) {
            filePath = filePath.trim();
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
            String pathName = filePath.replace(fileName, "");
            fileMap.put(FILE_NAME, fileName);
            fileMap.put(PATH_NAME, pathName);
        }
        return fileMap;
    }
    
    /**
     * 人脸识别图片下载
     * @param filePath 图片路径
     * @param fileName 文件名
     * @return 图片字节数组
     */
    public static byte[] downLoadFcPic(String filePath) {
        try {
            String newPathName = null;
            String newFileName = null;
            if(null != filePath) {
                Map<String, String> fileMap = getFilePathAndFileName(filePath);
                newPathName = fileMap.get(PATH_NAME);
                newFileName = fileMap.get(FILE_NAME);
            }
            
            return FileUtils.fromXFUD("downLoadFcPic", newPathName, newFileName);
           // return fileService.downLoadFcPic(newPathName, newFileName);
        } catch (Exception e) {
        	logger.info("图片下载失败"+e.getMessage());
            return null;
        }
    }
    
    /**
     * 人脸识别特征码下载
     * @param filePath 图片路径
     * @param fileName 文件名
     * @return 图片字节数组
     */
    public static byte[] downLoadFcFile(String filePath) {
        try {
            String newPathName = null;
            String newFileName = null;
            if(null != filePath) {
                Map<String, String> fileMap = getFilePathAndFileName(filePath);
                newPathName = fileMap.get(PATH_NAME);
                newFileName = fileMap.get(FILE_NAME);
            }
            
            return FileUtils.fromXFUD("downLoadFcFile", newPathName, newFileName);
        } catch (Exception e) {
        	logger.info("特征码下载失败"+e.getMessage());
            return null;
        }
    }
    
    /**
     * 人脸识别特征码下载
     * @param filePath 图片路径
     * @param fileName 文件名
     * @return 图片字节数组
     */
    public static byte[] downLoadFcFile(String filePath, String picName) {
        try {
            String newPathName = null;
            String newFileName = null;
            if(null != filePath) {
                Map<String, String> fileMap = getFilePathAndFileName(filePath);
                newPathName = fileMap.get(PATH_NAME);
                newFileName = fileMap.get(FILE_NAME);
            }
            
            return FileUtils.fromXFUD(picName, newPathName, newFileName);
        } catch (Exception e) {
        	logger.info("特征码下载失败"+e.getMessage());
            return null;
        }
    }
    
    /**
	 * 字节写入本地文件
	 * @param bytes
	 * @param filePath
	 */
	public static void writeLocalFile(String bytes, String filePath){
		FileOutputStream fos = null;
        try {
        	File file = new File(filePath);
        	if(!file.exists()){
        		File pathDir = file.getParentFile();
                pathDir.mkdirs();
                file.createNewFile();
        	}
			fos = new FileOutputStream(file);
			fos.write(bytes.getBytes(), 0, bytes.length());
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{  
            try {
                fos.close();  
            } catch (Exception e) {
                e.printStackTrace();  
            }                 
        } 
	}
	
	/**
	 * 字节写入本地文件
	 * @param bytes
	 * @param filePath
	 */
	public static void writeLocalFile(byte[] bytes,String filePath){
		FileOutputStream fos = null;
        try {
        	File file = new File(filePath);
        	if(!file.exists()){
        		File pathDir = file.getParentFile();
                pathDir.mkdirs();
                file.createNewFile();
        	}else{
        		//存在删除原来的
        		file.delete();
        		file.createNewFile();
        	}
			fos = new FileOutputStream(file);
			fos.write(bytes, 0, bytes.length);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{  
            try {
                fos.close();  
            } catch (Exception e) {
                e.printStackTrace();  
            }                 
        } 
	}
    
    
    /**
     * 上传文件
     *
     * @param module    模块
     * @param filePath  路径
     * @param fileName  文件名称
     * @param fileBytes 文件字节数组
     */
    public static void uploadFile(String airport, String flieType, String filePath, String fileName, byte[] fileBytes) {
    	//上传到文件服务器
    	toXFUD("uploadLookPic", filePath, fileName, fileBytes);
    }


    /**
     * 获取文件流
     *
     * @param filePath 含文件名称
     * @param fileName
     * @return
     */
    public static byte[] getFileInputStream(String airport, String flieType, String filePath, String fileName) throws FileNotFoundException {

    	filePath = filePath.replaceFirst(fileName, "");
    	return fromXFUD(flieType,filePath, fileName);
    }


    /**
     * 根据字节生成文件
     *
     * @param fileName
     * @param filePath
     * @param fileBytes
     */
    public static void createFileFromByte(String fileName, String filePath, byte[] fileBytes) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(fileBytes);
        } catch (Exception e) {
            System.out.println("exception  ");

        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //获得指定文件的byte数组
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 上传文件服务器
     *
     * @author zhongwc
     * @param fileType  文件类型
     * @param filePath  路径
     * @param fileName  文件名称
     * @param fileBytes 文件字节数组
     */
    private static void toXFUD(String fileType, String filePath, String fileName, byte[] fileBytes){
    	CommonFileServiceProxy commonFileServiceProxy = new CommonFileServiceProxy();
		try {
			String resultString = commonFileServiceProxy.upload(fileType,
					filePath, fileName, fileBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * 获取文件服务器文件流
     *
     * @author zhongwc
     * @param fileType  文件类型
     * @param filePath  路径
     * @param fileName  文件名称
     * @return
     */
    public static byte[] fromXFUD(String fileType, String filePath, String fileName){
    	CommonFileServiceProxy commonFileServiceProxy = new CommonFileServiceProxy();
		try {
			byte[] b = commonFileServiceProxy.download(fileType,
					filePath, fileName);
			if(b == null){
				System.out.println("b is null");
			}
			return b;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("bb is null");
			return null;
		}
    }
    
    private static byte[] inPutStreamToByte(InputStream inputStream){
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	byte[] buffer = new byte[1024];
    	int n = 0;
    	try {
    		while (-1 != (n = inputStream.read(buffer))) {
    			output.write(buffer, 0, n);
    		}
    		return output.toByteArray();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}finally{
    			try {
    				if (inputStream != null) inputStream.close();
    				if (output != null) output.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    	}
    	return null;
    }
    
    /**
     * 通用的下载流
     *
     * @param filePath
     * @param response
     * @throws java.io.UnsupportedEncodingException
     */
	public static byte[] inPutStream(HttpServletRequest request) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int n = 0;
			while (-1 != (n = inputStream.read(buffer))) {
				output.write(buffer, 0, n);
			}
			return output.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

}
