# TODOS

- [ ] Alle TODOs im Quelltext auflösen
- [X] Wenn ein Chatteilnehmer abrupt stirbt, muss der gestorbene Chatteilnehmer aus der Routing Tabelle entfernt werden.
- [ ] Das Programm muss kompatibel mit allen anderen Programmen sein
- [ ] Das Programm muss im Labor laufen können
- [X] Count to Infinity Problem muss vernünftig behoben sein
- [X] Disconnect muss zuverlässig funktionieren auch bei doppelten verbindungen
- [X] Ein Verbindungsabbruch muss behandelt werden
- [X] Wenn eine Verbindug aufgebaut wurde, muss sofort eine Routing Tabelle gesendet werden
- [X] Checksummen beim empfangen überprüfen
- [X] ttl impl
- [X] merge with muss mitbekommen ob jemand eine verbindung verloren hat
- [ ] lehne doppelte verbindungen ab
- [X] time triggerd mit sleep arbeiten
- [ ] exit implementieren
- [X] validieren, ob ein json-string im richtigen Format ist
- [ ] irgendetwas stimmt mit key.isValid() nicht. Weil Felix wird immer automatisch disconnected

## Bugs

- [X] Wenn jemand einen falschen json-string sendet kommt bei uns eine Nullpointerexception. (Wenn zum Beispiel in einer Json Nachricht ein erwarteter String nicht vorhanden ist)
- [ ] Wenn man eine nachricht sendet, ohne dass man mit dieser Person verbunden ist, gibt es eine Nullpointerexception
- [X] Connection mit sich selbst
- [X] Wenn sich ein Client mit einem Server verbindet hat der Client den Server in seiner `list` der Verbindungen. Der Server hat den Client nicht in seiner `list`. Der Server sollte den Client ebenfalls in seiner `list` haben
  - [X] 11:58: Server kann auch keine Nachricht an Client schicken
- [X] kein Feedback beim `disconnect` (keine Bestätigung)
- [ ] Es können Verbindungen mehrfach mit den selben Teilnehmern aufgebaut werden. Es muss bemerkt werden, wenn eine Verbindung redundant ist. Diese sollte dann automatisch wieder geschlossen werden.
- [X] Client kann sich mit sich selbst disconnecten. In diesem Fall wird der Routing-Eintrag für sich selbst aus der Routing-Tabelle entfernt - das ist nicht gut. Disconnect mit sich selbst sollte von der Routing-Tabelle verhindert werden.
- [X] Beim Verbindungsversuch mit einem nicht existierenden Client muss das behandelt werden, ohne dass das Programm abstürzt
  - [X] auf der Konsole wird noch der Stacktrace ausgegeben. Hier reicht es kurz das Timeout zu erwähnen
- [X] beim Verbindungsaufbau mit einem nicht existierenden User kann sich nicht verbunden werden. Da gibt es auch ein Timeout. Allerdings landet der nicht existierende User in der Routing Tabelle (ist zumindest in der list Ausgabe)
- [X] ui geht bei input "disconnect 192.168.50.34 4444list" kaputt
- [ ] send an nicht vorhandenen port gibt nullpointer exception im router
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
- [X] eine Validierungsmethode für Userinputs (mit regex :P)
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
> Beispiel mit Adresse 192.168.0.2:1111
- für jeden Befehl gibt es mehrere Möglichkeiten ihn aufzurufen
### connect
```
connect 192.168.0.2 1111
connect 192.168.0.2:1111
c 192.168.0.2 1111
c 192.168.0.2:1111
```

### send
```
send 192.168.0.2 1111 hey du
send 192.168.0.2:1111 hey du
s 192.168.0.2 1111 hey du
s 192.168.0.2:1111 hey du
```

### disconnect
```
disconnect 192.168.0.2 1111
disconnect 192.168.0.2:1111
d 192.168.0.2 1111
d 192.168.0.2:1111
```

### list
```
list
l
```
### exit
```
exit
e
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
