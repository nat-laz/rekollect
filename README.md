## Rekollect API - Personal Content Tracker (Backend)

_Rekollect_ is a personal content tracking application built to organize and categorize various content (books, movies,
podcasts, etc.). This repository focuses on the API/backend implementation using Java Spring Boot with a robust
Elasticsearch-based search and filtering system.

---

### Database Relationships Overview

- ```record``` → ```user``` (```Many-to-One```): Each record is owned by a user.
- ```record``` → ```category``` (```Many-to-One```): Each record belongs to one category.
- ```record``` → ```tag``` (```Many-to-Many``` via ```record_tags```): A record can have multiple tags.
- ```record``` → ```comment``` (```One-to-Many```): A record can have multiple comments.
- ```record``` → ```creator``` (```Many-to-Many``` via ```record_creator```): A record can have multiple creators.
- ```creator``` → ```role``` (```Many-to-Many ```via ```record_creator```): A creator can have multiple roles in
  different records.

#### Join Tables:

- ```record_tags``` (```record_id```, ```tag_id```) → Links records and tags.
- ```record_creator``` (```record_id```, ```creator_id```, ```role_id```) → Links records, creators, and roles.

**Each record is user-specific, making it a personal content tracker.**

---

### Elasticsearch Integration

Implemented Elasticsearch (Version 8.15.0) for efficient full-text search and filtering.

- **Full-Text Search:** Boosted fields (```title^3```, ```description^2```) for better ranking.

- **Fuzzy Search:** Typo tolerance with ```AUTO``` mode.

- **Nested Search:** Supports searching within `creators (firstName, lastName, role)`.

- **Filtering:** Supports filtering by `category`, `tags`, `releaseDate`, `createdAt`, `updatedAt`.

- **Pagination & Sorting:** Supported for optimized result handling.

- **Dockerized Setup:** Runs via Docker for easy setup and
  testing. [Setup Guide](src/main/resources/docs/elasticsearch-setup.md)

---

### Current Features (Implemented)

- **Add, Categorize, Tag, Filter, and Rate Content**: CRUD operations for ``records``, `categories`, `tags`, `creators`, and `comments`.

- **Search & Filtering:** Integrated Elasticsearch for full-text search, filtering, and pagination.

- **Data Persistence:** Relational data modeling using `PostgreSQL` & `JPA (Hibernate).`

- **Custom Validation:** Dynamic handling of categories, creators, roles, and tags.

- **Guest User Mode:** Simplified user management with dummy user handling.

### Planned Features (To Be Implemented)

- **Authentication & Security:** Implementing JWT-based Authentication for user-specific record handling.

- **User Management:** Registration, login, and role-based authorization.

- **Enhanced Record Management:** Editing, updating, and deleting records.

- **Frontend Application:** React-based UI for intuitive content management.

- **Testing:** Comprehensive unit and integration tests for Spring Boot services and controllers.

