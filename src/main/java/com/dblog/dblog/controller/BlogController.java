package com.dblog.dblog.controller;

import com.dblog.dblog.model.Blog;
import com.dblog.dblog.service.PostService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
@MultipartConfig
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class BlogController {
    @Autowired
    private PostService postService;

    @PostMapping(value = "/img")
    public ResponseEntity<?> uploadimg(@RequestParam("file") MultipartFile file) throws Exception {
      String cualquiercosa =  postService.uploadImage(file);
      return ResponseEntity.ok("todo ok capo");
    };

    @PostMapping(value ="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBlog(@RequestParam("file") MultipartFile file,
                                        @RequestParam("titulo") String titulo,
                                        @RequestParam("contenido") String contenido,
                                        @RequestParam("autorId") Long autorId,
                                        @RequestParam("categoria") String categoria) {
        try {
            // Obtén la imagen directamente como bytes desde la solicitud
            String imageData = postService.uploadImage(file);

            //**************************************//
            // Obtener la zona horaria de Argentina
            ZoneId zonaHorariaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
            ZonedDateTime horaActualArgentina = ZonedDateTime.now(zonaHorariaArgentina);
            //*************************************//
            // Crear un nuevo objeto de Blog
            Blog blog = new Blog();
            blog.setTitulo(titulo);
            blog.setContenido(contenido);
            blog.setAutorId(autorId);
            blog.setCategoria(categoria);
            blog.setImagen(imageData);
            blog.setTimeData(LocalDateTime.from(horaActualArgentina));

            // Guardar el blog en la base de datos
            postService.createBlog(blog);

            // Crear un mapa para devolver la respuesta JSON
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Blog creado exitosamente.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el blog.");
        }
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long blogId, HttpServletRequest request) {
        Blog blog = postService.getBlogById(blogId);

        if (blog != null) {
            return ResponseEntity.ok(blog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/category")
    public ResponseEntity<List<String>> getAllCategory(){
        List<String> category = postService.getAllCategory();
        return ResponseEntity.ok(category);
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
