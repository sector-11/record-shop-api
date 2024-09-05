package com.northcoders.recordshop.model;

public enum Genre {
    POP("Pop"),
    ROCK("Rock"),
    HIPHOP("Hip-Hop"),
    RNB("R&B"),
    COUNTRY("Country"),
    JAZZ("Jazz"),
    METAL("Metal"),
    CLASSICAL("Classical");

    final String desc;

    Genre (String description){
        this.desc = description;
    }

    @Override
    public String toString() {
        return this.desc;
    }

    public String get() {
        return desc;
    }
}
