package org.abbatia.bean;

public class Email {
    private String to;
    private String asunto;
    private String msg;
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Email(String to, String asunto, String msg) {
        this.to = to;
        this.asunto = asunto;
        this.msg = msg;
    }

    public Email() {
    }

    public void setTo(String to) {
        this.to = to;
    }


    public String getTo() {
        return to;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }


    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }


    public String getAsunto() {
        return asunto;
    }
}