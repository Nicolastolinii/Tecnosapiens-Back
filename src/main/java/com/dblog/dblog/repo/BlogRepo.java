package com.dblog.dblog.repo;

import com.dblog.dblog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepo extends JpaRepository<Blog, Long>{
}
