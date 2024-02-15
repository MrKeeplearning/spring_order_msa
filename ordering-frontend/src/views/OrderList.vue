<template>
  <OrderListComponent
  :isAdmin="true"
  apiUrl="/orders"
  />
</template>

<script>
import OrderListComponent from "@/components/OrderListComponent.vue";

export default {
  components:{
    OrderListComponent
  }
}
</script>