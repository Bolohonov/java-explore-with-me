# java-explore-with-me
Ссылка на PR
https://github.com/Bolohonov/java-explore-with-me/pull/1

Приложение состоит из двух сервисов, каждый из которых работает со своей БД - PostgreSQL.
Спецификации API сервисов в формате JSON:
ewm-server : https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json
ewm-statistics : https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json

Приложение можно запустить командой из корневой папки проекта при запущенном Docker
"docker-compose up" 
при этом будут развернуты 4 контейнера Docker: 2 для сервисов и 2 базы данных.

Каждый сервис может быть запущен отдельно с БД PostgreSQL для этого необходимо выполнить последовательность команд
в корневой директории проекта выполнить команды:

mvn clean install
cd ewm-server/target
java -jar server-0.0.1-snapshot.jar --spring.profiles.active=dev

затем перейти в другое окно терминала - в корневую папку с проектом и также выполнить команды:

mvn clean install
cd ewm-statistics/target
java -jar statistics-0.0.1-snapshot.jar --spring.profiles.active=dev

Сервис ewm-server приложение-афиша, где можно предложить какое-либо событие и набрать компанию для участия в нём.
Имеет два уровня REST API - открытый и закрытый (для администратора и авторизованных пользователей).
Реализована система поиска и фильтрации событий по различным критериям с ипользованием динамических запросов к БД.
Технологии: Spring Boot, PostgreSQL, JPA + Hibernate, JPA Criteria API

Сервис ewm-statistics позволяет сохранять статистику посещенных uri различных приложений.
Содержит эндоинты сохранения и получения статистики (REST API).
Технологии: Spring Boot, PostgreSQL, JPA + Hibernate, JPA Criteria API
