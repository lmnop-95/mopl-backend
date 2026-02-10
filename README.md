# Mopl backend

[![CI](https://github.com/lmnop-95/mopl-backend/actions/workflows/ci.yaml/badge.svg)](https://github.com/lmnop-95/mopl-backend/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/lmnop-95/mopl-backend/graph/badge.svg)](https://codecov.io/gh/lmnop-95/mopl-backend)

## Overview

## Tech Stack

### Backend

<div>
  <img src="https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white" alt="badge">
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.x-6DB33F?logo=springboot&logoColor=white" alt="badge">
  <img src="https://img.shields.io/badge/Gradle-9.3.1-02303A?logo=gradle&logoColor=white" alt="badge">
</div>

### Database & Cache

### Messaging & Realtime

### Monitoring & Logging

### Infrastructure & DevOps

<div>
  <img src="https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white" alt="badge">
  <img src="https://img.shields.io/badge/AWS_S3-569A31?logo=amazons3&logoColor=white" alt="badge">
  <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?logo=githubactions&logoColor=white" alt="badge">
</div>

### Testing

## Architecture

```
Root
├── applications (Spring Boot application)
│   └── 📦 api
├── core         (Domain module)
│   └── 📦 user
└── buildSrc     (Convention plugins)
```
