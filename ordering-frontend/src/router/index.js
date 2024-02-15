import {createRouter, createWebHistory} from 'vue-router';
// import HomeComponent from '@/components/HomeComponent.vue';
import ItemList from "@/views/ItemList.vue";
import LoginComponent from '@/views/LoginComponent.vue';
// export default인 경우에는 {}가 필요 없고, 여러 개 요소가 있을 경우에는 {} 필요.
import {memberRoutes} from "./memberRouter.js";
import {orderRoutes} from "./orderRouter.js";
import {itemRoutes} from "./itemRouter.js";
import BasicComponent from "@/components/BasicComponent.vue";

const routes = [
    {
        // url 경로 지정
        path: '/',
        // 현재 라우터의 이름
        name: 'HOME',
        component: ItemList
    },
    {
        // 회원 로그인
        path: '/login',
        name: 'Login',
        component: LoginComponent
    },
    {
        path: '/basic',
        name: 'BasicComponent',
        component: BasicComponent
    },
    // ...은 스프레드 연산자로 불리고, 주로 배열 요소를 다른 배열 요소에 합할 때 사용된다.
    ...memberRoutes,
    ...orderRoutes,
    ...itemRoutes,
];

const router = createRouter({
    // vue router는 내부적으로 두 가지 방식의 히스토리 관리를 제공
    // 대부분의 경우에 createWebHistory를 사용한다.
    // 1) createWebHistory, 2) createHashHistory
    history: createWebHistory(),
    routes
});

export default router;