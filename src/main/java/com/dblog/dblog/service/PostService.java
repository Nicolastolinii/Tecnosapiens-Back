package com.dblog.dblog.service;

import com.dblog.dblog.model.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Blog createBlog(Blog blog);

    String uploadImage(MultipartFile file) throws Exception;

    List<String> getAllCategory();


    Blog getBlogById(Long blogId);



    List<Blog> getAllBlogs();

    Blog updateView(Long blogId, Blog blog);

    Blog updateBlog(Long blogId, Blog blog);

    void deleteBlog(Long blogId);
}
