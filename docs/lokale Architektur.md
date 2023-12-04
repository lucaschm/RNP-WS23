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
## Routing Tabelle (Aufgaben)
- Message muss gesendet bzw. weitergeleitet werden: `String findNextHop(String destinationIP)`
	- next Hop mit minimalen Hop Count für die Zieladresse finden
- eine Routing Tabelle von einem anderen Netzwerkteilnehmer in eigene Routing Tabelle intigrieren: `mergeWith(Collection<RoutingEntry> routingEntries)`
- Eigene Routing Tabelle weitergeben. Dabei müssen wir Split Horizon einhalten: `getEntriesWithout(String originIP)`
# ConnectionHandler
- Kümmert sich um die Sockets
	- Auf und Abbau von Sockets
- Parallelität der Sockets serialisieren (Queue für Nachrichten)