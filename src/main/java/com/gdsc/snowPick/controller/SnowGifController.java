package com.gdsc.snowPick.controller;

import com.gdsc.snowPick.service.S3Service;
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
@RequestMapping("/api/v1/gif")
@RequiredArgsConstructor
@Tag(name="GIF", description = "눈내리는 GIF 처리")
public class SnowGifController {

    private final S3Service s3Service;

    @PostMapping
    @Operation(summary="gif 저장하기")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<String> saveFrame(@RequestPart MultipartFile gif)
            throws IOException {
        String url=s3Service.saveFile("gif",gif);
        return ResponseEntity.ok().body(url);
    }

    @Operation(summary="gif 삭제하기")
    @ApiResponse(responseCode = "200", description = "성공")
    @DeleteMapping
    public ResponseEntity<?> deleteFrame(@RequestBody String url){
        s3Service.deleteFrameImage(url);
        return ResponseEntity.ok().build();
    }

    @Operation(summary="gif 리스트 뿌려주기")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping
    public ResponseEntity<List<String>> getGifList(){
        return ResponseEntity.ok().body(s3Service.findImageUrls("gif"));
    }
}
