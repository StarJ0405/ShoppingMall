package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartItemOptions is a Querydsl query type for CartItemOptions
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartItemOptions extends EntityPathBase<CartItemOptions> {

    private static final long serialVersionUID = 1267592458L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartItemOptions cartItemOptions = new QCartItemOptions("cartItemOptions");

    public final QCartItem cart;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOptions options;

    public QCartItemOptions(String variable) {
        this(CartItemOptions.class, forVariable(variable), INITS);
    }

    public QCartItemOptions(Path<? extends CartItemOptions> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartItemOptions(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartItemOptions(PathMetadata metadata, PathInits inits) {
        this(CartItemOptions.class, metadata, inits);
    }

    public QCartItemOptions(Class<? extends CartItemOptions> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new QCartItem(forProperty("cart"), inits.get("cart")) : null;
        this.options = inits.isInitialized("options") ? new QOptions(forProperty("options"), inits.get("options")) : null;
    }

}

