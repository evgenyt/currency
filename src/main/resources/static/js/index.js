fetchCurrencies();

function fetchCurrencies() {
    $.ajax({
        type : "GET",
        url : "/api/v1/currencies",
        success: function(data, textStatus, jqXHR) {
            if (jqXHR.status === 204 || jqXHR.status !== 200) return;

            var table = $('#currencies tbody');
            table.empty();

            $.each(data, (i, el) => {
                let noteRow = '<tr>' +
                    '<td><a href="#" onclick="fetchExchangeRates(\''+ el.code +'\')">' + el.code + '</a></td>' +
                    '<td>' + el.description + '</td>' +
                    '</tr>';
                table.append(noteRow);
            });
        }
    });
}

function fetchExchangeRates(code) {
    var currentCurrency = $('#currentCurrency');
    currentCurrency.empty();
    currentCurrency.append(code);

    var daysCount = $('#daysCount').val();

    $.ajax({
        type : "GET",
        url : "/api/v1/exchange-rates/" + code + "?daysCount=" + daysCount,
        success: function(data, textStatus, jqXHR) {
            if (jqXHR.status === 204 || jqXHR.status !== 200) return;

            var table = $('#exchangeRates tbody');
            table.empty();

            $.each(data, (i, el) => {
                let noteRow = '<tr>' +
                    '<td>' + el.date + '</td>' +
                    '<td>' + el.code + '</td>' +
                    '<td>' + el.rate + '</td>' +
                    '</tr>';
                table.append(noteRow);
            });
        }
    });
}