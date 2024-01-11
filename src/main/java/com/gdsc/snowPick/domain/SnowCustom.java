package com.gdsc.snowPick.domain;

import com.gdsc.snowPick.dto.SnowDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "snowCustom")
public class SnowCustom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 눈 자체의 커스텀 이름
    @Column
    private String name;

    @Column
    private String image;

    @Column
    private int size;

    @Column
    private String color;

    @Column
    private int density;

    public static SnowCustom from(SnowDto snowDto){
        return SnowCustom.builder()
                .name(snowDto.getName())
                .image(snowDto.getImage())
                .size(snowDto.getSize())
                .color(snowDto.getColor())
                .density(snowDto.getDensity())
                .build();
    }
}
