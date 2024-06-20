package com.team.shopping.Repositories;

import com.team.shopping.Domains.FileSystem;
import com.team.shopping.Repositories.Custom.FileSystemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileSystemRepository extends JpaRepository<FileSystem, String>, FileSystemRepositoryCustom {
}
