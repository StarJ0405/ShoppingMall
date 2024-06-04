package com.team.shopping.Domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventProduct is a Querydsl query type for EventProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventProduct extends EntityPathBase<EventProduct> {

    private static final long serialVersionUID = 906482966L;

    public static final QEventProduct eventProduct = new QEventProduct("eventProduct");

    public final ListPath<Event, QEvent> event = this.<Event, QEvent>createList("event", Event.class, QEvent.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Product, QProduct> product = this.<Product, QProduct>createList("product", Product.class, QProduct.class, PathInits.DIRECT2);

    public QEventProduct(String variable) {
        super(EventProduct.class, forVariable(variable));
    }

    public QEventProduct(Path<? extends EventProduct> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEventProduct(PathMetadata metadata) {
        super(EventProduct.class, metadata);
    }

}

