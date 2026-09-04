# React Starter Template Setup

This folder contains the React starter project used by PromptCanvas when a new application is created.

PromptCanvas does **not** build every generated project from an empty folder. Instead, a ready-to-use React project is stored in MinIO and copied into a new project's storage area during project creation.

The AI can then read and modify those files through the PromptCanvas generation pipeline.

---

## How It Works

The backend currently expects two MinIO buckets:

```text
promptcanvas-starter-projects
promptcanvas
```

Their jobs are different:

```text
promptcanvas-starter-projects
        ↓
Stores reusable starter templates

promptcanvas
        ↓
Stores the actual files belonging to user projects
```

The current starter template name is:

```text
react-vite-tailwind-daisyui-starter
```

So the template bucket should contain objects with this prefix:

```text
promptcanvas-starter-projects/
└── react-vite-tailwind-daisyui-starter/
    ├── package.json
    ├── package-lock.json
    ├── vite.config.ts
    ├── src/
    ├── public/
    └── ...
```

When project `12` is created, PromptCanvas copies those objects into:

```text
promptcanvas/
└── 12/
    ├── package.json
    ├── package-lock.json
    ├── vite.config.ts
    ├── src/
    ├── public/
    └── ...
```

At the same time, metadata for those project files is stored in PostgreSQL.

---

## 1. Start MinIO

PromptCanvas uses MinIO as S3-compatible object storage for generated project files.

A local Docker Compose setup can look like this:

```yaml
services:
  minio:
    image: minio/minio:RELEASE.2025-09-07T16-13-09Z
    container_name: minio-promptcanvas
    command: server /data --console-address ":9001"

    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin123

    ports:
      - "9002:9000"
      - "9003:9001"

    volumes:
      - minio-data:/data

    restart: unless-stopped

volumes:
  minio-data:
```

Start it with:

```bash
docker compose up -d
```

With the port mapping above:

```text
http://localhost:9002 → MinIO S3 API
http://localhost:9003 → MinIO Web Console
```

Open the console at:

```text
http://localhost:9003
```

For the example configuration above, the local development credentials are:

```text
Username: minioadmin
Password: minioadmin123
```

These values are only suitable for local development. Use environment variables and proper secrets outside local development.

---

## 2. Configure PromptCanvas

The backend reads the MinIO connection from `application.yaml`.

Example:

```yaml
minio:
  url: http://localhost:9002
  access-key: minioadmin
  secret-key: minioadmin123
  project-bucket: promptcanvas
```

If the Spring Boot application is running directly on your machine, use:

```text
http://localhost:9002
```

If the backend later runs inside the same Docker network as MinIO, the internal service address would typically be something like:

```text
http://minio:9000
```

depending on the Docker Compose service name.

---

## 3. Create the Buckets

Create these two buckets in the MinIO console:

```text
promptcanvas-starter-projects
promptcanvas
```

### `promptcanvas-starter-projects`

Stores reusable starter templates.

### `promptcanvas`

Stores files belonging to actual PromptCanvas projects.

The project bucket is configured through:

```yaml
minio:
  project-bucket: promptcanvas
```

The starter-template bucket is currently defined in:

```text
ProjectTemplateServiceImpl
```

as:

```java
private static final String TEMPLATE_BUCKET = "promptcanvas-starter-projects";
```

---

## 4. Upload the React Starter

Upload the contents of this starter project into the template bucket under the following root folder:

```text
react-vite-tailwind-daisyui-starter/
```

The important part is that the object names in MinIO begin with exactly:

```text
react-vite-tailwind-daisyui-starter/
```

For example:

```text
react-vite-tailwind-daisyui-starter/package.json
react-vite-tailwind-daisyui-starter/src/App.tsx
react-vite-tailwind-daisyui-starter/src/main.tsx
react-vite-tailwind-daisyui-starter/src/index.css
```

Do **not** upload `node_modules`, build output, local `.env` files, or other machine-specific files.

Normally the template should include source/configuration files such as:

```text
src/
public/
package.json
package-lock.json
vite.config.ts
tsconfig files
eslint config
index.html
```

and anything else required for the starter to install and run normally.

---

## 5. Why the Folder Name Matters

`ProjectTemplateServiceImpl` currently contains:

```java
private static final String TEMPLATE_NAME =
        "react-vite-tailwind-daisyui-starter";
```

When PromptCanvas initializes a project, it lists every MinIO object whose key starts with:

```text
react-vite-tailwind-daisyui-starter/
```

It then removes that prefix before writing the file into the project's own location.

Example:

