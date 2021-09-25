<br/>
<p align="center">
    <a href="https://sulu.io/" target="_blank">
        <img width="50%" src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/Banco_Santander_Logotipo.svg/2560px-Banco_Santander_Logotipo.svg.png" alt="Sulu logo">
    </a>
</p>

<br/>

<h2 align="center">Your money secure with the best bank online in the market</h2>


<div align="center">
  <h3>
    <a href="https://media.istockphoto.com/photos/hole-in-white-paper-with-torns-edges-coming-soon-picture-id1279117626?b=1&k=20&m=1279117626&s=170667a&w=0&h=GE8hgZzX1SzETg6B1QnlDgVEJv9BSDHGG8Xyz1wNl30=">
      Website
    </a>
    <span> | </span>
    <a href="https://media.istockphoto.com/photos/work-in-progress-road-sign-picture-id155388466?k=20&m=155388466&s=612x612&w=0&h=6bdW6twAzWK1U6QV5-9PrF7WvMBOkUcG8LeUaiAinlE=">
      Create Account
    </a>
    <span> | </span>
    <a href="https://api.whatsapp.com/send?phone=625933118">
      Chat
    </a>
    <span> | </span>
    <a href="https://github.com/IMartinMenendez/bankingSystem/issues">
      Report an Issue
    </a>
  </h3>
</div>


<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block"> 📋 Table of Contents</h2></summary>
  <ol>
    <li><a href="#installationanddocumentation">Installation and Documentation</a>
    </li>
    <li><a href="-commands-to-use">Commands to use</a>
    </li>
    <li><a href="-commands-to-use">Technology used</a></li>
    <li><a href="#contact">Funcionallities</a></li>
    <li><a href="#contact">Author</a></li>
    <li><a href="#contact"> Have a question?</a></li>
    <li><a href="#contact"> Found a bug?</a></li>
    <li><a href="#contact"> Requirements</a></li>
    <li><a href="#contact"> License</a></li>
  </ol>
</details>

Welcome to the new generation bank online. You can easily create a different bank accounts where you can keep your money safe and make transfers.

- Types of Accounts:
  - [x] Checking Accounts.
  - [x] Saving Accounts.
  - [x] Credit Card Accounts.
  - [x] Student Checking accounts.
  
Please go to <a href="#functionalities">funcionalities</a> to know more about them.

---

##  🚀&nbsp; Installation and Documentation 

