package group.scheduler.model;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Creates an Appointment.
 * @author Nick Pantuso
 */
public class Appointment {

    private int apptId;
    private String title;
    private String desc;
    private String loc;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int custId;
    private int userId;
    private String contactName;

    public Appointment(int apptId, String title, String desc, String loc, String type, Timestamp start, Timestamp end, int custId, int userId, String contactName) {
        this.apptId = apptId;
        this.title = title;
        this.desc = desc;
        this.loc = loc;
        this.type = type;
        this.start = start;
        this.end = end;
        this.custId = custId;
        this.userId = userId;
        this.contactName = contactName;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getCustId() {
        return custId;
    }

    /**
     * Converts an end Timestamp to a local ZonedDateTime.
     * @return end ZonedDateTime
     */
    public ZonedDateTime getEnd() {
        ZoneId zone = ZoneId.systemDefault();
        return ZonedDateTime.of(end.toLocalDateTime(), zone);
    }

    /**
     * The end getter used by TableViews for easy formatting.
     * @return end ZonedDateTime String
     */
    public String getEndFormat() {
        ZonedDateTime ZDT = getEnd();
        return ZDT.getYear()+"-"+ZDT.getMonthValue()+"-"+ZDT.getDayOfMonth()+" "+ZDT.getHour()+":00";
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * Converts a start Timestamp to a local ZonedDateTime.
     * @return start ZonedDateTime
     */
    public ZonedDateTime getStart() {
        ZoneId zone = ZoneId.systemDefault();
        return ZonedDateTime.of(start.toLocalDateTime(), zone);
    }

    /**
     * The start getter used by TableViews for easy formatting.
     * @return start ZonedDateTime String
     */
    public String getStartFormat() {
        ZonedDateTime ZDT = getStart();
        return ZDT.getYear()+"-"+ZDT.getMonthValue()+"-"+ZDT.getDayOfMonth()+" "+ZDT.getHour()+":00";
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
