
import React from 'react';
import classNames from "classnames/bind";
import styles from './App.module.scss'

import {BrowserRouter as Router, Route,  Routes } from 'react-router-dom';
import { Fragment } from 'react';
import { publicRoutes } from './routes/routes';
import DefaultLayout from './Layout/DefaultLayout/DefaultLayout';


const cx = classNames.bind(styles);

function App() {
  return (
   <Router>
     <div className={cx('App')}>
    
      <Routes>
        {publicRoutes.map((route, index) => {
                        const Page = route.component;
                        let Layout = DefaultLayout;
                        if(route.layout){
                            Layout = route.layout;
                        }
                        else if (route.layout === null){
                            Layout = Fragment
                        }
                        return <Route key={index} path={route.path}
                                      element={<Layout><Page/></Layout>}
                        />
                    })}
      </Routes>
     
     </div>
   </Router>
  );
}

export default App;
