package com.dog.fileupload.utils;

import static java.awt.color.ColorSpace.TYPE_XYZ;

import com.dog.fileupload.common.error.ErrorCode;
import com.dog.fileupload.common.exception.ApiException;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EncodeFile {

    public String encodeVideo(String inputFilePath) {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(inputFilePath + "_encoded.mp4");

        try {
            encoding(inputFile, outputFile);
        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
            e.printStackTrace();
            throw new ApiException(ErrorCode.SERVER_ERROR, e, "인코딩에 실패했습니다.");
        }
        return outputFile.getName();
    }

    public String encodeImage(String inputFilePath) throws IOException {
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        OpenCVFrameConverter.ToMat matConverter = new OpenCVFrameConverter.ToMat();

        BufferedImage originalImage = ImageIO.read(new File(inputFilePath));

        Mat src = matConverter.convert(java2DFrameConverter.convert(originalImage));

        // 설정

        BufferedImage convertedImage = java2DFrameConverter.getBufferedImage(matConverter.convert(src), 1.0);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(convertedImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        File outputFile = new File(inputFilePath + "_encoded.jpg");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(imageInByte);
        }
        return outputFile.getName();

    }

    private void encoding(File inputFile, File outputFile) throws FrameGrabber.Exception, FrameRecorder.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        grabber.start();

        FFmpegFrameRecorder recorder = initRecorder(grabber, outputFile);
        recorder.start();

        recordFrames(grabber, recorder);

        grabber.stop();
        recorder.stop();
    }

    private FFmpegFrameRecorder initRecorder(FFmpegFrameGrabber grabber, File outputFile) {
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile,
                grabber.getImageWidth(),
                grabber.getImageHeight(),
                grabber.getAudioChannels());

        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
        recorder.setVideoOption("crf", "28");
        recorder.setVideoBitrate(3000000);
        recorder.setFrameRate(30.0);
        recorder.setSampleRate(grabber.getSampleRate());

        recorder.setAudioMetadata(grabber.getAudioMetadata());
        recorder.setAudioQuality(0);
        recorder.setAudioBitrate(grabber.getAudioBitrate());
        recorder.setVideoMetadata(grabber.getVideoMetadata());
        recorder.setFormat("mp4");
        return recorder;
    }

    private void recordFrames(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder)
            throws FrameGrabber.Exception, FrameRecorder.Exception {
        Frame frame;
        while ((frame = grabber.grab()) != null) {
            recorder.record(frame);
        }
    }
}
