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
    private static final long serialVersionUID = 1L;
    private InputStream oriContent;
    private  InputStream monContent;
    private static final String uploadPath = "D:/Javalearning/video_similarity/static/videos/";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //读取文件上传
        Part oriVideo = req.getPart("ori_video");
        Part monVideo = req.getPart("mon_video");
        System.out.println(oriVideo);
        File oriFile = new File(oriVideo.getSubmittedFileName());
        File monFile = new File(monVideo.getSubmittedFileName());
        oriVideo.write(oriFile.getAbsolutePath());
        monVideo.write(monFile.getAbsolutePath());

        //提取关键帧
        int ori = KeyFrameExtractor.video_process(oriFile, "D:/minIO/Files/images1/");
        int mon = KeyFrameExtractor.video_process(monFile, "D:/minIO/Files/images2/");
        int sum = 0;
        Double average=0.0;
        for (int i = 0; i <ori ; i+=20) {
            String oriName = "D:/minIO/Files/images1/"+"frame_" + i + ".jpg";
            File oriImage = new File("D:/minIO/Files/images1/frame_"+i+".jpg");
            System.out.println(oriImage.getAbsolutePath());
            for(int j=0;j<mon;j+=20){
                String monName = "D:/minIO/Files/images2/"+"frame_"+j+".jpg";
                File monImage = new File("D:/minIO/Files/images2/frame_"+j+".jpg");
                System.out.println(monImage.getAbsolutePath());
                String oriHash = ImageDHash.getDHash(oriImage);
                String monHash = ImageDHash.getDHash(monImage);

                long hammingDistance = ImageDHash.getHammingDistance(oriHash, monHash);
                sum+=hammingDistance;
            }
        }
        System.out.println(sum);
        int total_Frames = Math.max(ori, mon);
        Double cnt = total_Frames/20*1.0;
        System.out.println(ori);
        System.out.println(mon);
        System.out.println(cnt);
        average=sum/cnt;
        System.out.println(average);
        Double rate;
        if(average<5) {
             rate = 4.9/5*100;
        }
        else{
            rate = average%100;
        }
        req.setAttribute("message","两个视频相似度为"+rate+"%");
        req.getRequestDispatcher("/result.jsp").forward(req,resp);
    }
}
