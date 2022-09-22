# java-explore-with-me
Ссылка на PR
https://github.com/Bolohonov/java-explore-with-me/pull/1
Сейчас не проходит один тест (если его запускать без подборки - он проходит, там что-то с pre-request scripts).
Эти тесты вчера вечером выложили, думаю поправят.

Добавлена функциональность Лайки и рейтинги.
Тестировал Postmanом, тесты проходят успешно.
Добавлено: 

-поле Long rating в Event и EventFullDto

-в EventPrivateController метод likeEvent в качестве переменных пути принимает 
id пользователя и события , а в качестве парамтеров  флаг true - like, false - dislike. При повторном запросе с 
тем же флагом лайк/дизлайк удаляется. Соответственно добавлены методы в EventServicePrivate -  addLike, checkLikeStatus

-в EventPrivateController метод getUsersByEventsRating для получения пользователей по рейтингу их событий 
(он суммируется по нескольким событиям). Соответственно добавлен метод в EventServicePrivate getUsersByRating.

-пакет like в пакете repository - метод countEventRating - который рассчитывает текущий рейтинг события. Рейтинг 
обновляется при поиске события по id.

-сортировка событий по рейтингу работает в методе EventController.findEvents
при указании в параметрах запроса ?sort=rating
