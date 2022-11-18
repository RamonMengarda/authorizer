package com.ramonmengarda.authorizer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramonmengarda.authorizer.factory.CardFactory;
import com.ramonmengarda.authorizer.model.Card;
import com.ramonmengarda.authorizer.repository.CardRepository;

@Service
public class CardService {

    @Autowired
    private CardFactory cardFactory;

    @Autowired
    private CardRepository cardRepository;

    /*
     * Because there's a project constraint stating that all cards need to be
     * created with a 500.00 balance
     * we'll implement a factory that can help enforcing that rule as well as making
     * it easier to change when
     * necessary
     */
    public Card create(Long number, String password) {
        return cardFactory.create(number, password);
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public List<Card> getCardsByNumber(Long number) {
        return cardRepository.findByNumber(number);
    }
}
