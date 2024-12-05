package com.tkachenya.currency.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "base_currency_id", referencedColumnName = "id", nullable = false)
    public Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "quote_currency_id", referencedColumnName = "id", nullable = false)
    public Currency quoteCurrency;

    @Column(name = "rate", precision = 10, scale = 5, nullable = false)
    public BigDecimal rate;

    @Column(name = "date", nullable = false)
    public LocalDate date;
}