You will need to clone the project or download it as a zip from the [Banking System Repository](https://github.com/IMartinMenendez/bankingSystem.git). After that, you will need to replicate the database.
You can find the database structure on Scratches and Consoles file named [SQL Structure](https://github.com/IMartinMenendez/bankingSystem.git). Just copy and create them.

Also, you will need to set up the project and link it with the database. Please add the details on application.properties under the folder resources.
This set up will be very similar to this:

```
spring.flyway.enabled=false

spring.datasource.url=jdbc:mysql://yourdatabaseurl
spring.datasource.username=yourUserName
spring.datasource.password=yourPassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=validate

spring.jpa.show-sql=true

server.error.include-message=always
server.error.include-binding-errors=always

spring.jackson.serialization.write-dates-as-timestamps=false
```

You will need to do it also to test the application.

---

## ✏️&nbsp; Commands to use

###Admins

Admins should be able to access the balance for any account and to modify it.
###AccountHolders

AccountHolders should be able to access their own account balance
Account holders should be able to transfer money from any of their accounts to any other account (regardless of owner). The transfer should only be processed if the account has sufficient funds. The user must provide the Primary or Secondary owner name and the id of the account that should receive the transfer.
###Third-Party Users

There must be a way for third-party users to receive and send money to other accounts.
Third-party users must be added to the database by an admin.
In order to receive and send money, Third-Party Users must provide their hashed key in the header of the HTTP request. They also must provide the amount, the Account id and the account secret key.

### **Method available:**

  `GET` | `POST` 


<br>

| Method   | Endpoint                                 | Description                              | User                              |
| -------- | ---------------------------------------- | ---------------------------------------- |-----------------------------------
| `GET`    | `/accounts`         | Retrieve all accounts.                    | Admin                             |
| `POST`   | `/accounts`         | Create a new post.                       | AccountHolders                    |
| `GET`    | `/accounts/{id}`      | The user can see their own account       | AccountHolders                    |
| `GET`    | `/users/{id}`         | See an User with id                      | Admin, AccountHolders             |
| `POST`   | `/users`            | Create a new user                        | Admin                             |
| `POST`   | `/transfer`         | Make a transfer between accounts         | Admin, AccountHolders, Third-Party|

### **Available Roles:**

`ADMIN`  | `ACCOUNT HOLDER`| `THIRD-PARTY`

<br>

### **Json to get/post information:**

<br>

####To create new user:

```
{
"type": "account_holder",
"name": "user2",
"password": "123456",
"dateOfBirth": "1888-01-15",
"address": {
"street": "calle falsa 123",
"city": "London",
"country": "UK",
"postalCode": "123456"
},
"roles": [
"ADMIN"
]
}
```

####To create an account:

```
{
    "type": "checking",
    "balance": {
        "amount": 300,
        "currency": "EUR"
    },
    "primaryOwnerId":  1,
    "secondaryOwnerId": null,
    "status":"ACTIVE",
    "secretKey": "secret-key-123"
}
```

####To create a transaction:

```
{
    "type": "account_holder",
    "amount": {
        "amount": 200,
        "currency": "EUR"
    },
    "fromAccountId": 3,
    "toAccountId": 4
}
```



## 💻&nbsp; Technology used

![Java Badge](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) 


---

## ⚙️ Funcionalities

- Savings accounts:
    - [x] Savings accounts should have a default minimumBalance of 1000
    - [x] Have a default interest rate of 0.0025.
    - [x] Have a minimum balance of less than 1000 but no lower than 100.
  
- Credit Cards:
  - [x] Have a default creditLimit of 100.
  - [x] Credit Limit higher than 100 but not higher than 100000.
  - [x] Default interestRate of 0.2 not lower than 0.1.

- Checking Accounts:
    - [x] Minimum Balance of 250 and a monthlyMaintenanceFee of 12.

- Penalty Fee:
    - [x] The penaltyFee for all accounts should be 40.
    - [x] If any account drops below the minimumBalance, the penaltyFee should be deducted from the balance automatically
- Interest Rate:
    - [x] Interest on savings accounts is added to the account annually at the rate of specified interestRate per year.
    - [x] Interest on credit cards is added to the balance monthly.

---

[comment]: <> (## ❤️&nbsp; Community and Contributions)

##💁‍♀️️‍ Author

<a href="https://imartinmenendez.github.io/portfolio/">
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/79365505?v=4" width="100px;" alt=""/>
 <br />
 <sub><b>Irene Martín</b></sub></a> <a href="https://imartinmenendez.github.io/portfolio/">🚀</a>
 <br />

[![Instagram Badge](https://img.shields.io/badge/-IMartinMenendez-E4405F?Instagram-E4405F?style=flat-square&labelColor=E4405F&logo=instagram&logoColor=white&link=https://www.instagram.com/irene_mmenendez/)](https://www.instagram.com/irene_mmenendez/) [![Linkedin Badge](https://img.shields.io/badge/-Irene_Martín-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/irene1martin2menendez/)](https://www.linkedin.com/in/irene1martin2menendez/)
[![Gmail Badge](https://img.shields.io/badge/-irene1martin2menendez@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:irene1martin2menendez@gmail.com)](mailto:irene1martin2menendez@gmail.com?subject=[GitHub])

I love to create and be in constant growth. I am a very curious person, always trying to improve my skills, get out of my comfort zone and challenge myself without losing the positive mindset and can-do attitude.


---

## 📫&nbsp; Have a question? Want to chat? Ran into a problem?

We are happy to hearing about your questions and concerns. Please don't hesitate to contact me for any issue you can experiment.

---

## 🤝&nbsp; Found a bug? Missing a specific feature?

Feel free to **file a new issue** with a respective title and description on the [Banking System Repository](https://github.com/IMartinMenendez/bankingSystem/issues). I really will appreciate your feedback to improve the system.

---

## ✅&nbsp; Requirements

You should have Postman and IDE. Just clone the repository from [Banking System Repository](https://github.com/IMartinMenendez/bankingSystem.git) and try it!

---

## ©️&nbsp; License

Made with ❤️&nbsp;️ in London. Copyright © 2021 Irene Martin and follow the [MIT License](LICENSE).
