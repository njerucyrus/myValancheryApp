package com.hudutech.mymanjeri.models.digital_models;

public class AccountBalance implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String accountNo;
    private String userUid;
    private float balance;

    public AccountBalance() {
    }

    public AccountBalance(String docKey, String accountNo, String userUid, float balance) {
        this.docKey = docKey;
        this.accountNo = accountNo;
        this.userUid = userUid;
        this.balance = balance;
    }


    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "docKey='" + docKey + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", userUid='" + userUid + '\'' +
                ", balance=" + balance +
                '}';
    }
}
