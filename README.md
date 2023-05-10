#####IN-CLASS SECURITY FEATURES

1) MFA AUTHENTICATION_fLOW
![alt-text](data/Login_flow.png "AUTHENTICATION_fLOW")
   : The features should include:
    a. Ability to create an account - signup
    ![alt-text](data/login.png "AUTHENTICATION_fLOW")
      curl --location --request POST '' \
      --header 'Content-Type: application/json' \
      --data-raw '{
          "username":"test245",
           "password":"LongPassword23$$",
           "email": "testt29@gmail.com",
           "mfaEnabled": true,
           "role": ["ROLE_ADMIN"]
      }
      '
    
    
    

    b. Login and Logout
        ![alt-text](data/login_.png "AUTHENTICATION_fLOW")
          curl --location --request POST 'http://localhost:8080/api/auth/signin' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "username":"test245",
                 "password":"LongPassword23$$"
            }
            '
    c. Verify MFA
            ![alt-text](data/VerifyMFA.png "AUTHENTICATION_fLOW")

    curl --location --request POST 'http://localhost:8080/api/auth/verify-mfa-code/?code=951567&email=testt29@gmail.com' \
    --header 'Cookie: sessionId=4c44953f-08cb-4e30-8854-e2de6d021d51'
    
    
    D.GET USER PROFILE 
                ![alt-text](data/GetUserProfile.png "AUTHENTICATION_fLOW")

    curl --location --request GET 'http://localhost:8080/user-profile/4' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MjQiLCJpYXQiOjE2ODM2NjYxMzIsImV4cCI6MTY4Mzc1MjUzMn0.r6aeTYmS19acfMabh5xJ08SdwWzguw943cCxS_jcMHbZJ39iDSsHW63QDDczBZOTgIQzwk7oHPvmTiSpVXXh4Q'
    
    E. Post User Profile
                    ![alt-text](data/createPrile.png "AUTHENTICATION_fLOW")
    curl --location --request POST 'http://localhost:8080/user-profile/4/update' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MjQiLCJpYXQiOjE2ODM2NTk1ODEsImV4cCI6MTY4Mzc0NTk4MX0.I_2958C6besnxs8SirNZIBEuFuA7EkkgkYFIcyYzYn8bootMhfX8ZuEpSwvoNp4PiulV7G_B5fNWCMdUdqOBew' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "firstName": "Victor",
        "lastName": "udedibor",
        "gender": "MALE",
        "age": 30,
        "dateOfBirth": "2017-01-01",
        "maritalStatus": "SINGLE",
        "nationality": "Nigerian"
    }'
    
    
    
    
    
    Reset password
##



##
2) STRONG PASSWORD VERIFICATION FEATURE
```xml

public class PasswordValidatorUtil {

     public static String validatePassword(String password) {
            // Create a password validator with a list of rules
            List<Rule> rules = new ArrayList<>();
    
            PasswordValidator validator = new PasswordValidator(rules);
            //Rule 1: Password length should be in between 
            //8 and 16 characters
            rules.add(new LengthRule(8, 16));
            //Rule 2: No whitespace allowed
            rules.add(new WhitespaceRule());
            //Rule 3.a: At least one Upper-case character
            rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
            //Rule 3.b: At least one Lower-case character
            rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
            //Rule 3.c: At least one digit
            rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
            //Rule 3.d: At least one special character
            rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
    
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
3) ENCRYPTING SENSITIVE FILES BEFORE STORAGE
```
    @Override
    public InputStream encryptDocument(InputStream inputStream, SecretKey secretKey) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        cipherInputStream.close();
        outputStream.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
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
