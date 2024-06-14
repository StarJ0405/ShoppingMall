package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileSystem is a Querydsl query type for FileSystem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileSystem extends EntityPathBase<FileSystem> {

    private static final long serialVersionUID = 135135020L;

    public static final QFileSystem fileSystem = new QFileSystem("fileSystem");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath k = createString("k");

    public final StringPath v = createString("v");

    public QFileSystem(String variable) {
        super(FileSystem.class, forVariable(variable));
    }

    public QFileSystem(Path<? extends FileSystem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileSystem(PathMetadata metadata) {
        super(FileSystem.class, metadata);
    }

}

