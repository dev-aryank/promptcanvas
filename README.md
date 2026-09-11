# PromptCanvas

<p align="center">
  <b>Describe an app. Let AI build it. Keep changing it through conversation.</b>
</p>

<p align="center">
  PromptCanvas is an AI-powered application builder built with Spring Boot.
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

## What is PromptCanvas?

PromptCanvas is an AI-powered application builder inspired by tools like **Lovable** and **Bolt**.

The idea is pretty simple:

> Describe the application you want, let the AI generate it, preview the result, and keep modifying the same project through chat.

Something like:

```text
"Build me a task management dashboard"
                ↓
        PromptCanvas AI
                ↓
       Generates the project
                ↓
           Live Preview
                ↓
"Add dark mode and a calendar"
                ↓
      Existing project updated
```

But building that loop turned out to involve a little more than just calling an LLM API.

PromptCanvas currently handles things like:

* authentication
* projects and permissions
* subscriptions
* project file storage
* starter templates
* AI project context
* tool calling
* streaming generation
* chat history
* structured AI events
* generated file updates

And now the next step is making those generated applications actually **run automatically and appear as live previews**.

<p align="center">
  <img src="docs/diagrams/promptcanvas-high-level-system-flow.png" alt="PromptCanvas High-Level Flow" />
</p>

---

## Current Progress

The backend generation pipeline is already working end-to-end.

```text
User Prompt
    ↓
Project File Tree
    ↓
AI understands the project
    ↓
Reads existing files when needed
    ↓
Generates complete updated files
    ↓
Streams progress through SSE
    ↓
Files saved to MinIO
    ↓
Chat history saved to PostgreSQL
```

### Already working

* JWT authentication with Spring Security
* Project creation and management
* `OWNER`, `EDITOR`, and `VIEWER` project roles
* Permission-based authorization
* Stripe subscriptions and webhooks
* React starter project initialization
* MinIO-based project file storage
* AI code generation with Spring AI
* OpenRouter + GPT-5.3 Codex
* Project-aware AI context
* Custom `read_files` tool
* Read-before-edit generation flow
* SSE streaming with Reactor `Flux`
* Structured `<message>`, `<tool>`, and `<file>` responses
* Chat sessions and persistent generation history
* Automatic generated-file persistence

### Currently building

* PromptCanvas frontend
* AI chat interface
* File explorer/editor
* Kubernetes-based execution environments
* Live preview URLs
* Automatic preview updates after AI edits

The final loop should look like this:

```text
Describe
   ↓
Generate
   ↓
Run
   ↓
Preview
   ↓
Ask for changes
   ↓
Regenerate
   ↓
Updated Preview
```

---

## The Interesting Part: AI Code Generation

The AI does not get the entire project dumped into its prompt.

Instead, PromptCanvas first gives it the **project file tree**.

```text
src/
├── App.tsx
├── main.tsx
├── components/
│   ├── Navbar.tsx
│   └── Sidebar.tsx
└── pages/
    └── Dashboard.tsx
```

The model can then decide which files it actually needs.

For example, if the user asks:

> "Make the sidebar collapsible."

the model may decide that it only needs:

```text
src/components/Sidebar.tsx
src/App.tsx
```

It then calls a custom `read_files` tool to retrieve those files from MinIO.

```text
User Request
      ↓
Project File Tree
      ↓
LLM decides what it needs
      ↓
read_files
      ↓
Existing files loaded from MinIO
      ↓
LLM generates updated files
```

This follows one important rule:

> **Read before edit.**

If a file already exists, the model should inspect the real version before modifying it.

That keeps generation aware of the actual state of the project instead of blindly overwriting code.

---

## Structured AI Responses

The model does not return one giant blob of text.

PromptCanvas uses a small XML-like protocol so the backend can understand what each part of the response represents.

Example:

```xml
<tool args="src/App.tsx,src/components/Sidebar.tsx">
Reading the existing files...
</tool>

<message phase="planning">
I'll update the sidebar and adjust the main layout.
</message>

<file path="src/components/Sidebar.tsx">
Complete updated file...
</file>

<file path="src/App.tsx">
Complete updated file...
</file>

<message phase="completed">
The sidebar is now collapsible.
</message>
```

The backend parses these into ordered events:

```text
Assistant Response
       │
       ├── THOUGHT
       ├── TOOL_LOG
       ├── MESSAGE
       ├── FILE_EDIT
       ├── FILE_EDIT
       └── MESSAGE
```

This means the frontend can eventually show generation as an actual process:

```text
Thinking...

Reading Sidebar.tsx...

Planning changes...

Edited Sidebar.tsx
Edited App.tsx

Done.
```

instead of dumping raw model output onto the screen.

---

## Project Storage

Generated projects are stored in **MinIO**.

There are two main buckets.

```text
promptcanvas-starter-projects
```

contains reusable starter templates.

