# Cargo Shipment Tracker

> Object-Oriented Programming · Group Project 14
> Marmara University, Faculty of Engineering, Department of Electrical and Electronics Engineering · Spring 2025–2026

A console-based Java application that models a small Istanbul cargo company's daily operations. The system registers shipments of three priority classes (Standard, Express, Same-Day), advances each shipment through a strict lifecycle enforced by an enum-based state machine, refuses every illegal transition by throwing a typed custom exception, looks up shipments in **O(1)** by tracking ID via a `HashMap`, and exposes **three** interchangeable `Comparator` strategies for sorting daily planning reports.

The codebase is the academic deliverable for Group 14 of the EE1004 course (Project 14). It exercises every core OOP construct required by the project specification: an abstract base class, three concrete subclasses, an interface, an enum, a custom unchecked exception, multiple Comparator strategies, and dual `ArrayList`/`HashMap` storage in the aggregator class.

---

## Table of contents

1. [Quick start](#quick-start)
2. [Domain & scenario](#domain--scenario)
3. [Class architecture](#class-architecture)
4. [Pricing and insurance model](#pricing-and-insurance-model)
5. [Status state machine](#status-state-machine)
6. [Menu reference](#menu-reference)
7. [Sample run](#sample-run)
8. [Project structure](#project-structure)
9. [Design decisions](#design-decisions)
10. [Authors & contributions](#authors--contributions)
11. [Academic context](#academic-context)
12. [License](#license)

---

## Quick start

Requires **JDK 11** or later. No external dependencies — Java Standard Edition only.

```bash
git clone https://github.com/atabeyaydi/CargoShipmentTracker.git
cd CargoShipmentTracker
javac *.java
java Main
```

The application is fully interactive: it prompts for the company name, then presents a nine-option menu in a loop until the operator chooses **9. Exit**.

---

## Domain & scenario

Mehmet is an operations coordinator at a small but growing cargo company in Istanbul. Shipments arrive at the depot every morning with different priority classes, each with its own per-kilometre rate, its own weight cap, and its own insurance percentage. Once a shipment is registered, it follows a strict lifecycle. Mehmet wants a single tool that captures all of this cleanly, so he can stop relying on hand-written log sheets.

The system supports:

- Registering shipments of different priority classes with the right details for each
- Looking up any shipment instantly using its unique tracking ID
- Advancing a shipment through its lifecycle, with the system refusing any illegal transition
- Sorting shipments by different criteria (cost, distance, status) for daily planning reports
- Computing the total revenue and the total insurance liability across every shipment

---

## Class architecture

Nine classes, one per file, single (default) package:

| File | Role |
|---|---|
| `Shipment.java` | Abstract base class. Owns id, sender, recipient, distance, weight, status. Implements `Insurable`. |
| `StandardShipment.java` | 1.5 TL/km, max 30 kg, insurance 5 % of cost. |
| `ExpressShipment.java` | 3.0 TL/km, max 20 kg, insurance 8 % of cost. |
| `SameDayShipment.java` | 6.0 TL/km, max 10 kg, insurance 12 % of cost. |
| `Insurable.java` | Interface declaring `double getInsuranceCost()`. |
| `ShipmentStatus.java` | Enum + `canGoTo(...)` state-machine check. |
| `InvalidStatusTransitionException.java` | Custom unchecked exception thrown on illegal transitions. |
| `CargoCompany.java` | Aggregator: `ArrayList<Shipment>` + `HashMap<Integer, Shipment>` + three `Comparator`s. |
| `Main.java` | Entry point, menu loop, input validation. |

Full design discussion, UML class diagram, testing tables, and references are in
the [academic report](docs/EE1004_Group14_Project14_Report.pdf).

---

## Pricing and insurance model

| Type | Class | Rate (TL/km) | Max weight | Insurance |
|---|---|---:|---:|---:|
| Standard | `StandardShipment` | 1.5 | 30 kg | 5 % of cost |
| Express  | `ExpressShipment`  | 3.0 | 20 kg | 8 % of cost |
| Same-Day | `SameDayShipment`  | 6.0 | 10 kg | 12 % of cost |

Cost is always computed as `distance (km) × ratePerKm`. The insurance line item is a fixed percentage of the cost and is reported separately in the summary.

A registration attempt that exceeds the per-class weight cap is rejected with the exact message:

```
Weight exceeds capacity for this shipment class. Shipment not registered.
```

---

## Status state machine

```
        +-----------+      advance       +-------------+      advance       +-------------+
        |  PENDING  | -----------------> | IN_TRANSIT  | -----------------> |  DELIVERED  | (terminal)
        +-----------+                    +-------------+                    +-------------+
              |                                 |
              | advance                         | advance
              v                                 v
        +-----------+                     +-------------+
        |  RETURNED |                     |  RETURNED   |
        +-----------+                     +-------------+
          (terminal)                        (terminal)
```

Every other transition is illegal and raises:

```java
throw new InvalidStatusTransitionException(from, to);
// -> "Invalid status transition: <FROM> -> <TO>"
```

The `Main` menu wraps each `advanceStatus(...)` call in a `try/catch` and prints the exception message rather than letting the program crash.

---

## Menu reference

```
--- Cargo Menu ---
1. Register a standard shipment
2. Register an express shipment
3. Register a same-day shipment
4. Advance shipment status
5. List all shipments
6. Find shipment by ID
7. Sort shipments
8. Show revenue & insurance summary
9. Exit
```

Unknown options print `Unknown option. Skipping...` and the menu loops. The sort option accepts `cost`, `distance`, or `status`; any other criterion prints the same `Unknown option. Skipping...` message.

---

## Sample run

```
Enter company name: Hızlı Kargo

--- Cargo Menu ---
... (menu) ...
Choose an option (1-9): 1
Sender: Mehmet
Recipient: Ayşe
Distance (km): 250
Weight (kg): 12
Shipment registered. ID: 1

(register an express + a same-day in the same way ...)

Choose an option (1-9): 4
Enter shipment ID: 2
Enter new status (IN_TRANSIT/DELIVERED/RETURNED): DELIVERED
Invalid status transition: PENDING -> DELIVERED

Choose an option (1-9): 5
ID: 1 | [Standard] Mehmet -> Ayşe (250.0 km, 12.0 kg) | Cost: 375.00 TL | Status: DELIVERED
ID: 2 | [Express] Burak -> Cem (50.0 km, 5.0 kg) | Cost: 150.00 TL | Status: PENDING
ID: 3 | [SameDay] Deniz -> Ece (15.0 km, 3.0 kg) | Cost: 90.00 TL | Status: PENDING

Choose an option (1-9): 8
===== Revenue & Insurance Summary =====
Total Shipments: 3
Total Revenue: 615.00 TL
Total Insurance: 41.55 TL
(Standard: 18.75 + Express: 12.00 + SameDay: 10.80)
```

The full transcript is committed as `transcript.txt`.

---

## Project structure

```
.
├── README.md
├── README.txt
├── transcript.txt
├── Shipment.java
├── StandardShipment.java
├── ExpressShipment.java
├── SameDayShipment.java
├── Insurable.java
├── ShipmentStatus.java
├── InvalidStatusTransitionException.java
├── CargoCompany.java
└── Main.java
```

---

## Design decisions

- **Enum for status, not strings.** `ShipmentStatus` is type-safe; the transition switch is exhaustively checked at compile time. A typo'd string would slip through to runtime.
- **State machine co-located with the enum.** `ShipmentStatus.canGoTo(...)` keeps the transition rules in one file; adding a state means editing one file, not auditing every call site.
- **Unchecked custom exception.** `InvalidStatusTransitionException extends RuntimeException` so the violation can propagate to the menu loop that knows the user context, without forcing every method on the call stack to declare `throws`. The exception fields carry the exact states for any future logger.
- **HashMap alongside ArrayList.** The list preserves insertion order for listing and sorting; the map gives O(1) lookup by tracking ID, replacing the O(n) linear search the spec explicitly forbids.
- **Comparator lambdas, not Comparable.** `Comparable` allows only one natural ordering; three interchangeable strategies (cost ↓, distance ↓, status lifecycle order) are cleanly expressed as named `static final Comparator<Shipment>` constants — the Strategy pattern.
- **`Insurable` as a separate interface.** Insurance is a horizontal capability layered on top of the inheritance chain, not a feature of every base-class shape. Modelling it as an interface lets future shipment types opt in (or out) without restructuring the hierarchy.
- **Locale.US forced in `Main`.** All `printf("%.2f")` output uses a `.` decimal separator regardless of the host locale, which the automated grading scripts require.

---

## Authors & contributions

Group 14, Marmara University (Spring 2025–2026):

| Member | Student # | Primary contribution |
|---|---|---|
| Atabey Aydı | 150718503 | `ShipmentStatus` enum + `canGoTo` state machine, `InvalidStatusTransitionException` |
| Mehmet Açar | 150719020 | `Shipment` abstract base, static ID counter, `advanceStatus`, `toString` |
| İsmail Hanifi Nal | 150719025 | `StandardShipment`, `ExpressShipment`, `SameDayShipment`, `Insurable` interface |
| Abdulkadir Köroğlu | 150719695 | `CargoCompany`: `registerShipment`, `listAllShipments`, `findById`, totals |
| Burak Gökmen | 150720010 | `CargoCompany`: `listSortedBy` (three Comparator strategies), `summary` |
| Alperen Tufan Pelit | 150720012 | `Main` menu loop, input validation, test transcript, report compilation |

Each member committed from their own GitHub account; see the repository commit history for the full attribution.

**Supervision.** Course Lecturer: Assoc. Prof. Dr. Salih Bayar. Laboratory Assistant: Res. Asst. Salih Çolakoğlu.

---

## Academic context

This repository is the source-code deliverable for **Project 14 (Cargo Shipment Tracker)** of EE1004 Object-Oriented Programming at Marmara University, Spring 2025–2026 semester. The accompanying technical report (PDF) is submitted via Google Classroom and follows the Marmara University Institute of Pure and Applied Sciences thesis / Faculty of Engineering graduation-project format.

**Academic integrity.** All code in this repository is the original work of Group 14. External references (Java SE 11 documentation, Oracle tutorials, course slides) are cited in IEEE style in the report's References section. Any AI-assisted authoring was disclosed in the project report as required by the course policy.

---

## License

Released for academic evaluation as part of EE1004 (Marmara University). Re-use is permitted for educational reference with attribution to Group 14.
