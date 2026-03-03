# LinkedIn Clone – Microservices Architecture

A simple LinkedIn-like social networking application built using **Spring Boot Microservices** architecture.

## Architecture Overview

<img width="1209" height="471" alt="Microservices" src="https://github.com/user-attachments/assets/57ac1d26-8f7a-4fcd-b429-fc2bd5b785ea" />

###
The application is composed of multiple independent microservices:

- **User Service** – Handles user signup and login.
- **Post Service** – Manages post creation and interactions (likes, etc.).
- **Connection Service** – Manages connection requests and acceptance.
- **Notification Service** – Sends notifications for various events.
- **Discovery Service** – Eureka server for service discovery.
- **Kafka** – Event-driven communication between services.

All services are built using **Spring Boot**.

## Tech Stack

- **Backend Framework**: Spring Boot  
- **Databases**:
  - PostgreSQL (User, Post, Notification services)
  - Neo4j (Connection service – graph database)
- **Messaging System**: Apache Kafka
- **Interservice Communocation**: Feign Client used for interservice communication
- **Architecture Style**: Microservices  
- **API Communication**: REST APIs  
- **Build Tool**: Gradle  

## Services Description

### User Service
Responsible for:
- User registration (Signup)
- User authentication (Login)
- Managing user profile data

**Database**: PostgreSQL

### Post Service
Responsible for:
- Creating posts
- Fetching posts
- Liking posts

**Database**: PostgreSQL  

**Publishes Events**:
- Post Created
- Post Liked

### Connection Service
Responsible for:
- Sending connection requests
- Accepting connection requests
- Managing user connections

**Database**: Neo4j  

Graph database is used because it is more suitable for handling relationship-based data like connections and mutual networks.

**Publishes Events**:
- Connection Request Sent
- Connection Request Accepted

### Notification Service
Responsible for:
- Listening to Kafka events
- Sending notifications for:
  - New post
  - Post liked
  - New connection request
  - Connection accepted

**Database**: PostgreSQL  

**Consumes Events**:
- Post events
- Connection events

### Discovery Service
Responsible for service discovery

#### Eureka running on localhost screenshot
<img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/1591c2c2-fbe1-4e4d-8d61-302345d2e2f3" />

## Event-Driven Communication

The system uses **Apache Kafka** for asynchronous communication between services.

### Event Flow Examples

- When a **post is created** → Post Service publishes event → Notification Service consumes and sends notification.
- When a **post is liked** → Post Service publishes event → Notification Service sends notification.
- When a **connection request is sent/accepted** → Connection Service publishes event → Notification Service notifies users.

#### Krafbat UI Screenshot of topics created
<img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/6a3d25f2-18ec-4caf-8e83-8d679386015c" />

## DB Screenshots

## Connections DB(Neo4j)

<img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/79df9b74-88da-41f2-bd00-701645891e73" />

## Notifications DB

<img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/57d8b297-514a-4773-b045-2e34ef9aa956" />

## Posts DB

<img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/b650eb72-2a73-4e6f-b04f-c2c147697ce8" />

## Users DB

<img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/f09b695b-1ba7-4adf-b9ca-33437dbe2dda" />




