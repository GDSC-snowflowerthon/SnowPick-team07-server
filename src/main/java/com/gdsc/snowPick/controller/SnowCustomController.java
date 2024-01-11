package com.gdsc.snowPick.controller;

import com.gdsc.snowPick.dto.SnowDto;
import com.gdsc.snowPick.service.S3Service;
import com.gdsc.snowPick.service.SnowCustomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/custom")
@Tag(name="CUSTOM", description = "눈의 설정 정보 api")
public class SnowCustomController {
    private final SnowCustomService snowCustomService;
    private final S3Service s3Service;

    @PostMapping("/image")
    @Operation(summary="이미지 url받기")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<String> getImageUrl(@RequestPart MultipartFile image) throws IOException {
        String url=s3Service.saveFile("custom", image);
        return ResponseEntity.ok().body(url);
    }


    //눈 커스텀 정보 저장
    @PostMapping
    @Operation(summary="눈 정보 저장하기")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<SnowDto> createSnowCustom(@RequestBody SnowDto snowDto) {
//
        return ResponseEntity.ok().body(snowCustomService.createSnowCustom(snowDto));
    }

    // find all SnowCustom
    @GetMapping
    @Operation(summary="눈 정보 리스트 가져오기")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<List<SnowDto>> getAllSnowCustoms() {
        return ResponseEntity.ok().body(snowCustomService.getAllSnowCustoms());
    }
}
