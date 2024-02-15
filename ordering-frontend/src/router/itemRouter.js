import ItemList from '@/views/ItemList.vue'
import ItemListManage from "@/views/ItemListManage.vue";
import ItemCreate from "@/views/ItemCreate.vue";

export const itemRoutes = [
    {
        // 회원 목록
        path: '/items',
        name: 'ItemList',
        component: ItemList,
    },
    {
        path: '/items/manage',
        name: 'ItemListManage',
        component: ItemListManage
    },
    {
        path: '/item/create',
        name: 'ItemCreate',
        component: ItemCreate,
    }
];