# PromptCanvas

<p align="center">
  <b>AI-powered application builder built with Spring Boot</b>
</p>

<p align="center">
  Describe an app in natural language, generate or modify its code through conversation, and eventually run it with a live preview.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange" />
  <img src="https://img.shields.io/badge/Spring_Boot-Backend-brightgreen" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-blue" />
  <img src="https://img.shields.io/badge/Spring_AI-LLM_Integration-purple" />
  <img src="https://img.shields.io/badge/Stripe-Subscriptions-635BFF" />
  <img src="https://img.shields.io/badge/Status-In_Development-yellow" />
</p>

---

## Overview

PromptCanvas is an AI-powered application builder inspired by tools like Lovable and Bolt.

The goal is to let a user describe what they want to build, then continue modifying the generated application through conversation.

```text
User Prompt
    ↓
AI Generation
    ↓
Project Files
    ↓
Execution
    ↓
Live Preview
```

I am building the backend first so the AI layer has a proper system around it instead of being just a wrapper around an LLM call.

<p align="center">
  <img src="docs/diagrams/promptcanvas-high-level-system-flow.png" alt="PromptCanvas High-Level Flow" />
</p>

---

## Features

### Currently implemented

* JWT-based authentication with Spring Security
* Project creation and management
* Project membership with OWNER / EDITOR / VIEWER roles
* Permission-based authorization using `@PreAuthorize`
* PostgreSQL persistence with JPA / Hibernate
* MapStruct DTO mapping
* Stripe Checkout for subscriptions
* Stripe Customer Portal
* Subscription and billing-period tracking
* Stripe webhook verification and event handling
* Spring AI integration

### In progress / planned

* Streaming AI responses using WebFlux + SSE
* Conversational code generation
* Project-aware AI context
* File-tree/context advisor
* Tool calling
* MinIO-based project file storage
* Chat history
* Code execution environment
* Live application previews
* RAG / vector storage where useful

---

## Stripe Subscription Flow

PromptCanvas uses Stripe for subscription billing.

The backend creates Checkout sessions, stores subscription state locally, and uses Stripe webhooks as the source of truth for renewals, failures, updates and cancellations.

<p align="center">
  <img src="docs/diagrams/stripe-subscription-flow.png" alt="Stripe Subscription Flow" />
</p>

---

## Tech Stack

**Backend**

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* Spring WebFlux
* Spring AI
* MapStruct
* Maven

**Database & Storage**

* PostgreSQL
* MinIO *(planned)*

**Integrations**

* OpenAI
* Stripe Checkout
* Stripe Customer Portal
* Stripe Webhooks


---

## Configuration

Secrets are loaded through environment variables rather than being committed to Git.

Example:

```yaml
stripe:
  secret: ${STRIPE_SECRET_KEY}

spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
```

You will also need your PostgreSQL configuration and JWT secret.

---

## Run Locally

```bash
git clone https://github.com/dev-aryank/promptcanvas.git
cd promptcanvas
```

Configure the required environment variables and database, then run:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw spring-boot:run
```

---

## Why PromptCanvas?

I wanted to understand what actually sits behind:

> "Describe an application and AI builds it."

The LLM call itself is only one part of the problem.

The rest involves things like project context, file handling, authentication, authorization, billing, tool calling, execution and live previews.

PromptCanvas is my attempt at building that complete flow.

---

## Project Status

> 🚧 **PromptCanvas is still actively being built.**

The main backend foundation, authentication, authorization, project system, Stripe subscriptions and initial Spring AI integration are already in place.

I am currently working on the AI code-generation, file-management and execution side.

The goal is to complete the first full version within the next **week or two**.
