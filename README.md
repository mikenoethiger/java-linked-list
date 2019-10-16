# Aufgabenstellung

Im Selbststudium zu Algorithmen und Datenstrukturen 2 (ALGD2) soll eine LinkedList Implementation erstellt werden, die vom Design der LinkedList im Java Collections Framework abweicht.
Die genaue Aufgabenstellung ist im Dokument `Uebung3.pdf` beschrieben (liegt im selben Verzeichnis wie dieses README).

Zusammenfassung der Aufgabenstellung:

Wichtige Operationen in O(1) ohne dass viel �ffentlich ist und zerst�rt werden kann.
* Struktur nicht zerst�rbar.
* Bei ListItem m_next / m_prev nicht dem Benutzer zur Verf�gung stellen.
* Zwei Listen zusammenf�gen soll effizient m�glich sein, ohne alles umkopieren.
* Direkt Zugriff Anfang/Ende (Lesen/Schreiben/Einf�gen/L�schen) => O(1).
* Einf�gen/L�schen innerhalb der Liste => O(1), falls ListItem bekannt.
* Suchen: => O(n).

Bewertungskriterien: Design, Lesbarkeit, Effizienz, Sicherheit.

# Contributors

* Samuel Keusch
* Marco Waldmeier
* Mike N�thiger

# Antworten zum Aufgabenblatt

**Welches sind die Vor- und Nachteile der LinkedList gegenu�ber der ArrayList bez�glich dem Einf�gen und L�schen an beliebiger Stelle?**

Vorteile LinkedList:
* Belegt nicht unbenutzten Speicherplatz (dynamisch nicht halbdynamisch).
* L�schen und Einfügen an beliebiger Stelle in O(1) wenn man das Element kennt.

Nachteile LinkedList:
* Gr�sserer Verwaltungsaufwand, also mehr Speicherbedarf pro Element.
* L�schen und Einf�gen an beliebiger Stelle in O(n) wenn man das Daten Objekt, nicht aber das Element kennt.

# Efficiency

The provided LinkedList implementation was designed to be efficient as possible, therefore some features might be minimized (e.g. only LinkedLists are allowed as input list). 

# Security

A ListItem is guaranteed to be only member of one DLinkedList instance. This is acquired via the m_owner member. Each method in DLinkedList which accepts a ListItem therefore checks if m_owner == this. If this is not the case, the requested operation is denied.