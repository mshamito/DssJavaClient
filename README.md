# Пример работы по API c КриптоПро DSS / Ключ -ом

## предустановка
1. скачать [КриптоПро JCP-A](https://cryptopro.ru/products/csp/jcp/downloads) (для JVM10+) или JCSP-A 
2. распаковать дистрибутив в каталог libs проекта
3. указать алиас (контейнер) для двухстороннего тлс для оператора в application.yml
4. установить цепочку сертификатов для клиентского сертификата в контейнер или сохранить его цепочку в DER формате в каталоге certs
5. установить цепочку сертификатов для проверки веб-сервера в cacerts
6. получить ClientID [ и ClientSecret ] у администратора DSS
7. указать все необходимые адреса и настройки в application.yml

## сборка проекта
```shell
./gradle clean
./gradle bootJar
```

## запуск проекта
указать переменные в строке запуска
```shell
CLIENT_ID=<clientid> CLIENT_SECRET=<client-secret> java -jar build/libs/DssJavaClient-0.0.1-SNAPSHOT.jar
```

## доступен swagger
по адресу
`http://localhost:8080/swagger`

## возможности
* работа по APIv2 DSS
* работа с подтверждением через DSS SDK 2.0 (async only)
* отключаемое кэширование access token-ов