package com.ramonmengarda.authorizer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramonmengarda.authorizer.factory.CardFactory;
import com.ramonmengarda.authorizer.model.Card;

@Service
public class CardService {

    @Autowired
    private CardFactory cardFactory;

    public Card create(Long number, String password) {
        return cardFactory.create(number, password);
    }
}
