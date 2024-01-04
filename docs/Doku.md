# TODOS
- [ ] Alle TODOs im Quelltext auflösen
- [ ] Wenn ein Chatteilnehmer abrupt stirbt, muss der gestorbene Chatteilnehmer aus der Routing Tabelle entfernt werden.
- [ ] Das Programm muss kompatibel mit allen anderen Programmen sein
- [ ] Das Programm muss im Labor laufen können
- [ ] Count to Infinity Problem muss vernünftig behoben sein
- [ ] Disconnect muss zuverlässig funktionieren
- [ ] Ein Verbindungsabbruch muss behandelt werden
- [ ] Wenn eine Verbindug aufgebaut wurde, muss sofort eine Routing Tabelle gesendet werden
- [x] Checksummen beim empfangen überprüfen
## nice to have
- [ ] einheitliche Fehlerbehandlung (Logger oder Exceptions, oder was?)
	- [ ] das Programm muss alle Arten von Fehlern fangen und dem User den Grund ausgeben. Darf aber nicht abstürzen. Die Fehlermeldungen mit Stacktrace müssen in eine Logdatei geschrieben werden.
- [ ] gute Logfiles schreiben
	- es gibt 2 Loglevel:
		1. Fehler
		2. Info
- [ ] Beim Verbindungsversuch mit einem nicht existierenden Client muss das behandelt werden, ohne dass das Programm abstürzt
- [ ] eine Möglichkeit zu überprüfen, wie gut es dem Programm geht
	- zum Beispiel messen wie lange es dauert, die Routing Tabelle rauszuschicken
- [ ] Die Namen der Json Keys in einer Seperaten Klasse oder Enum haben
	```java
	private static final String IP = "ip";
	private static final String ID_PORT = "id_port";
	```
- [ ] eine Validierungsmethode für Userinputs (mit regex :P)
- [ ] doppeltes connecten mit selbem user verhindern (allready connected to ...)
- [ ] Alerts wenn routing tabellen zu groß werden. oder allgemein allerts bei interessanten sachen

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