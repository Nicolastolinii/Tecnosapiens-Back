package com.dblog.dblog.controller;

import com.dblog.dblog.model.Blog;
import com.dblog.dblog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/blog")
@AllArgsConstructor
public class BlogController {
    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        blog.setTimeData(LocalDateTime.now());
        Blog createdBlog = postService.createBlog(blog);
        return ResponseEntity.ok(createdBlog);
    }
    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long blogId) {
        Blog blog = postService.getBlogById(blogId);
        if (blog != null) {
            return ResponseEntity.ok(blog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = postService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }
    @PutMapping("/update/{blogId}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long blogId, @RequestBody Blog blog) {
        Blog updatedBlog = postService.updateBlog(blogId, blog);
        if (updatedBlog != null) {
            return ResponseEntity.ok(updatedBlog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long blogId) {
        postService.deleteBlog(blogId);
        return ResponseEntity.ok().build();
    }

}