```text
Source:
react-vite-tailwind-daisyui-starter/src/App.tsx

Project ID:
12

Destination:
12/src/App.tsx
```

So if you rename the template folder in MinIO, you must update `TEMPLATE_NAME` in the backend as well.

---

## 6. Changing Bucket Names

If you want to use different bucket names, make sure the backend and MinIO remain consistent.

### Changing the project bucket

Current configuration:

```yaml
minio:
  project-bucket: promptcanvas
```

`ProjectFileServiceImpl` uses this bucket when generated files are saved.

If you rename the bucket, for example:

```text
promptcanvas-project-files
```

update the configuration:

```yaml
minio:
  project-bucket: promptcanvas-project-files
```

Also make sure any remaining hardcoded project-bucket references in the code use the same value.

### Changing the starter-template bucket

The template bucket is currently hardcoded in `ProjectTemplateServiceImpl`:

```java
private static final String TEMPLATE_BUCKET =
        "promptcanvas-starter-projects";
```

If your MinIO bucket has a different name, change this constant accordingly.

### Changing the destination project bucket used during template copying

`ProjectTemplateServiceImpl` currently also contains:

```java
private static final String TARGET_BUCKET = "promptcanvas";
```

If the project bucket is renamed, this value must match it as well.

A cleaner future improvement would be to move both bucket names into configuration so there is only one source of truth.

For example:

```yaml
minio:
  project-bucket: promptcanvas
  template-bucket: promptcanvas-starter-projects
  template-name: react-vite-tailwind-daisyui-starter
```

Then inject those values instead of hardcoding them.

---

## 7. What Happens When a Project Is Created

Project creation currently follows this flow:

```text
Create Project
    ↓
Save Project in PostgreSQL
    ↓
Create OWNER ProjectMember
    ↓
initializeProjectFromTemplate(projectId)
    ↓
List all objects under the starter-template prefix
    ↓
Copy every object into projectId/<relative-path>
    ↓
Create ProjectFile metadata
    ↓
Save metadata in PostgreSQL
```

This happens automatically from `ProjectServiceImpl`.

You should therefore have MinIO running and the starter template uploaded **before creating a new project**.

Otherwise project initialization will fail when the backend tries to list or copy the template objects.

---

## 8. How Generated Files Are Stored

After the project is initialized, AI-generated changes use the project bucket.

For project `12`:

```text
12/src/App.tsx
12/src/components/Header.tsx
12/src/pages/Home.tsx
```

The object itself lives in MinIO while metadata such as the relative path and MinIO object key is stored in PostgreSQL.

This lets PromptCanvas:

- build the project file tree from PostgreSQL
- show that tree to the LLM
- let the LLM request existing files through `read_files`
- fetch file contents from MinIO
- write generated or modified files back to MinIO

---

## 9. Verify the Setup

Before running AI generation, verify:

- MinIO container is running
- `http://localhost:9003` opens the MinIO console
- `promptcanvas-starter-projects` exists
- `promptcanvas` exists
- the starter template is inside the template bucket
- the template objects begin with `react-vite-tailwind-daisyui-starter/`
- Spring Boot can connect to MinIO at the configured URL
- the configured access key and secret key are correct

Then create a PromptCanvas project.

After creation, the project bucket should contain something similar to:

```text
<projectId>/src/App.tsx
<projectId>/src/main.tsx
<projectId>/package.json
...
```

If those files appear, template initialization is working.

---

## Common Problems

### Project is created but no starter files appear

Check that the template objects actually use this prefix:

```text
react-vite-tailwind-daisyui-starter/
```

and that `TEMPLATE_NAME` matches it exactly.

### `The specified bucket does not exist`

Create the required bucket in MinIO or update the corresponding bucket name in the backend.

### `Connection refused`

Check that MinIO is running and that the backend is using the correct endpoint.

With the Docker mapping shown above, a backend running on the host should use:

```text
http://localhost:9002
```

not the MinIO console port.

### Files can be written but not read

Make sure every part of the backend uses the same project bucket.

At the moment the codebase contains both configured and hardcoded bucket references, so if you rename the bucket, update all of them together.

### Template was renamed

Update:

```java
private static final String TEMPLATE_NAME = "...";
```

to match the exact folder/prefix stored in MinIO.

---

## Template Stack

This starter is intended to provide PromptCanvas with a ready-to-modify frontend base using:

- React
- TypeScript
- Vite
- Tailwind CSS
- daisyUI

The AI generation prompt also assumes that the template already contains the frontend dependencies PromptCanvas expects to use.

When changing the starter template significantly, keep the AI system prompt and the installed dependencies in sync.
