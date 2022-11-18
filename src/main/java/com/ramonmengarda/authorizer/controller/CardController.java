package com.ramonmengarda.authorizer.controller;

import java.math.BigDecimal;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ramonmengarda.authorizer.exceptions.NonexistentCardException;
import com.ramonmengarda.authorizer.model.Card;
import com.ramonmengarda.authorizer.service.CardService;

@Controller
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/cartoes")
    @ResponseBody
    public ResponseEntity<JSONObject> createCard(@RequestBody JSONObject createCardJson) {

        JSONObject responseBody = new JSONObject();
        responseBody.put("senha", createCardJson.get("senha"));
        responseBody.put("numeroCartao", createCardJson.get("numeroCartao"));

        try {
            Card card = cardService.create(
                    Long.parseLong(createCardJson.get("numeroCartao").toString()),
                    createCardJson.get("senha").toString());

            cardService.save(card);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(responseBody);
        } catch (ObjectOptimisticLockingFailureException e) {
            responseBody.clear();
            responseBody.put("FAILURE",
                    "Card creation is locked due to too many simultaneous requests for the database. Try again.");
            return ResponseEntity.status(HttpStatus.LOCKED).body(responseBody);
        }
    }

    @GetMapping("/cartoes/{number}")
    public ResponseEntity<BigDecimal> getCardBalance(@PathVariable Long number) {

        try {
            List<Card> cardList = cardService.getCardsByNumber(number);

            if (cardList.isEmpty()) {
                throw new NonexistentCardException("CARTAO_INEXISTENTE");
            }

            Card card = cardList.get(0);

            return ResponseEntity.status(HttpStatus.OK).body(card.getBalance());
        } catch (NonexistentCardException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
}
