package haywood.tom.model;

import java.io.Serializable;

/**
 * Represents a credit card type. This is returned to the front end.
 */
public class CreditCardType implements Serializable {

    private static final long serialVersionUID = -7593977429374473286L;
    
    private String type;
    private String subType;

    public CreditCardType() {
    }

    public CreditCardType(String type, String subtype) {
        this.type = type;
        this.subType = subtype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subtype) {
        this.subType = subtype;
    }

}
