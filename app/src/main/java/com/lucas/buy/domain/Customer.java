package com.lucas.buy.domain;

/**
 * Created by 111 on 2017/7/8.
 */

/**
 *  "address": "beijing",
 "id": 4,
 "name": "lucas",
 "phone": "15110141563"
 */
public class Customer {
    private String address;
    private int id;
    private String name;
    private String phone;

    public Customer() {
    }

    public Customer(String address, int id, String name, String phone) {
        this.address = address;
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "address='" + address + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
