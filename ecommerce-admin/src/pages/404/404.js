import React from 'react';
import './404.scss';
import config from "../../config";

function NotFound_404() {
    return (
        <section className="page_404">
            <div className="container">
                <div className="row">
                    <div className="col-sm-12 ">
                        <div className="col-sm-10 col-sm-offset-1  text-center">
                            <div className="four_zero_four_bg">
                                <h1 className="text-center en-center">404</h1>
                                <div className="contant_box_404">
                                    <h3 className="en-center">
                                    PAGE NOT FOUND
                                    </h3>
                                    <p className={"en-center"}>The page you requested cloud not found.</p>
                                    <div className={"en-center"}>
                                        <a className="fancy" href={config.routes.login}>
                                            <span className="top-key"/>
                                            <span className="text">GO LOGIN</span>
                                            <span className="bottom-key-1"/>
                                            <span className="bottom-key-2"/>
                                        </a>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default NotFound_404;
