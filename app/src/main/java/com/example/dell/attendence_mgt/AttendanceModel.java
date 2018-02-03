package com.example.dell.attendence_mgt;

/**
 * Created by Dell on 06-12-2017.
 */

public class AttendanceModel {

    String name,att,stud_id;

    AttendanceModel(){

    }

    AttendanceModel(String stud_id,String name,String att){

        this.stud_id = stud_id;
        this.name = name;
        this.att = att;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAtt() {
        return att;
    }

    public void setAtt(String att) {
        this.att = att;
    }
}
