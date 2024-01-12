package com.gdsc.snowPick.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> findImageUrls(String directory) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucket)
                .withPrefix(directory+"/");

        ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);

        List<String> imageUrls = new ArrayList<>();
        do {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String imageUrl = amazonS3.getUrl(bucket, objectSummary.getKey()).toString();
                imageUrls.add(imageUrl);
            }

            // S3 객체 목록 요청은 페이지네이션될 수 있으므로, 다음 페이지가 있으면 계속 가져옵니다.
            listObjectsRequest.setMarker(objectListing.getNextMarker());
            objectListing = amazonS3.listObjects(listObjectsRequest);
        } while (objectListing.isTruncated());

        return imageUrls;
    }

    public String saveFile(String directory, MultipartFile multipartFile) throws IOException {

        //타임 스탬프로 파일이 계속 덮혀 쓰여지는 것 방지
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String originalFilename = directory+"/"+timeStamp+"_"+multipartFile.getOriginalFilename();


        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public void deleteFrameImage(String url)  {

        String originalFilename = extractFilenameFromS3Url(url);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, originalFilename));
    }

    private static String extractFilenameFromS3Url(String s3Url) {
        try {
            URL url = new URL(s3Url);
            String path = url.getPath();
            String[] pathComponents = path.split("/");
            String filename = pathComponents[pathComponents.length - 1];
            // URL 디코딩 후 파일 이름 추출
            String decodedFilename = java.net.URLDecoder.decode(filename, StandardCharsets.UTF_8.name());
            return decodedFilename;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid S3 URL", e);
        }
    }

    /*
    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" +  originalFilename + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);

    }

     */
    public byte[] downloadFile(String bucketName, String image) {

        String filename = image.substring(image.lastIndexOf('/') + 1);
        System.out.println("filename = " + filename);
        S3Object s3Object = amazonS3.getObject(bucketName, filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //BASE64를 디코딩하여 S3에 저장
    public String saveBase64Image(String directory, String base64Image) {
        // 파일 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String fileName = directory + "/" + timeStamp + ".jpg";

        try {
            // Base64 인코딩된 문자열을 바이트 배열로 디코딩
            byte[] decodedBytes = Base64.decodeBase64(base64Image);

            // S3에 업로드
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(decodedBytes.length);
            metadata.setContentType("image/jpeg");

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (AmazonServiceException e) {
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }
    }
}
