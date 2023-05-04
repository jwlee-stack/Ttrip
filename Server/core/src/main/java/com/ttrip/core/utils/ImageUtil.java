package com.ttrip.core.utils;

import com.ttrip.core.entity.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Component
public class ImageUtil {
    private final String parentPath;

    public ImageUtil(@Value("${custom.path.upload-images}") String parentPath) {
        this.parentPath = parentPath;
    }



    //기존 사진 삭제//
    public String removeImg(File rmImg) {
        if (rmImg.exists()) {
            if (!rmImg.delete())
                throw new RuntimeException("파일 삭제 실패");
        }
        return null;
    }

    //이미지 파일인지 확인//
    public boolean checkImageType(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            return contentType.startsWith("image");
        } catch (Exception e) { //이미지 파일이 아님
            throw new IllegalArgumentException("파일의 타입이 올바르지 않습니다.");
        }
    }

    //이미지 저장//
    public String saveImg(Member member, MultipartFile img, String folder) throws IOException {
        //경로 설정//
        log.info("uploadImagePath :" + parentPath);
        File dir = new File(parentPath + File.separator + folder); //저장할 폴더
        String originalFilename = img.getOriginalFilename(); //넘어온 파일의 이름
        String fileName = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);

        String uploadFileName = member.getMemberUuid().toString() + "_" + fileName;
        String childPath = File.separator + folder + File.separator + uploadFileName;

        try {
            if (!dir.exists()) {
                dir.mkdirs(); //폴더가 없으면 생성
            }
        } catch (SecurityException e) {
            throw new SecurityException("이미지 업로드 폴더 생성 오류");
        }

        //파일 객체 생성//
        File saveFile;
        try {
            saveFile = new File(parentPath, childPath);
        } catch (NullPointerException e) {
            throw new NullPointerException("child 파일 생성 불가");
        }

        //이미지 저장//
        log.info("originalFileName : " + img.getOriginalFilename());
        log.info("saveFile : " + saveFile);
        System.out.println("here it is : " + Paths.get(parentPath + childPath).toAbsolutePath().toString());
        try {
            img.transferTo(saveFile);
        } catch (IOException e) {
            log.info("IOException : 이미지 저장 과정에서 에러가 발생했습니다.");
            throw new IOException(e);
        } catch (IllegalStateException e) {
            log.info("IllegalStateException" + e.getMessage());
            throw new IllegalStateException("the file has already been moved in the filesystem and is not available anymore for another transfer");
        }
        return childPath;
    }
}
