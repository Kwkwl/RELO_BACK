# RELO_BACK
KOSTA Final Project

1. 프로젝트 Description

- 프로젝트 이름 및 설명
[RELO]
: 상품 리셀 사이트(Kream 벤치마킹 사이트)
(중고 물품을 원하는 가격에 사고 팔 수 있는 웹사이트를 개발하는 프로젝트 입니다.)
- 팀 구성: 5명(조장 1명, 팀원 4명)
- 상세역할: 개발 및 이슈사항 체크, 파트 조율

프로젝트 로고
<p>
  <img width="190" alt="logo" src="https://user-images.githubusercontent.com/112039872/229705047-f29f939d-25bf-4faa-9c09-8d9b9245a367.png">
<p>

2. 프로젝트 정보

- 프로젝트 개발 환경 및 사용 기술

 

3. 프로젝트 결과물

- 기능

1. 회원가입 : 정규표현식을 활용하여 각 입력란마다 유효성 검사 기능 구현, SMS API를 통해 본인 인증 기능 구현
2-1. 일반 로그인 : Session에 회원번호를 저장하고 10분마다 서버측과 통신하여 로그인 유지 기능 구현
2-2. 소셜 로그인 : 카카오 간편 로그인 API를 이용해서 간편 로그인 기능 구현 
3. 아이디 및 비밀번호 찾기 : 정규표현식을 활용하여 각 입력란마다 유효성 검사 기능 구현, Email API를 통해 임시 비밀번호 발급 기능 구현 
4. 회원탈퇴 : 탈퇴 불가한 조건에 미충족되는 회원만 탈퇴할 수 있으며, 모든 탈퇴 약관에 동의했는지 체크하는 기능 구현   
5. 마이페이지(프로필 정보 조회/수정) : 최신 프로필 정보 조회 기능 구현, 휴대폰 번호/비밀번호/이메일 정보 수정 기능 구현, 프로필 사진 변경 및 삭제 기능 구현
6. 공지사항 작성/조회/수정/삭제 : Summernote 에디터를 이용해서 관리자만 공지사항 작성 및 수정 기능 구현, 전체 목록/제목으로 검색/카테고리별 조회 기능 구현

+ 추가 구현 중인 기능
º SSE를 이용한 알림 기능
º 공지사항 제목 검색 + 카테고리 필터 동시 적용된 공지사항 목록 조회 기능

- 결과 예시 화면

<h3> 1. 회원가입 </h3>
<p align="center">
  <img src="https://user-images.githubusercontent.com/112039872/229702829-ca3ef9eb-f093-4f57-ac71-be23082041cd.PNG">
</p>
<p align="center">
  <img src="https://user-images.githubusercontent.com/112039872/229703218-fbab3a2c-58bd-434b-b4e5-c5a95a7831f6.PNG">
</p>
<p align="center">
  <img src="https://user-images.githubusercontent.com/112039872/229706127-6264b43c-284f-4f46-aa1b-27901b281904.jpg">
</p>
<br>

<h3> 2-1. 일반 로그인 </h3>
<p align="center">
  <img src="https://user-images.githubusercontent.com/112039872/229703840-d330c42c-54f9-4d4d-aac8-7976adf27e3a.PNG">
</p>
<br>

<h3> 2-2. 소셜 로그인 </h3>
<p align="center">
  <img src="https://user-images.githubusercontent.com/112039872/229704015-8d6c15e2-a149-4439-9001-f875ee0c4d7d.PNG">
</p>
<br>

<h3> 3. 아이디 및 비밀번호 찾기 </h3>
<p align="center">
  <img src="https://user-images.githubusercontent.com/112039872/229704189-eed72198-e179-48b9-8a74-1e12d7430323.PNG">
</p>
<p align="center">
  <img width="711" alt="임시비밀번호 발급_메일" src="https://user-images.githubusercontent.com/112039872/229704399-54edb786-053e-47db-a71b-a66bd59c57a1.PNG">
</p>
<br>

