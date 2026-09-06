# PromptCanvas

<p align="center">
  <b>AI-powered application builder built with Spring Boot</b>
</p>

<p align="center">
  Describe an app in natural language, generate or modify its code through conversation, and turn it into a live running application.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange" />
  <img src="https://img.shields.io/badge/Spring_Boot-Backend-brightgreen" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-blue" />
  <img src="https://img.shields.io/badge/Spring_AI-LLM_Integration-purple" />
  <img src="https://img.shields.io/badge/MinIO-Project_Storage-red" />
  <img src="https://img.shields.io/badge/Stripe-Subscriptions-635BFF" />
  <img src="https://img.shields.io/badge/Status-In_Development-yellow" />
</p>

---

## Overview

PromptCanvas is an AI-powered application builder inspired by tools like Lovable and Bolt.

The idea is simple: describe what you want to build, let the AI generate the application, and then continue modifying the same project through conversation.

```text
User Prompt
    ↓
Project Context + File Tree
    ↓
AI Generation + Tool Calling
    ↓
Structured Chat Events
    ↓
Generated / Modified Files
    ↓
MinIO Project Storage
    ↓
Execution Environment
    ↓
Live Preview
```

I started by building the backend so the AI layer has an actual system around it instead of being just a wrapper around an LLM API.

The backend now handles authentication, authorization, subscriptions, project storage, AI generation, tool calling, project-aware context, chat persistence, and generated file updates.

The next stage is connecting all of this to the frontend and building the runtime system that can automatically execute generated applications and display them as live previews.

<p align="center">
  <img src="docs/diagrams/promptcanvas-high-level-system-flow.png" alt="PromptCanvas High-Level Flow" />
</p>

---

## Features

### Currently implemented

#### Authentication & projects

- JWT-based authentication with Spring Security
- Project creation and management
- Project membership with `OWNER`, `EDITOR`, and `VIEWER` roles
- Permission-based authorization using `@PreAuthorize`
- PostgreSQL persistence with JPA / Hibernate
- MapStruct DTO mapping

#### Billing

- Stripe Checkout for subscriptions
- Stripe Customer Portal
- Subscription and billing-period tracking
- Stripe webhook verification and event handling
- Plan-based project creation checks

#### AI code generation

- Spring AI integration through an OpenAI-compatible API
- GPT-5.3 Codex through OpenRouter
- Streaming AI responses using Reactor `Flux` + Server-Sent Events
- Conversational code generation
- Project-aware file-tree context
- Custom Spring AI advisor for injecting the current project structure
- Tool calling for reading existing project files before modifying them
- Structured AI generation protocol using `<message>`, `<tool>`, and `<file>` events
- Complete-file generation instead of partial patches
- Automatic parsing and persistence of generated/modified files

#### AI chat & generation history

- Per-project chat sessions
- Persistent user and assistant messages
- Ordered chat events for each assistant response
- `MESSAGE`, `TOOL_LOG`, `FILE_EDIT`, and `THOUGHT` event handling
- Parsing streamed LLM output into structured chat events
- Tool activity stored alongside the generated response
- Generated file edits connected to the assistant turn that produced them
- Project chat-history retrieval API
- Basic generation-duration tracking

#### Project files & templates

- MinIO-based project file storage
- PostgreSQL metadata for project files
- File-tree and file-content APIs
- Preconfigured React starter template
- Automatic starter-template initialization when a project is created
- Server-side copying of template files into project-specific storage
- Generated file updates written directly back to the project in MinIO

### Currently working on

The backend generation pipeline is now largely in place.

The next stage is focused on two major pieces:

**Frontend**

- Building the PromptCanvas application interface
- Streaming AI responses into the chat UI
- Rendering message, tool, thought, and file-edit events
- Project file browsing and editing experience
- Connecting generated projects to their live previews

**Execution & Live Preview**

- Running generated applications automatically
- Creating isolated runtime environments for projects
- Kubernetes pod/container orchestration
- Building and serving generated React applications
- Managing preview lifecycle and status
- Exposing running applications through temporary preview URLs
- Updating the preview as the AI modifies project files

The goal is to reach the complete loop:

```text
Describe
   ↓
Generate
   ↓
Run
   ↓
Preview
   ↓
Modify through chat
   ↓
Regenerate
   ↓
Updated preview
```

---

## AI Code Generation Flow

A generation request is not sent to the model blindly.

PromptCanvas first gives the model information about the current project and allows it to inspect existing files before making changes.

```text
User asks for a change
        ↓
Authorization check
        ↓
Chat session created / loaded
        ↓
FileTreeContextAdvisor
        ↓
Current project file tree added to AI context
        ↓
LLM determines which existing files it needs
        ↓
<tool> event
        ↓
read_files tool call
        ↓
Files fetched from MinIO
        ↓
Contents returned to the LLM
        ↓
LLM plans the changes
        ↓
Complete updated files generated
        ↓
Response streamed through SSE
        ↓
Full response parsed into ChatEvents
        ↓
FILE_EDIT events written to MinIO
        ↓
Chat messages + events persisted in PostgreSQL
```

