<template>
  <HeaderComponent />
  <div>
    <router-view />
  </div>
  <FooterComponent />
</template>

<script>
// import HomeComponent from "./components/HomeCompo.vue"; // 절대경로
// import HomeComponent from "@/components/HomeCompo.vue"; // 상대경로 main.js 기준
import HeaderComponent from "@/components/HeaderComponent.vue";
import FooterComponent from "@/components/FooterComponent.vue";

export default {
  components: {
    // HomeComponent,
    HeaderComponent,
    FooterComponent
  }
}
</script>