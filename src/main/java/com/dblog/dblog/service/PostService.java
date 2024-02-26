package com.dblog.dblog.service;

import com.dblog.dblog.model.Blog;

import java.util.List;

public interface PostService {
    Blog createBlog(Blog blog);

    List<String> getAllCategory();


    Blog getBlogById(Long blogId);

    List<Blog> getAllBlogs();

    Blog updateBlog(Long blogId, Blog blog);

    void deleteBlog(Long blogId);
}
