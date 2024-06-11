package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentProduct is a Querydsl query type for PaymentProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentProduct extends EntityPathBase<PaymentProduct> {

    private static final long serialVersionUID = -1957453846L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentProduct paymentProduct = new QPaymentProduct("paymentProduct");

    public final StringPath brand = createString("brand");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPaymentLog paymentLog;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final QSiteUser seller;

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public QPaymentProduct(String variable) {
        this(PaymentProduct.class, forVariable(variable), INITS);
    }

    public QPaymentProduct(Path<? extends PaymentProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentProduct(PathMetadata metadata, PathInits inits) {
        this(PaymentProduct.class, metadata, inits);
    }

    public QPaymentProduct(Class<? extends PaymentProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.paymentLog = inits.isInitialized("paymentLog") ? new QPaymentLog(forProperty("paymentLog"), inits.get("paymentLog")) : null;
        this.seller = inits.isInitialized("seller") ? new QSiteUser(forProperty("seller"), inits.get("seller")) : null;
    }

}

