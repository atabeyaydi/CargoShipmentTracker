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
| Input | Result |
| :--- | :--- |
| `Hızlı Kargo` | Enter company name: Hızlı Kargo |
| `1` | --- Cargo Menu --- <br> 1. Register a standard shipment <br> 2. Register an express shipment <br> 3. Register a same-day shipment <br> 4. Advance shipment status <br> 5. List all shipments <br> 6. Find shipment by ID <br> 7. Sort shipments <br> 8. Show revenue & insurance summary <br> 9. Exit <br> Choose an option (1-9): 1 <br> Sender: Mehmet <br> Recipient: Ayşe <br> Distance (km): 250 <br> Weight (kg): 12 <br> Shipment registered. ID: 1 |
| `2` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 2 <br> Sender: Burak <br> Recipient: Cem <br> Distance (km): 50 <br> Weight (kg): 5 <br> Shipment registered. ID: 2 |
| `3` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 3 <br> Sender: Deniz <br> Recipient: Ece <br> Distance (km): 15 <br> Weight (kg): 3 <br> Shipment registered. ID: 3 |
| `4` <br> `1` <br> `IN_TRANSIT` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 4 <br> Enter shipment ID: 1 <br> Enter new status (IN_TRANSIT/DELIVERED/RETURNED): IN_TRANSIT <br> Status updated. ID 1: PENDING -> IN_TRANSIT |
| `4` <br> `2` <br> `DELIVERED` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 4 <br> Enter shipment ID: 2 <br> Enter new status (IN_TRANSIT/DELIVERED/RETURNED): DELIVERED <br> Invalid status transition: PENDING -> DELIVERED |
| `4` <br> `1` <br> `DELIVERED` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 4 <br> Enter shipment ID: 1 <br> Enter new status (IN_TRANSIT/DELIVERED/RETURNED): DELIVERED <br> Status updated. ID 1: IN_TRANSIT -> DELIVERED |

| `5` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 5 <br> ID: 1 \| [Standard] Mehmet -> Ayşe (250.0 km, 12.0 kg) \| Cost: 375.00 TL \| Status: DELIVERED <br> ID: 2 \| [Express] Burak -> Cem (50.0 km, 5.0 kg) \| Cost: 150.00 TL \| Status: PENDING <br> ID: 3 \| [SameDay] Deniz -> Ece (15.0 km, 3.0 kg) \| Cost: 90.00 TL \| Status: PENDING |
| `6` <br> `2` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 6 <br> Enter shipment ID: 2 <br> ID: 2 \| [Express] Burak -> Cem (50.0 km, 5.0 kg) \| Cost: 150.00 TL \| Status: PENDING \| Insurance: 12.00 TL |
| `6` <br> `9999` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 6 <br> Enter shipment ID: 9999 <br> No shipment found with that ID. |
| `7` <br> `cost` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 7 <br> Sort by (cost/distance/status): cost <br> ===== Sorted by Cost (high -> low) ===== <br> ID: 1 \| [Standard] Mehmet -> Ayşe (250.0 km, 12.0 kg) \| Cost: 375.00 TL \| Status: DELIVERED <br> ID: 2 \| [Express] Burak -> Cem (50.0 km, 5.0 kg) \| Cost: 150.00 TL \| Status: PENDING <br> ID: 3 \| [SameDay] Deniz -> Ece (15.0 km, 3.0 kg) \| Cost: 90.00 TL \| Status: PENDING |
| `8` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 8 <br> ===== Revenue & Insurance Summary ===== <br> Total Shipments: 3 <br> Total Revenue: 615.00 TL <br> Total Insurance: 41.55 TL (Standard: 18.75 + Express: 12.00 + SameDay: 10.80) |
| `9` | --- Cargo Menu --- <br> ... <br> Choose an option (1-9): 9 <br> Exiting. Goodbye! |

---

## Features
* **OOP Design:** Utilizes abstract classes (`Shipment`), interfaces (`Insurable`), and enums (`ShipmentStatus`).
* **State Machine:** Enforces strict lifecycle transitions (PENDING → IN_TRANSIT → DELIVERED/RETURNED) using custom exceptions (`InvalidStatusTransitionException`).
* **Efficient Lookup:** Uses `HashMap` for $O(1)$ shipment retrieval.
* **Sorting Strategies:** Implements multiple `Comparator` strategies for sorting by cost, distance, or status.
