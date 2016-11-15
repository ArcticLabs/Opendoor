package me.willeponken.opendoor;

class User {
    String name;
    String number;
    String password;
    boolean active = true;
    boolean caseSensitive = false;

    User(String name, String number, String password, boolean active, boolean caseSensitive) {
        this.name = name;
        this.number = number;
        this.password = password;
        this.active = active;
        this.caseSensitive = caseSensitive;
    }

    boolean toggleActive() {
        this.active = !this.active;
        return this.active;
    }

    boolean toggleCaseSensitive() {
        this.caseSensitive = !this.caseSensitive;
        return this.caseSensitive;
    }
}