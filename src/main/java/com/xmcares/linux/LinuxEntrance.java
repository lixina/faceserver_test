package com.xmcares.linux;

import com.xmcares.entrance.FacePerformance;

import java.io.IOException;

public class LinuxEntrance {
    public static void main(String[] args) {
        String ip = "10.83.2.233";
        String port = "18000";
        String name = "sa";
        String pwd = "123456";
        String dir = "/home/gpu/dist_1.2/xmkytest/pic/";
        //String dir = "F:\\rl\\关注库图片";
        String DBId = "rwtest";
        String picCount = "1000";
        FacePerformance test = new FacePerformance();
        try {
            test.sendRecogSearch(ip,port,name,pwd,dir,DBId,picCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
