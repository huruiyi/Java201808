package com.example.GenericType.OOP4.Override1;


abstract class Person {
    Person() {

    }

    Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    String name;
    String surname;

    abstract void getDescription();
}