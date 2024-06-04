package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBuyDetail is a Querydsl query type for BuyDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBuyDetail extends EntityPathBase<BuyDetail> {

    private static final long serialVersionUID = -1698677258L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBuyDetail buyDetail = new QBuyDetail("buyDetail");

    public final QBuyProduct buyProduct;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOption option;

    public QBuyDetail(String variable) {
        this(BuyDetail.class, forVariable(variable), INITS);
    }

    public QBuyDetail(Path<? extends BuyDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBuyDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBuyDetail(PathMetadata metadata, PathInits inits) {
        this(BuyDetail.class, metadata, inits);
    }

    public QBuyDetail(Class<? extends BuyDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyProduct = inits.isInitialized("buyProduct") ? new QBuyProduct(forProperty("buyProduct"), inits.get("buyProduct")) : null;
        this.option = inits.isInitialized("option") ? new QOption(forProperty("option"), inits.get("option")) : null;
    }

}

