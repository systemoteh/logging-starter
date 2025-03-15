# HTTP Logging Starter для Spring Boot 3

Стартер для автоматического логирования HTTP-запросов и ответов в Spring Boot 3 приложениях

## 📦 Возможности

- Логирование входящих HTTP-запросов и исходящих ответов
- Настраиваемые уровни детализации (INFO, DEBUG, TRACE)
- Поддержка конфигурации через свойства приложения
- Возможность включения/отключения логирования
- Опциональное логирование:
    - Заголовков запросов/ответов
    - Тела запросов/ответов
    - Времени выполнения запроса
- Безопасная обработка потоков данных
- Поддержка всех типов запросов (включая multipart/form-data)

## 🚀 Установка

Добавьте зависимость в ваш `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>http-logging-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## ⚙️ Конфигурация
Основные настройки
Добавьте в application.yml:

```yaml
logging:
  http:
    enabled: true             # Включить логирование
    level: DEBUG              # Уровень детализации (INFO, DEBUG, TRACE)
    include-headers: true     # Включать заголовки
    include-body: true        # Включать тело запросов/ответов
```

## 🎮 Примеры использования
Пример логов для GET-запроса
Copy
=== HTTP Log ===
Method: GET
URI: /api/users
Status: 200
Duration: 45ms
Headers:
accept: application/json
host: localhost:8080
Request Body: [empty]
Response Body: {"data":[...]}
=================
Пример логов для POST-запроса
Copy
=== HTTP Log ===
Method: POST
URI: /api/upload
Status: 201
Duration: 120ms
Headers:
content-type: multipart/form-data
authorization: Bearer ***
Request Body: [file content]
Response Body: {"status":"success"}
=================

## 🔧 Кастомизация
Переопределение настроек
```yaml
logging:
  http:
    level: TRACE
    include-body: false
    max-body-length: 2048
```

## ⚠️ Ограничения
Не рекомендуется включать include-body в production

Тела запросов > 1MB логируются частично

Не логирует файловые загрузки > 5MB

Чувствительные данные (пароли, токены) не маскируются автоматически