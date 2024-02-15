<!-- 연습용 파일입니다!! -->

<template>
  <h1>hello world {{ myLang }}</h1>
  <input type="text" value="myValue" />
  <!-- :value는 v-bind:value= 의 축약어 -->
  <!-- v-bind는 input 박스에서 많이 사용하는 단방향 데이터 바인딩 방식 -->
  <input type="text" :value="inputValue1" />
  <!-- v-model 양방향 데이터 바인딩 모델이라는 차이점 -->
  <input type="text" v-model="inputValue2" />
  <!-- onclick과 같은 역할을 하는 것이 @click -->
  <button @click="showValue">변수 변경 사항 확인</button>
  <div>
    <h2>{{count}}</h2>
    <button @click="increment">increment</button>
  </div>
  <div>
    <h2>{{doubleCount}}</h2>
    <button @click="increment">increment</button>
  </div>
</template>
  
<script>
export default {
  data() {
    return {
      myLang: "python",
      inputValue1: "python2",
      inputValue2: "python3",
      count: 0,
    }
  },

  // vue 생명주기에서 인스턴스가 생성되는 시점을 created라고 하고, 화면이 열리기 전에 실행되는 함수
  // api요청을 할 때 많이 사용된다.
  // 대부분 화면이 열리자마자 api호출을 하는 코드가 created에 많이 들어간다.
  created() {
    this.myLang = "java";
  },

  // computed는 종속된 반응형 데이터가 변경될 때만 함수를 다시 실행하여 값을 계산하는 계산함수이다.
  computed: {
    doubleCount() {
      return this.count * 2;
    }
  },

  methods: {
    increment() {
      this.count++;
    },
    showValue() {
      alert(this.inputValue2);
    }
  }
}
</script>