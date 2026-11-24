# inventory-tracking

This project contains of

* a backend service written in Kotlin using Spring Boot and
* a Kotlin Multiplatform frontend with WebAssembly (Kotlin/WASM) and JVM (Desktop) targets

## Local development

### Start the Backend

Run the main function in [App.kt](backend/src/main/kotlin/de/dagadeta/inventorytracking/backend/App.kt) or the Gradle
task:

```bash
./gradlew backend:bootRun
```

### Start the Frontend

__Desktop with Hot Reload__

```bash
./gradlew frontend:desktopApp:hotRun
```

__Desktop (normal)__

```bash
./gradlew frontend:desktopApp:run
```

__WebApp__

```bash
./gradlew frontend:webApp:wasmJsBrowserDevelopmentRun
```

### Backend Requests

When the backend is started, an OpenAPI Spec and Swagger UI are served without authentication.
The root path http://localhost:8080 redirects to it.

On first start, two default users get created:

* user `Default User`, password `secret`
* user `Admin`, password `very secret`

It is highly recommended to change their passwords soon.
