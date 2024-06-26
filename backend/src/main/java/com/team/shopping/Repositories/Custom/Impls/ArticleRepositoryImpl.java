package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.Article;
import com.team.shopping.Domains.QArticle;
import com.team.shopping.Domains.QCartItem;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Type;
import com.team.shopping.Repositories.Custom.ArticleRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QArticle qArticle = QArticle.article;

    @Override
    public List<Article> findMyArticleList(SiteUser user, Type type) {
        JPAQuery<Article> query = jpaQueryFactory
                .selectFrom(qArticle)
                .where(qArticle.author.eq(user));

        switch (type) {

            case REPORT:
                query.where(qArticle.type.eq(Type.REPORT));
                break;
            case FAQ:
                query.where(qArticle.type.eq(Type.FAQ));
                break;
            case NOTICE:
                query.where(qArticle.type.eq(Type.NOTICE));

            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return query.fetch();
    }
}
