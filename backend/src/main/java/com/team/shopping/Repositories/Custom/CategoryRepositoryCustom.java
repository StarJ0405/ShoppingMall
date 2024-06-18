package com.team.shopping.Repositories.Custom;

public interface CategoryRepositoryCustom {
    boolean isDuplicateNameAndParent(String name, String parent);
    boolean isDuplicateName(String name);

}
