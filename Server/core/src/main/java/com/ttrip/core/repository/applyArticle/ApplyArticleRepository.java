package com.ttrip.core.repository.applyArticle;

import com.ttrip.core.entity.applyArticle.ApplyArticle;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyArticleRepository extends JpaRepository <ApplyArticle, Integer> {

}
