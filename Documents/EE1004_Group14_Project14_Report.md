# EE1004 – Object-Oriented Programming
## Group Project (Project 14)

**MARMARA UNIVERSITY**  
Faculty of Engineering  
Department of Electrical and Electronics Engineering

---

# Cargo Shipment Tracker
## An Object-Oriented Console Application in Java

**Group 14 – Members**

| Name                  | Student ID | Email                        |
|-----------------------|------------|------------------------------|
| Atabey Aydı           | 150718503  | atabeyaydi@marun.edu.tr      |
| Mehmet Açar           | 150719020  | mehmetacar19@marun.edu.tr    |
| İsmail Hanifi Nal     | 150719025  | ismailnal@marun.edu.tr       |
| Abdulkadir Köroğlu    | 150719695  | akoroglu@marun.edu.tr        |
| Burak Gökmen          | 150720010  | burak.gokmen@marun.edu.tr    |
| Alperen Tufan Pelit   | 150720012  | alperenpelit@marun.edu.tr    |

**Supervision**

- **Course Lecturer:** Assoc. Prof. Dr. Salih Bayar
- **Laboratory Assistant:** Res. Asst. Salih Çolakoğlu

**Spring 2025–2026 | June 2026**

---

## Abstract

This report presents the design and implementation of a *Cargo Shipment Tracker*, a console-based Java application developed for the EE1004 group project. The system models three shipment classes—standard, express, and same-day—using an abstract base class, three concrete subclasses, and a separate `Insurable` interface, exercising the four pillars of object-oriented programming together with interface-based capability decoupling.

Shipping cost is computed polymorphically as the product of distance (km) and a per-class per-kilometre rate; insurance liability is reported as a class-specific percentage of cost. The shipment life cycle (`PENDING` → `IN_TRANSIT` → `DELIVERED` or `RETURNED`; `PENDING` may also short-cut to `RETURNED`) is enforced by an enum-based state machine that raises a custom unchecked exception (`InvalidStatusTransitionException`) on every illegal transition.

A `CargoCompany` aggregator stores shipments in both an `ArrayList` (insertion order) and a `HashMap` (O(1) lookup by tracking ID, as mandated by the specification), and exposes three interchangeable `Comparator` sorting strategies (by cost, by distance, by status). The application is fully menu-driven, validates all user input, and produces locale-correct numeric output suitable for automated grading. Testing against the specification’s sample I/O confirms an exact match.

**Index Terms**—object-oriented programming, abstract class, interface, inheritance, polymorphism, encapsulation, enum state machine, custom exception, Java collections, HashMap, Comparator, Strategy pattern.

---

## Table of Contents

