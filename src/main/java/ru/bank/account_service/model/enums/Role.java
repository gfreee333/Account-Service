package ru.bank.account_service.model.enums;

public enum Role {
    USER, MANAGER, ADMIN;

    public boolean isManager(){
        return this.equals(MANAGER);
    }

    public boolean isAdmin(){
        return this.equals(ADMIN);
    }

}
