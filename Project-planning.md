## resources

1. [Create an ERD (Entity Relationship Diagram)](https://dbdiagram.io/d/Rekollect-DB-67695f95d16109b4009714a1)
2. [Draft UI](https://excalidraw.com/#room=1da8aaa8d2251a05ebbb,VPmluEqwrbs6w_uSh7g3NA)

## Learning:

- ```Optional<T>``` is a Java class that represents a container object which may or may not contain a non-null value,
  encourages functional programming style.
- Separation of Concerns:```Repository``` only handles DB, ```Service``` handles logic, ```Controller``` handles API.
- **CreatorEntity:** ```@UniqueConstraint(columnNames = {"creator_first_name", "creator_last_name"})``` → ensures
  uniqueness of the full name.
- ```@CreationTimestamp & @UpdateTimestamp```  → Automatically handles timestamps when a record is created or updated (
  No need for manual ```@PrePersist``` and ```@PreUpdate``` methods.)



#### Lombok annotations:

- ```@RequiredArgsConstructor``` → automatically generates a constructor with required arguments, ensures dependencies
  are injected properly.
- ```@NoArgsConstructor``` → generates a no-argument constructor (useful for JPA/Hibernate).
- ```@AllArgsConstructor``` → constructor for all fields.

#### Special Naming Convention in Spring Data JPA

- ```findByNameIgnoreCase(String name)``` (Case-insensitive match) → ```SELECT * FROM category WHERE LOWER(category_name) = LOWER('Movies') LIMIT 1;```