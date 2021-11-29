1.Opis
restapi jest aplikacją sieciową służącą do rezerwacji miejsc na parkingu.
2.Budowa
Aby zbudować aplikację potrzebne jest środowisko korzystające z języka java 11 lub nowsza u języka maven
3.Uruchamianie
Aby uruchomić należy uruchomić skrypt RestapiApplication w środowisku programistycznym
4.Kommendy
aplikacja domyślnie korzysta z portu 8080 wieć adress w sieci na której uruchomiono aplikację to localhost:8080 a w innej ip:8080

GET adress
lista komend

GET adress/users 
lista uzytkownikow 

GET adress/places 
lista miejsc parkingowych 

GET adress/rezerwacje 
lista rezerwacje 

GET adress/freeplaces 
lista wolnych miejsc 

GET adress/usersplaces?name=x 
x-nazwa uzytkownika daje liste miejsc zajetych przez uzytkownika 

GET adress/reserve?reserve=x_X 
x-nazwa uzytkownika 
X-nazwa miejsca dodaje rezerwacje dodaje też użytkownika jeżeli to pierwsza rezerwacja
zwraca informacje o tym czy akcja się powiodła

GET adress/unreserve?unreserve=x_X 
x-nazwa uzytkownika 
X-nazwa miejsca usuwa rezerwacje usuwa też użytkownika jeżeli jest to jedyna rezerwacja
zwraca informacje o tym czy akcja się powiodła