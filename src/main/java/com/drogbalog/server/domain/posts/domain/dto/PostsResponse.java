package com.drogbalog.server.domain.posts.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "게시글 데이터 모델")
public class PostsResponse {

    @ApiModelProperty(value = "id")
    private long id;

    @ApiModelProperty(value = "email")
    private String email;

    @ApiModelProperty(value = "subject")
    private String subject;

    @ApiModelProperty(value = "contents")
    private String contents;

    @ApiModelProperty(value = "views")
    private BigInteger views;

    @ApiModelProperty(value = "createdDate")
    private LocalDateTime createdDate;

    @ApiModelProperty(value = "modifiedDate")
    private LocalDateTime modifiedDate;
}