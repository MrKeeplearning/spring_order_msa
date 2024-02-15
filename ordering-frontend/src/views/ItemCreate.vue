<template>
  <div class="container">
    <div class="page-header text-center" style="margin-top: 20px">
      <h1>상품 등록</h1>
    </div>
    <form @submit.prevent="itemCreate">
      <div class="form-group">
        <label for="name">상품명</label>
        <input type="text" v-model="name" class="form-control">
      </div>
      <div class="form-group">
        <label for="category">카테고리</label>
        <input type="text" v-model="category" class="form-control">
      </div>
      <div class="form-group">
        <label for="price">가격</label>
        <input type="number" v-model="price" class="form-control">
      </div>
      <div class="form-group">
        <label for="stockQuantity">재고수량</label>
        <input type="number" v-model="stockQuantity" class="form-control">
      </div>
      <div class="form-group">
        <label for="stockQuantity">상품이미지</label>
        <!-- @change 와 @click 비교:
             @click 은 요소가 클릭될 때마다 이벤트 함수 실행.
             @change 는 해당 태그의 값이 변할 때 함수를 실행.
             따라서, click 으로 두면 파일 업로드가 되지 않았음에도 함수가 실행되는 문제가 발생한다.
        -->
        <input type="file" class="form-control" accept="image/*" @change="fileUpload">
      </div>
      <div class="form-group">
        <button type="submit" class="btn btn-primary mt-3">등록</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      name:"",
      category:"",
      price: null,
      stockQuantity: null,
      itemImage: null
    }
  },
  methods: {
    fileUpload(event) {
      // event.target : 이벤트가 발생한 DOM 요소를 가리키는 객체이다. Change 이벤트가 발생한 상태를 알려준다.
      // files : 해당 이벤트에서 0번째 파일을 가져오겠다는 뜻이다.
      this.itemImage = event.target.files[0];
    },
    async itemCreate() {
      try {
        // multi-part formdata 형식의 경우 new FormData를 통해서 객체를 생성한다.
        const registerData = new FormData();
        registerData.append("name", this.name);
        registerData.append("category", this.category);
        registerData.append("price", this.price);
        registerData.append("stockQuantity", this.stockQuantity);
        registerData.append("itemImage", this.itemImage);

        const token = localStorage.getItem('token');
        const headers = token ? { Authorization: `Bearer ${token}` } : {};

        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/item/create`, registerData, {headers})

        this.$router.push("/items/manage");

      } catch (error) {
        const err_msg = error.response.data.error_message;
        if (err_msg) {
          console.log(err_msg);
          alert(err_msg);
        } else {
          console.log(error);
          alert("입력값 확인 필요");
        }
      }
    },
  }
}
</script>
