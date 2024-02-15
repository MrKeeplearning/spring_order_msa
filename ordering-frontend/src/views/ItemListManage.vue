<template>
  <ItemListComponent
      :isAdmin="true"
      pageTitle="상품 관리"
  />
</template>

<script>
import ItemListComponent from "@/components/ItemListComponent.vue";

export default {
  components: {ItemListComponent}
}
</script>