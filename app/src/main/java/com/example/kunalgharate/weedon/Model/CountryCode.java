package com.example.kunalgharate.weedon.Model;

/**
 * Created by Kunal Gharate on 25-12-2017.
 */

class CountryCode {

    String cName;
    String code;

    public CountryCode() {
    }

    public CountryCode(String cName, String code) {
        this.cName = cName;
        this.code = code;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
