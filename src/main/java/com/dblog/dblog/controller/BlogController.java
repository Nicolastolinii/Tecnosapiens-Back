package com.dblog.dblog.controller;

import com.dblog.dblog.model.Blog;
import com.dblog.dblog.model.IpView;

import com.dblog.dblog.model.dtos.BlogDto;
import com.dblog.dblog.model.dtos.UserDto;

import com.dblog.dblog.service.IpViewService;
import com.dblog.dblog.service.PostService;
import com.dblog.dblog.service.UserService;
import com.dblog.dblog.utils.LogDuration;
import com.dblog.dblog.utils.Logger;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("/blog")
@MultipartConfig
//@CrossOrigin(origins = {"https://www.tecnosapiens.blog", "https://tecnosapiens.blog"}, allowCredentials = "true")


@AllArgsConstructor
public class BlogController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private IpViewService ipViewService;

    private static final Logger logger = new Logger();
    private static final String IMAGE_DIR = "/root/app/image/";


    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path imagePath = Paths.get(IMAGE_DIR + fileName);
            File file = imagePath.toFile();
            if (file.exists()) {
                Resource resource = new UrlResource(file.toURI());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
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
            //String imageUrl ="imagen";
            //**************************************//
            ZoneId zonaHorariaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
            ZonedDateTime horaActualArgentina = ZonedDateTime.now(zonaHorariaArgentina);
            //*************************************//
            UserDto user = userService.getUserByIdPost(autorId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
            Blog blog = new Blog();
            blog.setTitulo(titulo);
            blog.setContenido(contenido);
            blog.setAutorId(autorId);
            blog.setCategoria(categoria);
            blog.setImagen(imageUrl);
            blog.setAutor(user.getUser());
            blog.setAutorImg(user.getImage());
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
            logger.log(Logger.LogLevel.INFO,("BLOG: " + blog));

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
    public ResponseEntity<List<BlogDto>> getAllBlogs() {
        long start = System.currentTimeMillis();
        List<BlogDto> blogs = postService.getAllBlogs();
        LogDuration.logDuration("getAllBlogs()", Duration.ofMillis(System.currentTimeMillis()-start), blogs.size());
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