<h3> 4. 회원탈퇴 </h3>
<p align="center">
  <img width="593" alt="회원탈퇴1" src="https://user-images.githubusercontent.com/112039872/229705903-636f112a-0741-4495-841d-088246022fc6.PNG">
</p>
<p align="center">
  <img width="634" alt="회원탈퇴2" src="https://user-images.githubusercontent.com/112039872/229706045-8d5760af-2918-4276-be75-89e193592dff.PNG">
</p>
<p align="center">
  <img width="565" alt="회원탈퇴3" src="https://user-images.githubusercontent.com/112039872/229706246-3d9d5c6b-6dc5-46a2-ba05-16979a9de24c.PNG">
</p>
<br>

<h3> 5. 마이페이지(프로필 정보 조회/수정) </h3>
<h4> 5-1. 프로필 정보 조회 </h4>
<p align="center">
  <img width="373" alt="마이페이지_프로필" src="https://user-images.githubusercontent.com/112039872/229706814-c25fea7c-7656-43e1-ad3d-bf35540a5cd0.PNG">
</p>

<h4> 5-2. 프로필 정보 수정 </h4>
<p align="center">
  <img width="354" alt="이메일주소,비밀번호 변경창" src="https://user-images.githubusercontent.com/112039872/229706974-c903d0ba-8be3-4f7c-8b22-97a323817210.PNG">
</p>
<p align="center">
  <img width="370" alt="휴대폰 변경창" src="https://user-images.githubusercontent.com/112039872/229706984-9dffcb23-e1d6-4008-8a88-4c45b8802d0a.PNG">
</p>
<br>

<h3> 6. 공지사항 작성/조회/수정/삭제 </h3>
<h4> 6-1. 공지사항 조회 </h4>
<p align="center">
  <img width="655" alt="공지사항 목록_전체" src="https://user-images.githubusercontent.com/112039872/229707433-4727b239-77fe-4892-a954-16e9212f2577.PNG">
</p>
<p align="center">
  <img width="517" alt="공지사항 목록_관리자" src="https://user-images.githubusercontent.com/112039872/229707513-ec8f8d86-6757-421f-9a55-617cb9e5c103.PNG">
</p>
<p align="center">
  <img width="523" alt="공지사항 목록_카테고리별" src="https://user-images.githubusercontent.com/112039872/229707480-2a81ff8c-bd22-42ce-882c-49016df390d0.PNG">
</p>
<p align="center">
  <img width="468" alt="공지사항 목록_검색" src="https://user-images.githubusercontent.com/112039872/229707488-7090dca5-8580-42b9-93a1-a6eed0bf5572.PNG">
</p>

<h4> 6-2. 공지사항 작성 </h4>
<p align="center">
  <img width="867" alt="공지사항 작성" src="https://user-images.githubusercontent.com/112039872/229707783-13b9708e-db2f-46b0-b01c-075be4e43f31.PNG">
</p>
<p align="center">
  <img width="813" alt="공지사항 작성_모달" src="https://user-images.githubusercontent.com/112039872/229707795-da4f66f9-de82-401c-9595-cc1aeb77e1fd.PNG">
</p>

<h4> 6-3. 공지사항 상세 </h4>
<p align="center">
  <img width="413" alt="공지사항 상세" src="https://user-images.githubusercontent.com/112039872/229707928-4ff41afd-be21-4177-a9ad-9df1bfbc739c.PNG">
</p>
<p align="center">
  <img width="531" alt="공지사항 상세_관리자" src="https://user-images.githubusercontent.com/112039872/229707934-8e8a2f9a-3153-4c95-aea9-88bee6bd7338.PNG">
</p>

<h4> 6-4. 공지사항 수정 </h4>
<p align="center">
  <img width="868" alt="공지사항 수정" src="https://user-images.githubusercontent.com/112039872/229708083-4f450b52-f763-43d3-9031-5ef3a0664e56.PNG">
</p>
<p align="center">
  <img width="871" alt="공지사항 수정_모달창" src="https://user-images.githubusercontent.com/112039872/229708096-cf461038-ba18-4175-97ec-3e9c0144eb75.PNG">
</p>
<br>
