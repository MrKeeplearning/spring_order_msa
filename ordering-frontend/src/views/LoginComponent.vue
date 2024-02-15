<template>
  <div class="container">
    <div class="page-header text-center" style="margin-top: 20px">
      <h1>로그인</h1>
    </div>
    <!-- submit은 기본적으로 폼 제출시 브라우저가 페이지를 새로고침하므로 해당 동작을 막기 위해 prevent를 사용 -->
    <form @submit.prevent="doLogin">
      <div class="form-group">
        <label for="email">email : </label>
        <input type="email" v-model="email" class="form-control">
      </div>
      <div class="form-group">
        <label for="password">password : </label>
        <input type="password" v-model="password" class="form-control">
      </div>
      <div class="form-group">
        <button type="submit" class="btn btn-primary mt-3">로그인</button>
      </div>

    </form>
  </div>
</template>
<script>
import axios from 'axios';
// 멀티 라이브러리를 꺼낼 때 주로 중괄호를 두른다.
// jwt-decode에서 export 시에 중괄호 넣어서 export하였기 때문에, import할 때도 {} 넣어서 import
import {jwtDecode} from 'jwt-decode';

export default {
  data() {
    return {
      email: "",
      password: ""
    }
  },
  methods: {
    async doLogin() {
      // 2가지 예외 가능성:
      // 1) 200번대 상태값이면서 token이 비어있는 경우
      // -> 매우 예외에 해당되는 상황
      // 2) 200번대 상태값이 아닌 경우
      try {
        const loginData = {email: this.email, password: this.password};
        const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/doLogin`, loginData);
        const token = response.data.result.token;
        if (token) {
          const decoded = jwtDecode(token);
          console.log(decoded);
          localStorage.setItem("token", token);
          localStorage.setItem("role", decoded.role);
          // created 함수는 reload될 때 1번만 실행되는 hook함수
          // 그런데, router.push를 통한 화면전환은 reload를 실행시키지 않으므로, created함수 호출이 되지 않음.
          // this.$router.push("/");
          window.location.href = "/";
        } else {
          console.log("200 ok, but token doesn't exist");
          alert("Login Failed");
        }
      } catch (error) {
        const err_msg = error.response.data.error_message;
        if (err_msg) {
          console.log(err_msg);
          alert(err_msg);
        } else {
          console.log(error);
          alert("Login Failed");
        }
      }
    }
  },
}
</script>