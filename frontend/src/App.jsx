import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png' 
import './App.css'
import Products from './components/products/Products';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Home from './components/home/Home'

 

function App() {
      return (
         <Router>
            <Routes>
               <Route path='/' element={<Home/>}/>
               <Route path='/products' element={<Products/>}/>
            </Routes>
         </Router>
      )

}

export default App
