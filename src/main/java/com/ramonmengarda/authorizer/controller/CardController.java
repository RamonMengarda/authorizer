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

import com.ramonmengarda.authorizer.exceptions.InsufficientBalanceException;
import com.ramonmengarda.authorizer.exceptions.InvalidPasswordException;
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
            /*
             * This was a developer's assumption.
             * 
             * Because we might be dealing with simultaneous access on the resources, I've
             * created a new
             * REST response that's not on the list of requirements, but I thought would be
             * necessary to
             * inform the caller that his request didn't go through and why.
             */
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

    @PostMapping("/transacoes")
    @ResponseBody
    public ResponseEntity<String> transaction(@RequestBody JSONObject transactionJson) {

        try {

            List<Card> cardList = cardService
                    .getCardsByNumber(Long.parseLong(transactionJson.get("numeroCartao").toString()));

            if (cardList.isEmpty()) {
                throw new NonexistentCardException("CARTAO_INEXISTENTE");
            }

            Card card = cardList.get(0);

            if (!card.getPassword().equals(transactionJson.get("senha").toString())) {
                throw new InvalidPasswordException("SENHA_INVALIDA");
            }

            if (card.getBalance().compareTo(new BigDecimal(transactionJson.get("valor").toString())) != -1) {
                card.setBalance(card.getBalance().subtract(new BigDecimal(transactionJson.get("valor").toString())));
                cardService.save(card);
            } else {
                throw new InsufficientBalanceException("SALDO_INSUFICIENTE");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
        } catch (NonexistentCardException e) {

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("CARTAO_INEXISTENTE");
        } catch (InvalidPasswordException e) {

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (InsufficientBalanceException e) {

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (ObjectOptimisticLockingFailureException e) {
            /*
             * This was a developer's assumption.
             * 
             * Because we might be dealing with simultaneous access on the resources, I've
             * created a new
             * REST response that's not on the list of requirements, but I thought would be
             * necessary to
             * inform the caller that his request didn't go through and why.
             */
            return ResponseEntity.status(HttpStatus.LOCKED).body(
                    "Transactions are locked due to too many simultaneous requests for the database. The balance value for the desired card might have changed. Try again.");
        }
    }
}
