<p align="middle" >
  <img width = "35%" src="https://github.com/pingppung/ContentHub/assets/57535999/6ba89020-f99d-429c-a36f-2defeb60a1eb"/>
</p>
<h1 align="middle"></h1>
<h3 align="middle">멀티미디어 콘텐츠 크롤링 서비스</h3>
<h5 align="middle">프로젝트 기간 : 2024년 1월 28일 ~ 개발 중</h5>
<br/>

# 📝 프로젝트 소개

## Description

`다양한 멀티미디어 콘텐츠(웹소설, 웹툰, 영화, 드라마 등)를 크롤링하고 이를 한 곳에서 효율적으로 제공하는 플랫폼`

이 프로젝트의 주요 목표는 크롤링 기술을 활용하여 멀티미디어 콘텐츠를 수집하고 제공하는 것입니다. 이를 통해 크롤링 기술에 대한 이해를 높이고, Spring Boot와 React를 다시 학습하고자 했습니다.

<br/>

## 기술 스택

- Language : `Java` `JavaScript`
- Library & Framework : `SpringBoot` `React` `Selenium`
- Database : `MongoDB`
- Tool : `IntelliJ`
- 
  <br/>

## 🚨 트러블 슈팅

#### 문제: 웹페이지 구조 변경 시 요소를 찾지 못하는 문제

![image](https://github.com/pingppung/ContentHub/assets/57535999/41f0a45c-50de-4318-b010-ef7b066c0233)
- **상황**: 웹페이지 구조 변경된다면 `By.xpath` 방식으로 요소를 찾지 못하는 문제가 발생했습니다.
- **현재**: 정기적인 코드 업데이트와 테스트로 대응 중입니다.

#### 고민해본 해결 방법

1. **CSS Selectors 사용**: XPath 대신 CSS Selectors를 사용하여 변경에 더 유연하게 대응합니다.
   ```java
   WebElement element = driver.findElement(By.cssSelector("#content ul li"));




