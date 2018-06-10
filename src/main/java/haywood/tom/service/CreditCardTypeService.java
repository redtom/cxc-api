package haywood.tom.service;

import haywood.tom.model.CreditCardType;

public interface CreditCardTypeService {

    int BIN_LENGTH = 6;
    
    /**
     * Return the credit cart type and subtype for a given BIN.
     * 
     * @param cardNumber The credit card number
     * @return The credit card type if found, or the default configured type if not.
     * @throws IllegalArgumentException if cardNumber isn't in a form that type can be determined. 
     * 
     * Note: IllegalArgumentException does not need to be declared, but it is more obvious if it is.
     */
    CreditCardType getType(String cardNumber) throws IllegalArgumentException;
}
