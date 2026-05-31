import React, { useEffect } from 'react'
import ProductCard from './ProductCard.jsx'
import { FaExclamationTriangle } from 'react-icons/fa';
import { useDispatch, useSelector } from 'react-redux';
import { fetchProducts } from '../store/actions/index.js';


const Products = () => {


  const { products = [], loading: isLoading, error: errorMessage } = useSelector((state) => state.products);

  const dispatch = useDispatch();
  useEffect(() => {dispatch(fetchProducts())}, [dispatch]);

  
 


  return (
    <div className='lg:px-14 sm:px-9 px-4 py-14 2xl:w-[90%] 2xl:mx-auto'>
      {
        isLoading ? (
               <p>It is loading...</p>
              ) : errorMessage ? (
               <div className='flex justify-center item-center h-[200]'>
                <FaExclamationTriangle className='text-slate-800 text-3xl mr-2'/>
                <span className='text-slate-800 text-lg font-medium'>
                  {errorMessage}
                </span>
               </div>  
              ) : (
                <div className='min-h-[700]'>
                  <div className='pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6'>
                       {products && products?.map((item, idx) => (<ProductCard key={idx} {...item} />
))}
                  </div>
                </div>
              )
     }
    </div>
  );
}

export default Products;