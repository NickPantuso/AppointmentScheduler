package group.qam2.model;

/**
 * Creates a Customer.
 * @author Nick Pantuso
 */
public class Customer {

    private int custId;
    private String custName;
    private String address;
    private String postal;
    private String phoneNum;
    private String divName;

    public Customer(int custId, String custName, String address, String postal, String phoneNum, String divName) {
        this.custId = custId;
        this.custName = custName;
        this.address = address;
        this.postal = postal;
        this.phoneNum = phoneNum;
        this.divName = divName;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }
}
