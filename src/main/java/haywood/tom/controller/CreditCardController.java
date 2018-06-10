package haywood.tom.controller;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import haywood.tom.model.CreditCard;
import haywood.tom.model.CreditCardType;
import haywood.tom.service.CreditCardService;
import haywood.tom.service.CreditCardTypeService;

/**
 * The userId is being passed as a REST path parameter. This is nowhere close to acceptable
 * for a real application. Usually the userId would be passed in a JWT token in the Authorization
 * header where it can be trusted as valid, and not so easily hacked.
 */
@CrossOrigin(origins = "*")
@RestController
public class CreditCardController {

    @Autowired
    private CreditCardTypeService creditCardTypeService;

    @Autowired
    private CreditCardService creditCardService;

    /**
     * Utility function that calls a lamda and then does some common HTTP status setting on errors.
     */
    private <T> T callAndSetResponse(HttpServletResponse response, Function<HttpServletResponse, T> lamda) {
        try {
            T result = lamda.apply(response);
            if (result == null) {
                response.setStatus(SC_NOT_FOUND);
            } else {
                return result;
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(SC_BAD_REQUEST);
        }
        return null;
    }
    
    @RequestMapping(value = "/card/type/{bin}", method = GET)
    public CreditCardType getType(@PathVariable String bin, HttpServletResponse response) {
        return callAndSetResponse(response, resp -> creditCardTypeService.getType(bin));
    }

    @RequestMapping(value = "/card/{userId}/{cardId}", method = GET)
    public CreditCard getCardById(@PathVariable String userId, @PathVariable Long cardId, HttpServletResponse response) {
        return callAndSetResponse(response, resp -> creditCardService.get(userId, cardId));
    }

    @RequestMapping(value = "/card", method = POST)
    public CreditCard createCard(@RequestBody CreditCard card, HttpServletResponse response) {
        return callAndSetResponse(response, resp -> creditCardService.insert(card));
    }

    @RequestMapping(value = "/card", method = PUT)
    public CreditCard updateCard(@RequestBody CreditCard card, HttpServletResponse response) {
        return callAndSetResponse(response, resp -> creditCardService.update(card));
    }

    @RequestMapping(value = "/card/{userId}/{cardId}", method = DELETE)
    public Boolean deleteCard(@PathVariable String userId, @PathVariable Long cardId, HttpServletResponse response) {
        return callAndSetResponse(response, resp -> creditCardService.delete(userId, cardId));
    }

    @RequestMapping(value = "/cards/{userId}", method = GET)
    public List<CreditCard> getAllCards(@PathVariable String userId, HttpServletResponse response) {
        return callAndSetResponse(response, resp -> creditCardService.getAll(userId));
    }

    @RequestMapping(value = "/cards/{userId}/{number}/{nickName}/{type}/{subType}/{holderName}", method = GET)
    public List<CreditCard> findCards(
            @PathVariable String userId, 
            @PathVariable String number, 
            @PathVariable String nickName, 
            @PathVariable String type,
            @PathVariable String subType, 
            @PathVariable String holderName,
            HttpServletResponse response) throws IllegalArgumentException {
        return callAndSetResponse(
                response, 
                resp -> creditCardService.find(
                            convertEmpty(userId), 
                            convertEmpty(number), 
                            convertEmpty(nickName), 
                            convertEmpty(type), 
                            convertEmpty(subType), 
                            convertEmpty(holderName)));
    }
    
    /**
     * Convert empty REST parameter to null. Done here because it a direct REST concern that doesn't belong in the service.
     */
    protected String convertEmpty(String pathParam) {
        return "-".equals(pathParam) ? null : pathParam;
    }
}
