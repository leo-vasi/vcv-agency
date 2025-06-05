package com.leo.vcv.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("NATIONAL")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class NationalTravel extends Travel {

    @Override
    public String getTravelType() {
        return "NATIONAL";
    }
}