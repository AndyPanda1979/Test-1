There are just test items results below...

000:
1) проверка на четность
2) реализации циклического буфера
3) сортировки

001:  (к скрипту папка templates с html и css)
Тестовое задание: сделать приложение: 
1) Сервер каждые 5 секунд генерирует число 
2) При проходе по адресу браузер получает страницу с предложением авторизации через GitHub (https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps),
   чтобы получать сообщения от сервера 
3) После авторизации браузер получает страницу, на которую через SSE поступают данные от сервера в реальном времени 
4) Если отменить авторизацию (logout) -> возвращаемся к п.1 
*** Для работы нужно получить ключи ( client_id, CLIENT_ID -> index.html, test3serve.py 
                                      CLIENT_SECRET -> test3serve.py

Для доделок: 
*Сделать привязку нескольких экземпляров сессии к одному экземпляру User 
*Доделать отправку cookies со сроком действия, чтобы после закрытия браузера, при повторном его открытии с восстановлением сессии, приложение понимало, что это известная ему сессия. 
*Добавить к регистрации новой сессии (класс User) отпечаток даты и период хранения как и у cookies, по истечении сроков хранения удалять экзепляр User и все его "хвосты" 

