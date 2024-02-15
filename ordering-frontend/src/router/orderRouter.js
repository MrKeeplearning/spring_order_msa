import OrderList from '@/views/OrderList.vue'
import OrderCart from "@/views/OrderCart.vue";

export const orderRoutes = [
    {
        // 회원 목록
        path: '/orders',
        name: 'OrderList',
        component: OrderList,
    },
    {
        path: '/ordercart',
        name: 'OrderCart',
        component: OrderCart,
    },
];