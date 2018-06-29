package com.hudutech.mymanjeri.models.digital_models;

public class SBankAccount implements java.io.Serializable{

    private static final long serialVersionUID = 1L;
    private String docKey;
    private String photoUrl;
    private String batchNo;
    private String customerName;
    private String accountNo;
    private String email;

    private SBankAccount() {

    }

    public SBankAccount(String docKey, String photoUrl, String batchNo, String customerName, String accountNo, String email) {
        this.docKey = docKey;
        this.photoUrl = photoUrl;
        this.batchNo = batchNo;
        this.customerName = customerName;
        this.accountNo = accountNo;
        this.email = email;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
