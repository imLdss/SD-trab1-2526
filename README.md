# SD T1 - Distributed Message Delivery System

A distributed messaging platform developed for the **Distributed Systems** course at **NOVA School of Science and Technology (FCT NOVA)**.

The project implements a domain-based message delivery system inspired by email, where users belong to independent domains and interact with distributed services to manage accounts, mailboxes, and message delivery.

---

## 📌 Project Overview

Each user belongs to a **domain** and has a mailbox managed by that domain.

Users always interact with the servers of their own domain. When a message is sent to a user in another domain, the sender's domain is responsible for forwarding it to the appropriate remote message server.

The system is composed of multiple independent domains, each potentially containing:

- a **Users Server**;
- a **Messages Server**;
- an optional **Gateway Server**.

The project focuses on core distributed-systems concepts such as:

- remote invocation;
- service discovery;
- inter-domain communication;
- concurrent request handling;
- communication failure recovery;
- REST APIs;
- optional gRPC interoperability.

---

## 🏗️ Architecture

Each domain manages its own users and mailboxes independently.

A typical domain contains:

```text
                 Client
                   |
                   v
            +---------------+
            |    Gateway    |   (optional)
            +---------------+
              /           \
             v             v
    +--------------+  +--------------+
    | Users Server |  |Message Server|
    +--------------+  +--------------+
                           |
                           | Inter-domain delivery
                           v
                    Other Domains
```

### Users Service

The **Users Service** is responsible for:

- creating users;
- retrieving user information;
- updating user information;
- deleting users;
- searching for users.

### Messages Service

The **Messages Service** is responsible for:

- sending messages to one or more recipients;
- listing message identifiers from a user's mailbox;
- filtering mailbox messages using a search string;
- retrieving individual messages;
- deleting received messages;
- deleting messages previously sent by a user;
- forwarding operations to other domains when necessary.

### Gateway Service

A domain may optionally expose a **Gateway Server**.

The gateway acts as a proxy between clients and the domain's internal services and exposes both the Users and Messages interfaces.

---

## 🌐 Service Communication

The system uses distributed remote invocation technologies.

### REST

REST services are implemented using **JAX-RS**.

The predefined service interfaces must be respected so that the implementation remains compatible with the automatic test suite.

### gRPC

The assignment also supports optional **gRPC servers**.

An advanced implementation may support:

- REST-only communication;
- gRPC-only communication;
- interoperability between REST and gRPC services.

---

## 🔎 Automatic Service Discovery

Servers must be discoverable dynamically instead of relying on hard-coded addresses.

The system uses **IP multicast** for service discovery.

Each service periodically announces itself using a message with the format:

```text
<service-name>@<domain-name><TAB><server-uri>
```

Example:

```text
Users@fct    http://users0.fct/rest
```

Supported service names are:

```text
Users
Messages
Gateway
```

The server URI identifies both the address of the service and the communication protocol:

```text
http://...
grpc://...
```

This mechanism allows services and the automated test infrastructure to discover available servers dynamically.

---

## 📨 Message Delivery

When a user sends a message:

1. The client contacts the **Messages Server** of the sender's domain.
2. The server determines the domains of the recipients.
3. Messages for local users are stored in local mailboxes.
4. Messages for remote users are forwarded to the corresponding remote domain.
5. Temporary communication failures must be handled correctly.

This architecture keeps user and mailbox management decentralized across domains.

---

## ⚠️ Delivery Failures

The system must correctly handle unsuccessful deliveries.

### Unknown User

If the destination user does not exist, the sender receives a notification message containing the original message data.

The notification subject follows the format:

```text
FAILED TO SEND <mid> TO <user>: UNKNOWN USER
```

### Delivery Timeout

Advanced implementations may also handle long communication failures.

If delivery cannot be completed within the configured timeout, the sender receives a failure notification:

```text
FAILED TO SEND <mid> TO <user>: TIMEOUT
```

---

## ⚡ Concurrency

The REST servers must support multiple clients accessing the system concurrently.

The implementation therefore needs to ensure correct synchronization when multiple requests operate on shared resources such as:

- user collections;
- mailboxes;
- message metadata;
- delivery state.

Correct concurrency control prevents inconsistent state, race conditions, and lost updates.

---

## 🔄 Communication Failures

The system does not require component replication or tolerance of permanent server crashes.

However, it must account for **temporary communication failures** between distributed services.

The base project requires correct operation when communication failures last up to approximately **10 seconds**.

More advanced implementations can support longer outages and configurable delivery timeouts.

---

## 🛠️ Technologies

- **Java 17**
- **JAX-RS**
- **REST APIs**
- **gRPC** *(optional / advanced)*
- **IP Multicast**
- **Maven**
- **Docker**
- **Concurrent Programming**
- **Distributed Systems**

---

## 📁 Suggested Repository Structure

```text
.
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
│       └── java/
├── pom.xml
├── Dockerfile
└── README.md
```

The exact structure may differ depending on the implementation.

---

## 🧠 Distributed Systems Concepts

This project explores several fundamental distributed-systems concepts:

- **Remote Procedure / Method Invocation**
- **Distributed Service Architecture**
- **Service Discovery**
- **IP Multicast**
- **Domain-based Data Partitioning**
- **RESTful Communication**
- **gRPC**
- **Protocol Interoperability**
- **Concurrency Control**
- **Fault Handling**
- **Retries and Timeouts**
- **Distributed Message Delivery**
- **Containerized Deployment**

---

## 🎯 Key Learning Outcomes

The project demonstrates how to design a distributed application where several independent services cooperate over the network while preserving a clear separation of responsibilities.

Important engineering challenges include:

- discovering remote services dynamically;
- routing messages across independent domains;
- maintaining local domain state;
- supporting concurrent clients safely;
- preserving predefined API contracts;
- handling temporary network failures;
- designing robust distributed communication logic.

---

## 🐳 Development Environment

The project targets:

```text
Linux
Java 17
Maven
Docker
```

The official validation environment uses Docker containers to launch and test the distributed services.

---

## 👤 Author

**Luís Santos Pereira**  
Computer Science / Computer Engineering Student — NOVA School of Science and Technology

GitHub: https://github.com/imLdss
