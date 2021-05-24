package com.whopuppy.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Transactional
@Component
public class S3Util {
    @Resource
    private AmazonS3 amazonS3;

    @Value("${s3.bucket}")
    private String bucket;

    @Value("${s3.custom-domain}")
    private String customDomain;

    public String uploadObject(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();

        int index = fileName.lastIndexOf(".");
        String fileExt = fileName.substring(index+1); // 확장자 추출
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(new Date()); // 디렉토리 이름을 정하기 위한 간단한 날짜

        UUID uid = UUID.randomUUID(); // 유니크한 uuid

        String savedName = uid.toString() + "-" + System.currentTimeMillis() + "." + fileExt; // uuid로는 불안해서 더 추가

        ObjectMetadata omd = new ObjectMetadata();// 메타데이터 객체
        omd.setContentType(multipartFile.getContentType());
        omd.setContentLength(multipartFile.getSize());

        amazonS3.putObject(new PutObjectRequest(bucket+ "/" + date,
                savedName, multipartFile.getInputStream(), omd) // uuid + currentTime = file name
                .withCannedAcl(CannedAccessControlList.PublicRead)); // 퍼블릭 권한으로 버킷 + 날짜 디렉토리

        //return amazonS3.getUrl(bucket+ "/" + date,savedName).toString();
        return "https://" + customDomain + "/" + date + "/" + savedName;
    }
    public void deleteObject(String path,String savedName,boolean isHard) throws AmazonServiceException {
        if(isHard){ // 하드
            amazonS3.deleteObject(new DeleteObjectRequest(bucket+ "/" + path,savedName)); // 단순 삭제
        }else{ // 소프트
            String key = path+"/"+savedName;
            AccessControlList acl = amazonS3.getObjectAcl(bucket,key); // 권한 리스트를 가져옴
            acl.getGrantsAsList().clear(); // 권한 초기화
            acl.grantPermission(new CanonicalGrantee(acl.getOwner().getId()), Permission.FullControl); // 권한추가, 주인만
            amazonS3.setObjectAcl(bucket, key, acl); // 권한 변경
        }
    }

    public void deleteHardObject(String url){
        //ex https://static.whopuppy.com/2021/05/11/663e1b93-8b08-4121-b3b0-b1c3393a2598-1620666835082.jpg
        url = url.substring(28);
        amazonS3.deleteObject(bucket,url);
    }
    public org.springframework.core.io.Resource getObject(String path, String savedName) throws IOException {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket+"/" + path,savedName));// 객체 get
        return new InputStreamResource(s3Object.getObjectContent()); // Spring.io.Resource 인터페이스의 구현체 중 InputStream을 받는 객체로 초기화 후 반환
    }
}
