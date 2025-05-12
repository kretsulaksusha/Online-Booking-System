#!/bin/bash

# CONFIG
BASE_URL="http://localhost:8084"  # Change port if your auth-service uses a different one

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
echo "Creating test user..."
register_response=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password123","role":"USER"}')

echo "Register response: $register_response"

# Get HTTP status code for register (using a separate call with -I to get headers)
register_http_code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password123","role":"USER"}')

echo "Register HTTP status code: $register_http_code"

# 3. Login Test
echo "Logging in test user..."
# Create a temp file to store the response body
temp_file=$(mktemp)

login_response=$(curl -s -w "%{http_code}" -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password123"}' -o "$temp_file")

# Extract the HTTP status code and the body of the response
login_http_code=$(echo "$login_response" | tail -n 1)  # HTTP status code is the last line
login_body=$(cat "$temp_file")                        # Response body is in the temp file
rm "$temp_file"                                      # Clean up temp file

echo "Login HTTP status code: $login_http_code"
echo "Login response body: $login_body"

# Check if the login was successful based on HTTP code and content
if [ "$login_http_code" -eq 200 ]; then
  # Try to extract the token using jq if status is 200
  TOKEN=$(echo "$login_body" | jq -r '.token')

  if [ -z "$TOKEN" ]; then
    echo "❌ Token not found!"
    exit 1
  else
    echo "✅ Token extracted: $TOKEN"
  fi
else
  echo "❌ Login failed with HTTP status code $login_http_code"
  exit 1
fi

echo "Smoke test completed successfully."
