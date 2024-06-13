package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddress extends EntityPathBase<Address> {

    private static final long serialVersionUID = -1045387469L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAddress address = new QAddress("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final StringPath deliveryMessage = createString("deliveryMessage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mainAddress = createString("mainAddress");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Integer> postNumber = createNumber("postNumber", Integer.class);

    public final StringPath recipient = createString("recipient");

    public final StringPath title = createString("title");

    public final QSiteUser user;

    public QAddress(String variable) {
        this(Address.class, forVariable(variable), INITS);
    }

    public QAddress(Path<? extends Address> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAddress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAddress(PathMetadata metadata, PathInits inits) {
        this(Address.class, metadata, inits);
    }

    public QAddress(Class<? extends Address> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QSiteUser(forProperty("user"), inits.get("user")) : null;
    }

}

