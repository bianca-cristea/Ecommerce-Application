import { configureStore } from "@reduxjs/toolkit";
import { productReducer } from "./productReducer";
import { errorReducer } from "./errorReducer";



const store = configureStore({
    reducer: {
        products: productReducer,
        errors: errorReducer
    },
    preloadedState: {}
});

export default store;