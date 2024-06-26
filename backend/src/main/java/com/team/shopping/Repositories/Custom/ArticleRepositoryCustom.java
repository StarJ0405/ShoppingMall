package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Article;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Type;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<Article> findMyArticleList(SiteUser user, Type type);
}
