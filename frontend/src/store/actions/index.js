import api from "../../api/api";

export const fetchProducts = () => async (dispatch) => {
  dispatch({ type: "FETCH_PRODUCTS_REQUEST" });
  try{
    const {data} = await api.get("/public/products");
    dispatch({
      type: "FETCH_PRODUCTS_SUCCESS",
      payload: data.content,
      pageNumber: data.pageNumber,
      pageSize: data.pageSize,
      totalElements: data.totalElements,
      totalPages: data.totalPages,
      lastPage: data.lastPage
    })
  } catch(error) {
    // surface the error for debugging and update state
    // eslint-disable-next-line no-console
    console.error(error);
    dispatch({ type: "FETCH_PRODUCTS_FAILURE", payload: error?.message || 'Failed to fetch products' });
  }
  
}