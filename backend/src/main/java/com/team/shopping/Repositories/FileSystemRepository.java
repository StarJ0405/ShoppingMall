package com.team.shopping.Repositories;

import com.team.shopping.Domains.FileSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileSystemRepository extends JpaRepository<FileSystem, Long> {
    FileSystem findByK(String key);
}
