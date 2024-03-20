# Приложение с задачами по программированию

## Стек технологий

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white)
![React Query](https://img.shields.io/badge/-React%20Query-FF4154?style=for-the-badge&logo=react%20query&logoColor=white)
![SASS](https://img.shields.io/badge/SASS-hotpink.svg?style=for-the-badge&logo=SASS&logoColor=white)
![NodeJS](https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black)

## Для начала работы с приложением необходимо:

### 1. Склонировать репозиторий

```
git clone git@github.com:ikilpikov/codetasks.git
```

### 2. Добавить в docker-compose.yml значения переменным среды:

- custom-properties.executor.key (ключ от API Onecompiller)

- spring.datasource.password (пароль от базы данных)

- spring.datasource.username (имя пользователя от базы данных)

### 3. Открыть в терминале директорию проекта и исполнить команду:
```
docker-compose up
```

### 4. Открыть в браузере <localhost:5173/auth>

## Примечания:

- База данных находится на удаленном сервере
- Для входа с ролью администратора нужно ввести пару логин-пароль admin-admin
- Регистрация администратора не предусмотрена
- С приложением можно взаимодейстовать с помощью postman-коллекции, каждый раз передавая токен в запросы