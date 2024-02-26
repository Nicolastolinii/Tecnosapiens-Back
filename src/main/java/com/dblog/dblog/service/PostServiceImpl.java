package com.dblog.dblog.service;

import com.dblog.dblog.model.Blog;
import com.dblog.dblog.repo.BlogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private BlogRepo blogRepo;


    @Override
    public Blog createBlog(Blog blog) {
        return blogRepo.save(blog);
    }

    @Override
    public List<String> getAllCategory() {
        // Obtener todos los blogs
        List<Blog> blogs = blogRepo.findAll();

        // Extraer las categorías únicas en un conjunto para evitar duplicados
        Set<String> uniqueCategories = new HashSet<>();
        for (Blog blog : blogs) {
            uniqueCategories.add(blog.getCategoria());  // Asume que la propiedad "categoria" contiene la categoría del blog
        }

        // Convertir el conjunto a una lista y devolverla
        return new ArrayList<>(uniqueCategories);
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return blogRepo.findById(blogId).orElse(null);
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepo.findAll();
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
