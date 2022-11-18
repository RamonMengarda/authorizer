package com.ramonmengarda.authorizer.service;

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
