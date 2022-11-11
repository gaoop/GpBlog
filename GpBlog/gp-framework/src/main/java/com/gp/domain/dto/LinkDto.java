package com.gp.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {
    private Long id;

    private String name;

    private String logo;

    private String status;

    private String description;
    //网站地址
    private String address;

}