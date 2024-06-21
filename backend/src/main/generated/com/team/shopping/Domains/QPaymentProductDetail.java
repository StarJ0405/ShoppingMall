package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentProductDetail is a Querydsl query type for PaymentProductDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentProductDetail extends EntityPathBase<PaymentProductDetail> {

    private static final long serialVersionUID = 1411484891L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentProductDetail paymentProductDetail = new QPaymentProductDetail("paymentProductDetail");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> optionCount = createNumber("optionCount", Integer.class);

    public final StringPath optionListName = createString("optionListName");

    public final StringPath optionName = createString("optionName");

    public final NumberPath<Integer> optionPrice = createNumber("optionPrice", Integer.class);

    public final QPaymentProduct paymentProduct;

    public QPaymentProductDetail(String variable) {
        this(PaymentProductDetail.class, forVariable(variable), INITS);
    }

    public QPaymentProductDetail(Path<? extends PaymentProductDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentProductDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentProductDetail(PathMetadata metadata, PathInits inits) {
        this(PaymentProductDetail.class, metadata, inits);
    }

    public QPaymentProductDetail(Class<? extends PaymentProductDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.paymentProduct = inits.isInitialized("paymentProduct") ? new QPaymentProduct(forProperty("paymentProduct"), inits.get("paymentProduct")) : null;
    }

}

