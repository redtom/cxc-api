package haywood.tom.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * JPA Entity class that represents a Credit card stored in the CREDIT_CARD table
 */
@Entity
@Table(name = "CREDIT_CARD")
public class CreditCard implements Serializable {

    private static final long serialVersionUID = -1506508435368721777L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDIT_CARD_SEQ")
    @SequenceGenerator(sequenceName = "CREDIT_CARD_SEQ", allocationSize = 1, name = "CREDIT_CARD_SEQ")
    private Long id;

    private String userId;
    private String number;
    private String nickName;
    private String type;
    private String subType;
    private String holderName;

    public CreditCard() {
    }

    public CreditCard(Long id, String userId, String number, String nickName, String type, String subType, String holderName) {
        this.id = id;
        this.userId = userId;
        this.number = number;
        this.nickName = nickName;
        this.type = type;
        this.subType = subType;
        this.holderName = holderName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

}
