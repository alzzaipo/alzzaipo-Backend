<p align="center">
  <img width="900" alt="image" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/03982b7b-c59d-44bd-bc2c-8f6841779fdc">
  <br>
</p>
<p align="center">
  <img src="https://img.shields.io/badge/Java-orange"/>
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F"/>
  <img src="https://img.shields.io/badge/JPA-6DB33F"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1"/>
  <img src="https://img.shields.io/badge/Redis-DC382D"/>
  <img src="https://img.shields.io/badge/Nginx-009639"/>
  <br>
  <img alt="EC2" src="https://img.shields.io/badge/EC2-FF9900"/>
  <img alt="S3" src="https://img.shields.io/badge/S3-569A31?"/>
  <img alt="RDS" src="https://img.shields.io/badge/RDS-527FFF"/>
  <img alt="ElastiCache" src="https://img.shields.io/badge/ElastiCache-527FFF"/>
  <img alt="CodeDeploy" src="https://img.shields.io/badge/CodeDeploy-527FFF"/>
  <img alt="Route53" src="https://img.shields.io/badge/Route53-8C4FFF"/>
  <img alt="CloudFront" src="https://img.shields.io/badge/CloudFront-8C4FFF"/>
  <img alt="ACM" src="https://img.shields.io/badge/ACM-DC382D"/>
</p>

# 프로젝트 개요

**알짜공모주는 공모주 투자의 진입장벽을 낮추고자, 경험적인 투자 노하우를 바탕으로 기획한 웹 서비스입니다.**

<br>

## 유사 서비스와의 차별점

### **공모주 수익률 분석 기능**
  - 수요예측결과를 기준으로 과거 공모주의 수익률을 분석할 수 있습니다.
  - 과거 유사한 공모주의 평균 수익률을 제공함으로써 사용자의 투자 결정을 돕습니다.
### **사용자 맞춤 알림 서비스**
  - 사용자가 설정한 수요예측결과 기준을 만족하는 공모주를 선별하여 이메일로 청약 알림을 전송합니다.
  - 각 사용자의 투자 기준에 부합하는 청약 정보만을 전달하여, 불필요한 알림으로 인한 사용자의 피로도를 줄이고자 노력했습니다.
>

<br>

# 주요 기능

<details>
  <summary>[설명만 보기]</summary>
  
## 회원가입 및 로그인

- 로컬 회원가입 (ID/PW)
- 소셜 회원가입 (OAuth)
- 간편 로그인 : 로컬 계정과 소셜 서비스 계정간의 연동 지원

## 공모주 분석

- ‘기관 경쟁률’과 ‘의무보유확약비율’을 기준으로 과거 공모주의 수익률 분석 정보 제공 (상장일 시초가 기준)

## 포트폴리오

- 공모주 투자 내역을 관리할 수 있는 포트폴리오 관리 기능
- 총 수익 및 수익률 통계 정보 제공

## 신규 공모주 알림 서비스

- 사용자가 설정한 기준에 부합하는 신규 공모주의 청약 정보를 이메일로 전송해주는 서비스

## 마이 페이지

- 로컬 계정
    - 프로필 관리 : 닉네임, 이메일, 비밀번호 변경 가능
    - 회원 탈퇴 : 비밀번호 인증 요구
- 소셜 계정
    - 프로필 관리 : 닉네임 변경 가능
    - 회원 탈퇴 : 탈퇴 확인 문구 입력 요구
</details>


<details>
  <summary>[화면과 함께 보기]</summary>
  
  ## 회원가입 및 로그인
<div>
<img width="400" alt="로그인 화면" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/e69ff9cc-2b3d-4bb5-bb69-420e9be06229">
<img width="400" alt="회원가입 화면" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/dd4ec03a-e4a6-4670-9e54-e390f9b05d2a">
<div>

- **로컬 회원가입 (ID/PW)**
- **소셜 회원가입 (OAuth)**
- **간편 로그인 : 로컬 계정과 소셜 서비스 계정간의 연동 지원**

<br>

## 공모주 분석
<img width="800" alt="공모주 분석 화면" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/27556ee9-2773-49d3-9879-e96947c85d14">

- **‘기관 경쟁률’과 ‘의무보유확약비율’을 기준으로 과거 공모주의 수익률 분석 정보 제공 (상장일 시초가 기준)**

<br>

## 포트폴리오
<img width="800" alt="포트폴리오 화면" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/9786f7bb-30ae-47aa-8cd5-5d0035e63d46">

- **공모주 투자 내역을 관리할 수 있는 포트폴리오 관리 기능**
- **총 수익 및 수익률 통계 정보 제공**

<br>

## 신규 공모주 알림 서비스
<p>
<img width="400" alt="알림 설정 화면" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/15fc61b1-64e3-41d1-8791-117e8765e63a">
<img width="300" alt="신규 공모주 알림 이메일 화면" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/d023538d-1a1d-471f-8d60-67abcd0317b5">
<p>

- **사용자가 설정한 기준에 부합하는 신규 공모주의 청약 정보를 이메일로 전송해주는 서비스**

<br>

## 마이 페이지
<img width="800" alt="마이페이지 화면" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/43bc53de-c1aa-4b16-a486-7b317a81cfd6">

- **로컬 계정**
    - **프로필 관리 : 닉네임, 이메일, 비밀번호 변경 가능**
    - **회원 탈퇴 : 비밀번호 인증 요구**
- **소셜 계정**
    - **프로필 관리 : 닉네임 변경 가능**
    - **회원 탈퇴 : 탈퇴 확인 문구 입력 요구**
</details>

<br>



# 서비스 개선 노력

### 로그인 유연성 개선
- (위키 작성 예정)

### 이메일 인증 방법 개선
- (위키 작성 예정)

### 헥사고날 아키텍처 전환
- [관련 PR](https://github.com/alzzaipo/alzzaipo-Backend/pulls?q=is%3Apr+is%3Aclosed+label%3AHexagonal)
- (위키 작성 예정)

### 성능 개선
- [관련 PR](https://github.com/alzzaipo/alzzaipo-Backend/pull/90)
- (위키 작성 예정)

<br>

# 다이어그램

### [Infrastructure]
<img width="800" alt="인프라 구조" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/0c3d5b8d-68e5-4c7c-b55c-e8790bc0321f">
<br>

### [CI/CD]
<img width="800" alt="CI/CD 파이프라인" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/5e590e81-ec80-42da-955a-7bea8da9c2fa">
<br>

### [DB Schema]
<img width="800" alt="DB 스키마" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/6a7486bb-e2f4-42b2-a76d-30c8dc2a32da">

### [UseCase Diagram]
- [유스케이스 다이어그램](https://github.com/alzzaipo/alzzaipo-Backend/wiki/%EC%9C%A0%EC%8A%A4%EC%BC%80%EC%9D%B4%EC%8A%A4-%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8)
