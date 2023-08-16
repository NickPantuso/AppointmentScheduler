package group.scheduler.model;

/**
 * Creates a FirstLevelDivision.
 * @author Nick Pantuso
 */
public class FirstLevelDivision {

    private int divId;
    private String divName;
    private int countryId;

    public FirstLevelDivision(int divId, String divName, int countryId) {
        this.divId = divId;
        this.divName = divName;
        this.countryId = countryId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setDivId(int divId) {
        this.divId = divId;
    }

    public int getDivId() {
        return divId;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }
}
