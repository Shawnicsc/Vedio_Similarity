package com.calculate;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class KeyFrameExtractor {

    /**
     *
     * @author shawni
     * @date 2023/4/17 13:05
     * 提取视频关键帧
     * 以20帧为间隔
     * @return 传入文件的总帧数
     */
    @Test
    public static int video_process(File file,String outputPath) {

        int frameInterval = 20; // 每隔20帧提取一个关键帧

        try {
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
            grabber.start();
            //视频总帧数
            int frameCount = grabber.getLengthInFrames();
            //System.out.println(frameCount);
            int frameRate = (int) grabber.getFrameRate();
            //提取图片
            for (int i = 0; i < frameCount; i += frameInterval) {
                Frame frame = grabber.grabImage();
                BufferedImage image = Java2DFrameUtils.toBufferedImage(frame);

                String outputFilePath = outputPath + "frame"+"_" + i + ".jpg";
                ImageIO.write(image, "jpg", new File(outputFilePath));
            }

            grabber.stop();
            return frameCount;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }


}