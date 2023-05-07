MFA AUTHENTICATION_fLOW
![alt-text](data/Login_flow.png "AUTHENTICATION_fLOW")
1. The features should include:
    a. Ability to create an account - signup
    ![alt-text](data/login.png "AUTHENTICATION_fLOW")

    b. Login and Logout
        ![alt-text](data/login_.png "AUTHENTICATION_fLOW")
    c. Reset password
##

IN-CLASS SECURITY 
```xml
public class PasswordValidatorUtil {

    public static String validatePassword(String password) {
        // Create a password validator with a list of rules
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new NumericalSequenceRule(3, false),
                new AlphabeticalSequenceRule(3, false),
                new QwertySequenceRule(3, false),
                new WhitespaceRule()));

        // Validate the password using the validator
        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return null;
        } else {
            // Return the error messages if the password doesn't meet the requirements
            List<String> messages = validator.getMessages(result);
            return messages.stream().collect(Collectors.joining(", "));
        }
    }
}
```

## Dependency
– If you want to use PostgreSQL:
```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```
– or MySQL:
```xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>
```
## Configure Spring Datasource, JPA, App properties
Open `src/main/resources/application.properties`
- For PostgreSQL:
```
spring.datasource.url= jdbc:postgresql://localhost:5432/testdb
spring.datasource.username= postgres
spring.datasource.password= 123

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation= true
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto= update

# App Properties
irembo.app.jwtSecret=SecretKey
irembo.app.jwtExpirationMs= 86400000
```
- For MySQL
```
spring.datasource.url= jdbc:mysql://localhost:3306/testdb?useSSL=false
spring.datasource.username= root
spring.datasource.password= 123456

spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto= update

# App Properties
irembo.app.jwtSecret= SecretKey
irembo.app.jwtExpirationMs= 86400000
```
## Run Spring Boot application
```
mvn spring-boot:run
```

## Run following SQL insert statements
```
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```
