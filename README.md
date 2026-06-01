# inventory-tracking

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
