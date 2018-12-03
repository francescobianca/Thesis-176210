import React from 'react';
import { Route } from 'react-router-dom';
import HomePage from "./components/pages/HomePage";
import LoginPage from "./components/pages/LoginPage";
import RegistrationPage from "./components/pages/RegistrationPage";
import MainPage from "./components/pages/MainPage";
import AggiungiSiti from './components/pages/AggiungiSiti';
import RimuoviSiti from './components/pages/RimuoviSiti';

const App = () => (

  <div>
    <Route path="/" exact component={HomePage}></Route>
    <Route path="/Login" exact component={LoginPage}></Route>
    <Route path="/Registration" exact component={RegistrationPage}></Route>
    <Route path="/MainPage" exact component={MainPage}></Route>
    <Route path="/AggiungiSiti" exact component={AggiungiSiti}></Route>
    <Route path="/RimuoviSiti" exact component={RimuoviSiti}></Route>
  </div>

);


export default App;
