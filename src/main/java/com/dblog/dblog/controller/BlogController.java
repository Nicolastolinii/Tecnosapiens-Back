package com.dblog.dblog.controller;

import com.dblog.dblog.model.Blog;
import com.dblog.dblog.model.IpView;
import com.dblog.dblog.service.CookieCleanService;
import com.dblog.dblog.service.IpViewService;
import com.dblog.dblog.service.PostService;
import com.dblog.dblog.utils.Logger;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/blog")
@MultipartConfig
@CrossOrigin(origins = {"https://www.tecnosapiens.blog", "https://tecnosapiens.blog"},allowCredentials = "true")
//@CrossOrigin(origins = "*")

@AllArgsConstructor
public class BlogController {
    @Autowired
    private PostService postService;
    @Autowired
    private IpViewService ipViewService;

    private static final Logger logger = new Logger();
    private static final String IMAGE_DIR = "/root/app/image/";


    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            // Construir la ruta del archivo
            Path imagePath = Paths.get(IMAGE_DIR + fileName);
            File file = imagePath.toFile();
            // Verificar si el archivo existe
            if (file.exists()) {
                // Cargar el recurso de la imagen
                Resource resource = new UrlResource(file.toURI());
                // Configurar el encabezado de la respuesta para que el navegador pueda mostrar la imagen correctamente
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                // Devolver la respuesta con el recurso de la imagen
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                // Si el archivo no existe, devolver un ResponseEntity not found
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            // Manejar cualquier excepción que pueda ocurrir al cargar la imagen
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping(value ="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBlog(@RequestParam("file") MultipartFile file,
                                        @RequestParam("titulo") String titulo,
                                        @RequestParam("contenido") String contenido,
                                        @RequestParam("autorId") Long autorId,
                                        @RequestParam("categoria") String categoria,
                                        HttpServletRequest request) {
        try {
            String imageData = postService.uploadImage(file);
            String imageUrl = "https://api.tecnosapiens.blog" + "/blog/image/" + FilenameUtils.getName(imageData);
            //**************************************//
            ZoneId zonaHorariaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
            ZonedDateTime horaActualArgentina = ZonedDateTime.now(zonaHorariaArgentina);
            //*************************************//
            Blog blog = new Blog();
            blog.setTitulo(titulo);
            blog.setContenido(contenido);
            blog.setAutorId(autorId);
            blog.setCategoria(categoria);
            blog.setImagen(imageUrl);
            blog.setTimeData(LocalDateTime.from(horaActualArgentina));
            postService.createBlog(blog);
            //*************************************//
            String ipAddress = request.getRemoteAddr();
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            logger.log(Logger.LogLevel.INFO,("Post creado desde la IP : " + ipAddress));
            logger.log(Logger.LogLevel.INFO,("X-Forwarded-For: " + xForwardedFor));
            //*************************************//
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Post creado exitosamente.");
            response.put("imageUrl", imageUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.log(Logger.LogLevel.ERROR,e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Post.");
        }
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long blogId, HttpServletRequest request) {
        Blog blog = postService.getBlogById(blogId);
        if (blog != null) {
            boolean userIdCookieExists = false;
            String ipAddress = request.getRemoteAddr();
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("userId".equals(cookie.getName())) {
                        logger.log(Logger.LogLevel.INFO,("name: "+cookie.getName() +" valor: "+ cookie.getValue()));
                        userIdCookieExists = true;
                        String userId = cookie.getValue();
                        boolean ipView = ipViewService.findByIpAddressAndPostId(userId,blogId);

                        if (!ipView) {
                            postService.updateView(blogId, blog);
                            IpView ip = new IpView();
                            ip.setIpaddress(userId);
                            ip.setPostid(blogId);
                            ipViewService.saveip(ip);
                            logger.log(Logger.LogLevel.INFO, "Vista registrada para el blog: " + blogId + " para el usuario: " + userId + "IP: " + xForwardedFor);
                        }
                        break;
                    }
                }
            }
            if (!userIdCookieExists) {
                logger.log(Logger.LogLevel.INFO,("userid existe ?: "+userIdCookieExists));
                String userCookie = UUID.randomUUID().toString();
                IpView ip = new IpView();
                ip.setIpaddress(userCookie);
                ip.setPostid(blogId);
                ipViewService.saveip(ip);
                postService.updateView(blogId, blog);
                ResponseCookie springCookie = ResponseCookie.from("userId", userCookie)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(24 * 60 * 60)
                        .domain("tecnosapiens.blog")
                        .build();
                logger.log(Logger.LogLevel.INFO, "Nueva cookie 'userId' creada para el blog: " + blogId + " de la IP: " + xForwardedFor );
                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                        .body(blog);

            }
            logger.log(Logger.LogLevel.INFO,("Solicitud para ver el Blog ID " + blogId + ": - " + ipAddress));
            logger.log(Logger.LogLevel.INFO,("X-Forwarded-For: " + xForwardedFor));
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
