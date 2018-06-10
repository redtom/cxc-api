package haywood.tom.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.Ostermiller.util.ExcelCSVParser;

import haywood.tom.model.BinEntry;
import haywood.tom.model.CreditCardType;

/**
 * Credit card type service based on a modified CSV of BIN entries taken from https://github.com/binlist/data.git
 * 
 * Note: This is by no means a complete list of entries, but a complete list costs money.
 */
@Service
public class CSVCreditCardTypeService implements CreditCardTypeService {

    // TODO: move this to properties file
    public static final String CSV_PATH = "bin_ranges.csv";    
    private List<BinEntry> binEntries;
    
    /**
     * Read the CSV containing all binEntries. 
     */
    @PostConstruct
    public void initialise() throws IOException {
        processBinEntries(new ClassPathResource(CSV_PATH));
    }
    
    public void processBinEntries(Resource binRanges) throws IOException {
        binEntries = new ArrayList<BinEntry>();
        ExcelCSVParser csvParser = new ExcelCSVParser(binRanges.getInputStream());
        
        // Process all lines, skipping first line that contains the header
        String[][] lines = csvParser.getAllValues();
        for (int i = 1; i < lines.length; ++i) {
            binEntries.add(processBinEntry(lines[i]));
        }
    }
    
    /**
     * Process one entry in the CSV returning the created Object.
     * If there is no iiEnd, then the iiStart is used instead.
     * 
     * Note: there is no validation on the contents of the file. This would be unacceptable in a real system.
     * TODO: If there is sufficient time, fix this.
     */
    public BinEntry processBinEntry(String[] line) {
        int cell = 0;
        int iinStart = Integer.valueOf(line[cell++]);
        String iinEndString = line[cell++];
        int iinEnd = StringUtils.isNotEmpty(iinEndString) ? Integer.valueOf(iinEndString) : iinStart;
        String type = line[cell++];
        String subType = line[cell++];
        
        return new BinEntry(iinStart, iinEnd, type, subType);
    }
    
    public CreditCardType getType(String binString) throws IllegalArgumentException {
        if (validateBin(binString)) {
            int bin = Integer.valueOf(binString.substring(0, BIN_LENGTH));
            Optional<BinEntry> matchingEntry = binEntries
                .stream()
                .filter(binEntry -> (bin >= binEntry.getIinStart()) && (bin <= binEntry.getIinEnd()))
                .findFirst();
            
            if (matchingEntry.isPresent()) {
                return new CreditCardType(matchingEntry.get().getType(), matchingEntry.get().getSubType());
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public boolean validateBin(String bin) {
        return StringUtils.isNotEmpty(bin) && bin.matches("[0-9]{6,}");
    }

    public List<BinEntry> getBinEntries() {
        return binEntries;
    }

}
