package com.ramonmengarda.authorizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ramonmengarda.authorizer.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByNumber(Long number);
}
