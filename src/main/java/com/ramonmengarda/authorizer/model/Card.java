package com.ramonmengarda.authorizer.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private Long number;
    private BigDecimal balance;
    private String password;
}
