import React, { useEffect, useState } from 'react'
import {FiArrowDown, FiArrowUp, FiRefreshCw, FiSearch} from "react-icons/fi";
import {FormControl, InputLabel, MenuItem, Select, Tooltip, Button} from '@mui/material'
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';

const Filter = ({categories}) => {
  const [searchParams] = useSearchParams();
  const params = new URLSearchParams(searchParams);
  const pathName = useLocation().pathname; 
  const navigate = useNavigate();

  const [category, setCategory] = useState("all");
  const [sortOrder, setSortOrder] = useState("asc");
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    const currentCategory = searchParams.get("category") || "all";
    const currentSortOrder = searchParams.get("sortBy") || "asc";
    const currentSearchTerm = searchParams.get("keyword") || "";

    setCategory(currentCategory)
    setSortOrder(currentSortOrder)
    setSearchTerm(currentSearchTerm)
  }, [searchParams])

  useEffect(() => {
    const handler = setTimeout(() => {
      const newParams = new URLSearchParams(searchParams);
      if(searchTerm){
        newParams.set("keyword", searchTerm.toString())
      } else {
        newParams.delete("keyword")
      }
      navigate(`${pathName}?${newParams.toString()}`)
    }, 700);

    return () => clearTimeout(handler)
  }, [searchTerm,categories])  

  const handleCategoryChange = (event) => {
    const selectedCategory = event.target.value;
    const newParams = new URLSearchParams(searchParams);
    
    if(selectedCategory === "all"){
      newParams.delete("category")
    } else {
      newParams.set("category", selectedCategory)  
    }

    navigate(`${pathName}?${newParams}`) 
    setCategory(selectedCategory)
  }

  const toggleSortOrder = () => {
    setSortOrder((prevOrder) => {
      const newOrder = prevOrder === "asc" ? "desc" : "asc";
      const newParams = new URLSearchParams(searchParams);
      newParams.set("sortBy", newOrder);
      navigate(`${pathName}?${newParams}`)
      return newOrder;
    })
  }

  const handleClearFilters = () => {
    navigate(pathName)  
  }

  return (
    <div className='flex lg:flex-row flex-col-reverse lg:justify-between justify-center items-center gap-4'>
        <div className='relative flex items-center w-2/4'>
          <input 
              type='text' 
              placeholder='Search product' 
              value={searchTerm} 
              onChange={(e) => setSearchTerm(e.target.value)} 
              className='border border-gray-400 text-slate-800 rounded-md py-2 pl-10 pr-4 w-full focus:outline-none focus:ring-2 focus:ring-[#1976d2]'/>
          <FiSearch className="absolute left-3 text-slate-800 w-5 h-5" />
        </div>

        <div className='flex sm:flex-row flex-col w-auto gap-4 items-center'>
          <FormControl variant='outlined' size='small' className='text-slate-800 border-slate-700'>
              <InputLabel id="category-select-label">Category</InputLabel>
              <Select 
                  labelId="category-select-label" 
                  value={category}
                  onChange={handleCategoryChange}
                  label="Category"
                  className="min-w-[120] text-slate-800 border-slate-700">
                    <MenuItem value="all">All</MenuItem>
                    {categories.map((item) => 
                      <MenuItem key={item.categoryId} value={item.categoryId}> {/* ✅ value=categoryId */}
                          {item.categoryName}
                      </MenuItem>)}
              </Select>
          </FormControl>

          <Tooltip title={`Sorted by price: ${sortOrder === "asc" ? "asc" : "desc"}`}>
             <Button variant='contained' 
                     onClick={toggleSortOrder}
                     color='primary' 
                     className="flex items-center gap-2 h-10">
                     <span className='text-xs'>Sort by</span>
                     {sortOrder === "asc" ? <FiArrowUp size={20} /> : <FiArrowDown size={20}/>}
             </Button>
          </Tooltip>

          <button className='cursor-pointer flex items-center gap-2 bg-rose-900 text-white px-3 py-2 rounded-md transition duration-300 ease-in shadow-md focus:outline-none'
                  onClick={handleClearFilters}>
                <FiRefreshCw className='font-semibold' size={16}/>
                <span className='font-semibold text-xs'>Clear filter</span>
          </button>
        </div>
    </div>
  )
}

export default Filter;