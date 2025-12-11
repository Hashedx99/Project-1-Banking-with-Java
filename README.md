# Banking System – README

## Overview

This project is a console-based banking management system built in Java. It includes login authentication, customer and
banker roles, checking and savings accounts, overdraft protection, file-based storage, transaction history, debit card
limits, fraud detection, and date-range transaction filtering.

---

# Technologies Used

- Java 17
- JSON (used Jackson databind for JSON parsing)
- JUnit 5
- Java Time API (`LocalDate`, `LocalDateTime`)
- Object-oriented programming principles
- Java Collections Framework
- Java Streams and lambda expressions
- SHA-256 hashing via `MessageDigest`

---

# Trello Board (User Stories and Planning)

https://trello.com/b/q6WwgCya/project-1-banking-with-java

---

# Additional Resources

- Java official documentation
- baeldung for sha256 hashing
- Lucidchart for ERD diagrams
- Stack Overflow for troubleshooting

---

# Planning and Development Process

## System Design

Key design decisions:

- Abstract `Account` superclass
- Concrete `SavingsAccount` and `CheckingAccount` classes
- FileHandler interface adopted by all file storage classes
- Debit card limits implemented via inheritance
- Password hashing with stored salt
- Transaction objects stored per user for history and auditing

## Implementation Sequence

1. Authentication and password hashing
2. File handler infrastructure (Customer, Banker, Accounts, Debit Cards)
3. Account creation and debit card assignment
4. Core actions: withdraw, deposit, transfer
5. Overdraft protection and status management
6. Transaction logging
7. Date-range filtering system
8. Unit testing

## Problem-Solving Strategy

- Encapsulated repeated logic into helper methods
- Used defensive file handling to avoid data corruption and ensure system continuity
- Employed polymorphism for debit card types to simplify limit management
- Created custom exceptions for specific error handling
- Leveraged Java Streams for efficient data processing
- Used Java Time API for robust date and time handling
- Applied streams and filters for lookup operations

---

## File Design Philosophy (MongoDB-Inspired Structure)

The file storage system in this project was intentionally designed using principles similar to MongoDB. Since the
project requirements specified file-based storage rather than a relational database, the goal was to create a structure
that remained easy to scale, simple to query, and flexible enough to evolve over time without breaking existing data.
The resulting design mirrors the strengths of NoSQL systems.

### Document-Based Storage

Each entity in the system—Customer, Banker, Account, DebitCard, and Transaction—is stored as its own JSON document. This
closely resembles how NoSQL databases store documents instead of relational rows and tables.

Examples include:

- `Customer-<Name>-<userId>.json`
- `Accounts/<userId>.json`
- `DebitCards/<accountId>.json`
- `Transactions/<userId>/<UUID>.json`

Each file contains all necessary data about that entity, enabling fast reads and avoiding the risk of massive file
sizes.

### Hierarchical Folder Structure

The directory layout mimics a collection-based NoSQL structure:

```
Data/
 ├── Customers/
 ├── Bankers/
 ├── Accounts/
 ├── DebitCards/
 ├── SystemLogs/
 └── Transactions/
      └── <customerID>/
```

Relation is achieved through unique ids stored within each document. which are used to name files that need to be
associated with a particular entity. This allows for:

- Natural partitioning of data by customer
- Easy retrieval of all transactions for a specific user
- Clear organization that scales well as more customers and transactions are added

### Schema Flexibility

Because each file is a JSON document, the system benefits from schema flexibility:

- Fields can be added or removed over time.
- Older documents remain valid.
- No migrations or schema updates are required.

### Decoupled Components

Each major object type has its own FileHandler class promoting:

- Low coupling between components
- High cohesion in how each file type is managed
- Cleaner and more maintainable code
- Clear separation of file I/O logic from business logic

### Transaction Logging

Transactions are stored as individual JSON files within a subdirectory for each customer. This allows:

- Easy appending of new transactions without modifying existing files
- Simple filtering and querying by date range
- Audit trails that are human-readable and easy to debug

### Defensive File Handling

All read and write operations follow defensive patterns:

- Directory creation when missing
- Graceful handling of absent or corrupted files
- `try/catch` blocks around all I/O operations
- Safe fallback values (null or empty lists)

### Benefits of This Design

- Easy evolution of data structures
- Natural partitioning by customer
- Scales well with large transaction histories
- Simple debugging with human-readable JSON

---

## Pride Points

- Thoughtful file design inspired by NoSQL principles
- Authentication with salted password hashing
- Clear separation of concerns with FileHandler interfaces
- Effective use of OOP principles like inheritance and polymorphism
- Comprehensive unit tests covering core functionality
- User-friendly console interface
- Transactions are performed using a proper sequence of objects: debit card initiates transaction, account class 
  handles it
- Detailed planning and user story tracking via Trello

---

# ERD Diagram

[![ERD Diagram](https://github.com/Hashedx99/Project-1-Banking-with-Java/blob/main/Project-01-Banking-With-JAVA.png)](https://lucid.app/lucidchart/75b38a77-4350-4304-9061-08398ce4fcd9/edit?viewport_loc=-316%2C-179%2C2825%2C1521%2C0_0&invitationId=inv_7fa0ae58-b0d4-4883-955a-3234baea575f)