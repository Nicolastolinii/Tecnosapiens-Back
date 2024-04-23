package com.dblog.dblog.service;

import com.dblog.dblog.model.Blog;
import com.dblog.dblog.model.dtos.BlogDto;
import com.dblog.dblog.repo.BlogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private BlogRepo blogRepo;


    @Override
    public Blog createBlog(Blog blog) {
        return blogRepo.save(blog);
    }


    @Override
    public String uploadImage(MultipartFile file) throws Exception {
      try {
          if (file.isEmpty()) {
              throw new Exception("File is empty");
          }
          String fileName = UUID.randomUUID().toString();
          byte[] bytes = file.getBytes();
          String fileOriginalName = file.getOriginalFilename();
          if (fileOriginalName == null || !fileOriginalName.contains(".")) {
              throw new Exception("Invalid file name or extension.");
          }
          String fileExt = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
          String newFileName = fileName + fileExt;
          File folder = new File("/root/app/image");
          if (!folder.exists()){
              folder.mkdir();
          }
          Path path = Paths.get("/root/app/image/" + newFileName);
          Files.write(path,bytes);
          return path.toString();
      }catch (Exception e){
          throw new Exception(e.getMessage());
      }
    };


    @Override
    public List<String> getAllCategory() {
        return new ArrayList<>(blogRepo.findDistinctCategoria());
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return blogRepo.findById(blogId).orElse(null);
    }

    @Override
    public List<BlogDto> getAllBlogs() {
        return blogRepo.findAllBlogDtos();
    }

    @Override
    public  Blog updateView(Long blogId, Blog blog) {
        Blog existingBlog = blogRepo.findById(blogId).orElse(null);
        if (existingBlog != null){
            if (existingBlog.getView() != null){
                existingBlog.setView(existingBlog.getView() + 1);
            }else {
                existingBlog.setView(+1);
            }
            return blogRepo.save(existingBlog);
        }else {
            return null;
        }
    }
    @Override
    public Blog updateBlog(Long blogId, Blog blog) {
        Blog existingBlog = blogRepo.findById(blogId).orElse(null);
        if (existingBlog != null) {
            existingBlog.setTitulo(blog.getTitulo());
            existingBlog.setContenido(blog.getContenido());
            existingBlog.setImagen(blog.getImagen());
            existingBlog.setCategoria(blog.getCategoria());
            return blogRepo.save(existingBlog);
        } else {
            return null; // Blog no encontrado
        }
    }

    @Override
    public void deleteBlog(Long blogId) {
        blogRepo.deleteById(blogId);
    }
}
