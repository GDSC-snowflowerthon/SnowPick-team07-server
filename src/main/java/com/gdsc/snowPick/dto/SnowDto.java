package com.gdsc.snowPick.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SnowDto {
    private String name;
    private int size;
    private String color;
    private int density;
    private String image;
}
