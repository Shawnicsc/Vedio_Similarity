package com.service;


import com.calculate.ImageDHash;
import com.calculate.KeyFrameExtractor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author shawni
 * @Description TODO
 * @Date 2023/4/17 14:38
 * @Version 1.0
 */
@WebServlet("/similarity")
@MultipartConfig
public class SimilarityServlet extends HttpServlet {
    //显式声明 版本号
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //读取文件上传
        Part oriVideo = req.getPart("ori_video");
        Part monVideo = req.getPart("mon_video");
        //System.out.println(oriVideo);
        //存储上传的文件，保存在地址
        File oriFile = new File(oriVideo.getSubmittedFileName());
        File monFile = new File(monVideo.getSubmittedFileName());
        oriVideo.write(oriFile.getAbsolutePath());
        monVideo.write(monFile.getAbsolutePath());

        //提取关键帧
        int ori = KeyFrameExtractor.video_process(oriFile, "D:/minIO/Files/images1/");
        int mon = KeyFrameExtractor.video_process(monFile, "D:/minIO/Files/images2/");
        int sum = 0;  //汉明距离总和
        Double average=0.0;  //均值
        for (int i = 0; i <ori ; i+=20) {
            //绝对路径取出图片文件
            File oriImage = new File("D:/minIO/Files/images1/frame_"+i+".jpg");
            //System.out.println(oriImage.getAbsolutePath());
            for(int j=0;j<mon;j+=20){
                File monImage = new File("D:/minIO/Files/images2/frame_"+j+".jpg");
                //System.out.println(monImage.getAbsolutePath());

                //获取两个图片文件的dhash值
                String oriHash = ImageDHash.getDHash(oriImage);
                String monHash = ImageDHash.getDHash(monImage);

                //计算二者的汉明距离
                long hammingDistance = ImageDHash.getHammingDistance(oriHash, monHash);
                sum+=hammingDistance;
            }
        }
        //System.out.println(sum);
        //取得总帧数
        int total_Frames = Math.max(ori, mon);
        //获取关键帧图片数量
        Double cnt = total_Frames/20*1.0;
//        System.out.println(ori);
//        System.out.println(mon);
//        System.out.println(cnt);
        //计算均值
        average=sum/cnt;
//        System.out.println(average);
        //比率
        Double rate;
        if(average<5) {
            //这里有一个bug，5 为阈值，<5 认定为同一张图片，这里的相似度计算还需要斟酌一下计算
             rate = 4.9/5*100;
        }
        else{
            rate = average%100;
        }
        //转发结果，调用界面
        req.setAttribute("message","两个视频相似度为"+rate+"%");
        req.getRequestDispatcher("/result.jsp").forward(req,resp);
    }
}
