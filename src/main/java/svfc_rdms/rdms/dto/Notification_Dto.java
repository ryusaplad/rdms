package svfc_rdms.rdms.dto;

public class Notification_Dto {

    private long notifId;
    private String title;
    private String message;
    private String messageType;
    private String dateAndTime;
    private boolean status;
    private String from;
    private String to;

    public Notification_Dto(long notifId, String title, String message, String messageType, String dateAndTime,
            boolean status, String from, String to) {
        this.notifId = notifId;
        this.title = title;
        this.message = message;
        this.messageType = messageType;
        this.dateAndTime = dateAndTime;
        this.status = status;
        this.from = from;
        this.to = to;
    }

    public long getNotifId() {
        return notifId;
    }

    public void setNotifId(long notifId) {
        this.notifId = notifId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
