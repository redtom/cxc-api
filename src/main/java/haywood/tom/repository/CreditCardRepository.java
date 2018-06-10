package haywood.tom.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import haywood.tom.model.CreditCard;

/**
 * Spring data repository interface.
 * 
 * Note: Briefly, in case you're unaware, how this works is Spring uses reflection on this interface
 * to provide a Repository implementation to a JPA Entity. There is a naming convention that is used
 * to generate JPA queries. Kind of cool, and saves a lot of repetitive code. (Also cuts down on testing).
 */
public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {

    CreditCard findByUserIdAndId(String userId, Long cardId);
    List<CreditCard> findByUserId(String userId);
    List<CreditCard> findByUserIdLikeAndNumberLikeAndNickNameLikeAndTypeLikeAndSubTypeLikeAndHolderNameLike(
            String userId, 
            String number, 
            String nickName, 
            String type, 
            String subType, 
            String holderName);
    
    void deleteByUserIdAndId(String userId, Long cardId);
}
