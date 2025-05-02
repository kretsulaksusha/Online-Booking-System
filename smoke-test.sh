#!/usr/bin/env bash
set -euo pipefail

echo "🏃‍♂️ Smoke-test: Inventory → Booking"

# 1) Create inventory
INV=$(curl -s -X POST http://localhost:8080/inventory \
  -H "Content-Type: application/json" \
  -d '{"type":"X","availableCount":2,"price":99.99}')
ID=$(echo "$INV" | jq -r '.id')
echo "Inventory created: $ID"

# 2) Read back inventory
echo "🔍 Read inventory item:"
curl -s http://localhost:8080/inventory/"$ID" | jq .

# 3) Create booking
BOOK=$(curl -s -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d "{\"itemId\":\"$ID\",\"quantity\":1}")
BID=$(echo "$BOOK" | jq -r '.id')
echo "Booking created: $BID"

# 4) Read back booking
echo "🔍 Read booking:"
curl -s http://localhost:8080/bookings/"$BID" | jq .

echo "✅ Smoke-test пройшов успішно!"

