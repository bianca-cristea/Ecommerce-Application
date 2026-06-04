import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { fetchProducts } from "../store/actions/actions";

const useProductFilter = () => {
  const [searchParams] = useSearchParams();
  const dispatch = useDispatch();

  useEffect(() => {
    const params = new URLSearchParams();

    const currentPage = searchParams.get("page") ?
     Number(searchParams.get("page")) 
     : 1

    params.set("pageNumber", currentPage - 1)

    const sortOrder = searchParams.get("sortBy") || "asc"
    const keyword = searchParams.get("keyword") || null
    const categoryId = searchParams.get("category") || null

    params.set("sortBy", "price")
    params.set("sortOrder", sortOrder)

    if(keyword){
      params.set("keyword", keyword)
    }

    const queryString = params.toString() 
    console.log("QUERY STRING", queryString);

   if (categoryId) {
      dispatch(fetchProducts(`/public/categories/${categoryId}/products?pageSize=2&${queryString}`))
    } else {
      dispatch(fetchProducts(`/public/products?pageSize=2&${queryString}`))
    }
    

  }, [dispatch, searchParams])
}

export default  useProductFilter;
