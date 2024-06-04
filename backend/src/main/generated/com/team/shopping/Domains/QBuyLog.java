package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBuyLog is a Querydsl query type for BuyLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBuyLog extends EntityPathBase<BuyLog> {

    private static final long serialVersionUID = 149743551L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBuyLog buyLog = new QBuyLog("buyLog");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath info = createString("info");

    public final QSiteUser order;

    public QBuyLog(String variable) {
        this(BuyLog.class, forVariable(variable), INITS);
    }

    public QBuyLog(Path<? extends BuyLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBuyLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBuyLog(PathMetadata metadata, PathInits inits) {
        this(BuyLog.class, metadata, inits);
    }

    public QBuyLog(Class<? extends BuyLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QSiteUser(forProperty("order"), inits.get("order")) : null;
    }

}

