package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartItemDetail is a Querydsl query type for CartItemDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartItemDetail extends EntityPathBase<CartItemDetail> {

    private static final long serialVersionUID = 1239823461L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartItemDetail cartItemDetail = new QCartItemDetail("cartItemDetail");

    public final QCartItem cartItem;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Options, QOptions> optionList = this.<Options, QOptions>createList("optionList", Options.class, QOptions.class, PathInits.DIRECT2);

    public QCartItemDetail(String variable) {
        this(CartItemDetail.class, forVariable(variable), INITS);
    }

    public QCartItemDetail(Path<? extends CartItemDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartItemDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartItemDetail(PathMetadata metadata, PathInits inits) {
        this(CartItemDetail.class, metadata, inits);
    }

    public QCartItemDetail(Class<? extends CartItemDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cartItem = inits.isInitialized("cartItem") ? new QCartItem(forProperty("cartItem"), inits.get("cartItem")) : null;
    }

}

