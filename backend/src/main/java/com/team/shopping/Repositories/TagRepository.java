package com.team.shopping.Repositories;

import com.team.shopping.Domains.Tag;
import com.team.shopping.Repositories.Custom.TagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {

}
