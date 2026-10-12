# Currency Converter (Spring Boot + ExchangeRate-API)

Java 17, Spring Boot 3.3, Maven. Rates come from [ExchangeRate-API](https://www.exchangerate-api.com)
and are cached (Caffeine, 60 min by default).

## Run
```bash
# optional: use your own key (otherwise the free keyless endpoint open.er-api.com is used)
export EXCHANGE_API_KEY=your_key_here

mvn spring-boot:run
```
Open http://localhost:8080 for the web UI.

## API
| Endpoint | Example |
|---|---|
| Convert | `GET /api/convert?from=USD&to=INR&amount=100` |
| Rates for a base | `GET /api/rates/USD` |
| Supported currencies | `GET /api/currencies` |

Sample response:
```json
{"from":"USD","to":"INR","amount":100,"rate":83.12,"result":8312.00,"timestamp":"2026-10-12T10:00:00Z"}
```

Errors follow RFC 7807 (`ProblemDetail`): 400 invalid input, 404 unknown currency, 502 upstream failure.

## Test
```bash
mvn test
```

## Config (`application.yml`)
`exchange-api.api-key`, `exchange-api.base-url`, `exchange-api.open-url`, `exchange-api.cache-ttl-minutes`
