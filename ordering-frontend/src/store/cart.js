// npm install vuex(라이브러리) => store 계층 파일(cart.js)
import { createStore } from 'vuex';

// initState, updateLocalStorage와 같은 함수는 스토어 정의 바깥에 위치한다.
// 책임과 권한을 분리하는 개념적인 의도도 있지만, 다른 스토어나 다른 상황에서 재사용성을 높이기 위한 아키텍처.
function initState() {
    return {
        // JSON 객체를 자바스크립트로 변환
        cartItems: JSON.parse(localStorage.getItem('cartItems')) || [],
        totalQuantity: localStorage.getItem('totalQuantity') || 0,
    }
}

function updateLocalStorage(cartItems, totalQuantity) {
    localStorage.setItem('cartItems', JSON.stringify(cartItems));
    localStorage.setItem('totalQuantity', totalQuantity);
}

export default createStore ({
    // state: 상태를 의미하는 객체로서 initState 를 통해 상태를 초기화
    state: initState,

    // mutations는 상태를 변경하는 함수들의 집합이다.
    // vuex에서 commit이라는 용어는 상태변경을 위해 mutation을 호출하는 과정을 의미한다.
    mutations: {
        // addToCart 함수는 외부 컴포넌트(또는 action)에서 호출될 예정.
        addToCart(state, item) {
            const existItem = state.cartItems.find(i => i.itemId == item.itemId);

            if (existItem) {
                existItem.count += item.count;
            } else {
                state.cartItems.push(item);
            }

            // totalCount
            state.totalQuantity = parseInt(state.totalQuantity) + item.count;
            console.log(state.totalQuantity);
            updateLocalStorage(state.cartItems, state.totalQuantity);
        },

        clearCart(state) {
            state.cartItems = [];
            state.totalQuantity = 0;
            updateLocalStorage([], 0);
        },
    },

    // actions를 통해서 여러 뮤테이션을 연속적으로 커밋하거나, 비동기작업을 진행.
    // 일반적으로 컴포넌트에서 actions의 메서드를 호출하고, actions에서 mutation 메서드를 호출한다.
    actions: {
        // context 매개변수가 주입. context 매개변수 안에 state, commit 등이 존재한다.
        addToCart(context, item) {
            context.commit('addToCart', item);
        },
        clearCart(context) {
            context.commit('clearCart');
        },
    },

    // getters: 상태를 반환하는 함수들의 집합
    getters: {
        getCartItems: state => state.cartItems,
        getTotalQuantity: state => state.totalQuantity
    }
});