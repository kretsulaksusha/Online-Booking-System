#!/bin/bash

# CONFIGURATION
AUTH_URL="http://localhost:8085/auth"
USERNAME="testuser_1"
#USERNAME="testuser_$(date +%s)"     # Unique username per run
PASSWORD="securePassword123!"
TOKEN_FILE=".token"

# Color Logging
info()  { echo -e "\033[1;34m[INFO]\033[0m $1"; }
warn()  { echo -e "\033[1;33m[WARN]\033[0m $1"; }
error() { echo -e "\033[1;31m[ERROR]\033[0m $1"; }
success() { echo -e "\033[1;32m[SUCCESS]\033[0m $1"; }

# Function: Health Check
health_check() {
  info "Checking health endpoint..."
  status=$(curl -s -o /dev/null -w "%{http_code}" "$AUTH_URL/../actuator/health")
  if [[ "$status" == "200" ]]; then
    success "Health check passed."
  else
    error "Health check failed with HTTP status: $status"
    exit 1
  fi
}

# Function: Register user
register_user() {
  info "Registering new user: $USERNAME"
  resp=$(curl -s -w "\n%{http_code}" -X POST "$AUTH_URL/register" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}")

  code=$(echo "$resp" | tail -n1)
  body=$(echo "$resp" | sed '$d')

  if [[ "$code" == "200" ]]; then
    success "User registered successfully."
  elif [[ "$code" == "409" ]]; then
    warn "User already exists."
  else
    error "Failed to register. HTTP $code: $body"
    exit 1
  fi
}

# Function: Login and extract JWT
login_user() {
  info "Logging in user: $USERNAME"
  resp=$(curl -s -w "\n%{http_code}" -X POST "$AUTH_URL/login" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}")

  code=$(echo "$resp" | tail -n1)
  body=$(echo "$resp" | sed '$d')

  if [[ "$code" != "200" ]]; then
    error "Login failed. HTTP $code"
    echo "$body"
    exit 1
  fi

  if command -v jq &>/dev/null; then
    TOKEN=$(echo "$body" | jq -r '.token')
  else
    TOKEN=$(echo "$body" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
  fi

  if [[ -z "$TOKEN" ]]; then
    error "JWT token not found in login response!"
    exit 1
  else
    echo "$TOKEN" > "$TOKEN_FILE"
    success "JWT token extracted and saved to $TOKEN_FILE"
  fi
}

# Function: Validate token
validate_token() {
  info "Validating token..."
  token=$(cat "$TOKEN_FILE")
#  token=$(cat "$TOKEN_FILE" | tr -d '\n' | tr -d '\r')
  echo "$token"
  code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$AUTH_URL/validate" \
    -H "Content-Type: text/plain" \
    -d "$token")
  if [[ "$code" == "200" ]]; then
    success "Token is valid ✅"
  else
    error "Token validation failed ❌ (HTTP $code)"
  fi
}

# Function: Logout
logout_user() {
  info "Logging out user..."
  token=$(cat "$TOKEN_FILE")
  code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$AUTH_URL/logout" \
    -H "Authorization: Bearer $token")
  if [[ "$code" == "204" ]]; then
    success "Logout successful (token revoked)"
  else
    warn "Logout failed or not supported (HTTP $code)"
  fi
}

# EXECUTION
health_check
register_user
login_user
validate_token
logout_user        # Optional
validate_token     # Check again after logout to confirm revocation

info "✅ All authentication tests completed successfully."
