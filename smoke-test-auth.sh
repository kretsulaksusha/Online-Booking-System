#!/bin/bash

# CONFIG
BASE_URL="http://localhost:8085"  # Change port if your auth-service uses a different one
USERNAME="testuser_1"
#USERNAME="testuser_$(date +%s)"   # Unique username with timestamp
PASSWORD="securePassword123!"     # Strong password

# 1. Health Check
echo "Checking health endpoint..."
health_response=$(curl -s -w "%{http_code}" -o /dev/null "$BASE_URL/actuator/health")
echo "Health check HTTP status code: $health_response"

if [ "$health_response" -eq 200 ]; then
  echo "✅ Health check passed."
else
  echo "❌ Health check failed!"
  exit 1
fi

# 2. Register Test User
echo "Creating test user with username: $USERNAME"

# Perform register request, capture response body and HTTP status code in one call
response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"'"$USERNAME"'","password":"'"$PASSWORD"'","role":"USER"}')

# Extract HTTP status code (last line)
http_code=$(echo "$response" | tail -n 1)  # Get the last line (status code)

# Extract body (everything except last line)
body=$(echo "$response" | sed '$ d')       # Get all lines except the last

echo ---
echo "Response body: $body"
echo "HTTP Code: $http_code"
echo ---

if [[ "$http_code" == "200" ]]; then
  echo "User registered successfully."
elif [[ "$http_code" == "409" ]]; then
  echo "User already exists."
  exit 1
else
  echo "Registration failed with status $http_code"
  exit 1
fi

# 3. Login Test
echo "Logging in test user: $USERNAME"

login_response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"'"$USERNAME"'","password":"'"$PASSWORD"'"}')

# Split response into body and status code
login_http_code=$(echo "$login_response" | tail -n 1)
login_body=$(echo "$login_response" | sed '$ d')

echo "Login HTTP status code: $login_http_code"
echo "Login response body: $login_body"

if [ "$login_http_code" -eq 200 ]; then
  # Try to extract the token using jq if available, otherwise use basic pattern matching
  if command -v jq &> /dev/null; then
    TOKEN=$(echo "$login_body" | jq -r '.token')
  else
    TOKEN=$(echo "$login_body" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
  fi

  if [ -z "$TOKEN" ]; then
    echo "❌ Token not found in response!"
    exit 1
  else
    echo "✅ Token extracted: ${TOKEN:0:20}..."  # Show first 20 chars for security
    echo "Full token saved to .token file"
    echo "$TOKEN" > .token
  fi
else
  echo "❌ Login failed with HTTP status code $login_http_code"
  exit 1
fi

echo "✅ Smoke test completed successfully."
