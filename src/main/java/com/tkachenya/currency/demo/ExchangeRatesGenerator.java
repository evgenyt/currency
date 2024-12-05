package com.tkachenya.currency.demo;

import com.tkachenya.currency.domain.Currency;
import com.tkachenya.currency.domain.ExchangeRate;
import com.tkachenya.currency.repository.CurrencyRepository;
import com.tkachenya.currency.repository.ExchangeRateRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.RoundingMode.HALF_UP;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@Profile("demo")
public class ExchangeRatesGenerator {
    private final Logger logger = getLogger(ExchangeRatesGenerator.class);

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    // Минимальное и максимальное значение для генерации курсов
    private static final BigDecimal MIN_VALUE = new BigDecimal("0.005");
    private static final BigDecimal MAX_VALUE = new BigDecimal("0.05");
    // Количество дней для генерации данных
    private static final int DAYS_COUNT = 20;

    @Autowired
    public ExchangeRatesGenerator(CurrencyRepository currencyRepository, ExchangeRateRepository exchangeRateRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @PostConstruct
    @Transactional
    public void setUp() {
        clearAll();

        generateExchangeRates(generateCurrencies());
    }

    // Очистка БД
    private void clearAll() {
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    // Метод для генерации валют
    private List<Currency> generateCurrencies() {
        List<Currency> currencies = new ArrayList<>();

        Currency rub = new Currency();
        rub.code = "RUB";
        rub.description = "Российский рубль";
        currencies.add(rub);

        Currency eur = new Currency();
        eur.code = "EUR";
        eur.description = "Евро";
        currencies.add(eur);

        Currency usd = new Currency();
        usd.code = "USD";
        usd.description = "Доллар США";
        currencies.add(usd);

        currencies.forEach(currencyRepository::save);

        return currencies;
    }

    // Метод для генерации курсов валют
    private void generateExchangeRates(List<Currency> currencies) {
        LocalDate today = LocalDate.now();

        // Генерируем курс по датам
        for (int i = 0; i < DAYS_COUNT; i++) {
            LocalDate currentDate = today.minusDays(i);
            Map<Currency, ExchangeRate> rates = new HashMap<>();

            // Базовой считается первая валюта в списке
            for (int j = 0; j < currencies.size(); j++) {
                for (int k = j + 1; k < currencies.size(); k++) {
                    // Генерируем прямой курс
                    ExchangeRate fwExchangeRate = new ExchangeRate();
                    fwExchangeRate.date = currentDate;
                    fwExchangeRate.baseCurrency = currencies.get(j);
                    fwExchangeRate.quoteCurrency = currencies.get(k);

                    if (j == 0) {
                        // Случайное значение курса
                        fwExchangeRate.rate = getRandomBigDecimal(MIN_VALUE, MAX_VALUE);
                    } else {
                        // Вычисляем кросс-курс через базовую валюту
                        fwExchangeRate.rate = generatCrossRate(fwExchangeRate, rates);
                    }
                    exchangeRateRepository.save(fwExchangeRate);

                    // Генерируем обратный курс
                    ExchangeRate revExchangeRate = new ExchangeRate();
                    revExchangeRate.date = currentDate;
                    revExchangeRate.baseCurrency = fwExchangeRate.quoteCurrency;
                    revExchangeRate.quoteCurrency = fwExchangeRate.baseCurrency;
                    revExchangeRate.rate = BigDecimal.ONE.divide(fwExchangeRate.rate, 5, HALF_UP);
                    exchangeRateRepository.save(revExchangeRate);

                    rates.put(fwExchangeRate.quoteCurrency, revExchangeRate);
                }
            }
        }
    }

    // Вычисление кросс-курса
    private BigDecimal generatCrossRate(ExchangeRate fwExchangeRate, Map<Currency, ExchangeRate> rates) {
        return BigDecimal.ONE
                .divide(rates.get(fwExchangeRate.baseCurrency).rate, 5, HALF_UP)
                .multiply(rates.get(fwExchangeRate.quoteCurrency).rate);
    }

    // Генерация случайного значения
    public static BigDecimal getRandomBigDecimal(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException("Минимальное значение должно быть меньше максимального");

        BigDecimal range = max.subtract(min);
        BigDecimal scaled = BigDecimal.valueOf(Math.random()).multiply(range);

        return min.add(scaled);
    }
}
