import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router/index.js'
import store from './store/cart.js'
import '@/assets/css/bootstrap.min.css'
import axios from "axios";

// index.html의 id인 app에 마운트가 되도록 하는 코드
// createApp(App).mount('#app')
const app = createApp(App);

// 401 응답의 경우 interceptor를 통해서 공통적으로 토큰 제거 후 로그아웃 처리
axios.interceptors.response.use(response => response, error => {
    if (error.response && error.response.status === 401) {
        localStorage.clear();
        window.location.href = "/login";
    }
    // 401 에러 외의 나머지 에러는 무시한다.
    return Promise.reject(error);
})

// vue 애플리케이션에서 전역적으로 기능 사용할 경우에 use 문법을 사용한다.
app.use(router);    // 라우터를 어디에서든 사용할 수 있게 만든다.
app.use(store);
app.mount('#app');