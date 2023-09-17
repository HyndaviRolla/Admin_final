package com.prodapt.learningspring.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.prodapt.learningspring.entity.LikeId;
import com.prodapt.learningspring.entity.LikeRecord;
import com.prodapt.learningspring.entity.Post;

import jakarta.transaction.Transactional;

public interface LikeCRUDRepository extends CrudRepository<LikeRecord, LikeId>{
  public Integer countByLikeIdPost(Post post);

public void deleteByLikeIdPost(Post post);


}
