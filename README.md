# 📅 Schedule 관리 API
내일배움캠프 Spring 5기 일정 관리 API 프로젝트입니다.
<br>Spring Boot와 Swagger를 활용하여 사용자와 일정 CRUD 기능을 제공합니다.
<br><br><br>


## 📚 목차
* [📌 프로젝트 설명](#프로젝트-설명)
* [📌 주요 기능](#주요-기능)
* [📌 사용 방법](#사용-방법)
* [📑 API 문서](#api-문서)
* [📑 ERD](#erd)
* [📂 파일 구조](#파일-구조)
* [📌사용된 기술](#사용된-기술)
* [📞 Contact](#contact)
<br><br><br>


## 📌프로젝트 설명
이 프로젝트는 **일정을 관리할 수 있는 API**를 구현한 것입니다.  
사용자는 사용자와 일정을 **등록, 조회, 수정 삭제**할 수 있으며,  
Swagger UI를 통해 API를 쉽게 확인하고 테스트할 수 있습니다.
<br><br><br>

## 📌주요 기능
### ✅ 로그인과 로그아웃
- **로그인** (`POST /auth/login`)  
  - 이메일과 비밀번호를 입력하여 로그인합니다.
- **로그아웃** (`POST /auth/logout`)  
  - 현재 로그인된 계정을 로그아웃 합니다.
<br>

### ✅ 사용자 관리 (CRUD)
- **사용자 생성** (`POST /users/signup`)  
  - 새로운 사용자를 USER 권한으로 등록합니다.
- **사용자 조회** (`GET /users`)  
  - 로그인한 사용자가 모든 사용자를 조회합니다.
  - 사용자와 이메일의 필터링을 통해 특정 사용자를 조회할 수 있습니다.
- **사용자 수정 (전체 및 부분 수정)** (`PATCH /users/my`)  
  - 비밀번호를 입력하여 로그인한 사용자의 정보를 수정할 수 있습니다.
- **사용자 삭제** (`DELETE /users/my`)  
  - 비밀번호를 입력하여 로그인한 사용자를 삭제합니다.
- **삭제된 사용자 조회** (`GET /users/deleted`)
  - 관리자 권한의 사용자가 삭제 요청된 사용자를 조회할 수 있습니다.
- **삭제된 사용자 복구** (`PUT /users/restore/{id}`)
  - 관리자 권한의 사용자가 2주 이내로 삭제 요청된 사용자를 복구할 수 있습니다.
<br>
  
### ✅ 일정 관리 (CRUD)
- **일정 추가** (`POST /schedules`)  
  - 로그인한 사용자가 새로운 일정을 등록합니다.
- **일정 조회** (`GET /schedules`)  
  - 로그인한 사용자가 모든 일정을 조회합니다.
  - 사용자와 제목 필터링을 통해 특정 일정을 조회할 수 있습니다.
- **일정 수정 (전체 및 부분 수정)** (`PATCH /schedules/{id}`)  
  - 로그인한 사용자가 비밀번호를 입력하여, 사용자가 작성한 특정 일정의 전체 또는 일부 정보를 수정할 수 있습니다.
- **일정 삭제** (`DELETE /schedules/{id}`)  
  - 로그인한 사용자가 비밀번호를 입력하여, 사용자가 작성한 특정 일정을 삭제합니다.
<br>

### ✅ Swagger를 통한 API 문서 확인
Swagger를 통해 API를 쉽게 테스트할 수 있습니다.
<br> 
#### Swagger UI
http://localhost:8080/swagger-ui/index.html
<br> 
#### OpenAPI 문서 (JSON)
http://localhost:8080/v3/api-docs
<br><br><br>


## 📌사용 방법
### 기본 사용 방법
1. 애플리케이션 실행 (`Spring Boot`)
2. Swagger UI (`http://localhost:8080/swagger-ui/index.html`)에서 API 테스트
3. Postman 또는 REST 클라이언트로 API 호출 가능
4. MySQL 데이터베이스에서 일정 데이터를 관리
<br><br><br>

## 📑API 문서
### 🔐 로그인과 로그아웃
```plaintext
+---------------+------------------+-----------------------------------+
| HTTP Method   | URL              | 설명                               |
+---------------+------------------+-----------------------------------+
| `POST`        | `/auth/login`    | 이메일과 비밀번호를 입력하여 로그인합니다    |
| `POST`        | `/auth/logout`   | 현재 로그인된 계정을 로그아웃합니다        |
+---------------+------------------+-----------------------------------+
```
### 👤 사용자 관리 (CRUD)
```plaintext
+---------------+----------------------------+----------------------------------------------------+
| HTTP Method   | URL                        | 설명                                                |
+---------------+----------------------------+----------------------------------------------------+
| `POST`        | `/users/signup`            | 새로운 사용자를 USER 권한으로 등록합니다                    |
| `GET`         | `/users`                   | 로그인한 사용자가 모든 사용자를 조회합니다                    |
|               |                            | (사용자 및 이메일로 필터링 가능)                          |
| `PATCH`       | `/users/my`                | 비밀번호를 입력하여 로그인한 사용자의 정보를 수정합니다          |
| `DELETE`      | `/users/my`                | 비밀번호를 입력하여 로그인한 사용자를 삭제합니다               |
| `GET`         | `/users/deleted`           | 관리자 권한의 사용자가 삭제 요청된 사용자를 조회합니다          |
| `PUT`         | `/users/restore/{id}`      | 관리자 권한의 사용자가 2주 이내 삭제된 사용자를 복구합니다       |
+---------------+----------------------------+----------------------------------------------------+
```

### 📅 일정 관리 (CRUD)
```plaintext
+---------------+---------------------------+-----------------------------------------------------+
| HTTP Method   | URL                       | 설명                                                 |
+---------------+---------------------------+-----------------------------------------------------+
| `POST`        | `/schedules`             | 로그인한 사용자가 새로운 일정을 등록합니다                      |
| `GET`         | `/schedules`             | 로그인한 사용자가 모든 일정을 조회합니다                       |
|               |                           | (사용자 및 제목으로 필터링 가능)                           |
| `PATCH`       | `/schedules/{id}`        | 로그인한 사용자가 비밀번호를 입력하여 일정을 수정합니다            |
| `DELETE`      | `/schedules/{id}`        | 로그인한 사용자가 비밀번호를 입력하여 일정을 삭제합니다            |
+---------------+---------------------------+-----------------------------------------------------+
```
<br><br><br>

## 📑ERD
<img width="587" alt="Image" src="https://github.com/user-attachments/assets/7230d5ea-791e-47ff-80f6-de2496b986fd" />
<br><br><br>

## 📂파일 구조

```
├── src/main/java/com/example/Schedule
│   ├── config
│   │     ├── FilterConfig.java        # Filter 설정
│   │     ├── PasswordEncorder.java    # 비밀번호 암호화 설정
│   │     ├── SwaggerConfig.java       # Swagger 설정
│   ├── controller
│   │     ├── AuthController.java      # 로그인/로그아웃 API 컨트롤러
│   │     ├── ScheduleController.java  # 일정 API 컨트롤러
│   │     ├── UserController.java      # 사용자 API 컨트롤러
│   ├── dto
│   │     ├── login
│   │     │     ├── LoginRequestDto.java     # 로그인 요청 DTO
│   │     ├── schedule
│   │     │     ├── ScheduleRequestDto.java  # 일정 요청 DTO
│   │     │     ├── ScheduleResponseDto.java # 일정 응답 DTO
│   │     ├── user
│   │     │     ├── UserRequestDto.java      # 사용자 요청 DTO
│   │     │     ├── UserResponseDto.java     # 사용자 응답 DTO
│   ├── entity
│   │     ├── BaseEntity.java          # 기본 엔티티 (생성날짜, 수정날짜)
│   │     ├── Role.java                # 권한 설정 이넘
│   │     ├── Schedule.java            # 일정 엔티티
│   │     ├── User.java                # 사용자 엔티티
│   ├── filter
│   │     ├── AuthFilter.java          # 로그인 필터
│   ├── repository
│   │     ├── ScheduleRepository.java  # 일정 데이터베이스 인터페이스
│   │     ├── UserRepository.java  # 사용자 데이터베이스 인터페이스
│   ├── service
│   │     ├── LoginService.java        # 로그인 서비스
│   │     ├── ScheduleService.java     # 일정 서비스
│   │     ├── UserService.java         # 사용자 서비스
│   ├── ScheduleApplication.java       # 프로젝트 시작 파일
```
<br><br><br>

## 📌사용된 기술
<div align=center> 
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"> 
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> 
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>
<br><br><br>

## 📞Contact
프로젝트 관련 문의는 [@Seoyeon](https://github.com/MythologyDevSeoyeon)으로 연락주세요
<br><br><br>

