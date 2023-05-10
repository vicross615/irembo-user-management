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

}
```
3) ENCRYPTING SENSITIVE FILES BEFORE STORAGE








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
