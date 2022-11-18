package com.ramonmengarda.authorizer.factory;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.ramonmengarda.authorizer.model.Card;

@Service
public class CardFactory implements ICardFactory {

    @Override
    public Card create(Long number, String password) {
        return new Card(null, number, new BigDecimal("500.00"), password, null);
    }

}
