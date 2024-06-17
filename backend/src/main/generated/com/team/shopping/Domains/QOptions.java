package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOptions is a Querydsl query type for Options
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOptions extends EntityPathBase<Options> {

    private static final long serialVersionUID = -1147170339L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOptions options = new QOptions("options");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QOptionList optionList;

    public final NumberPath<Integer> price = createNumber("totalPrice", Integer.class);

    public QOptions(String variable) {
        this(Options.class, forVariable(variable), INITS);
    }

    public QOptions(Path<? extends Options> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOptions(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOptions(PathMetadata metadata, PathInits inits) {
        this(Options.class, metadata, inits);
    }

    public QOptions(Class<? extends Options> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.optionList = inits.isInitialized("optionList") ? new QOptionList(forProperty("optionList"), inits.get("optionList")) : null;
    }

}

