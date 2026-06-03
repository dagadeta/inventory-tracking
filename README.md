# inventory-tracking

This project contains of

* a backend service written in Kotlin using Spring Boot and
* a Kotlin Multiplatform frontend using Web Assembly (Kotlin/WASM)

## Local development

### Start the Backend

Run the main function in [App.kt](backend/src/main/kotlin/de/dagadeta/inventorytracking/backend/App.kt) or the Gradle
task:

```bash
./gradlew backend:bootRun
```

### Start the Frontend

```bash
./gradlew frontend:webApp:wasmJsBrowserDevelopmentRun
```

## List users with default password

```bash
curl --basic -u 'Default User:secret' "http://localhost:8080/api/users"
```

## List inventory items with default password

```bash
curl --basic -u 'Default User:secret' "http://localhost:8080/api/inventory/items"
```

## Change password

```bash
curl --basic -u 'Default User:secret' \
    -H "Content-Type: application/json" \
    -X POST \
    -d '{"currentPassword":"secret","newPassword":"xyz"}' \
    -i \
    "http://localhost:8080/api/change-password"
```
