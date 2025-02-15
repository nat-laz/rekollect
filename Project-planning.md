## resources

1. [Create an ERD (Entity Relationship Diagram)](https://dbdiagram.io/d/Rekollect-DB-67695f95d16109b4009714a1)
2. [Draft UI](https://excalidraw.com/#room=1da8aaa8d2251a05ebbb,VPmluEqwrbs6w_uSh7g3NA)

## Learning:

- ```Optional<T>``` is a Java class that represents a container object which may or may not contain a non-null value,
  encourages functional programming style.
- ```@RequiredArgsConstructor``` is a Lombok annotation that automatically generates a constructor with required
  arguments, ensures dependencies are injected properly.
- ```@NoArgsConstructor``` → Generates a no-argument constructor (useful for JPA/Hibernate).
- ```@AllArgsConstructor``` → constructor for all fields.
- Separation of Concerns:```Repository``` only handles DB, ```Service``` handles logic, ```Controller``` handles API.

