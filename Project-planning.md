## resources

1. [Create an ERD (Entity Relationship Diagram)](https://dbdiagram.io/d/Rekollect-DB-67695f95d16109b4009714a1)
2. [Draft UI](https://excalidraw.com/#room=1da8aaa8d2251a05ebbb,VPmluEqwrbs6w_uSh7g3NA)
3. Entities:

 - categoryName -> ENUM with fixed names
 - Record cover_image -> default img for each category


4. Tables 

- categories: first created with fixed fields (ENUM: ```CategoryName```) than category by user assign to the id from DB: Table ```categories```
- 