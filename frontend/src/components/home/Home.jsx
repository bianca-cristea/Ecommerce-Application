import React, { useEffect } from 'react'
import HeroBanner from './HeroBanner'
import { useDispatch, useSelector } from 'react-redux'
import { fetchProducts } from '../../store/actions/actions';

const Home = () => {
  const dispatch = useDispatch();
  const {products} = useSelector((state) => state.products)
 
  
  useEffect(() => {
    dispatch(fetchProducts())
  }, [dispatch])

  return (
    <div>
      <HeroBanner/> 
      <div>
        {products && 
        products?.slice(0,8)
             .map((item,i) => <ProductCard key={i} {...item}/>)}
      </div>
    </div> 
  )
}

export default Home
