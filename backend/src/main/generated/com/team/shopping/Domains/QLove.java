package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLove is a Querydsl query type for Love
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLove extends EntityPathBase<Love> {

    private static final long serialVersionUID = 1046256787L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLove love = new QLove("love");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Product, QProduct> targetList = this.<Product, QProduct>createList("targetList", Product.class, QProduct.class, PathInits.DIRECT2);

    public final EnumPath<com.team.shopping.Enums.Type> targetType = createEnum("targetType", com.team.shopping.Enums.Type.class);

    public final QSiteUser user;

    public QLove(String variable) {
        this(Love.class, forVariable(variable), INITS);
    }

    public QLove(Path<? extends Love> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLove(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLove(PathMetadata metadata, PathInits inits) {
        this(Love.class, metadata, inits);
    }

    public QLove(Class<? extends Love> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QSiteUser(forProperty("user"), inits.get("user")) : null;
    }

}

