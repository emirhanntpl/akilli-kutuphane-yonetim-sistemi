package com.library.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoReview {
    private Long id;
    private Integer rating;
    private String comment;
    private String username;
    private Date createTime;
}
