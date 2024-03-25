package com.dblog.dblog.model.dtos;

import com.dblog.dblog.model.Blog;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserDto {
    private Long id;
    private String user;
    private String correo;
    private List<Blog> blogs;



}
