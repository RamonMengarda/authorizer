package com.ramonmengarda.authorizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ramonmengarda.authorizer.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}
