const initialState = {
  products: [],
  loading: false,
  error: null,
  pagination: {
    pageNumber: 0,
    pageSize: 0,
    totalElements: 0,
    totalPages: 0,
    lastPage: false
  }
};

export const productReducer = (state = initialState, action) => {
  switch (action.type) {
    case "FETCH_PRODUCTS_REQUEST":
      return {
        ...state,
        loading: true,
        error: null
      };

    case "FETCH_PRODUCTS_SUCCESS":
      return {
        ...state,
        loading: false,
        products: action.payload || [],
        pagination: {
          pageNumber: action.pageNumber,
          pageSize: action.pageSize,
          totalElements: action.totalElements,
          totalPages: action.totalPages,
          lastPage: action.lastPage
        }
      };

    case "FETCH_PRODUCTS_FAILURE":
      return {
        ...state,
        loading: false,
        error: action.payload || 'Failed to fetch products'
      };

    default:
      return state;
  }
};