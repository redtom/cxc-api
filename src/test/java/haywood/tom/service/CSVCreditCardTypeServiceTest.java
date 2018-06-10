package haywood.tom.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;

import haywood.tom.model.BinEntry;
import haywood.tom.model.CreditCardType;

public class CSVCreditCardTypeServiceTest {

    private CSVCreditCardTypeService service;
    
    @Before
    public void setup() {
        service = new CSVCreditCardTypeService();
    }

    private void assertBin1(BinEntry binEntry) {
        assertNotNull(binEntry);
        assertEquals(123456, binEntry.getIinStart());
        assertEquals(234567, binEntry.getIinEnd());
        assertEquals("amex", binEntry.getType());
        assertEquals("credit", binEntry.getSubType());
    }

    private void assertBin2(BinEntry binEntry) {
        assertNotNull(binEntry);
        assertEquals(454545, binEntry.getIinStart());
        assertEquals(454545, binEntry.getIinEnd()); // Assert end is same as start
        assertEquals("mastercard", binEntry.getType());
        assertEquals("debit", binEntry.getSubType());
    }
    
    @Test
    public void testProcessBinEntryWithEnd() {
        BinEntry binEntry = service.processBinEntry(new String[]{"123456", "234567", "amex", "credit"});        
        assertBin1(binEntry);
    }
    
    @Test
    public void testProcessBinEntryWithNoEnd() {
        BinEntry binEntry = service.processBinEntry(new String[]{"454545", "", "mastercard", "debit"});
        assertBin2(binEntry);
    }
    
    @Test
    public void testProcessBinEntries() throws IOException {
        String entries = 
                "iinStart,iinEnd,type,subType\n"
                + "123456,234567,amex,credit\n"
                + "454545,,mastercard,debit\n";
        service.processBinEntries(new ByteArrayResource(entries.getBytes()));
        
        List<BinEntry> binEntries = service.getBinEntries();
        
        assertNotNull(binEntries);
        assertEquals(2, binEntries.size());
        assertBin1(binEntries.get(0));
        assertBin2(binEntries.get(1));
    }
    
    // Some might argue that the following tests should be moved to an integration tests folder, 
    // given they rely on the "production" CSV file.
        
    @Test
    public void testInitialise() throws IOException {
        service.initialise();

        List<BinEntry> binEntries = service.getBinEntries();
        
        assertNotNull(binEntries);
        assertEquals(5805, binEntries.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeNullBin() throws IOException {
        assertNull(service.getType(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeEmptyBin() throws IOException {
        assertNull(service.getType(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeShortBin() throws IOException {
        assertNull(service.getType("12345"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeNonNumberBin() throws IOException {
        assertNull(service.getType("AAAAAA"));
    }
    
    @Test
    public void testGetTypeMissingBin() throws IOException {
        service.initialise();

        CreditCardType type = service.getType("999999");
        
        assertNull(type);
    }
    
    @Test
    public void testGetTypeAmexCreditStartOfRange() throws IOException {
        service.initialise();

        CreditCardType type = service.getType("371556");
        
        assertNotNull(type);
        assertEquals("amex", type.getType());
        assertEquals("credit", type.getSubType());
    }
    
    @Test
    public void testGetTypeAmexCreditEndOfRange() throws IOException {
        service.initialise();

        CreditCardType type = service.getType("371557");
        
        assertNotNull(type);
        assertEquals("amex", type.getType());
        assertEquals("credit", type.getSubType());
    }
    
    @Test
    public void testGetTypeVisaDebitNotPartOfRange() throws IOException {
        service.initialise();

        CreditCardType type = service.getType("4031601234567");
        
        assertNotNull(type);
        assertEquals("visa", type.getType());
        assertEquals("debit", type.getSubType());
    }
}
