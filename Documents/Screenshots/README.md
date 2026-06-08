# Cargo Shipment Tracker (Project 14)

A professional Java-based Cargo Shipment Tracking system designed to model operations for a cargo company using Object-Oriented Programming (OOP) principles, custom exceptions, and collection-based management.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [System Architecture](#system-architecture)
- [Usage Guide](#usage-guide)
- [Example Execution](#example-execution)
- [Technical Details](#technical-details)
- [Requirements](#requirements)
- [Installation](#installation)
- [How to Run](#how-to-run)

---

## 🎯 Overview

The **Cargo Shipment Tracker** is a comprehensive system that manages cargo shipments for a logistics company. It supports multiple shipment types (Standard, Express, Same-Day), enforces strict state transitions, and provides efficient shipment lookup and sorting capabilities.

**Key Capabilities:**
- Register shipments with different service levels
- Track shipment status through a defined lifecycle
- Sort shipments by multiple criteria (cost, distance, status)
- Validate state transitions with custom exceptions
- Generate shipment summaries and reports

---

## ✨ Features

### 1. **OOP Design**
- **Abstract Classes:** `Shipment` base class defining common shipment properties
- **Interfaces:** `Insurable` interface for shipment insurance capabilities
- **Enums:** `ShipmentStatus` for managing shipment lifecycle states
- **Inheritance:** Specialized shipment types (Standard, Express, SameDay)

### 2. **State Machine**
- Enforces strict lifecycle transitions: `PENDING` → `IN_TRANSIT` → `DELIVERED`/`RETURNED`
- Custom exception `InvalidStatusTransitionException` prevents invalid state changes
- Validates all status updates before applying changes

### 3. **Efficient Data Management**
- **HashMap:** Provides $$O(1)$$ constant-time shipment retrieval by ID
- **Collections Framework:** Leverages `ArrayList`, `HashMap`, and `Comparator` interfaces
- **Memory Optimization:** Efficient storage and quick access to shipment records

### 4. **Sorting Strategies**
- Sort by **Cost** (high to low)
- Sort by **Distance** (ascending/descending)
- Sort by **Status** (alphabetical order)
- Implements multiple `Comparator` strategies for flexibility

### 5. **User-Friendly Menu System**
- Interactive command-line interface
- Clear menu options for all operations
- Input validation and error handling
- Informative output messages

---

## 📁 Project Structure

```
CargoShipmentTracker/
├── src/
│   ├── Shipment.java                    # Abstract base class
│   ├── StandardShipment.java            # Standard service implementation
│   ├── ExpressShipment.java             # Express service implementation
│   ├── SameDayShipment.java             # Same-day service implementation
│   ├── ShipmentStatus.java              # Enum for shipment states
│   ├── Insurable.java                   # Interface for insurance
│   ├── InvalidStatusTransitionException.java  # Custom exception
│   ├── CargoCompany.java                # Main business logic
│   └── Main.java                        # Application entry point
├── README.md                            # Project documentation
└── LICENSE                              # License file
```

---

## 🏗️ System Architecture

### Class Hierarchy

```
Shipment (Abstract)
├── StandardShipment
├── ExpressShipment
└── SameDayShipment

Interfaces:
└── Insurable

Enums:
└── ShipmentStatus {PENDING, IN_TRANSIT, DELIVERED, RETURNED}

Exceptions:
└── InvalidStatusTransitionException
```

### Data Flow

```
User Input → Menu Handler → CargoCompany Manager → HashMap Storage
                                      ↓
                            Validation & Exception Handling
                                      ↓
                            Output Display
```

---

## 📖 Usage Guide

### Main Menu Options

| Option | Description |
|--------|-------------|
| **1** | Register a Standard Shipment |
| **2** | Register an Express Shipment |
| **3** | Register a Same-Day Shipment |
| **4** | Update Shipment Status |
| **5** | List All Shipments |
| **6** | Find Shipment by ID |
| **7** | Sort Shipments |
| **8** | Display Summary Report |
| **9** | Exit Application |

### Shipment Types & Pricing

| Type | Base Cost Formula | Delivery Time |
|------|-------------------|---------------|
| **Standard** | Distance × 1.5 TL/km | 3-5 days |
| **Express** | Distance × 3.0 TL/km | 1-2 days |
| **Same-Day** | Distance × 6.0 TL/km + Weight × 5 TL/kg | Same day |

### State Transition Rules

```
PENDING
  ↓
IN_TRANSIT
  ↓
DELIVERED or RETURNED
```

**Valid Transitions:**
- `PENDING` → `IN_TRANSIT`
- `IN_TRANSIT` → `DELIVERED`
- `IN_TRANSIT` → `RETURNED`

**Invalid Transitions:** Any other combination will raise `InvalidStatusTransitionException`

---

## 💡 Example Execution

### Sample Interaction

```
Enter company name: Hızlı Kargo
--- Cargo Menu ---
1. Register Standard
2. Register Express
3. Register Same-Day
4. Update Status
5. List All
6. Find Shipment
7. Sort Shipments
8. Summary
9. Exit

Choose an option (1-9): 1
Sender: Mehmet
Recipient: Ayşe
Distance (km): 250
Weight (kg): 12
Shipment registered. ID: 1

Choose an option (1-9): 2
Sender: Burak
Recipient: Cem
Distance (km): 50
Weight (kg): 5
Shipment registered. ID: 2

Choose an option (1-9): 4
Enter shipment ID: 1
Enter new status: IN_TRANSIT
Status updated successfully.

Choose an option (1-9): 7
Sort by (cost/distance/status): cost
===== Sorted by Cost (high -> low) =====
ID: 1 | [Standard] Mehmet -> Ayşe | Cost: 375.00 TL
ID: 2 | [Express] Burak -> Cem | Cost: 150.00 TL

Choose an option (1-9): 9
Exiting. Goodbye!
```

### Error Handling Example

```
Choose an option (1-9): 4
Enter shipment ID: 2
Enter new status: DELIVERED
❌ Invalid status transition: PENDING -> DELIVERED

Choose an option (1-9): 6
Enter shipment ID: 9999
❌ Shipment not found with ID: 9999
```

---

## 🔧 Technical Details

### Custom Exceptions

**InvalidStatusTransitionException**
- Thrown when attempting an invalid state transition
- Provides clear error messages about allowed transitions
- Prevents data inconsistency

### Comparator Implementations

```java
// Cost Comparator (High to Low)
Comparator<Shipment> costComparator = (s1, s2) -> 
    Double.compare(s2.calculateCost(), s1.calculateCost());

// Distance Comparator (Low to High)
Comparator<Shipment> distanceComparator = (s1, s2) -> 
    Integer.compare(s1.getDistance(), s2.getDistance());

// Status Comparator (Alphabetical)
Comparator<Shipment> statusComparator = (s1, s2) -> 
    s1.getStatus().compareTo(s2.getStatus());
```

### Time Complexity Analysis

| Operation | Complexity | Notes |
|-----------|-----------|-------|
| Register Shipment | $$O(1)$$ | HashMap insertion |
| Find by ID | $$O(1)$$ | HashMap lookup |
| List All | $$O(n)$$ | Linear scan |
| Sort | $$O(n \log n)$$ | Merge sort/Quick sort |
| Update Status | $$O(1)$$ | Direct access + validation |

---

## 📋 Requirements

- **Java Version:** JDK 8 or higher
- **IDE:** IntelliJ IDEA, Eclipse, or NetBeans (optional)
- **Build Tool:** Maven or Gradle (optional)
- **Memory:** Minimum 512 MB RAM
- **Operating System:** Windows, macOS, or Linux

---

## 🚀 Installation

### Step 1: Clone or Download the Project

```bash
git clone https://github.com/yourusername/cargo-shipment-tracker.git
cd cargo-shipment-tracker
```

### Step 2: Compile the Source Code

```bash
javac -d bin src/*.java
```

### Step 3: Verify Compilation

```bash
ls bin/  # Should show all .class files
```

---

## ▶️ How to Run

### Option 1: Direct Execution

```bash
java -cp bin Main
```

### Option 2: Using IDE

1. Open the project in your IDE
2. Right-click on `Main.java`
3. Select "Run" or press `Ctrl+Shift+F10` (IntelliJ)

### Option 3: Using Maven (if configured)

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="Main"
```

---

## 📊 Sample Output

```
===== Sorted by Cost (high -> low) =====
ID: 1 | [Standard] Mehmet -> Ayşe | Distance: 250 km | Weight: 12 kg | Cost: 375.00 TL | Status: PENDING
ID: 2 | [Express] Burak -> Cem | Distance: 50 km | Weight: 5 kg | Cost: 150.00 TL | Status: IN_TRANSIT
ID: 3 | [SameDay] Deniz -> Ece | Distance: 15 km | Weight: 3 kg | Cost: 90.00 TL | Status: DELIVERED

===== Summary Report =====
Total Shipments: 3
Total Revenue: 615.00 TL
Average Cost: 205.00 TL
Status Distribution:
  - PENDING: 1
  - IN_TRANSIT: 1
  - DELIVERED: 1
```

---

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Abstract classes and inheritance
- ✅ Interface implementation
- ✅ Enum usage for state management
- ✅ Custom exception handling
- ✅ Collections Framework (HashMap, ArrayList)
- ✅ Comparator and sorting strategies
- ✅ State machine design pattern
- ✅ Input validation and error handling
- ✅ Object-oriented design principles

---

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---
