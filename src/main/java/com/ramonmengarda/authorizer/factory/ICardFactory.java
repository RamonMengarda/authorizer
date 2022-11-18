package com.ramonmengarda.authorizer.factory;

import com.ramonmengarda.authorizer.model.Card;

public interface ICardFactory {
    Card create(Long number, String password);
}