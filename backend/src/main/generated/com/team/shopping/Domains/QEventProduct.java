package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventProduct is a Querydsl query type for EventProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventProduct extends EntityPathBase<EventProduct> {

    private static final long serialVersionUID = 906482966L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventProduct eventProduct = new QEventProduct("eventProduct");

    public final QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProduct product;

    public QEventProduct(String variable) {
        this(EventProduct.class, forVariable(variable), INITS);
    }

    public QEventProduct(Path<? extends EventProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventProduct(PathMetadata metadata, PathInits inits) {
        this(EventProduct.class, metadata, inits);
    }

    public QEventProduct(Class<? extends EventProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event"), inits.get("event")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

