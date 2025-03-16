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

### Elasticsearch Endpoints

→ **Full-Text Search**



  ```shell
E.g:  GET http://localhost:8080/records/search?query=movies
  ```

- Boosted fields (`title^3`, `description^2`) for better ranking
- Fuzzy search (`AUTO`) for typo tolerance
- Nested search for `creators` (firstName, lastName, role)
- Supports multi-field matching (title, description, category, tags, etc.) 