```text
promptcanvas
```

contains actual user projects.

When a project is created:

```text
react-vite-tailwind-daisyui-starter/
            ↓
        copied into
            ↓
          12/
```

So something like:

```text
promptcanvas-starter-projects/
└── react-vite-tailwind-daisyui-starter/
    ├── src/
    ├── public/
    └── package.json
```

becomes:

```text
promptcanvas/
└── 12/
    ├── src/
    ├── public/
    └── package.json
```

The AI then reads and modifies files directly inside that project's storage.

For starter-template setup:

[Starter Template Setup](starter-templates/README.md)

---

## Generated App Stack

New projects currently start with a preconfigured frontend stack:

* React 18
* TypeScript
* Vite
* Tailwind CSS 4
* daisyUI v5
* React Router
* TanStack Query
* React Hook Form
* Zod
* Lucide React
* Sonner
* date-fns
* Recharts

The goal is to give the model a capable environment without making it install a new ecosystem for every generated application.

---

## Stripe Billing

PromptCanvas also includes subscription billing through Stripe.

The backend handles:

* Stripe Checkout
* customer creation and reuse
* Customer Portal
* subscription state
* billing periods
* renewals
* cancellations
* failed payments
* webhook signature verification

Stripe webhooks are treated as the source of truth for subscription state.

<p align="center">
  <img src="docs/diagrams/stripe-subscription-flow.png" alt="Stripe Subscription Flow" />
</p>

---

## Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* Spring AI
* Project Reactor
* Server-Sent Events
* MapStruct
* Maven

### Database & Storage

* PostgreSQL
* MinIO

### AI

* Spring AI
* OpenRouter
* GPT-5.3 Codex
* Tool calling
* Custom Spring AI advisors
* Structured response parsing

### Billing

* Stripe Checkout
* Stripe Customer Portal
* Stripe Webhooks

### Generated Applications

* React
* TypeScript
* Vite
* Tailwind CSS
* daisyUI

### Execution Layer

Currently being built with:

* Docker
* Kubernetes

---

## Architecture

At a high level:

```text
                    ┌─────────────────┐
                    │    Frontend     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   Spring Boot   │
                    │     Backend     │
                    └───────┬─────────┘
                            │
          ┌─────────────────┼─────────────────┐
          │                 │                 │
          ▼                 ▼                 ▼
    PostgreSQL            MinIO             Stripe
    Users / Chat       Project Files       Billing
          │
          │
          ▼
      Spring AI
          │
          ▼
      OpenRouter
          │
          ▼
   GPT-5.3 Codex

              ↓ upcoming

         Kubernetes
              │
              ▼
      Generated App Pod
              │
              ▼
        Live Preview
```

---

## Configuration

Secrets should be provided through environment variables.

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

MinIO:

```yaml
minio:
  url: http://localhost:9002
  access-key: ${MINIO_ACCESS_KEY}
  secret-key: ${MINIO_SECRET_KEY}
  project-bucket: promptcanvas
```

You will also need configuration for:

* PostgreSQL
* JWT
* MinIO
* Stripe
* OpenRouter

Never commit real credentials or API keys.

---

## Running Locally

Clone the repository:

```bash
git clone https://github.com/dev-aryank/promptcanvas.git
cd promptcanvas
```

Make sure the required services are running:

```text
PostgreSQL
MinIO
```

Configure the required environment variables.

The starter React project must also exist inside the configured MinIO template bucket.

Setup instructions:

[Starter Template Setup](starter-templates/README.md)

Then run the backend.

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows

```powershell
.\mvnw spring-boot:run
```

---

## Why am I building this?

Because:

> "User writes a prompt → AI builds an app"

sounds much simpler than it actually is.

Calling the model is probably the easiest part.

Around that one API call you still need:

```text
Authentication
Permissions
Billing
Project state
File storage
Project context
Tool calling
Streaming
Chat history
Code persistence
Execution
Isolation
Networking
Live previews
```

PromptCanvas is my attempt to build that whole system rather than stopping at:

```java
chatModel.call(prompt);
```

and calling it an AI application builder.

---

## What's Next?

The generation side is now in a good place.

The next major milestone is the runtime layer:

```text
Generated files in MinIO
        ↓
Create isolated Kubernetes workload
        ↓
Install / build / run the project
        ↓
Expose the running application
        ↓
Generate preview URL
        ↓
Show preview inside PromptCanvas
```

Then when the user sends another prompt:

```text
Chat request
     ↓
AI edits project
     ↓
Files updated
     ↓
Running application refreshed
     ↓
Preview updates
```

That's the part I'm building next.

---

## Project Status

> 🚧 **Actively under development**

PromptCanvas is not finished yet, but the core backend and AI generation pipeline are already working.

The goal is to eventually reach this:

```text
Prompt → Generate → Run → Preview → Modify → Repeat
```

One commit at a time.
