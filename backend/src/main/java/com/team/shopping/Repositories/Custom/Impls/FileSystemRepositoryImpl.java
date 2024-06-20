package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.FileSystem;
import com.team.shopping.Domains.QFileSystem;
import com.team.shopping.Repositories.Custom.FileSystemRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FileSystemRepositoryImpl implements FileSystemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QFileSystem qFileSystem = QFileSystem.fileSystem;


    @Override
    public List<FileSystem> findByKeyList(String k) {
        return jpaQueryFactory.selectFrom(qFileSystem).where(qFileSystem.k.eq(k)).fetch();
    }
}
