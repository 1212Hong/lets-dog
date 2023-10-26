package com.dog.fileupload.utils;

import com.dog.fileupload.common.error.ErrorCode;
import com.dog.fileupload.common.exception.ApiException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
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
            throw new ApiException(ErrorCode.SERVER_ERROR,e, "인코딩에 실패했습니다.");
        }
        return outputFile.getName();
    }
    
    public String encodeImage(String inputFilePath) {
        //TODO : 이미지 인코딩 필요
        File inputFile = new File(inputFilePath);
        return inputFile.getName();
    }

    private void encoding(File inputFile, File outputFile) throws FrameGrabber.Exception, FrameRecorder.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        grabber.start();

        FFmpegFrameRecorder recorder= initRecorder(grabber, outputFile);
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

        recorder.setVideoBitrate(2000000);
        recorder.setFrameRate(30);
        recorder.setAudioBitrate(192000);
        recorder.setSampleRate(44100);
        recorder.setFormat("mp4");
        return recorder;
    }

    private void recordFrames(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) throws FrameGrabber.Exception, FrameRecorder.Exception{
        Frame frame;
        while ((frame = grabber.grab()) != null) {
            recorder.record(frame);
        }
    }
}
