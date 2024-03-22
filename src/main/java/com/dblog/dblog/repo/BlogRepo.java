package com.dblog.dblog.repo;

import com.dblog.dblog.model.Blog;
import com.dblog.dblog.model.dtos.BlogDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepo extends JpaRepository<Blog, Long>{

    @Query("SELECT DISTINCT b.categoria FROM Blog b")
    List<String> findDistinctCategoria();
    @Query("""
    SELECT new com.dblog.dblog.model.dtos.BlogDto(
        b.id, b.titulo, b.contenido, b.view, b.timeData, b.autorId, b.categoria, b.imagen, b.autor, b.autorImg) 
    FROM Blog b
""")
    List<BlogDto> findAllBlogDtos();
}
