package com.tkachenya.currency.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Column(name = "code", length = 3, nullable = false, unique = true)
    public String code;

    @Column(name = "description", length = 50)
    public String description;
}
