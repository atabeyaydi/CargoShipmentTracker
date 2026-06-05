EE1004 — Project 14 — Cargo Shipment Tracker
================================================

Group 14, Marmara University, Faculty of Engineering, Dept. of EEE
Spring 2025–2026

Build:
    javac *.java
Run:
    java Main

The application is a console-only Java program. It registers cargo shipments
of three priority classes (Standard, Express, Same-Day), advances them through
a strict PENDING -> IN_TRANSIT -> DELIVERED/RETURNED lifecycle enforced by an
enum-based state machine, refuses every illegal transition by throwing a
custom InvalidStatusTransitionException, looks up shipments in O(1) via a
HashMap, and exposes three Comparator strategies for sorting (by cost, by
distance, by status).

Files (9 .java, single package):
    Shipment.java                       (abstract base, implements Insurable)
    StandardShipment.java               (1.5 TL/km, max 30 kg, insurance 5 %)
    ExpressShipment.java                (3.0 TL/km, max 20 kg, insurance 8 %)
    SameDayShipment.java                (6.0 TL/km, max 10 kg, insurance 12 %)
    Insurable.java                      (interface: getInsuranceCost())
    ShipmentStatus.java                 (enum + canGoTo state machine)
    InvalidStatusTransitionException.java
    CargoCompany.java                   (ArrayList + HashMap + Comparators)
    Main.java                           (menu loop, input validation)

Example I/O reproducing the project specification exactly is included in the
report appendix ("Console Transcript"). The grading transcript was captured
on OpenJDK 11.

Locale.US is forced in Main so the decimal separator in printf("%.2f")
output is always a point, regardless of the host locale.
