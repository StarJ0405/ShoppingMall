package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSiteUser is a Querydsl query type for SiteUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSiteUser extends EntityPathBase<SiteUser> {

    private static final long serialVersionUID = -448290733L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSiteUser siteUser = new QSiteUser("siteUser");

    public final QAuth auth;

    public final DatePath<java.time.LocalDate> birthday = createDate("birthday", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> createDate = createDate("createDate", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final EnumPath<com.team.shopping.Enums.Gender> gender = createEnum("gender", com.team.shopping.Enums.Gender.class);

    public final DatePath<java.time.LocalDate> modifyDate = createDate("modifyDate", java.time.LocalDate.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final EnumPath<com.team.shopping.Enums.UserRole> role = createEnum("role", com.team.shopping.Enums.UserRole.class);

    public final StringPath username = createString("username");

    public QSiteUser(String variable) {
        this(SiteUser.class, forVariable(variable), INITS);
    }

    public QSiteUser(Path<? extends SiteUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSiteUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSiteUser(PathMetadata metadata, PathInits inits) {
        this(SiteUser.class, metadata, inits);
    }

    public QSiteUser(Class<? extends SiteUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.auth = inits.isInitialized("auth") ? new QAuth(forProperty("auth"), inits.get("auth")) : null;
    }

}

