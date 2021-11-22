package com.wispr.wispr.LoginAndRegistration;

import com.wispr.wispr.adapter.SuperEntity;

public class AutoCompleteEntity extends SuperEntity {

    private String completeText;

    public AutoCompleteEntity(){

    }
    public AutoCompleteEntity(String completeText){
        this.completeText=completeText;
    }

    @Override
    public int getType() {
        return 0;
    }

    public String getCompleteText() {
        return completeText;
    }

    public void setCompleteText(String completeText) {
        this.completeText = completeText;
    }
}
