package com.dblog.dblog.repo;

import com.dblog.dblog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepo extends JpaRepository<Blog, Long>{

    @Query("SELECT DISTINCT b.categoria FROM Blog b")
    List<String> findDistinctCategoria();
}
