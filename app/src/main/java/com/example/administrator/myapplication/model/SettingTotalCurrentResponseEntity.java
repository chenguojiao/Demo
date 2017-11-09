package com.example.administrator.myapplication.model;

import java.io.Serializable;

public class SettingTotalCurrentResponseEntity implements Serializable {


    private SettingTotalCurrentRequestEntity.DataEntity data;

    public void setData(SettingTotalCurrentRequestEntity.DataEntity data) {
        this.data = data;
    }

    public SettingTotalCurrentRequestEntity.DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * a_255 : 00000001
         * a_2 : 12
         * a_253 : 1
         */

        private String a_255;
        private String a_2;
        private String a_253;
        private String cmd;

        public String getCmd() {
            return cmd;
        }

        public void setCmd(String cmd) {
            this.cmd = cmd;
        }

        public String getA_2() {

            return a_2;
        }

        public void setA_255(String a_255) {
            this.a_255 = a_255;
        }

        public void setA_2(String a_2) {
            this.a_2 = a_2;
        }

        public void setA_253(String a_253) {
            this.a_253 = a_253;
        }

        public String getA_255() {
            return a_255;
        }

        public String getA_2(String s) {
            return a_2;
        }

        public String getA_253() {
            return a_253;
        }
    }
}
