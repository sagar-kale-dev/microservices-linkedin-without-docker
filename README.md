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

## Event-Driven Communication

The system uses **Apache Kafka** for asynchronous communication between services.

### Event Flow Examples

- When a **post is created** → Post Service publishes event → Notification Service consumes and sends notification.
- When a **post is liked** → Post Service publishes event → Notification Service sends notification.
- When a **connection request is sent/accepted** → Connection Service publishes event → Notification Service notifies users.

