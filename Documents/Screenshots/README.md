# Cargo Shipment Tracker (Project 14)

This project is a Java-based Cargo Shipment Tracking system designed to model operations for a cargo company using OOP principles, custom exceptions, and collection-based management.

# Execution Log

The following table demonstrates the application's I/O flow based on the project requirements.

| Action | User Input | System Output / Result |
| :--- | :--- | :--- |
| **Initialize** | `Hızlı Kargo` | Displays Main Menu |
| **Reg. Standard** | `1`, `Mehmet`, `Ayşe`, `250`, `12` | Shipment registered. ID: 1 |
| **Reg. Express** | `2`, `Burak`, `Cem`, `50`, `5` | Shipment registered. ID: 2 |
| **Reg. SameDay** | `3`, `Deniz`, `Ece`, `15`, `3` | Shipment registered. ID: 3 |
| **Update Status** | `4`, `1`, `IN_TRANSIT` | Status updated: PENDING -> IN_TRANSIT |
| **Update Status** | `4`, `2`, `DELIVERED` | Invalid status transition: PENDING -> DELIVERED |
| **Update Status** | `4`, `1`, `DELIVERED` | Status updated: IN_TRANSIT -> DELIVERED |
| **List All** | `5` | Lists all shipments (ID 1, 2, 3) |
| **Find ID 2** | `6`, `2` | Displays details for ID 2 |
| **Find ID 9999** | `6`, `9999` | No shipment found with that ID. |
| **Sort by Cost** | `7`, `cost` | Displays list sorted high -> low |
| **Revenue Summary**| `8` | Shows Revenue & Insurance stats |
| **Exit** | `9` | Exiting. Goodbye! |

---

## Features
* **OOP Design:** Utilizes abstract classes (`Shipment`), interfaces (`Insurable`), and enums (`ShipmentStatus`).
* **State Machine:** Enforces strict lifecycle transitions (PENDING → IN_TRANSIT → DELIVERED/RETURNED) using custom exceptions (`InvalidStatusTransitionException`).
* **Efficient Lookup:** Uses `HashMap` for $O(1)$ shipment retrieval.
* **Sorting Strategies:** Implements multiple `Comparator` strategies for sorting by cost, distance, or status.
