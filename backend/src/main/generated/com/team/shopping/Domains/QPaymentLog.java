package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentLog is a Querydsl query type for PaymentLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentLog extends EntityPathBase<PaymentLog> {

    private static final long serialVersionUID = -58315841L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentLog paymentLog = new QPaymentLog("paymentLog");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath info = createString("info");

    public final QSiteUser order;

    public final EnumPath<com.team.shopping.Enums.PaymentStatus> paymentStatus = createEnum("paymentStatus", com.team.shopping.Enums.PaymentStatus.class);

    public QPaymentLog(String variable) {
        this(PaymentLog.class, forVariable(variable), INITS);
    }

    public QPaymentLog(Path<? extends PaymentLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentLog(PathMetadata metadata, PathInits inits) {
        this(PaymentLog.class, metadata, inits);
    }

    public QPaymentLog(Class<? extends PaymentLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QSiteUser(forProperty("order"), inits.get("order")) : null;
    }

}

