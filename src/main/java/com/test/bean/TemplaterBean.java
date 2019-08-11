package com.test.bean;

public class TemplaterBean {

    //    @RequestParam("create_time") String create_time,
//    @RequestParam("index_type") String index_type,
//    @RequestParam("templature") String templature,
//    @RequestParam("code_in") String code_in,
//    @RequestParam("code_out") String code_out)
   private String create_time;
    private long a_id;
    private String index_type;
    private double templature;
    private String code_in;
    private String code_out;

    public void setA_id(long a_id) {
        this.a_id = a_id;
    }

    public long getA_id() {
        return a_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIndex_type() {
        return index_type;
    }

    public void setIndex_type(String index_type) {
        this.index_type = index_type;
    }

    public double getTemplature() {
        return templature;
    }

    public void setTemplature(double templature) {
        this.templature = templature;
    }

    public String getCode_in() {
        return code_in;
    }

    public void setCode_in(String code_in) {
        this.code_in = code_in;
    }

    public String getCode_out() {
        return code_out;
    }

    public void setCode_out(String code_out) {
        this.code_out = code_out;
    }
}
