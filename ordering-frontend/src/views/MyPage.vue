<template>
  <div class="container">
    <div class="page-header text-center" style="margin-top: 20px">
      <h1>회원 정보</h1>
    </div>
    <table class="table">
      <tr>
        <th>이름</th>
        <td>{{ memberInfo.name }}</td>
      </tr>
      <tr>
        <th>도시</th>
        <td>{{ memberInfo.city }}</td>
      </tr>
      <tr>
        <th>상세주소</th>
        <td>{{ memberInfo.street }}</td>
      </tr>
      <tr>
        <th>우편정보</th>
        <td>{{ memberInfo.zipcode }}</td>
      </tr>
    </table>
  </div>
  <OrderListComponent
      :isAdmin="false"
      apiUrl="/member/myorders"
  />
</template>

<script>
import OrderListComponent from "@/components/OrderListComponent.vue";
import axios from "axios";

export default {
  components: {
    OrderListComponent
  },
  data() {
    return {
      memberInfo: {},
    }
  },
  created() {
    this.fetchMember();
  },
  methods:{
    async fetchMember() {
      try {
        const token = localStorage.getItem('token');
        const headers = token ? {Authorization: `Bearer ${token}`} : {};
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/myInfo`, {headers});
        this.memberInfo = response.data;
      } catch (error) {
        console.log(error);
      }
    },
  },
}
</script>