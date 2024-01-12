package com.gdsc.snowPick.controller;

import com.gdsc.snowPick.dto.SnowDto;
import com.gdsc.snowPick.service.S3Service;
import com.gdsc.snowPick.service.SnowCustomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/custom")
@Tag(name="CUSTOM", description = "눈의 설정 정보 api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SnowCustomController {
    private final SnowCustomService snowCustomService;
    private final S3Service s3Service;

    /*
    @PostMapping("/image")
    @Operation(summary="눈송이 이미지 저장")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<String> getImageUrl(@RequestPart MultipartFile image) throws IOException {
        String url=s3Service.saveFile("custom", image);
        return ResponseEntity.ok().body(url);
    }


     */
    @PostMapping("/image")
    @Operation(summary="눈송이 이미지 저장")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<String> getImageUrl(@RequestPart String base64Photo) throws IOException {
        String url=s3Service.saveBase64Image("custom", base64Photo);
        return ResponseEntity.ok().body(url);
    }



    //눈 커스텀 정보 저장
    @PostMapping
    @Operation(summary="!사용X! 눈 정보 저장하기")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<SnowDto> createSnowCustom(@RequestBody SnowDto snowDto) {
//
        return ResponseEntity.ok().body(snowCustomService.createSnowCustom(snowDto));
    }



    // find all SnowCustom
    @GetMapping
    @Operation(summary="!사용X! 눈 정보 리스트 가져오기")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<List<SnowDto>> getAllSnowCustoms() {
        return ResponseEntity.ok().body(snowCustomService.getAllSnowCustoms());
    }

    @GetMapping("/image")
    @Operation(summary="눈송이 이미지 리스트 가져오기")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<List<String>> getImageList(){
        return ResponseEntity.ok().body(s3Service.findImageUrls("custom"));
    }

    @Operation(summary="눈송이 다운받기")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadGif(@RequestParam(value = "image") String image){
        byte[] data = s3Service.downloadFile("image", image);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + image + "\"")
                .body(resource);
    }
}
