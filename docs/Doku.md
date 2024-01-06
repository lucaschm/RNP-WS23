# TODOS

- [ ] Alle TODOs im Quelltext auflösen
- [X] Wenn ein Chatteilnehmer abrupt stirbt, muss der gestorbene Chatteilnehmer aus der Routing Tabelle entfernt werden.
- [ ] Das Programm muss kompatibel mit allen anderen Programmen sein
- [ ] Das Programm muss im Labor laufen können
- [ ] Count to Infinity Problem muss vernünftig behoben sein
- [X] Disconnect muss zuverlässig funktionieren auch bei doppelten verbindungen
- [X] Ein Verbindungsabbruch muss behandelt werden
- [ ] Wenn eine Verbindug aufgebaut wurde, muss sofort eine Routing Tabelle gesendet werden
- [X] Checksummen beim empfangen überprüfen
- [ ] ttl impl
- [X] merge with muss mitbekommen ob jemand eine verbindung verloren hat
- [ ] lehne doppelte verbindungen ab
- [ ] time triggerd mit sleep arbeiten

## Bugs

- [ ] Connection mit sich selbst
- [ ] Wenn sich ein Client mit einem Server verbindet hat der Client den Server in seiner `list` der Verbindungen. Der Server hat den Client nicht in seiner `list`. Der Server sollte den Client ebenfalls in seiner `list` haben
  - [ ] 11:58: Server kann auch keine Nachricht an Client schicken
- [ ] kein Feedback beim `disconnect` (keine Bestätigung)
- [ ] Man kann sich mehrfach mit dem selben Node verbinden. Doppeltes connecten sollte verhindert werden (allready connected to ...)
- [ ] Bug um 11:51: 1111 hat mehrfach verbindung mit 2222 aufgebaut. list bei 2222 ist leer. 2222 macht disconnect mit 2222 (sich selbst). 2222 steht jetzt in der list von 2222. Ganz komisches Verhalten...
  - [ ] dieser Listeneintrag mit sich selbst geht auch nicht mehr weg, wenn sich 1111 disconnected
- [ ] 11:55: 1111 steht nicht in der list von 2222. 2222 kann sich nicht von 1111 disconnecten. 1111 ist aber mit 2222 verbunden.
- [ ] Beim Verbindungsversuch mit einem nicht existierenden Client muss das behandelt werden, ohne dass das Programm abstürzt
  - [ ] auf der Konsole wird noch der Stacktrace ausgegeben. Hier reicht es kurz das Timeout zu erwähnen
- [ ] beim Verbindungsaufbau mit einem nicht existierenden User kann sich nicht verbunden werden. Da gibt es auch ein Timeout. Allerdings landet der nicht existierende User in der Routing Tabelle (ist zumindest in der list Ausgabe)
- [ ] wenn man sich doppelt mit dem selben Teilnehmer verbindet wird scheinbar ein zweiter Socket und key angelegt. Die Routing Tabelle hat trotzdem nur einen Eintrag für diesen Teilnehmer. Es muss also nur noch verhindert werden, dass zwei Sockets für die selbe Verbindung gespeichert werden.
- [ ] Wenn man sich versucht mit einem nicht existierenden Teilnehmer zu verbinden wird trotzdem ein Routingeintrag erstellt.
## nice to have
- [ ] Die Tabelle sollte nur aktualisiert werden, wenn der Nachbar eine neue abgeänderte Routingnachricht geschickt hat.
- [ ] einheitliche Fehlerbehandlung (Logger oder Exceptions, oder was?)
  - [ ] das Programm muss alle Arten von Fehlern fangen und dem User den Grund ausgeben. Darf aber nicht abstürzen. Die Fehlermeldungen mit Stacktrace müssen in eine Logdatei geschrieben werden.
- [ ] gute Logfiles schreiben
  - [X] allgemeine Infos
  - [ ] Fehler
  - [X] Entwicklung der Routing Tabelle
  - [X] Wie Routing Tabellen an die Nachbarn geteilt werden
  - [ ] Buffer Log
  - [ ] Log von Nachrichten
  - [X] User Log
- [ ] eine Möglichkeit zu überprüfen, wie gut es dem Programm geht
  - zum Beispiel messen wie lange es dauert, die Routing Tabelle rauszuschicken
- [ ] Die Namen der Json Keys in einer Seperaten Klasse oder Enum haben
  ```java
  private static final String IP = "ip";
  private static final String ID_PORT = "id_port";
  ```
- [ ] eine Validierungsmethode für Userinputs (mit regex :P)
- [ ] Alerts wenn routing tabellen zu groß werden. oder allgemein allerts bei interessanten sachen
- [ ] feedback wenn eine Nachricht gesendet wurde
- [ ] check ob port schon benutzt wird beim start
- [ ] farbliche konsole der wichtigen sachen

# Config

Es gibt eine Klasse Config.java für konfigurationen

## lokale IP-Adresse

wenn localIPAdress = null, dann wird versucht die lokale IP-Adresse zu ermitteln
Es kann vorkommen, dass eine falsche Adresse ermittelt wird.
In diesem Fall kann man die Adresse hier manuell setzen.

```java
public static String localIPAdress = null;
```

# das UI unserer Chatanwendung

```
connect <IP> <Port>
```

```
send <IP> <Port> ???message???
```

```
disconnect <IP> <Port>
```

```
list
```

```
exit
```

# lokale Architektur

## UI

- Zeilen printen und einlesen

## Controller

- main und superloop

## MessageInterpreter

- Interpretiert die Nachricht und Entscheidet, was als nächstes gemacht werden muss
- Prüfen ob eine Nachricht für uns ist

## Router

- hat die Routing-Tabelle
- entscheidet die Wegewahl
- Kümemert sich darum, dass die Nachrichten an die richtige stelle gesendet werden

### Routing Tabelle (Aufgaben)

- Message muss gesendet bzw. weitergeleitet werden: `String findNextHop(String destinationIP)`
  - next Hop mit minimalen Hop Count für die Zieladresse finden
- eine Routing Tabelle von einem anderen Netzwerkteilnehmer in eigene Routing Tabelle intigrieren: `mergeWith(Collection<RoutingEntry> routingEntries, String origin)`
- Eigene Routing Tabelle weitergeben. Dabei müssen wir Split Horizon einhalten: `getEntriesWithout(String originIP)`

## ConnectionHandler

- Kümmert sich um die Sockets
  - Auf und Abbau von Sockets
- Parallelität der Sockets serialisieren (Queue für Nachrichten)
