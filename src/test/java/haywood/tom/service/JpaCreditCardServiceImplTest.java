package haywood.tom.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import haywood.tom.model.CreditCard;
import haywood.tom.model.CreditCardType;
import haywood.tom.repository.CreditCardRepository;

@RunWith(MockitoJUnitRunner.class)
public class JpaCreditCardServiceImplTest {
    
    private static final long CARD_ID = 1L;

    private static final String USER_ID = "x";

    @Mock
    private CreditCardTypeService creditCardTypeService;
    
    @Mock
    private CreditCardRepository creditCardRepository;
    
    @InjectMocks
    private JpaCreditCardServiceImpl jpaCreditCardServiceImpl;
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEmptyUserId() {
        jpaCreditCardServiceImpl.get(null, 1L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEmptyCardId() {
        jpaCreditCardServiceImpl.get(USER_ID, null);
    }
    
    @Test
    public void testGet() throws IllegalArgumentException {
        CreditCard card = new CreditCard();
        when(creditCardRepository.findByUserIdAndId(USER_ID, CARD_ID)).thenReturn(card);
        
        assertEquals(card, jpaCreditCardServiceImpl.get(USER_ID, 1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllEmptyUserId() {
        jpaCreditCardServiceImpl.getAll("");
    }

    @Test
    public void testGetAll() {
        List<CreditCard> cards = new ArrayList<>();
        when(creditCardRepository.findByUserId(USER_ID)).thenReturn(cards);
        
        assertEquals(cards, jpaCreditCardServiceImpl.getAll(USER_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullCard() {
        jpaCreditCardServiceImpl.insert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullUserId() {
        CreditCard card = new CreditCard();
        jpaCreditCardServiceImpl.insert(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNotNullCardId() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setId(CARD_ID); // should be blank for a new card
        jpaCreditCardServiceImpl.insert(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        jpaCreditCardServiceImpl.insert(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertShortNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setNumber("12345");
        jpaCreditCardServiceImpl.insert(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertLongNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setNumber("123456789012345678");
        jpaCreditCardServiceImpl.insert(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertAlphaNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setNumber("abcdefghijk");
        jpaCreditCardServiceImpl.insert(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertUnknownNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setNumber("1234567890");
        jpaCreditCardServiceImpl.insert(card);
    }

    @Test
    public void testInsert() {
        // Mock the type service
        String type = "amex";
        String subtype = "credit";
        CreditCardType cardType = new CreditCardType(type, subtype);
        when(creditCardTypeService.getType("123456")).thenReturn(cardType);
        
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setNumber("1234 5678 9012 3456");
        
        // Mock save to return passed in card
        when(creditCardRepository.save(card)).thenAnswer(x -> x.getArguments()[0]);
        
        CreditCard savedCard = jpaCreditCardServiceImpl.insert(card);
        
        assertNotNull(savedCard);
        assertEquals(type, savedCard.getType());
        assertEquals(subtype, savedCard.getSubType());
        assertEquals("1234567890123456", savedCard.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullCard() {
        jpaCreditCardServiceImpl.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullUserId() {
        CreditCard card = new CreditCard();
        card.setId(CARD_ID);
        jpaCreditCardServiceImpl.update(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullCardId() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        jpaCreditCardServiceImpl.update(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setId(CARD_ID);
        jpaCreditCardServiceImpl.update(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateShortNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setId(CARD_ID);
        card.setNumber("12345");
        jpaCreditCardServiceImpl.update(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateLongNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setId(CARD_ID);
        card.setNumber("123456789012345678");
        jpaCreditCardServiceImpl.update(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAlphaNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setId(CARD_ID);
        card.setNumber("abcdefghijk");
        jpaCreditCardServiceImpl.update(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUnknownNumber() {
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setId(CARD_ID);
        card.setNumber("1234567890");
        jpaCreditCardServiceImpl.update(card);
    }

    @Test
    public void testUpdate() {
        // Mock the type service
        String type = "amex";
        String subtype = "credit";
        CreditCardType cardType = new CreditCardType(type, subtype);
        when(creditCardTypeService.getType("123456")).thenReturn(cardType);
        
        CreditCard card = new CreditCard();
        card.setUserId(USER_ID);
        card.setId(CARD_ID);
        card.setNumber("1234 5678 9012 3456");
        
        // Mock save to return passed in card
        when(creditCardRepository.save(card)).thenAnswer(x -> x.getArguments()[0]);
        
        CreditCard savedCard = jpaCreditCardServiceImpl.update(card);
        
        assertNotNull(savedCard);
        assertEquals(type, savedCard.getType());
        assertEquals(subtype, savedCard.getSubType());
        assertEquals("1234567890123456", savedCard.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNullUserId() {
        jpaCreditCardServiceImpl.delete(null, CARD_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNullCardId() {
        jpaCreditCardServiceImpl.delete(USER_ID, null);
    }

    @Test
    public void testDelete() {
        jpaCreditCardServiceImpl.delete(USER_ID, CARD_ID);
        verify(creditCardRepository).deleteByUserIdAndId(USER_ID, CARD_ID);
    }

    @Test
    public void testFind() {
        jpaCreditCardServiceImpl.find("a", null, "", "b", null, "");
        verify(creditCardRepository).findByUserIdLikeAndNumberLikeAndNickNameLikeAndTypeLikeAndSubTypeLikeAndHolderNameLike(
                "a", // userId must be exact 
                "%", 
                "%", 
                "%b%", 
                "%", 
                "%");
    }
}
