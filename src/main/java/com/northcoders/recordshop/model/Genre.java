package com.northcoders.recordshop.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum Genre {
    POP("Pop"),
    ROCK("Rock"),
    @JsonAlias({"Hip Hop", "Hip-Hop", "Rap"})
    HIPHOP("Hip-Hop"),
    @JsonAlias({"RNB", "R&B", "Rhythm and Blues", "R'n'B"})
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
