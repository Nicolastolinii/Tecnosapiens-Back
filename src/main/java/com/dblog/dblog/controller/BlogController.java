package com.dblog.dblog.controller;

import com.dblog.dblog.model.Blog;
import com.dblog.dblog.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class BlogController {
    @Autowired
    private PostService postService;

    @PostMapping(value ="/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBlog(@RequestBody BlogRequest blogRequest) {
        try {
            // Obtener la cadena Base64 sin el prefijo
            String imageDataWithPrefix = blogRequest.getImageBase64();
            String imageDataWithoutPrefix = imageDataWithPrefix.substring(imageDataWithPrefix.indexOf(',') + 1);

            // Decodificar la imagen Base64
            byte[] imageData = Base64.getDecoder().decode(imageDataWithoutPrefix);
            // Obtener la zona horaria de Argentina
            ZoneId zonaHorariaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");

            // Obtener la fecha y hora actual en Argentina
            ZonedDateTime horaActualArgentina = ZonedDateTime.now(zonaHorariaArgentina);

            // Crear un nuevo objeto de Blog
            Blog blog = new Blog();
            blog.setTitulo(blogRequest.getTitulo());
            blog.setContenido(blogRequest.getContenido());
            blog.setAutorId(blogRequest.getAutorId());
            blog.setCategoria(blogRequest.getCategoria());
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
