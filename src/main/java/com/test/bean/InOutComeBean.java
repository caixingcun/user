package com.test.bean;

public class InOutComeBean {
    private Double money;
    private String reason;
    private String create_time;
    private Long i_id;

    public void setI_id(Long i_id) {
        this.i_id = i_id;
    }

    public Long getI_id() {
        return i_id;
    }

    public Double getMoney() {
        return money;
    }

    public String getReason() {
        return reason;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