This means the model works with the actual state of the project instead of blindly regenerating files without knowing what already exists.

---

## Structured AI Responses

PromptCanvas uses a small structured protocol around LLM responses so generation can be understood by both the backend and frontend.

A response can contain events such as:

```xml
<tool args="src/App.tsx,src/App.css">
Reading the existing application files...
</tool>

<message phase="planning">
I'll update the main application layout and styling.
</message>

<file path="src/App.tsx">
Complete updated file...
</file>

<file path="src/App.css">
Complete updated file...
</file>

<message phase="completed">
Updated the application layout and styling.
</message>
```

The backend parses this response into ordered `ChatEvent`s.

```text
Assistant ChatMessage
        │
        ├── THOUGHT
        ├── TOOL_LOG
        ├── MESSAGE
        ├── FILE_EDIT
        ├── FILE_EDIT
        └── MESSAGE
```

This lets the frontend eventually reconstruct the generation process instead of displaying the entire LLM response as one large block of text.

It also keeps actual file modifications separate from conversational messages and tool activity.

---

## Starter Project

Every new PromptCanvas project starts from a preconfigured React template.

The current template uses:

- React 18
- TypeScript
- Vite
- Tailwind CSS 4
- daisyUI v5
- React Router
- TanStack Query
- React Hook Form + Zod
- Lucide React
- Sonner
- date-fns
- Recharts

The template is stored in MinIO and copied into a project-specific location whenever a new project is created.

```text
promptcanvas-starter-projects

└── react-vite-tailwind-daisyui-starter/
    ├── src/
    ├── public/
    ├── package.json
    └── ...

              ↓ new project #12

promptcanvas

└── 12/
    ├── src/
    ├── public/
    ├── package.json
    └── ...
```

For local setup instructions, see:

[Starter Template Setup](starter-templates/README.md)

---

## Stripe Subscription Flow

PromptCanvas uses Stripe for subscription billing.

The backend creates Checkout sessions, stores subscription state locally, and uses Stripe webhooks as the source of truth for renewals, failures, updates, and cancellations.

<p align="center">
  <img src="docs/diagrams/stripe-subscription-flow.png" alt="Stripe Subscription Flow" />
</p>

---

## Tech Stack

**Backend**

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Spring AI
- Project Reactor
- Server-Sent Events
- MapStruct
- Maven

**Database & Storage**

- PostgreSQL
- MinIO

**AI**

- Spring AI
- OpenRouter
- GPT-5.3 Codex
- Tool calling
- Custom project-context advisors
- Structured LLM response parsing

**Integrations**

- Stripe Checkout
- Stripe Customer Portal
- Stripe Webhooks

**Frontend / Generated Applications**

- React
- TypeScript
- Vite
- Tailwind CSS
- daisyUI

**Runtime / Preview — In Progress**

- Docker
- Kubernetes

---

## Configuration

Secrets should be loaded through environment variables rather than committed to Git.

Example:

```yaml
stripe:
  api:
    secret: ${STRIPE_SECRET_KEY}
  webhook:
    secret: ${STRIPE_WEBHOOK_SECRET_KEY}

spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      base-url: https://openrouter.ai/api/v1
      chat:
        model: openai/gpt-5.3-codex
```

You will also need:

- PostgreSQL configuration
- JWT secret
- MinIO URL and credentials
- MinIO project bucket configuration

Example MinIO configuration:

```yaml
minio:
  url: http://localhost:9002
  access-key: ${MINIO_ACCESS_KEY}
  secret-key: ${MINIO_SECRET_KEY}
  project-bucket: promptcanvas
```

Do not commit real API keys or production credentials.

---

## Run Locally

Clone the repository:

```bash
git clone https://github.com/dev-aryank/promptcanvas.git
cd promptcanvas
```

Start the required local services, configure the environment variables, and make sure PostgreSQL and MinIO are available.

The React starter template also needs to exist in the configured MinIO template bucket before creating projects.

See:

[Starter Template Setup](starter-templates/README.md)

Then run the backend:

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

Calling an LLM is only one part of that problem.

A real system also needs to deal with project context, existing files, authentication, permissions, billing, tool calling, storage, conversation history, streaming, execution, isolation, and live previews.

PromptCanvas is my attempt at building that complete system instead of stopping at the generation API.

---

## Project Status

> 🚧 **PromptCanvas is actively being built.**

The core backend flow is now working:

```text
Authentication
      ↓
Projects + Permissions
      ↓
Subscriptions
      ↓
Starter Project
      ↓
Project File Storage
      ↓
AI Context
      ↓
Tool Calling
      ↓
Code Generation
      ↓
SSE Streaming
      ↓
Chat Events + History
      ↓
Generated File Persistence
```

The focus is now shifting to the **PromptCanvas frontend** and the **Kubernetes-based execution system**.

The next major milestone is to take the code already being generated and stored by PromptCanvas, run it automatically inside an isolated environment, and display the result as a live preview.

Once that is connected to the chat interface, PromptCanvas will have the full generate → run → preview → modify loop.