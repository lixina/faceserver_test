import com.xmcares.entrance.FaceAccuracyRate;
import com.xmcares.entrance.FacePerformance;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainTest {
    static org.slf4j.Logger logger = LoggerFactory.getLogger(MainTest.class);
    public static void main(String[] args) throws IOException, InterruptedException {
        // 性能测试
        FacePerformance test = new FacePerformance();
        // 速度
        //test.sendRecogSearch("10.83.2.232", "10010", "megvii", "megvii", "F:\\rl\\pic","kstest","100");
        // 添加图片
        test.sendFaceAdd( "10.83.2.232", "10010", "megvii", "megvii", "test");
        //test.sendFaceAdd( "10.83.2.233", "1618", "admin", "12345", "jyt04");

        // 准确率测试
        FaceAccuracyRate accuracyRateTest = new FaceAccuracyRate();
        // 插入数据
        //accuracyRateTest.searchPicFace("10.83.2.232","10010","test","megvii", "megvii","20200425","2020042501");
        //accuracyRateTest.searchPicFace("10.83.2.233","1618","jyt03","admin", "12345","20200418","2020041801");
        // 计算
        //accuracyRateTest.calculationAccuracy("2020042002","2020042003");

        //并发测试
        //CountDownLatch 为唯一的、共享的资源
/*        final CountDownLatch latch = new CountDownLatch(10000);

        PerformanceDemo1 performanceDemo = new PerformanceDemo1(latch);

        long begin = System.currentTimeMillis();

        for (int i = 0; i <10 ; i++) {
            new Thread(performanceDemo).start();
        }
        try {
            //多线程运行结束前一直等待
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("总耗费时间："+(end-begin)+"ms");
        logger.info("总耗费时间："+(end-begin)+"ms");
    }*/
    }
}
