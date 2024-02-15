<template>
  <div class="container">
    <div class="page-header text-center" style="margin-top: 20px"><h1>주문 목록</h1></div>
    <table class="table">
      <thead>
      <th>#</th>
      <th>회원 이메일</th>
      <th>주문 상태</th>
      <th v-if="isAdmin === true">ACTION</th>
      </thead>
      <tbody>
      <template v-for="order in orderList" :key="order.id">
        <tr @click="toggleOrder(order.id)" style="cursor: pointer">
          <td>{{ order.id }}</td>
          <td>{{ order.memberEmail }}</td>
          <td>{{ order.ordersStatus }}</td>
          <td v-if="isAdmin === true">
            <button v-if="order.ordersStatus === 'ORDERED'" @click.stop="cancelOrder(order.id)"> CANCEL</button>
          </td>
        </tr>
        <tr v-if="visibleOrder.has(order.id)">
          <td colspan="4">
              <span v-for="orderItem in order.orderItems" :key="orderItem.id">
                {{ orderItem.itemName }} {{ orderItem.count }}개
              </span>
          </td>
        </tr>
      </template>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: ['isAdmin', 'apiUrl'],
  data() {
    return {
      orderList: [],
      visibleOrder: new Set(),
    }
  },
  async created() {
    try {
      const token = localStorage.getItem('token');
      const headers = token ? {Authorization: `Bearer ${token}`} : {};
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}${this.apiUrl}`, {headers});
      this.orderList = response.data;
    } catch (error) {
      console.log(error);
    }
  },
  methods: {
    toggleOrder(orderId) {
      if (this.visibleOrder.has(orderId)) {
        this.visibleOrder.delete(orderId);
      } else {
        this.visibleOrder.add(orderId);
      }
    },
    async cancelOrder(orderId) {
      if (confirm("정말 취소하시겠습니까?")) {
        try {
          const token = localStorage.getItem('token');
          const headers = token ? {Authorization: `Bearer ${token}`} : {};
          await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/order/${orderId}/cancel`, {headers});
          const order = this.orderList.find(order => order.id === orderId);
          order.ordersStatus = "CANCELED";
        } catch (error) {
          console.log(error);
          alert("주문취소를 실패했습니다.");
        }
      }
    },
  },

}
</script>