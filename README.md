# Springboot 와 React, AWS 로 구현한 Todo😀

Back-end 와 Front-end 를 분리해보고 싶어서 제작해본 Todo Application 입니다.  

제작한 Application 을 AWS 에 배포했습니다. 배포 시 단순히 자동 할당 주소를 사용하지 않고 엘라스틱 빈스톡을 활용하여 **로드 밸런서, 오토스케일링그룹** 등 스케일링에 필요한 서비스를 사용했습니다.

어플리케이션 접속 시 login 화면으로 리다이렉트 됩니다.
회원가입 후 로그인을 하면 **Todo 리스트**를 작성할 수 있습니다. Todo 리스트는 기본적인 CRUD 가 가능합니다.



<h2 id="table-of-contents">🔎 목차</h2>
<details open="open">
  <summary> 목차</summary>
  <ol>
    <li><a href="#skill"> 🔧 기술 스택</a></li>
    <li><a href="#role"> 📜 역할 분담</a></li>
    <li><a href="#view"> 👀 뷰 페이지</a></li>
  </ol>
</details>


<h2 id="skill">🔧 기술 스택</h2>

---

- Front-end: React.js(Material-Ui)

- Back-end: Spring-boot

- DB: MySql, H2

- Cloud Computing: AWS, Elastic Beanstalk


<h2 id="role">📜 역할 분담</h2>

---

Front-end 💙 [`melonicedlatte`](https://github.com/melonicedlatte)

Back-end 💛 [`minmi`](https://github.com/minji0123)


<h2 id="view">👀 뷰 페이지</h2>

---

### 로그인 & 회원가입 페이지

어플리케이션 접속 시 login 화면으로 리다이렉트 됩니다.

![''](/image/login.png)

![''](/image/signup.png)


### TODO 리스트 페이지

회원가입 후 로그인을 하면 **Todo 리스트**를 작성할 수 있습니다.    
Todo 리스트는 기본적인 CRUD 가 가능합니다.   

![''](/image/main.png)

![''](/image/add.png)
