package com.xmcares.util;

import com.xmcares.entity.TMatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleJdbcUtils {
	//数据库连接对象
    private static Connection conn = null;
     
    private static String driver = "oracle.jdbc.driver.OracleDriver"; //驱动
     
    private static String url = "jdbc:oracle:thin:@//10.83.3.128:1521/orcl"; //连接字符串
     
    private static String username = "face_admin"; //用户名
     
    private static String password = "faceadmin"; //密码
     
     
    // 获得连接对象
    private static synchronized Connection getConn(){
        if(conn == null){
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
    // 新增 用于准确率计算数量 lix 20200130
    public List<TMatch> querySimilarity(String sql, boolean isSelect) throws SQLException {
        List<TMatch> tMatchs=new ArrayList<TMatch>();
        PreparedStatement pstmt;
        try {
            pstmt = getConn().prepareStatement(sql);
            //建立一个结果集，用来保存查询出来的结果
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TMatch tMatch = new TMatch();
                tMatch.setSimilarity(rs.getString("td_similarity"));
                tMatchs.add(tMatch);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tMatchs;
    }
    //执行查询语句
    public List<TMatch> query(String sql, boolean isSelect) throws SQLException {
    	List<TMatch> tMatchs=new ArrayList<TMatch>();
        PreparedStatement pstmt;
        try {
            pstmt = getConn().prepareStatement(sql);
            //建立一个结果集，用来保存查询出来的结果
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	TMatch tMatch = new TMatch();
    			tMatch.setDpsrId(rs.getString("pm_r_id"));//td_id
    			tMatch.setDpsrphotocut(rs.getString("pm_r_comp"));//td_isperson
    			tMatch.setCardphoto(rs.getString("pm_c_path"));// td_similarity td_cost
                //tMatch.setSimilarity(rs.getString("td_similarity"));
                tMatchs.add(tMatch);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tMatchs;
    }
    // 新增 用于准确率计算数量 lix 20200130
    public List<TMatch> queryCount(String sql, boolean isSelect) throws SQLException {
        List<TMatch> tMatchs=new ArrayList<TMatch>();
        PreparedStatement pstmt;
        try {
            pstmt = getConn().prepareStatement(sql);
            //建立一个结果集，用来保存查询出来的结果
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TMatch tMatch = new TMatch();
                tMatch.setDpsrId(rs.getString("td_isperson"));
                tMatchs.add(tMatch);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tMatchs;
    }

    public void query(String sql) throws SQLException {
        PreparedStatement pstmt;
        pstmt = getConn().prepareStatement(sql);
        pstmt.execute();
        pstmt.close();
    }
     
     
    //关闭连接
    public void close(){
        try {
            getConn().close();
            conn=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
