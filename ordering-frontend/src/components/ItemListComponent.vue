<template>
  <div class="container">
    <div class="page-header text-center" style="margin-top: 20px"><h1>{{ pageTitle }}</h1></div>
    <div class="d-flex justify-content-between" style="margin-top:20px;">
      <form @submit.prevent="searchItems">
        <select v-model="searchType" class="form-control"
                style="display: inline-block; width: auto; margin-right: 5px;">
          <option value="name" selected>선택</option>
          <option value="name">상품명</option>
          <option value="category">카테고리</option>
        </select>
        <input v-model="searchValue" type="text" class="form-control" placeholder="검색어를 입력하세요"
               style="display: inline-block; width: 200px; margin-right: 5px;"/>
        <button type="submit" class="btn btn-outline-primary mb-1"
                style="display: inline-block; width: auto; margin-right: 5px;">
          검색
        </button>
      </form>
      <div v-if="!isAdmin">
        <button @click="addCart" class="btn btn-secondary">장바구니</button>
        <button @click="placeOrder" class="btn btn-success">주문하기</button>
      </div>
      <div v-if="isAdmin">
        <a href="/item/create"><button class="btn btn-success">상품 등록</button></a>
      </div>
    </div>
    <table class="table">
      <thead>
      <tr>
        <th v-if="!isAdmin"></th>
        <th>상품사진</th>
        <th>상품명</th>
        <th>가격</th>
        <th>재고 수량</th>
        <th v-if="!isAdmin">주문 수량</th>
        <th v-if="isAdmin">Action</th>
        <th>id</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in itemList" :key="item.id">
        <!-- 체크박스를 선택하면 value가 true가 담기게 됨. -->
        <td v-if="!isAdmin"><input type="checkbox" v-model="selectedItems[item.id]"/></td>
        <td><img :src="getImage(item.id)" style="height: 100px; width: auto;"/></td>
        <td>{{ item.name }}</td>
        <td>{{ item.price }}</td>
        <td>{{ item.stockQuantity }}</td>
        <td v-if="!isAdmin">
          <!-- 실제 데이터베이스와 달리 item.quantity를 자바스크립트 자체에서 td로 추가시킨다. -->
          <input type="number" min="0" v-model="item.quantity" style="width: 60px"/>
        </td>
        <td v-if="isAdmin">
          <button @click="deleteItem(item.id)" class="btn btn-danger">삭제</button>
        </td>
        <td>{{ item.id }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';
import {mapActions} from "vuex";

export default {
  props: [ 'isAdmin', 'pageTitle' ],
  data() {
    return {
      itemList: [],
      pageSize: 5,
      currentPage: 0,
      searchType: 'name',
      searchValue: '',
      isLastPage: false,
      isLoading: false,
      selectedItems: {},
    }
  },

  created() {
    this.loadItems();
  },
  //mounted: window dom 객체가 생성된 이후에 실행되는 hook함수
  mounted() {
    // scroll 동작이 발생할 때마다 scrollPagination 함수 호출된다는 의미
    window.addEventListener('scroll', this.scrollPagination);
  },
  methods: {
    // actions 직접 호출 방식 관련
    ...mapActions(['addToCart']),

    addCart() {
      const orderItems = Object.keys(this.selectedItems)
                              .filter(key => this.selectedItems[key] === true)
                              .map(key => {
                                const item = this.itemList.find(item => item.id == key);
                                return {itemId: item.id, name: item.name, count: item.quantity};
                              });
      // mutation 직접 호출 방식
      // orderItems.forEach(item => this.$store.commit('addToCart', item));
      // actions 직접 호출 방식
      orderItems.forEach(item => this.$store.dispatch('addToCart', item));
      this.selectedItems = [];
    },

    async deleteItem(itemId) {
      // delete id item
      const token = localStorage.getItem('token');
      const headers = token ? {Authorization: `Bearer ${token}`} : {};
      if (confirm("정말로 삭제하시겠습니까?")) {
        try {
          await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/item/${itemId}/delete`, {headers});
          alert("상품 삭제가 완료되었습니다.");
          window.location.reload();
        } catch (error) {
          console.log(error);
          alert("상품 삭제에 실패했습니다.")
        }
      }
    },

    async placeOrder() {
      // 데이터 형식은 아래와 같다. 이런 메서드를 선언할 때는 로그를 찍어보면서 확인 후 작성한다.
      // {1: false, 2: false, 3: true, 4: true, 6: true, 10: true}
      console.log(this.selectedItems);




      // Object.keys : 위의 데이터구조에서 1, 2 등 key 값만 추출하는 메서드.
      const orderItems = Object.keys(this.selectedItems)
          .filter(key => this.selectedItems[key] === true)
          .map(key => {
            const item = this.itemList.find(item => item.id == key);
            return {itemId: item.id, count: item.quantity};
          });

      if (orderItems.length < 1) {
        alert("장바구니에 물건이 없습니다.")
        return;
      }

      if (!confirm(`${orderItems.length}개의 상품을 주문하시겠습니까?`)) {
        console.log("주문이 취소되었습니다.");
        return;
      }


      console.log(typeof orderItems);
      const token = localStorage.getItem('token');
      const headers = token ? {Authorization: `Bearer ${token}`} : {};
      try {
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/order/create`, orderItems, {headers});
        console.log(orderItems);
        alert("주문이 완료되었습니다.");
        window.location.reload();
      } catch (error) {
        console.log(error);
        alert("주문이 실패되었습니다.");
      }

    },

    scrollPagination() {
      // innerHeight : 브라우저 창의 내부높이를 픽셀단위로 변환
      // scrollY : 스크롤을 통해 Y축을 이동한 거리
      // offsetHeight : 전체 브라우저 창의 높이
      const nearBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - 500;
      if (nearBottom && !this.isLastPage && !this.isLoading) {
        this.currentPage++;
        this.loadItems();
      }
    },

    // 스크롤로 내리다보면 itemList와 currentPage의 값이 증가하여 검색 기능 활용 시 검색 결과를 itemList에 넣는 작업에 실패한다.
    // 따라서, 스크롤로 채워진 itemList와 currentPage를 비워주어 검색 결과를 itemList에 담아 화면에 띄워줄 수 있게 한다.
    searchItems() {
      this.itemList = [];
      this.selectedItems = [];
      this.currentPage = 0;
      this.isLastPage = false;
      this.loadItems();
    },

    getImage(id) {
      return `${process.env.VUE_APP_API_BASE_URL}/item/${id}/image`;
    },

    async loadItems() {
      this.isLoading = true;
      try {
        // params 키워드를 사용해야 파라미터 방식으로 axios 요청 가능
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          [this.searchType]: this.searchValue, // 아래의 if-else 문과 같은 역할을 한다.
        }
        // if (this.searchType === "name") {
        //   params.name = this.searchValue;
        // } else if (this.searchType === "category") {
        //   params.category = this.searchValue;
        // }
        console.log(params);
        const response = await axios.get(
            `${process.env.VUE_APP_API_BASE_URL}/items`, {params});
        const additionalItemList = response.data.map(item => ({...item, quantity: 1}));
        if (additionalItemList.length < this.pageSize) {
          this.isLastPage = true;
        }
        this.itemList = [...this.itemList, ...additionalItemList];
      } catch (error) {
        console.log(error);
      }
      this.isLoading = false;
    },
  },
}
</script>