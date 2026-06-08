# Cargo Shipment Tracker (Project 14)

This project is a Java-based Cargo Shipment Tracking system designed to model operations for a cargo company using OOP principles, custom exceptions, and collection-based management.

## Example Execution Log

The following table demonstrates the interaction flow of the application.

| Input Sequence | System Output |
| :--- | :--- |

| `Hızlı Kargo`<br>`1` (Register Standard)<br>`Mehmet`<br>`Ayşe`<br>`250`<br>`12`<br>`2` (Register Express)<br>`Burak`<br>`Cem`<br>`50`<br>`5`<br>`3` (Register Same-Day)<br>`Deniz`<br>`Ece`<br>`15`<br>`3`<br>`4`<br>`1` (Update ID 1)<br>`IN_TRANSIT`<br>`4`<br>`2` (Update ID 2)<br>`DELIVERED`<br>`4`<br>`1` (Update ID 1)<br>`DELIVERED`<br>`5` (List)<br>`6` (Find ID 2)<br>`6` (Find ID 9999)<br>`7` (Sort Cost)<br>`cost`<br>`8` (Summary)<br>`9` (Exit) | **Enter company name:** Hızlı Kargo<br>--- Cargo Menu ---<br>...<br>Choose an option (1-9): 1<br>Sender: Mehmet<br>Recipient: Ayşe<br>Distance (km): 250<br>Weight (kg): 12<br>Shipment registered. ID: 1<br><br>...<br>Choose an option (1-9): 4<br>Enter shipment ID: 2<br>Enter new status: DELIVERED<br>**Invalid status transition: PENDING -> DELIVERED**<br><br>...<br>Choose an option (1-9): 7<br>Sort by (cost/distance/status): cost<br>===== Sorted by Cost (high -> low) =====<br>ID: 1 \| [Standard] Mehmet -> Ayşe \| Cost: 375.00 TL<br>ID: 2 \| [Express] Burak -> Cem \| Cost: 150.00 TL<br>ID: 3 \| [SameDay] Deniz -> Ece \| Cost: 90.00 TL<br><br>...<br>Choose an option (1-9): 9<br>**Exiting. Goodbye!** |

---

## Features
* **OOP Design:** Utilizes abstract classes (`Shipment`), interfaces (`Insurable`), and enums (`ShipmentStatus`).
* **State Machine:** Enforces strict lifecycle transitions (PENDING → IN_TRANSIT → DELIVERED/RETURNED) using custom exceptions (`InvalidStatusTransitionException`).
* **Efficient Lookup:** Uses `HashMap` for $O(1)$ shipment retrieval.
* **Sorting Strategies:** Implements multiple `Comparator` strategies for sorting by cost, distance, or status.
