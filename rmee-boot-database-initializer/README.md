
- Flyway is good to perform migrations
- Spring Boot integrates Flyway well
- Initialization is often something different, executed with different privledges
- Spring Boot and Flyway have limited support for this, e.g. no placeholders in Spring Boot.
- This project performs such an initialization
- In the future we should consider using here a second "admin" Flyway instance. That one
  is shared within a project with multiple services/schemas and takes care of all intialization/admin/updates
  related tasks accross all services.
