package com.gp.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuDto {
    private static final long serialVersionUID = 1L;
    private Long parentId;
    private String label;
    private Long id;
    private List<MenuDto> children;


}