1. [Introduction](#1-introduction)
   - 1.1 [Project Overview](#11-project-overview)
   - 1.2 [Scenario](#12-scenario)
   - 1.3 [Objectives](#13-objectives)
   - 1.4 [Development Environment](#14-development-environment)
2. [Requirements Analysis](#2-requirements-analysis)
   - 2.1 [Functional Requirements](#21-functional-requirements)
   - 2.2 [Non-Functional Requirements](#22-non-functional-requirements)
3. [System Design](#3-system-design)
   - 3.1 [Class Architecture](#31-class-architecture)
   - 3.2 [Class Responsibilities and Pricing](#32-class-responsibilities-and-pricing)
   - 3.3 [OOP Pillars and Interface Decoupling](#33-oop-pillars-and-interface-decoupling)
   - 3.4 [Design Rationale](#34-design-rationale)
   - 3.5 [Shipment Life-Cycle State Machine](#35-shipment-life-cycle-state-machine)
4. [Implementation](#4-implementation)
   - 4.1 [Interface and Abstract Class](#41-interface-and-abstract-class)
   - 4.2 [Enum State Machine](#42-enum-state-machine)
   - 4.3 [Custom Exception](#43-custom-exception)
   - 4.4 [Aggregator with Dual Storage and Comparator Strategies](#44-aggregator-with-dual-storage-and-comparator-strategies)
   - 4.5 [Menu-Driven Main Loop](#45-menu-driven-main-loop)
5. [Testing and Results](#5-testing-and-results)
   - 5.1 [Specification Walkthrough](#51-specification-walkthrough)
   - 5.2 [Numeric Verification](#52-numeric-verification)
   - 5.3 [Edge-Case Test Results](#53-edge-case-test-results)
6. [Limitations and Future Work](#6-limitations-and-future-work)
   - 6.1 [Current Limitations](#61-current-limitations)
   - 6.2 [Potential Extensions](#62-potential-extensions)
7. [Conclusion](#7-conclusion)
8. [References](#references)
9. [Appendices](#appendices)
   - A. [Console Transcript (Full Run)](#a-console-transcript-full-run)
   - B. [Member Contributions](#b-member-contributions)
   - C. [OOP Concept Self-Assessment](#c-oop-concept-self-assessment)
   - D. [Pre-Submission Checklist](#d-pre-submission-checklist)
   - E. [GitHub Repository Information](#e-github-repository-information)
   - F. [AI-Use Disclosure](#f-ai-use-disclosure)

---

# 1 Introduction

## 1.1 Project Overview

This project implements a *Cargo Shipment Tracker*: a command-line Java program that registers cargo shipments at an Istanbul depot, tracks each shipment through a strict delivery life cycle, computes shipping cost from a distance-based tariff, and lets the operator search, list, sort, and summarise the daily roster.

The application is designed as a teaching vehicle for object-oriented design and therefore exercises every core OOP construct required by the EE1004 project specification [1]:

- an abstract base class
- three concrete subclasses
- an interface
- a Java enum
- a custom exception
- multiple `Comparator` strategies
- `HashMap`-based fast lookup

## 1.2 Scenario

The customer for the system is Mehmet, an operations coordinator at a small but growing cargo company in Istanbul. Each morning shipments arrive at the depot with different priority classes—standard, express, and same-day—each with its own per-kilometre rate, its own weight cap, and its own insurance percentage.

Once a shipment is registered it follows a strict lifecycle: it starts as `PENDING`, moves to `IN_TRANSIT` when the driver picks it up, and ends as either `DELIVERED` or `RETURNED`. Mehmet wants a Java application that captures all of this cleanly so that he can stop relying on hand-written log sheets.

## 1.3 Objectives

The concrete objectives of the project are:

- to model a realistic logistics domain with a clean class hierarchy and an interface for the cross-cutting insurance concern;
- to demonstrate abstraction, inheritance, polymorphism, encapsulation, and interface-based capability decoupling in a single coherent codebase;
- to enforce business rules (status transitions, weight caps) with type-safe constructs (an enum state machine and a custom unchecked exception);
- to provide efficient data access using the Java collections framework (mandatory `HashMap` for O(1) lookup by tracking ID); and
- to deliver a robust, menu-driven interface that never crashes on bad input and emits locale-correct output for automated grading.

## 1.4 Development Environment

Table 1 summarises the environment used to build and run the application.

**Table 1: Development environment**

| Item                | Value                              |
|---------------------|------------------------------------|
| Language            | Java (OpenJDK 11 compatible)       |
| Source files        | 9 `.java` files (single package)   |
| Build command       | `javac *.java`                     |
| Run command         | `java Main`                        |
| External libraries  | None (Java Standard Edition only)  |
| Character encoding  | UTF-8                              |
| Numeric locale      | `Locale.US` (decimal point)        |

---

# 2 Requirements Analysis

## 2.1 Functional Requirements

Table 2 lists the functional requirements (FR) the application fulfils. These correspond directly to the nine main-menu options demanded by the specification.

**Table 2: Functional requirements**

| ID    | Requirement |
|-------|-------------|
| FR-01 | Register a new shipment of type standard, express, or same-day, capturing sender, recipient, distance in km, and weight in kg. |
| FR-02 | Assign every shipment a unique, sequential integer tracking ID automatically (static counter shared by all subclasses). |
| FR-03 | Refuse registration when `weightKg` exceeds the per-class cap; print the exact message *"Weight exceeds capacity for this shipment class. Shipment not registered."* and do not store the object. |
| FR-04 | Advance a shipment’s status, permitting only the legal life-cycle transitions; raise `InvalidStatusTransitionException` on any illegal request. |
| FR-05 | List all registered shipments in insertion order (polymorphic `toString()`). |
| FR-06 | Find a single shipment by its tracking ID using `HashMap.get()` (no linear search). When found, also display its insurance line. |
| FR-07 | Sort and display shipments by cost (descending), distance (descending), or status (lifecycle order: pending first, terminal states last). |
| FR-08 | Display a revenue & insurance summary giving the total number of shipments, total revenue, total insurance, and a per-class insurance breakdown. |
| FR-09 | Exit the application cleanly on operator request, printing *"Exiting. Goodbye!"*. |

## 2.2 Non-Functional Requirements

Table 3 lists the non-functional requirements (NFR), most of which are imposed verbatim by the project specification and are checked by the auto-grader.

**Table 3: Non-functional requirements**

| ID    | Requirement |
|-------|-------------|
| NFR-01 | The program must never terminate abnormally on invalid input. |
| NFR-02 | All risky parsing operations (`Double.parseDouble`, `Integer.parseInt`, `Enum.valueOf`) are wrapped in `try/catch` blocks; a friendly skip message is printed on failure. |
| NFR-03 | Unknown menu options print *"Unknown option. Skipping..."* and the menu loops. |
| NFR-04 | The custom exception message format is fixed: `Invalid status transition: <FROM> -> <TO>`. |
| NFR-05 | Numeric output uses `Locale.US` so the decimal separator is always a point, regardless of the host locale. |
| NFR-06 | No magic numbers: per-km rates, weight caps, and insurance percentages are `private static final` constants in their owning subclass. |
| NFR-07 | `findById(int)` **MUST** use `HashMap.get`; a linear scan over the list is forbidden. |
| NFR-08 | The `Insurable` interface, the `ShipmentStatus` enum, and the `InvalidStatusTransitionException` class each live in their own `.java` file. |

---

# 3 System Design

## 3.1 Class Architecture

The system is organised around an abstract `Shipment` base class that implements an `Insurable` interface, three concrete subclasses (`StandardShipment`, `ExpressShipment`, `SameDayShipment`) with class-specific constants, a status enum carrying the state-machine check, a custom unchecked exception, a `CargoCompany` aggregator/manager, and the `Main` entry-point class.

**Figure 1: UML class diagram of the full system (nine classes, single package).**

```
+------------------------------------------------------------+
|                    <<interface>>                           |
|                       Insurable                            |
+------------------------------------------------------------+
| + getInsuranceCost() : double                              |
+------------------------------------------------------------+
                           ^
                           | implements
+------------------------------------------------------------+
|                   <<abstract>>                             |
|                       Shipment                             |
+------------------------------------------------------------+
| - {static} counter     : int = 0                           |
| # id                   : int                               |
| # sender, recipient    : String                            |
| # distanceKm, weightKg : double                            |
| # status               : ShipmentStatus = PENDING          |
+------------------------------------------------------------+
| + Shipment(sender, recipient, distanceKm, weightKg)        |
| + {abstract} getRatePerKm()      : double                  |
| + {abstract} getMaxWeightKg()    : double                  |
| + {abstract} typeLabel()         : String                  |
| + getCost()                      : double                  |
| + advanceStatus(next : ShipmentStatus) : void              |
| + toString()                     : String                  |
| + getters: id, sender, recipient, distanceKm, weightKg,    |
|            status                                          |
+------------------------------------------------------------+
                           ^
            +--------------+--------------+
            |              |              |
+--------------------+ +--------------------+ +-----------------------+
| StandardShipment   | | ExpressShipment    | | SameDayShipment       |
+--------------------+ +--------------------+ +-----------------------+
| -RATE_PER_KM  1.5  | | -RATE_PER_KM  3.0  | | -RATE_PER_KM  6.0     |
| -MAX_WEIGHT_KG 30  | | -MAX_WEIGHT_KG 20  | | -MAX_WEIGHT_KG 10     |
| -INSURANCE_PCT 0.05| | -INSURANCE_PCT 0.08| | -INSURANCE_PCT 0.12   |
+--------------------+ +--------------------+ +-----------------------+
| +getRatePerKm()    | | +getRatePerKm()    | | +getRatePerKm()       |
| +getMaxWeightKg()  | | +getMaxWeightKg()  | | +getMaxWeightKg()     |
| +typeLabel()       | | +typeLabel()       | | +typeLabel()          |
| +getInsuranceCost()| | +getInsuranceCost()| | +getInsuranceCost()   |
+--------------------+ +--------------------+ +-----------------------+

+----------------------------------------------------------------------+
| CargoCompany (aggregator)                                            |
+----------------------------------------------------------------------+
| - companyName     : String                                           |
| - shipments       : ArrayList<Shipment>   (insertion order)          |
| - shipmentById    : HashMap<Integer,Shipment> (O(1) lookup)          |
| + {static} BY_COST_DESC          : Comparator<Shipment>              |
| + {static} BY_DISTANCE_DESC      : Comparator<Shipment>              |
| + {static} BY_STATUS_LIFECYCLE   : Comparator<Shipment>              |
+----------------------------------------------------------------------+
| + CargoCompany(companyName : String)                                 |
| + registerShipment(s : Shipment) : void   [weight-cap guard]         |
| + findById(id : int)             : Shipment  [HashMap.get]           |
| + listAllShipments()             : void                              |
| + listSortedBy(criterion : String) : void                            |
| + getTotalRevenue()              : double                            |
| + getTotalInsurance()            : double                            |
| + summary()                      : void                              |
+----------------------------------------------------------------------+
                           ^
                           | uses (1 company ---- * shipments)
+----------------------------------------------------------------------+
| Main                                                                 |
+----------------------------------------------------------------------+
| + {static} main(args : String[]) : void                              |
|   -- reads company name, loops over 9-option menu,                   |
|      wraps advanceStatus(...) in try/catch,                          |
|      prints "Unknown option. Skipping..." for unknown choices        |
+----------------------------------------------------------------------+
```

*(Note: The original report contains a professionally rendered UML diagram. The ASCII representation above preserves the essential structure for GitHub viewing. A high-resolution version is available in the accompanying PDF report.)*

## 3.2 Class Responsibilities and Pricing

`Shipment` (abstract) holds the shared state (tracking ID, sender, recipient, distance, weight, status) and the shared concrete behaviour (cost formula, status-validated update, string rendering). It declares the abstract methods `getRatePerKm()`, `getMaxWeightKg()`, and `typeLabel()` that each subclass must implement; it implements `Insurable` but leaves `getInsuranceCost()` abstract so that each subclass applies its own percentage.

`StandardShipment`, `ExpressShipment`, `SameDayShipment` each supply their own per-kilometre rate, weight cap, and insurance percentage as `private static final` constants, and override `getInsuranceCost()` accordingly.

**Table 4: Per-class pricing, capacity, and insurance**

| Type      | Class              | Rate (TL/km) | Max weight | Insurance    |
|-----------|--------------------|--------------|------------|--------------|
| Standard  | `StandardShipment` | 1.5          | 30 kg      | 5 % of cost  |
| Express   | `ExpressShipment`  | 3.0          | 20 kg      | 8 % of cost  |
| Same-Day  | `SameDayShipment`  | 6.0          | 10 kg      | 12 % of cost |

`CargoCompany` is the aggregator/manager: it holds the shipment collections (an `ArrayList` for insertion order and a `HashMap` keyed by tracking ID for O(1) lookup) and exposes the full create–read–update surface along with three named static `Comparator` constants. `Main` is the entry point and user-interface class; it owns the menu loop and the input-validation `try/catch` blocks, and wraps every call to `advanceStatus(...)` in a `try/catch` so that the typed exception’s message is printed verbatim without terminating the program.

## 3.3 OOP Pillars and Interface Decoupling

Table 5 maps each OOP principle to its concrete realisation in the code.

**Table 5: OOP principle mapping**

| Principle          | Class / method                                      | Explanation |
|--------------------|-----------------------------------------------------|-----------|
| **Abstraction**    | `Shipment` (abstract); `getRatePerKm()`, `getMaxWeightKg()`, `typeLabel()`, `getInsuranceCost()` (abstract) | `Shipment` cannot be instantiated; it defines the interface each subclass must fulfil without specifying how. |
| **Inheritance**    | `StandardShipment`, `ExpressShipment`, `SameDayShipment` each `extends Shipment` | All three subclasses reuse the constructor, the static ID counter, `advanceStatus()`, `toString()`, and `getCost()`, adding only their specific rate, weight cap, and insurance percentage. |
| **Polymorphism**   | `ArrayList<Shipment>`; overridden `getRatePerKm()`, `getInsuranceCost()`, `typeLabel()` | A single loop over `ArrayList<Shipment>` automatically invokes the correct override for each concrete type. |
| **Encapsulation**  | `private`/`protected` fields; `private static final` constants; `advanceStatus()` | No external code can set an invalid weight, bypass the state machine, or mutate the tracking ID after construction. |
| **Interface decoupling** | `Insurable` interface implemented by `Shipment` and overridden by every subclass | Insurance is modelled as a horizontal capability layered on top of the inheritance chain; future shipment types can opt in (or out) without restructuring the hierarchy. |

## 3.4 Design Rationale

Table 6 records the key design decisions and the reasoning behind them.

**Table 6: Design rationale for key decisions**

| Decision | Alternative | Rationale |
|----------|-------------|-----------|
| Use an `enum` for shipment status | Plain `String` constants | An enum gives compile-time type safety; `canGoTo(...)` switches exhaustively over the constants; a string would let any typo slip through at runtime. |
| Encode transitions inside the enum (`canGoTo`) | A separate validator class | Co-locating rules with states keeps them consistent; adding a state later requires editing only one file. |
| Custom `InvalidStatusTransitionException` (unchecked) | Return a `boolean`; or a checked exception | An unchecked exception lets the violation propagate to the menu loop that knows the user context, without forcing every method on the call stack to declare `throws`; its fields carry the exact `from`/`to` states. |
| `HashMap` alongside `ArrayList` | `ArrayList` alone with linear search | The list preserves insertion order for listing and sorting; the map gives O(1) lookup for find-by-ID and status-advance, which the specification mandates explicitly. |
| Three named `Comparator` lambdas | Implement `Comparable` | `Comparable` supports only one ordering; three interchangeable strategies (cost ↓, distance ↓, status lifecycle) are cleanly expressed as separate static constants—the Strategy pattern. |
| Separate `Insurable` interface | A concrete `getInsuranceCost()` in the base class | The interface makes insurance an opt-in capability; a future `UninsuredBulkShipment` could extend `Shipment` without inheriting the insurance contract. |

## 3.5 Shipment Life-Cycle State Machine

Figure 2 shows the permitted status transitions. A shipment starts in `PENDING`; the legal moves are `PENDING` → `IN_TRANSIT` or `PENDING` → `RETURNED`, then `IN_TRANSIT` → `DELIVERED` or `IN_TRANSIT` → `RETURNED`. `DELIVERED` and `RETURNED` are terminal: every other transition raises `InvalidStatusTransitionException`.

**Figure 2: Shipment life-cycle state diagram.**

```
          PENDING
             |
             | (or short-cut)
             v
        IN_TRANSIT
       /          \
      v            v
  DELIVERED     RETURNED
 (terminal)    (terminal)
```

Any transition not shown raises `InvalidStatusTransitionException` with the exact message `"Invalid status transition: <FROM> -> <TO>"`.

*(A professionally rendered version of this diagram appears in the PDF report.)*

---

# 4 Implementation

Full source code (nine `.java` files) is available in the project repository (see Appendix E). This section presents the key excerpts that demonstrate how each required OOP concept is realised.

## 4.1 Interface and Abstract Class

```java
// Listing 1: Insurable interface (one file, one method)
public interface Insurable {
    double getInsuranceCost();
}
```

```java
// Listing 2: Shipment abstract base class (key parts)
public abstract class Shipment implements Insurable {
    private static int counter = 0;          // shared static ID counter

    protected final int id;
    protected final String sender, recipient;
    protected final double distanceKm, weightKg;
    protected ShipmentStatus status = ShipmentStatus.PENDING;

    protected Shipment(String sender, String recipient,
                       double distanceKm, double weightKg) {
        this.id          = ++counter;        // unique sequential ID
        this.sender      = sender;
        this.recipient   = recipient;
        this.distanceKm  = distanceKm;
        this.weightKg    = weightKg;
    }

    // Subclass-supplied constants
    public abstract double getRatePerKm();
    public abstract double getMaxWeightKg();
    public abstract String typeLabel();      // "Standard"/"Express"/"SameDay"

    // Shared cost formula -- Template Method
    public double getCost() { return distanceKm * getRatePerKm(); }

    // Status update delegates to the enum state machine
    public void advanceStatus(ShipmentStatus next) {
        if (!status.canGoTo(next))
            throw new InvalidStatusTransitionException(status, next);
        this.status = next;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
            "ID: %d | [%s] %s -> %s (%.1f km, %.1f kg) | Cost: %.2f TL | Status: %s",
            id, typeLabel(), sender, recipient,
            distanceKm, weightKg, getCost(), status);
    }
}
```

The pattern `distanceKm * getRatePerKm()` is an application of the **Template Method** design pattern: the algorithm skeleton is fixed in the base class, while the per-km rate is supplied by each concrete subclass.

```java
// Listing 3: SameDayShipment – every subclass follows the same shape
public class SameDayShipment extends Shipment {
    private static final double RATE_PER_KM     = 6.0;
    private static final double MAX_WEIGHT_KG   = 10.0;
    private static final double INSURANCE_PCT   = 0.12;

    public SameDayShipment(String sender, String recipient,
                           double distanceKm, double weightKg) {
        super(sender, recipient, distanceKm, weightKg);
    }

    @Override public double getRatePerKm()      { return RATE_PER_KM; }
    @Override public double getMaxWeightKg()    { return MAX_WEIGHT_KG; }
    @Override public String typeLabel()         { return "SameDay"; }
    @Override public double getInsuranceCost()  { return getCost() * INSURANCE_PCT; }
}
```

## 4.2 Enum State Machine

```java
// Listing 4: ShipmentStatus enum with embedded canGoTo
public enum ShipmentStatus {
    PENDING, IN_TRANSIT, DELIVERED, RETURNED;

    public boolean canGoTo(ShipmentStatus next) {
        if (next == null) return false;
        switch (this) {
            case PENDING:
                return next == IN_TRANSIT || next == RETURNED;
            case IN_TRANSIT:
                return next == DELIVERED || next == RETURNED;
            case DELIVERED:
            case RETURNED:
            default:
                return false;   // terminal states
        }
    }
}
```

The enum constant itself is the switch subject, so the compiler guarantees that every constant is considered. Terminal states fall through to `default` and always return `false`, correctly preventing any “resurrection” of a finished shipment.

## 4.3 Custom Exception

```java
// Listing 5: InvalidStatusTransitionException (exact message format)
public class InvalidStatusTransitionException extends RuntimeException {
    private final ShipmentStatus fromStatus, toStatus;

    public InvalidStatusTransitionException(ShipmentStatus from,
                                            ShipmentStatus to) {
        super("Invalid status transition: " + from + " -> " + to);
        this.fromStatus = from;
        this.toStatus   = to;
    }

    public ShipmentStatus getFromStatus() { return fromStatus; }
    public ShipmentStatus getToStatus()   { return toStatus; }
}
```

Extending `RuntimeException` means callers need not declare `throws`. In `Main` the exception is caught at the menu boundary so the program continues:

```java
try {
    ShipmentStatus next = ShipmentStatus.valueOf(st);
    s.advanceStatus(next);
    System.out.println("Status updated. ID " + id + ": " + prev + " -> " + next);
} catch (InvalidStatusTransitionException ex) {
    System.out.println(ex.getMessage());
} catch (IllegalArgumentException ex) {   // unknown enum name
    System.out.println("Unknown status. Skipping...");
}
```

## 4.4 Aggregator with Dual Storage and Comparator Strategies

```java
// Listing 6: CargoCompany – dual storage, three Comparator strategies, summary
public class CargoCompany {
    private final ArrayList<Shipment> shipments
            = new ArrayList<>();
    private final HashMap<Integer, Shipment> shipmentById
            = new HashMap<>();

    public static final Comparator<Shipment> BY_COST_DESC =
            (a, b) -> Double.compare(b.getCost(), a.getCost());

    public static final Comparator<Shipment> BY_DISTANCE_DESC =
            (a, b) -> Double.compare(b.getDistanceKm(), a.getDistanceKm());

    public static final Comparator<Shipment> BY_STATUS_LIFECYCLE =
            Comparator.comparingInt(s -> s.getStatus().ordinal());

    public void registerShipment(Shipment s) {
        if (s.getWeightKg() > s.getMaxWeightKg()) {
            System.out.println(
                "Weight exceeds capacity for this shipment class. Shipment not registered.");
            return;
        }
        shipments.add(s);
        shipmentById.put(s.getId(), s);   // both updated atomically
        System.out.println("Shipment registered. ID: " + s.getId());
    }

    public Shipment findById(int id) { return shipmentById.get(id); } // O(1)

    public void listSortedBy(String criterion) {
        Comparator<Shipment> cmp; String header;
        switch (criterion.trim().toLowerCase(Locale.US)) {
            case "cost":
                cmp = BY_COST_DESC;
                header = "===== Sorted by Cost (high -> low) ====="; break;
            case "distance":
                cmp = BY_DISTANCE_DESC;
                header = "===== Sorted by Distance (high -> low) ====="; break;
            case "status":
                cmp = BY_STATUS_LIFECYCLE;
                header = "===== Sorted by Status (PENDING -> RETURNED) ====="; break;
            default:
                System.out.println("Unknown option. Skipping..."); return;
        }
        List<Shipment> copy = new ArrayList<>(shipments); // defensive copy
        copy.sort(cmp);
        System.out.println(header);
        for (Shipment s : copy) System.out.println(s);
    }

    public void summary() {
        double stdIns = 0, expIns = 0, sdIns = 0;
        for (Shipment s : shipments) {
            if (s instanceof StandardShipment)      stdIns += s.getInsuranceCost();
            else if (s instanceof ExpressShipment)  expIns += s.getInsuranceCost();
            else if (s instanceof SameDayShipment)  sdIns  += s.getInsuranceCost();
        }
        System.out.println("===== Revenue & Insurance Summary =====");
        System.out.println("Total Shipments: " + shipments.size());
        System.out.printf(Locale.US, "Total Revenue: %.2f TL%n", getTotalRevenue());
        System.out.printf(Locale.US, "Total Insurance: %.2f TL%n", getTotalInsurance());
        System.out.printf(Locale.US,
            "(Standard: %.2f + Express: %.2f + SameDay: %.2f)%n",
            stdIns, expIns, sdIns);
    }
}
```

Every `registerShipment()` call updates both structures atomically, keeping them consistent. `listSortedBy()` sorts a defensive copy so the original insertion order in the list is preserved.

## 4.5 Menu-Driven Main Loop

`Main.main()` runs a `while (running)` loop, reads one line per iteration, and dispatches to the matching `CargoCompany` method via a `switch`. Unknown options print *"Unknown option. Skipping..."* (NFR-03) and the loop continues; the loop exits cleanly on option 9 with *"Exiting. Goodbye!"*.

Numeric parsing is guarded by `try/catch` (`NumberFormatException`) and the status-advance call is wrapped in the two-arm catch shown above. The first line of `main()` calls `Locale.setDefault(Locale.US)` so that every `%.2f` output uses a point as decimal separator regardless of the host locale.

---

# 5 Testing and Results

## 5.1 Specification Walkthrough

The application was tested against the exact sample I/O given in the project specification. With the inputs “Hızlı Kargo” (company name), three registrations (Standard 250 km / 12 kg, Express 50 km / 5 kg, Same-Day 15 km / 3 kg), one legal status advance (ID 1 to `IN_TRANSIT`), one illegal status advance (ID 2 `PENDING` → `DELIVERED`), and the lookup of a non-existent ID (9999), the produced output matches the specification line-for-line.

The cost-sort produces:

```
375.00 TL   >   150.00 TL   >   90.00 TL
Standard         Express         Same-Day
```

and the summary line reads:

```
Total Revenue: 615.00 TL, Total Insurance: 41.55 TL
(Standard: 18.75 + Express: 12.00 + SameDay: 10.80)
```

The full captured transcript is reproduced verbatim in Appendix A.

## 5.2 Numeric Verification

```
CostStd = 250 × 1.5 = 375.00 TL
CostExp = 50 × 3.0  = 150.00 TL
CostSD  = 15 × 6.0  = 90.00 TL

Total Revenue   = 375.00 + 150.00 + 90.00 = 615.00 TL

InsuranceStd = 0.05 × 375.00 = 18.75 TL
InsuranceExp = 0.08 × 150.00 = 12.00 TL
InsuranceSD  = 0.12 × 90.00  = 10.80 TL

Total Insurance = 18.75 + 12.00 + 10.80 = 41.55 TL
```

## 5.3 Edge-Case Test Results

Table 7 reports the edge-case tests executed in addition to the specification walkthrough.

**Table 7: Edge-case test results**

| ID   | Scenario / input                                      | Expected behaviour                                      | Result |
|------|-------------------------------------------------------|---------------------------------------------------------|--------|
| EC-01 | Register Same-Day with weight 11 kg (> 10 cap)       | "Weight exceeds capacity..." message; not stored        | ✓ Pass |
| EC-02 | Non-numeric weight (`abc`)                            | `NumberFormatException` caught; "Invalid number..."     | ✓ Pass |
| EC-03 | Non-numeric tracking ID at lookup                     | "Invalid ID. Skipping..."; menu loops                   | ✓ Pass |
| EC-04 | Lookup non-existent ID (9999)                         | `findById` returns `null`; "No shipment found..."       | ✓ Pass |
| EC-05 | Illegal transition `PENDING` → `DELIVERED`            | Exception caught; "Invalid status transition: ..."      | ✓ Pass |
| EC-06 | Illegal transition from terminal `DELIVERED`          | Exception caught; program continues                     | ✓ Pass |
| EC-07 | Unknown enum name as new status                       | "Unknown status. Skipping..."                           | ✓ Pass |
| EC-08 | Unknown main-menu option (e.g. `x`)                   | "Unknown option. Skipping..."; loop continues           | ✓ Pass |
| EC-09 | Unknown sort criterion (`weight`)                     | "Unknown option. Skipping..." in `listSortedBy`         | ✓ Pass |
| EC-10 | Sort does not alter underlying list order             | Insertion order restored on list-all                    | ✓ Pass |
| EC-11 | Sequential auto IDs (3 registrations)                 | IDs 1, 2, 3 (single static counter)                     | ✓ Pass |
| EC-12 | Locale correctness with TR host locale                | Decimal separator stays `.` (`Locale.US` forced)        | ✓ Pass |

---

# 6 Limitations and Future Work

## 6.1 Current Limitations

- **No persistence.** Data is held in memory and lost on exit.
- **Console-only interface.** There is no graphical user interface (an optional GUI is offered as a bonus by the specification).
- **Single session, single user.** Concurrent access is unsupported.
- **No date/time tracking.** Status transitions are not timestamped.
- **No cancellation.** A registered shipment cannot be removed.
- **No search by sender or recipient** beyond manual iteration.

## 6.2 Potential Extensions

- Persistence via object serialisation or a SQLite/JDBC database.
- Graphical interface using Swing (`JTable`, dialogs); an optional GUI earns bonus marks per the specification.
- Multi-depot support as suggested by the project scenario.
- Billing module that ingests the summary line items.
- Real-time tracking integrating GPS pings into the status update path.
- Timestamps using `java.time.LocalDateTime`.

---

# 7 Conclusion

This project delivered a fully functional, console-based *Cargo Shipment Tracker* in Java, demonstrating every OOP concept required for EE1004 Project 14.

The abstract class and inheritance hierarchy separate shared state and behaviour from type-specific tariffs, so a fourth shipment type could be added without touching the aggregator or the menu. The `Insurable` interface keeps the insurance contract orthogonal to the inheritance chain. Polymorphic dispatch on `getRatePerKm()`, `getInsuranceCost()`, and `typeLabel()` lets the rest of the program treat all shipments uniformly while each type behaves correctly, and encapsulation keeps every object in a consistent state.

The enum state machine showed that Java enums can carry methods and enforce invariants typo-proof at compile time; the custom exception showed how typed exceptions propagate context-rich errors without burdening intermediate layers; the dual `ArrayList` + `HashMap` design illustrated the classic space–time trade-off mandated by the specification; and the three `Comparator` strategies showed how the Strategy pattern turns behaviour into a first-class value.

The team gained hands-on experience with the core Java OOP constructs, the collections API, lambda expressions, the `Insurable` interface idiom, and the robustness requirements enforced by the auto-grader.

---

# References

[1] Marmara University, Faculty of Engineering, Dept. of EEE, “EE1004 — Java Programming: Project Report Guideline, Group Project, Spring 2025–2026,” Marmara University, Istanbul, 2026.

[2] ——, “EE1004 — Java Programming: OOP Study Guide, Spring 2025–2026,” Marmara University, Istanbul, 2026.

[3] Oracle Corporation, “Java Platform, Standard Edition 11 API Specification,” 2018. [Online]. Available: https://docs.oracle.com/en/java/. [Accessed: June 2026].

[4] B. Eckel, *Thinking in Java*, 4th ed. Upper Saddle River, NJ: Prentice Hall, 2006.

[5] J. Bloch, *Effective Java*, 3rd ed. Boston, MA: Addison-Wesley, 2018.

[6] E. Gamma, R. Helm, R. Johnson, and J. Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*. Reading, MA: Addison-Wesley, 1994.

[7] Oracle Corporation, “Enum Types — The Java Tutorials,” 2024. [Online]. Available: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html. [Accessed: June 2026].

[8] ——, “The Comparator Interface — The Java Tutorials,” 2024. [Online]. Available: https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html. [Accessed: June 2026].

---

# Appendices

## A Console Transcript (Full Run)

The transcript below was captured by running `java Main` under OpenJDK 11 with the inputs supplied by the specification’s worked example (“Hızlı Kargo” company, three shipments, three status advances, two find queries, one cost-sort, the revenue/insurance summary, and exit).

<details>
<summary><strong>Click to expand full console transcript (very long)</strong></summary>

```text
Enter company name: Hızlı Kargo

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
Choose an option (1-9): 1
Sender: Mehmet
Recipient: Ayşe
Distance (km): 250
Weight (kg): 12
Shipment registered. ID: 1

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
Choose an option (1-9): 2
Sender: Burak
Recipient: Cem
Distance (km): 50
Weight (kg): 5
Shipment registered. ID: 2

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
Choose an option (1-9): 3
Sender: Deniz
Recipient: Ece
Distance (km): 15
Weight (kg): 3
Shipment registered. ID: 3

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
Choose an option (1-9): 4
Enter shipment ID: 1
Enter new status (IN_TRANSIT/DELIVERED/RETURNED): IN_TRANSIT
Status updated. ID 1: PENDING -> IN_TRANSIT

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
Choose an option (1-9): 4
Enter shipment ID: 2
Enter new status (IN_TRANSIT/DELIVERED/RETURNED): DELIVERED
Invalid status transition: PENDING -> DELIVERED

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
Choose an option (1-9): 4
Enter shipment ID: 1
Enter new status (IN_TRANSIT/DELIVERED/RETURNED): DELIVERED
Status updated. ID 1: IN_TRANSIT -> DELIVERED

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
Choose an option (1-9): 5
ID: 1 | [Standard] Mehmet -> Ayşe (250.0 km, 12.0 kg) | Cost: 375.00 TL | Status: DELIVERED
ID: 2 | [Express] Burak -> Cem (50.0 km, 5.0 kg) | Cost: 150.00 TL | Status: PENDING
ID: 3 | [SameDay] Deniz -> Ece (15.0 km, 3.0 kg) | Cost: 90.00 TL | Status: PENDING

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
Choose an option (1-9): 6
Enter shipment ID: 2
ID: 2 | [Express] Burak -> Cem (50.0 km, 5.0 kg) | Cost: 150.00 TL | Status: PENDING
Insurance: 12.00 TL

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
Choose an option (1-9): 6
Enter shipment ID: 9999
No shipment found with that ID.

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
Choose an option (1-9): 7
Sort by (cost/distance/status): cost
===== Sorted by Cost (high -> low) =====
ID: 1 | [Standard] Mehmet -> Ayşe (250.0 km, 12.0 kg) | Cost: 375.00 TL | Status: DELIVERED
ID: 2 | [Express] Burak -> Cem (50.0 km, 5.0 kg) | Cost: 150.00 TL | Status: PENDING
ID: 3 | [SameDay] Deniz -> Ece (15.0 km, 3.0 kg) | Cost: 90.00 TL | Status: PENDING

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
Choose an option (1-9): 8
===== Revenue & Insurance Summary =====
Total Shipments: 3
Total Revenue: 615.00 TL
Total Insurance: 41.55 TL
(Standard: 18.75 + Express: 12.00 + SameDay: 10.80)

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
Choose an option (1-9): 9
Exiting. Goodbye!
```
</details>

## B Member Contributions

**Table 8: Member contributions (signed by every group member)**

| Member                | Student ID | Components implemented                                                                 | Approx. |
|-----------------------|------------|----------------------------------------------------------------------------------------|---------|
| Atabey Aydı           | 150718503  | `ShipmentStatus` enum and `canGoTo(...)` state machine; `InvalidStatusTransitionException` with the exact spec-mandated message. | ≈17 % |
| Mehmet Açar           | 150719020  | `Shipment` abstract base class, the static ID counter, `advanceStatus(...)`, and the spec-format `toString()`. | ≈17 % |
| İsmail Hanifi Nal     | 150719025  | `StandardShipment`, `ExpressShipment`, `SameDayShipment` subclasses with their rate/cap/insurance constants; the `Insurable` interface. | ≈17 % |
| Abdulkadir Köroğlu    | 150719695  | `CargoCompany`: `registerShipment` weight-cap guard, `listAllShipments`, `findById` (`HashMap` path), `getTotalRevenue`/`getTotalInsurance`. | ≈17 % |
| Burak Gökmen          | 150720010  | `CargoCompany`: `listSortedBy` with the three `Comparator` strategies (cost / distance / status), and the `summary()` revenue & insurance breakdown. | ≈17 % |
| Alperen Tufan Pelit   | 150720012  | `Main` menu loop, input validation (`try/catch` around every parse), the two-arm catch for status advance, the captured console transcript, and report compilation. | ≈17 % |

All members reviewed the complete codebase, contributed to debugging and testing, and committed from their own GitHub accounts (see the repository commit history).

## C OOP Concept Self-Assessment

**Table 9: OOP concept self-assessment**

| Status | Requirement                                      | Location |
|--------|--------------------------------------------------|----------|
| ✓      | Abstract class with ≥3 concrete subclasses       | `Shipment`; `Standard`/`Express`/`SameDayShipment` |
| ✓      | Interface implemented by every subclass          | `Insurable` / `getInsuranceCost()` |
| ✓      | Polymorphic, overridden `toString()` for printing| `Shipment.toString()` dispatched on `typeLabel()` |
| ✓      | Aggregator class with an `ArrayList` of objects  | `CargoCompany.shipments` |
| ✓      | `HashMap` for O(1) lookup (mandatory)            | `CargoCompany.shipmentById` |
| ✓      | Constants as `private static final` (no magic numbers) | `RATE_PER_KM`, `MAX_WEIGHT_KG`, `INSURANCE_PCT` |
| ✓      | Robust error handling (no crashes)               | `try/catch` in advance/find/parse paths; default switch branches |
| ✓      | `enum` status with state machine                 | `ShipmentStatus.canGoTo()` |
| ✓      | Custom exception (unchecked)                     | `InvalidStatusTransitionException` |
| ✓      | Three named `Comparator` strategies              | `cost`, `distance`, `status` constants in `CargoCompany` |
| ✓      | Static ID counter                                | `counter` in `Shipment` |
| ✓      | `@Override` on every overriding method           | all overrides in subclasses |
| ✓      | Locale-correct numeric formatting                | `Locale.US` in `toString()` and `summary()` |

## D Pre-Submission Checklist

**Table 10: Pre-submission checklist**

| Item | Done? |
|------|-------|
| Cover page complete (members, IDs, project number, instructors, date) | ✓ |
| Abstract and keywords included | ✓ |
| Table of contents included | ✓ |
| UML class diagram present and matches the code | ✓ |
| OOP-pillars + interface decoupling table completed | ✓ |
| Testing section with spec walkthrough and edge-case table | ✓ |
| Console transcript included verbatim (Appendix A) | ✓ |
| References in IEEE style; all sources cited | ✓ |
| GitHub repository public; link and commit hash stated (Appendix E) | ☐ *(to be completed before final submission)* |
| Source `.zip` includes all 9 `.java` files | ✓ |
| Contribution table completed and signed by every member | ✓ |
| AI-use disclosure included | ✓ |
| Report exported to PDF | ✓ |

## E GitHub Repository Information

**Action required before submission:** complete the fields below with the group’s actual repository URL and the commit hash of the submitted version. Verify the link in a private/incognito window before submitting.

**Table 11: GitHub repository information**

| Field                  | Value |
|------------------------|-------|
| Repository URL         | https://github.com/atabeyaydi/CargoShipmentTracker |
| Commit hash (40 chars) | `[PASTE FULL COMMIT HASH HERE]` |
| Branch                 | `main` |
| Verified in incognito window | ☐ |

**Build and run (for the grader):** `javac *.java` then `java Main`, supplying the console inputs shown in Appendix A.

## F AI-Use Disclosure

In accordance with the academic-integrity policy of the EE1004 Project Report Guideline [1], the group discloses the following:

- **Tools used:** An AI programming assistant (Grok) was used during the development and report-writing phases.

- **Purpose:** Drafting the report scaffolding (structure, table layouts), reviewing the enum state machine and custom-exception design, proof-reading the implementation chapter, and assisting with conversion of the report to Markdown format for the GitHub repository.

- **Team understanding:** Every member has reviewed, discussed, and understands every line of submitted code, and is prepared to explain their assigned component in an individual viva.

- **Attribution:** All external sources are cited in the References; no code was copied from other groups or uncited web sources.

---

*End of Report*

**Note for GitHub:** This Markdown version is provided for easy viewing and collaboration on GitHub. For the official formatted submission, please refer to the accompanying PDF (`EE1004_Group14_Project14_Report.pdf`) which contains the professionally typeset layout, embedded UML diagrams, and exact pagination required by the course guidelines.
