package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.FileSystem;

import java.util.List;

public interface FileSystemRepositoryCustom {
    List<FileSystem> findByKeyList(String k);
}
