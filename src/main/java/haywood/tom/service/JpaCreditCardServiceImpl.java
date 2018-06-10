package haywood.tom.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import haywood.tom.model.CreditCard;
import haywood.tom.model.CreditCardType;
import haywood.tom.repository.CreditCardRepository;

/**
 * JPA based implementation of the CreditCardService interface.
 */
@Service
@Transactional
public class JpaCreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardTypeService creditCardTypeService;
    
    @Autowired
    private CreditCardRepository creditCardRepository;
    
    private void validateNotEmpty(String value) throws IllegalArgumentException {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException();
        }
    }
    
    private <T> void validateNotNull(T value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException();
        }
    }
    
    private <T> void validateNull(T value) throws IllegalArgumentException {
        if (value != null) {
            throw new IllegalArgumentException();
        }
    }
    
    private void validateUserIdAndCardId(String userId, Long cardId) throws IllegalArgumentException {
        validateNotEmpty(userId);
        validateNotNull(cardId);
    }
    
    private void populateTypeAndSubType(CreditCard card) {
        if (StringUtils.isNotEmpty(card.getNumber())) {
            // Strip spaces and then validate length.
            // Different credit card types can have different length, hence exact length validation is implemented.
            String number = card.getNumber().replaceAll(" ", "");
            if (number.matches("[0-9]{6,16}")) {
                CreditCardType cardType = creditCardTypeService.getType(number.substring(0, 6));
                if (cardType != null) {
                    card.setType(cardType.getType());
                    card.setSubType(cardType.getSubType());
                    card.setNumber(number);
                    return;
                }
            }
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public CreditCard get(String userId, Long cardId) throws IllegalArgumentException {
        validateUserIdAndCardId(userId, cardId);
        return creditCardRepository.findByUserIdAndId(userId, cardId);
    }

    @Override
    public List<CreditCard> getAll(String userId) throws IllegalArgumentException {
        validateNotEmpty(userId);
        return creditCardRepository.findByUserId(userId);
    }

    @Override
    public CreditCard insert(CreditCard card) throws IllegalArgumentException {
        validateNotNull(card);
        validateNotEmpty(card.getUserId());
        validateNull(card.getId());
        populateTypeAndSubType(card);
        return creditCardRepository.save(card);
    }

    @Override
    public CreditCard update(CreditCard card) throws IllegalArgumentException {
        validateNotNull(card);
        validateUserIdAndCardId(card.getUserId(), card.getId());
        populateTypeAndSubType(card);
        return creditCardRepository.save(card);
    }

    @Override
    public boolean delete(String userId, Long cardId) throws IllegalArgumentException {
        validateUserIdAndCardId(userId, cardId);
        creditCardRepository.deleteByUserIdAndId(userId, cardId);
        return true;
    }
    
    /**
     * Apply wild card operators to JPA query parameter.
     */
    private String applyWildCards(String jpaParameter) {
        if (StringUtils.isEmpty(jpaParameter)) {
            return "%";
        } else {
            return "%" + jpaParameter + "%";
        }
    }

    @Override
    public List<CreditCard> find(
            String userId, 
            String number, 
            String nickName, 
            String type, 
            String subType, 
            String holderName) throws IllegalArgumentException {
        return creditCardRepository.findByUserIdLikeAndNumberLikeAndNickNameLikeAndTypeLikeAndSubTypeLikeAndHolderNameLike(
                userId, 
                applyWildCards(number), 
                applyWildCards(nickName), 
                applyWildCards(type), 
                applyWildCards(subType), 
                applyWildCards(holderName));
    }

}
