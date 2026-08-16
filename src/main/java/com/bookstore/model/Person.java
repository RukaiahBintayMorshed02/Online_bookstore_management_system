package com.bookstore.model;

/**
 * Person is an ABSTRACT class: it defines fields and behaviour shared by any
 * "person" in the system, but it can never be instantiated directly
 * (you can never write "new Person(...)").
 *
 * Right now Customer is the only subclass, but making Person abstract and
 * separate from Customer demonstrates ABSTRACTION: we are modelling the
 * general concept of a person before specialising it. It also gives us a
 * clean place to add other person types later (e.g. Admin) without touching
 * Customer's code.
 */
public abstract class Person {

    // Fields are private -> this is ENCAPSULATION. Nothing outside this
    // class can change name/email directly; they must go through the
    // getters/setters below, which lets us validate input in one place.
    private String id;
    private String name;
    private String email;

    public Person(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // A tiny bit of validation lives here because it's a rule about
        // Person's own data - this is exactly the kind of thing
        // encapsulation is for: the rule lives with the data it protects.
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'");
        }
        this.email = email;
    }

    /**
     * Every Person subclass must be able to describe itself. Because this
     * method has no body here, each subclass is FORCED to implement its own
     * version - this is how we guarantee polymorphism works later: whatever
     * type of Person we have, calling describe() always does "the right
     * thing" for that specific type.
     */
    public abstract String describe();
}
