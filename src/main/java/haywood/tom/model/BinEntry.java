package haywood.tom.model;

/**
 * Represents one line in bin_ranges.csv
 */
public class BinEntry {

    private int iinStart;
    private int iinEnd;
    private String type;
    private String subType;

    public BinEntry() {
    }

    public BinEntry(int iinStart, int iinEnd, String type, String subType) {
        this.iinStart = iinStart;
        this.iinEnd = iinEnd;
        this.type = type;
        this.subType = subType;
    }

    public int getIinStart() {
        return iinStart;
    }

    public void setIinStart(int iinStart) {
        this.iinStart = iinStart;
    }

    public int getIinEnd() {
        return iinEnd;
    }

    public void setIinEnd(int iinEnd) {
        this.iinEnd = iinEnd;
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

}
