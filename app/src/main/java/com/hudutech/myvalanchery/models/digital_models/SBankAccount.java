package com.hudutech.myvalanchery.models.digital_models;

public class SBankAccount implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String docKey;
    private String userUid;
    private String photoUrl;
    private String batchNo;
    private String customerName;
    private String accountNo;
    private String email;
    private float balance;

    private SBankAccount() {

    }

    public SBankAccount(String docKey, String userUid, String photoUrl, String batchNo, String customerName, String accountNo, String email, float balance) {
        this.docKey = docKey;
        this.userUid = userUid;
        this.photoUrl = photoUrl;
        this.batchNo = batchNo;
        this.customerName = customerName;
        this.accountNo = accountNo;
        this.email = email;
        this.balance = balance;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
