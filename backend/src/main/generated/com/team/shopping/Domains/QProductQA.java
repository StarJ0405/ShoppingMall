package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductQA is a Querydsl query type for ProductQA
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductQA extends EntityPathBase<ProductQA> {

    private static final long serialVersionUID = -1521381698L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductQA productQA = new QProductQA("productQA");

    public final QSiteUser author;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final QProduct product;

    public final StringPath title = createString("title");

    public QProductQA(String variable) {
        this(ProductQA.class, forVariable(variable), INITS);
    }

    public QProductQA(Path<? extends ProductQA> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductQA(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductQA(PathMetadata metadata, PathInits inits) {
        this(ProductQA.class, metadata, inits);
    }

    public QProductQA(Class<? extends ProductQA> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new QSiteUser(forProperty("author"), inits.get("author")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

