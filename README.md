Operationen in O(1) ohne dass viel öffentlich ist und zerstört werden kann
Struktur nicht zerstörbar

Bei ListItem m_next / m_prev nicht dem Benutzer zur Verfügung

2 Listen zusammenfügen soll effizient möglich sein, ohne alles umkopieren


Bewertungskriterien: Design, Lesbarkeit, Effizienz, Sicherheit

# Concept

The provided LinkedList implementation was designed to be efficient as possible, therefore some features might be minimized (e.g. only LinkedLists are allowed as input list) and security was neglected sometimes. 

# Answers

**Welches sind die Vor- und Nachteile der LinkedList gegenüber der ArrayList bezüglich dem Einfügen und Löschen an beliebiger Stelle?**

Vorteile LinkedList:
* Belegt nicht unbenutzten Speicherplatz (dynamisch nicht halbdynamisch).
* Löschen und Einfügen an beliebiger Stelle in O(1) wenn man das Element kennt.

Nachteile LinkedList:
* Höherer Verwaltungsaufwand, also mehr Speicherbedarf pro Element.
* Löschen und Einfügen an beliebiger Stelle in O(n) wenn man das Daten Objekt, nicht aber das Element kennt.