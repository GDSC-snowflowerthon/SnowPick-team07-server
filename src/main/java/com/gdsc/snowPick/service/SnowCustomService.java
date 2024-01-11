package com.gdsc.snowPick.service;

import com.gdsc.snowPick.domain.SnowCustom;
import com.gdsc.snowPick.dto.SnowDto;
import com.gdsc.snowPick.repository.SnowCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SnowCustomService {
    private final SnowCustomRepository snowInfoRepository;
    @Transactional
    public SnowDto createSnowCustom(SnowDto snowDto) {
        SnowCustom snowCustom = SnowCustom.from(snowDto);
        snowInfoRepository.save(snowCustom);
        return convertToDto(snowCustom);
    }

    private SnowDto convertToDto(SnowCustom snowCustom) {
        return SnowDto.builder()
                .image(snowCustom.getImage())
                .size(snowCustom.getSize())
                .color(snowCustom.getColor())
                .density(snowCustom.getDensity())
                .build();
    }

    public List<SnowDto> getAllSnowCustoms() {
        List<SnowCustom> snowCustoms = snowInfoRepository.findAll();
        return snowCustoms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
