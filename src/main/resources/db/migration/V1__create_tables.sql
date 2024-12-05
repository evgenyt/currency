CREATE TABLE currencies (
    id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    code VARCHAR(3) NOT NULL UNIQUE,
    description VARCHAR(50)
);

CREATE TABLE exchange_rates (
    id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    base_currency_id INT NOT NULL,
    quote_currency_id INT NOT NULL,
    rate DECIMAL(10, 5) NOT NULL,
    date DATE NOT NULL,
    CONSTRAINT fk_base_currency FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
    CONSTRAINT fk_quote_currency FOREIGN KEY (quote_currency_id) REFERENCES currencies(id),
    UNIQUE (base_currency_id, quote_currency_id, date)
);
