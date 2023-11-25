- Es wird Time-Trigered gearbeitet. Außer der ConnectionHandler, der hat einen Thread Pool.

# UI
- Zeilen printen und einlesen
# Controller
- main und superloop
# MessageInterpreter
- Interpretiert die Nachricht und Entscheidet, was als nächstes gemacht werden muss
- Prüfen ob eine Nachricht für uns ist
# Router
- hat die Routing-Tabelle
- entscheidet die Wegewahl
# ConnectionHandler
- Kümmert sich um die Sockets
	- Auf und Abbau von Sockets
- Parallelität der Sockets serialisieren (Queue für Nachrichten)