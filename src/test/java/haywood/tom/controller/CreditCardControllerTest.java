package haywood.tom.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import haywood.tom.model.CreditCard;
import haywood.tom.model.CreditCardType;
import haywood.tom.service.CreditCardService;
import haywood.tom.service.CreditCardTypeService;

/**
 * The purpose of these tests is to verify all spring wiring and JSON marshalling is correct.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreditCardControllerTest {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Mock
    private CreditCardTypeService creditCardTypeService;
    
    @Mock
    private CreditCardService creditCardService;
    
    @InjectMocks
    private CreditCardController controller;

    private MockMvc mockMvc;
    private CreditCard card;
    
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        card = new CreditCard(1L, "a", "b", "c", "d", "e", "f");
    }

    private void validateCardResponse(ResultActions actions) throws Exception {
        actions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.userId", is("a")))
            .andExpect(jsonPath("$.number", is("b")))
            .andExpect(jsonPath("$.nickName", is("c")))
            .andExpect(jsonPath("$.type", is("d")))
            .andExpect(jsonPath("$.subType", is("e")))
            .andExpect(jsonPath("$.holderName", is("f")));
    }

    private void validateCardsResponse(ResultActions actions) throws Exception {
        actions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].userId", is("a")))
            .andExpect(jsonPath("$[0].number", is("b")))
            .andExpect(jsonPath("$[0].nickName", is("c")))
            .andExpect(jsonPath("$[0].type", is("d")))
            .andExpect(jsonPath("$[0].subType", is("e")))
            .andExpect(jsonPath("$[0].holderName", is("f")));
    }

    private void validateNotFoundResponse(ResultActions actions) throws Exception {
        actions
            .andExpect(status().isNotFound())
            .andExpect(content().string(""));
    }

    private void validateBadRequestResponse(ResultActions actions) throws Exception {
        actions
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""));
    }

    @Test
    public void testGetType() throws Exception {
        when(creditCardTypeService.getType("123456")).thenReturn(new CreditCardType("Amex", "Credit"));
        mockMvc.perform(get("/card/type/123456"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.type", is("Amex")))
            .andExpect(jsonPath("$.subType", is("Credit")));
    }

    @Test
    public void testGetTypeNullResponse() throws Exception {
        when(creditCardTypeService.getType("123456")).thenReturn(null);
        ResultActions actions = mockMvc.perform(get("/card/type/123456"));
        validateNotFoundResponse(actions);
    }

    @Test
    public void testGetTypeInvalidBin() throws Exception {
        when(creditCardTypeService.getType("abcde")).thenThrow(new IllegalArgumentException());
        ResultActions actions = mockMvc.perform(get("/card/type/abcde"));
        validateBadRequestResponse(actions);
    }

    @Test
    public void testGetCardById() throws Exception {
        when(creditCardService.get("a", 1L)).thenReturn(card);
        ResultActions actions =  mockMvc.perform(get("/card/a/1"));
        validateCardResponse(actions);
    }

    @Test
    public void testGetCardNullResponse() throws Exception {
        ResultActions actions = mockMvc.perform(get("/card/a/1"));
        validateNotFoundResponse(actions);
    }

    @Test
    public void testGetCardInvalidArguments() throws Exception {
        when(creditCardService.get("a", 1L)).thenThrow(new IllegalArgumentException());
        ResultActions actions =  mockMvc.perform(get("/card/a/1"));
        validateBadRequestResponse(actions);
    }
    
    @Test
    public void testCreateCard() throws Exception {
        when(creditCardService.insert(any(CreditCard.class))).thenReturn(card);        
        String requestBody = objectMapper.writeValueAsString(card);
        
        ResultActions actions = mockMvc.perform(post("/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        validateCardResponse(actions);
    }

    @Test
    public void testCreateCardNullResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(card);
        
        ResultActions actions = mockMvc.perform(post("/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        validateNotFoundResponse(actions);
    }

    @Test
    public void testCreateCardInvalidArguments() throws Exception {
        when(creditCardService.insert(any(CreditCard.class))).thenThrow(new IllegalArgumentException());
        
        String requestBody = objectMapper.writeValueAsString(card);
        
        ResultActions actions = mockMvc.perform(post("/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        validateBadRequestResponse(actions);
    }
    
    @Test
    public void testUpdateCard() throws Exception {
        when(creditCardService.update(any(CreditCard.class))).thenReturn(card);
        String requestBody = objectMapper.writeValueAsString(card);
        
        ResultActions actions = mockMvc.perform(put("/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        validateCardResponse(actions);
    }

    @Test
    public void testUpdateCardNullResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(card);
        
        ResultActions actions = mockMvc.perform(put("/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        validateNotFoundResponse(actions);
    }

    @Test
    public void testUpdateCardInvalidArguments() throws Exception {
        when(creditCardService.update(any(CreditCard.class))).thenThrow(new IllegalArgumentException());
        
        String requestBody = objectMapper.writeValueAsString(card);
        
        ResultActions actions = mockMvc.perform(put("/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        validateBadRequestResponse(actions);
    }
    
    @Test
    public void testDeleteCard() throws Exception {
        when(creditCardService.delete("a", 1L)).thenReturn(true);
        
        mockMvc.perform(delete("/card/a/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void testDeleteCardInvalidArguments() throws Exception {
        when(creditCardService.delete("a", 1L)).thenThrow(new IllegalArgumentException());        
        
        ResultActions actions = mockMvc.perform(delete("/card/a/1"));
        validateBadRequestResponse(actions);
    }

    @Test
    public void testGetCards() throws Exception {
        when(creditCardService.getAll("a")).thenReturn(Lists.newArrayList(card));
        ResultActions actions =  mockMvc.perform(get("/cards/a"));
        validateCardsResponse(actions);
    }

    @Test
    public void testGetCardsNullResponse() throws Exception {
        when(creditCardService.getAll("a")).thenReturn(null);
        ResultActions actions = mockMvc.perform(get("/cards/a"));
        validateNotFoundResponse(actions);
    }

    @Test
    public void testGetCardsInvalidArguments() throws Exception {
        when(creditCardService.getAll("a")).thenThrow(new IllegalArgumentException());
        ResultActions actions =  mockMvc.perform(get("/cards/a"));
        validateBadRequestResponse(actions);
    }

    @Test
    public void testFindCards() throws Exception {
        when(creditCardService.find(
                "a", 
                null, 
                "b", 
                null, 
                "c", 
                null)).thenReturn(Lists.newArrayList(card));
        ResultActions actions =  mockMvc.perform(get("/cards/a/-/b/-/c/-"));
        validateCardsResponse(actions);
    }

    @Test
    public void testFindCardsNullResponse() throws Exception {
        when(creditCardService.find(
                any(String.class), 
                any(String.class), 
                any(String.class), 
                any(String.class), 
                any(String.class), 
                any(String.class))).thenReturn(null);
        ResultActions actions = mockMvc.perform(get("/cards/a/b/c/d/e/f"));
        validateNotFoundResponse(actions);
    }

    @Test
    public void testFindCardsInvalidArguments() throws Exception {
        when(creditCardService.find(
                any(String.class), 
                any(String.class), 
                any(String.class), 
                any(String.class), 
                any(String.class), 
                any(String.class))).thenThrow(new IllegalArgumentException());
        ResultActions actions =  mockMvc.perform(get("/cards/a/b/c/d/e/f"));
        validateBadRequestResponse(actions);
    }
}
