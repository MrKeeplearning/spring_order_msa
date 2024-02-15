<template>
  <nav class="navbar navbar-expand-lg bg-dark navbar-dark">
    <div class="navbar-collapse w-100 order-1 order-md-0 dual-collapse2">
      <ul class="navbar-nav mr-auto" v-if="userRole === 'ADMIN'">
        <li class="nav-item"><a class="nav-link" href="/members">회원관리</a></li>
        <li class="nav-item"><a class="nav-link" href="/items/manage">상품관리</a></li>
        <li class="nav-item"><a class="nav-link" href="/orders">주문관리</a></li>
      </ul>
    </div>
    <div class="mx-auto order-0">
      <a class="navbar-brand mx-auto" href="/">java shop</a>
    </div>
    <div class="navbar-collapse w-100 order-3 dual-collapse2">
      <ul class="navbar-nav ml-auto">
        <!-- getTotalQuantity: getter 함수명을 명시 -->
        <li class="nav-item" v-if="isLogin"><a class="nav-link" href="/ordercart">장바구니({{getTotalQuantity}})</a></li>
        <li class="nav-item"><a class="nav-link" href="/items">상품목록</a></li>
        <li class="nav-item" v-if="isLogin"><a class="nav-link" href="/mypage">My Page</a></li>
        <li class="nav-item" v-if="!isLogin"><a class="nav-link" href="/member/create">회원가입</a></li>
        <li class="nav-item" v-if="!isLogin">
          <a class="nav-link" href="/login">로그인</a>
        </li>
        <li class="nav-item" v-if="isLogin">
          <a class="nav-link" href="#" @click="doLogout">로그아웃</a>
        </li>
      </ul>
    </div>
  </nav>
</template>
<script>
// store에 getters 함수를 사용하기 위한 import
import {mapGetters} from "vuex";
export default {
  //
  computed: {
    ...mapGetters(['getTotalQuantity'])
    // ... 연산자를 통해 getter를 통해 반환되는 객체의 속성들을 현재 객체 내에 풀어서(spread) 넣기 위함
    // getTotalQuantity: function() {
    //   return this.$store.totalQuantity;
    // }

  },
  data() {
    return {
      isLogin: false,
      userRole: null
    }
  },
  created() {
    if (localStorage.getItem("token")) {
      this.isLogin = true;
      this.userRole = localStorage.getItem("role");
    }
  },
  methods: {
    doLogout() {
      localStorage.clear();
      // 로그아웃 후 새로고침을 해서 해당 component에 대한 상태값을 초기화시켜 확실하게 로그아웃시킨다.
      window.location.reload();
    }
  },
}
</script>