package haywood.tom.service;

import java.util.List;

import haywood.tom.model.CreditCard;

/**
 * This interface exposes all CRUD access to credit card storage.
 * 
 * All methods can throw IllegalArgumentException if the arguments are invalid.
 * No details of what is invalid is provided as it is assumed the front end validates user input.
 * But, as always you can never trust the front end, so validation must be repeated on the server.
 */
public interface CreditCardService {
    
    CreditCard get(String userId, Long cardId) throws IllegalArgumentException;
    List<CreditCard> getAll(String userId) throws IllegalArgumentException;
    
    CreditCard insert(CreditCard card) throws IllegalArgumentException;
    CreditCard update(CreditCard card) throws IllegalArgumentException;
    boolean delete(String userId, Long cardId) throws IllegalArgumentException;
    
    List<CreditCard> find(
            String userId, 
            String number, 
            String nickName, 
            String type,
            String subType, 
            String holderName) throws IllegalArgumentException;
}
