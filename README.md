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

## 프로젝트 개요

**알짜공모주는 공모주 투자의 진입장벽을 낮추고자, 경험적인 투자 노하우를 바탕으로 기획한 웹 서비스입니다.**

<br>

## 주요 기능

#### 공모주 수익률 분석 기능
  - 수요예측결과를 기준으로 과거 공모주의 수익률을 분석합니다.
  - 과거 유사한 공모주의 평균 수익률을 제공함으로써 사용자의 투자 결정을 돕습니다.
#### 사용자 맞춤 알림 서비스
  - 사용자가 설정한 수요예측결과 기준을 만족하는 공모주를 선별하여 청약 알림 이메일을 전송합니다.
  - 각 사용자의 투자 기준에 부합하는 청약 알림만을 전송하여, 사용자의 피로도를 줄이고자 노력했습니다.
>

<br>

## 구현 기능
- [구현 기능](https://github.com/alzzaipo/backend/wiki/%EA%B5%AC%ED%98%84-%EA%B8%B0%EB%8A%A5)
- [유스케이스 다이어그램](https://github.com/alzzaipo/alzzaipo-Backend/wiki/%EC%9C%A0%EC%8A%A4%EC%BC%80%EC%9D%B4%EC%8A%A4-%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8)

## 개선 노력
- [이메일 인증 로직 개선](https://github.com/alzzaipo/backend/wiki/%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%9D%B8%EC%A6%9D-%EB%B0%A9%EB%B2%95-%EA%B0%9C%EC%84%A0)
- [헥사고날 아키텍처 전환](https://github.com/alzzaipo/alzzaipo-Backend/pulls?q=is%3Apr+is%3Aclosed+label%3AHexagonal)
- [서비스 로직 성능 개선](https://github.com/alzzaipo/backend/wiki/%EC%84%9C%EB%B9%84%EC%8A%A4-%EB%A1%9C%EC%A7%81-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0)
- [로그인 취약점 개선 - 무차별 대입 공격](https://github.com/alzzaipo/backend/wiki/%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EB%AC%B4%EC%B0%A8%EB%B3%84-%EB%8C%80%EC%9E%85-%EA%B3%B5%EA%B2%A9-%EC%B7%A8%EC%95%BD%EC%A0%90-%EA%B0%9C%EC%84%A0)

<br>

## 다이어그램

### Infrastructure
<img width="600" alt="인프라 구조" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/0c3d5b8d-68e5-4c7c-b55c-e8790bc0321f">
<br>

### Deployment
<img width="600" alt="CI/CD 파이프라인" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/5e590e81-ec80-42da-955a-7bea8da9c2fa">
<br>

### Database
<img width="600" alt="DB 스키마" src="https://github.com/alzzaipo/alzzaipo-Backend/assets/107951175/6a7486bb-e2f4-42b2-a76d-30c8dc2a32da">

