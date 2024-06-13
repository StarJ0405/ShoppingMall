package com.team.shopping.Repositories;

import com.team.shopping.Domains.FileSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileSystemRepository extends JpaRepository<FileSystem, Long> {
    Optional<FileSystem> findByK(String key);
}
