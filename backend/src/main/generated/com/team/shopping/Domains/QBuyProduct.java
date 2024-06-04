package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBuyProduct is a Querydsl query type for BuyProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBuyProduct extends EntityPathBase<BuyProduct> {

    private static final long serialVersionUID = 1308384234L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBuyProduct buyProduct = new QBuyProduct("buyProduct");

    public final QBuyLog buyLog;

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Product, QProduct> productList = this.<Product, QProduct>createList("productList", Product.class, QProduct.class, PathInits.DIRECT2);

    public QBuyProduct(String variable) {
        this(BuyProduct.class, forVariable(variable), INITS);
    }

    public QBuyProduct(Path<? extends BuyProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBuyProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBuyProduct(PathMetadata metadata, PathInits inits) {
        this(BuyProduct.class, metadata, inits);
    }

    public QBuyProduct(Class<? extends BuyProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyLog = inits.isInitialized("buyLog") ? new QBuyLog(forProperty("buyLog"), inits.get("buyLog")) : null;
    }

}

