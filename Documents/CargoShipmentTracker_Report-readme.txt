CARGO SHIPMENT TRACKER
================================================================================
EE1004 Object-Oriented Programming — Group Project (Project 14)
Marmara University
Faculty of Engineering
Department of Electrical and Electronics Engineering

Spring 2025–2026
June 2026

Group 14
Atabey Aydı (150718503) | Mehmet Açar (150719020) | İsmail Hanifi Nal (150719025)
Abdulkadir Köroğlu (150719695) | Burak Gökmen (150720010) | Alperen Tufan Pelit (150720012)

Supervision: Assoc. Prof. Dr. Salih Bayar (Course Lecturer)
               Res. Asst. Salih Çolakoğlu (Laboratory Assistant)

================================================================================
PROJECT OVERVIEW
================================================================================

This repository contains the complete, self-contained implementation of the Cargo
Shipment Tracker, a console-based Java application developed in fulfillment of the
EE1004 Object-Oriented Programming group project requirement (Project 14).

The application models a realistic logistics operations domain for a cargo company
operating from an Istanbul depot. It supports registration of three distinct shipment
classes (standard, express, and same-day), enforces a strict shipment lifecycle through
a finite-state machine, computes distance-based shipping costs polymorphically, and
maintains insurance liability records via an orthogonal interface capability. All
functional requirements (FR-01 through FR-09) and non-functional requirements (NFR-01
through NFR-08) mandated by the project specification are satisfied, including mandatory
HashMap-based O(1) lookup by tracking identifier, locale-correct numeric formatting,
and comprehensive input validation that prevents abnormal termination.

The design and implementation serve as a pedagogical exemplar of rigorous
object-oriented engineering at the undergraduate-to-graduate transition level,
exercising abstraction, inheritance, polymorphism, encapsulation, interface decoupling,
enumerated state machines, custom exception handling, the Strategy pattern, and the
Template Method pattern within a cohesive, production-quality codebase.

================================================================================
REPOSITORY STRUCTURE AND CONTENTS
================================================================================

The repository root comprises the following primary artifacts:

Source Implementation (flat package, nine .java files, Java SE 11 compatible):
  • Insurable.java
      Public interface declaring the getInsuranceCost() contract; implemented by the
      Shipment hierarchy to decouple insurance liability from core shipment semantics.
  • Shipment.java
      Abstract base class providing shared state (immutable tracking ID via static
      counter, sender/recipient, distance, weight, status), the Template Method
      getCost() implementation, polymorphic toString(), and delegation of status
      advancement to the enum state machine.
  • StandardShipment.java, ExpressShipment.java, SameDayShipment.java
      Concrete subclasses supplying private static final constants for per-kilometre
      rate, maximum weight capacity, and insurance percentage; each overrides the
      three abstract methods and getInsuranceCost().
  • ShipmentStatus.java
      Type-safe enumeration (PENDING, IN_TRANSIT, DELIVERED, RETURNED) embedding the
      canGoTo(ShipmentStatus) transition predicate that encodes the lifecycle
      state machine with exhaustive switch semantics and terminal-state semantics.
  • InvalidStatusTransitionException.java
      Unchecked RuntimeException subclass carrying the precise from/to states and
      emitting the specification-mandated message format.
  • CargoCompany.java
      Central aggregator employing dual storage (ArrayList<Shipment> for insertion
      order preservation; HashMap<Integer, Shipment> for mandated O(1) ID lookup),
      three static Comparator constants realizing the Strategy pattern (BY_COST_DESC,
      BY_DISTANCE_DESC, BY_STATUS_LIFECYCLE), weight-cap guard on registration, and
      revenue/insurance summary aggregation with per-class breakdown.
  • Main.java
      Application entry point implementing the interactive, menu-driven control loop
      with defensive parsing (try/catch around Integer.parseInt, Double.parseDouble,
      Enum.valueOf), two-arm exception handling around status advancement, and
      Locale.US enforcement for numeric output.

