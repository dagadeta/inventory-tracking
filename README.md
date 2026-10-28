# inventory-tracking

This project contains of

* a backend service written in Kotlin using Spring Boot and
* a Kotlin Multiplatform frontend with Web Assembly (Kotlin/WASM) and JVM (Desktop) targets

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

#### List users with default password

```bash
curl --basic -u 'Admin:very secret' "http://localhost:8080/api/users"
```

#### List inventory items with default password

```bash
curl --basic -u 'Default User:secret' "http://localhost:8080/api/inventory/items"
```

#### Change password

```bash
curl --basic -u 'Default User:secret' \
    -H "Content-Type: application/json" \
    -X POST \
    -d '{"currentPassword":"secret","newPassword":"xyz"}' \
    -i \
    "http://localhost:8080/api/change-password"
```
