Operationen in O(1) ohne dass viel öffentlich ist und zersört werden kann
Struktur nicht zerstörbar

* Bei ListItem m_next / m_prev nicht dem Benutzer zur Verfügung stellen
* 2 Listen zusammenfügen soll effizient möglich sein, ohne alles umkopieren
* direkt Zugriff Anfang/Ende (Lesen/Schreiben/Einfügen/Löschen) => O(1)
* Einfügen/Löschen innerhalb der Liste => O(1), falls ListItem bekannt.
* Suchen: => O(n)


Bewertungskriterien: Design, Lesbarkeit, Effizienz, Sicherheit

# Concept

The provided LinkedList implementation was designed to be efficient as possible, therefore some features might be minimized (e.g. only LinkedLists are allowed as input list) and security was neglected sometimes. 

# Security
A ListItem is guaranteed to be only member of one DLinkedList instance. This is acquired via the m_owner member. Each method in DLinkedList which accepts a ListItem therefore checks if m_owner == this. If this is not the case, the requested operation is denied.

# Answers

**Welches sind die Vor- und Nachteile der LinkedList gegenuüber der ArrayList bezüglich dem Einfügen und Löschen an beliebiger Stelle?**

Vorteile LinkedList:
* Belegt nicht unbenutzten Speicherplatz (dynamisch nicht halbdynamisch).
* Löschen und EinfÃ¼gen an beliebiger Stelle in O(1) wenn man das Element kennt.

Nachteile LinkedList:
* Grösserer Verwaltungsaufwand, also mehr Speicherbedarf pro Element.
* Löschen und Einfügen an beliebiger Stelle in O(n) wenn man das Daten Objekt, nicht aber das Element kennt.