Documentation:
  • EE1004_Group14_Project14_Report.md
      GitHub-optimized Markdown version of the complete academic project report,
      containing UML diagrams (ASCII-rendered), design rationale tables, implementation
      excerpts, full console transcript, edge-case verification matrix, member
      contribution breakdown, and pre-submission compliance checklist.
  • EE1004_Group14_Project14_Report.pdf
      Authoritative, professionally typeset PDF report (22 pages) suitable for formal
      course submission; includes rendered UML class and state diagrams, formatted
      tables, and exact pagination.
  • readme.txt (this file)
      Descriptive technical summary of repository contents and project architecture,
      intended for rapid orientation by reviewers, collaborators, and archival purposes.

The codebase contains no external dependencies, magic numbers, or third-party libraries.
All constants are declared private static final within their owning classes. The
application has been verified against the specification’s sample input/output and an
extensive suite of edge cases (non-numeric input, weight violations, illegal transitions,
unknown menu options, non-existent IDs, locale variations).

================================================================================
KEY TECHNICAL AND DESIGN CHARACTERISTICS
================================================================================

Object-Oriented Architecture:
  The system is organized around a single-inheritance hierarchy rooted at the abstract
  Shipment class, which implements the Insurable interface. This arrangement
  demonstrates interface-based capability decoupling: insurance computation is treated
  as a horizontal, opt-in concern rather than being embedded in the base class, thereby
  preserving extensibility for future shipment categories that may elect not to
  participate in the insurance contract.

State Management:
  Lifecycle integrity is guaranteed by the ShipmentStatus enum, whose canGoTo method
  implements an exhaustive finite-state machine. Only the four documented transitions
  are permitted; all others result in immediate throwing of
  InvalidStatusTransitionException, whose message payload exactly matches the
  specification-mandated format. Terminal states (DELIVERED, RETURNED) are
  compile-time protected against further mutation.

Data Structures and Algorithms:
  CargoCompany maintains two synchronized collections: an ArrayList preserving
  chronological insertion order (required for list-all and sort operations) and a
  HashMap providing constant-time retrieval by tracking ID (explicitly required by
  NFR-07). Sorting is performed on defensive copies via interchangeable Comparator
  instances, illustrating the Strategy pattern and avoiding mutation of the primary
  insertion-order collection.

Robustness and Correctness:
  Every risky operation is wrapped in try/catch blocks. Numeric output is forced to
  Locale.US to guarantee decimal-point separators irrespective of the execution
  environment. The implementation produces byte-for-byte identical output to the
  specification’s worked example and passes all twelve enumerated edge-case scenarios.

================================================================================
BUILD, EXECUTION, AND VERIFICATION
================================================================================

Compilation (from repository root):
    javac *.java

Execution:
    java Main

The program first solicits a company name, then enters a persistent nine-option menu
loop. All user input is validated; malformed entries produce friendly diagnostic
messages and return control to the menu without program termination.

For complete interaction transcript, numeric verification calculations, and
edge-case test matrix, consult Section 5 and Appendix A of the accompanying project
report (both .md and .pdf versions).

================================================================================
ACADEMIC AND PEDAGOGICAL VALUE
================================================================================

This project constitutes a capstone demonstration of the four pillars of
object-oriented programming together with modern Java idioms (enums with behavior,
lambda-expressed Comparators, defensive copying, and precise exception modeling).
It satisfies every structural and behavioral mandate of the EE1004 specification
while maintaining conceptual clarity, extensibility, and strict adherence to
encapsulation and type safety. The dual-storage aggregator, enum-encoded state
machine, and interface-decoupled insurance mechanism are particularly illustrative
of design decisions that scale beyond toy examples toward production logistics or
enterprise systems.

The full design rationale, UML documentation, contribution attribution, and
self-assessment against the course’s OOP concept checklist are contained in the
project report documents included herein.

================================================================================
LICENSING AND ATTRIBUTION
================================================================================

This work is submitted in partial fulfillment of the academic requirements of
EE1004 Object-Oriented Programming, Marmara University, Spring 2025–2026. All
group members have reviewed and understand the complete codebase. External sources
are cited in the References section of the project report. No code was copied from
other student groups or uncited public repositories.

Repository location: https://github.com/atabeyaydi/CargoShipmentTracker

================================================================================
This readme.txt furnishes a concise yet technically rigorous orientation to the
contents and architecture of the repository. For exhaustive specification compliance
documentation, implementation walkthroughs, and verification artifacts, please refer
to EE1004_Group14_Project14_Report.pdf (primary submission document) and its Markdown
counterpart.

Last revised: June 2026
================================================================================