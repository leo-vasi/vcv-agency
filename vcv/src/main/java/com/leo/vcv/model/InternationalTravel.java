package com.leo.vcv.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("INTERNATIONAL")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class InternationalTravel extends Travel {

    @Column(name = "needs_visa")
    private boolean needsVisa;

    @Override
    public String getTravelType() {
        return "INTERNATIONAL";
    }
}