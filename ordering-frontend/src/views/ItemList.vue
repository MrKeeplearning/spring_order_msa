<template>
  <ItemListComponent
      :isAdmin="false"
      pageTitle="상품 목록"
  />
</template>

<script>
import ItemListComponent from "@/components/ItemListComponent.vue";

export default {
  components: {ItemListComponent}

}
</script>