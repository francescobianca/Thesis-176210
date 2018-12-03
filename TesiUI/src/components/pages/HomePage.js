import React from 'react';


class HomePage extends React.Component {

    constructor(props){
        super(props);
        this.state = {
        
        }
    }

    render() {
        return (
            <div>
                <nav className="navbar navbar-expand-lg navbar-light fixed-top" id="mainNav">
                    <div className="container">
                        <a className="navbar-brand js-scroll-trigger" href="#page-top">NewsRT - News Real Time</a>
                        <button className="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                        Menu
                        <i className="fas fa-bars"></i>
                        </button>
                        <div className="collapse navbar-collapse" id="navbarResponsive">
                        <ul className="navbar-nav ml-auto">
                            <li className="nav-item">
                            <a className="nav-link js-scroll-trigger" href="#features">Features</a>
                            </li>
                            <li className="nav-item">
                            <a className="nav-link js-scroll-trigger" href="#contact">Contact</a>
                            </li>
                            <li className="nav-item">
                            <a className="nav-link js-scroll-trigger" href="/Login">Login</a>
                            </li>
                            <li className="nav-item">
                            <a className="nav-link js-scroll-trigger" href="/Registration">Registrati</a>
                            </li>
                        </ul>
                        </div>
                    </div>
                </nav>

                <header className="masthead">
                <div className="container h-100">
                    <div className="row h-100">
                    <div className="col-lg-7 my-auto">
                        <div className="header-content mx-auto">
                        <h1 className="mb-5">Tutte in unico posto.</h1>
                        <h2 className="mb-5">NewsRT ti aggiorna constantemente con le news dei tuoi siti preferiti.</h2>
                        <a href="/Registration" className="btn btn-outline btn-xl js-scroll-trigger">Provalo gratuitamente!</a>
                        </div>
                    </div>
                    <div className="col-lg-5 my-auto">
                        <div className="device-container">
                        <div className="device-mockup iphone6_plus portrait white">
                            <div className="device">
                            <div className="screen">
                                <img src="bootstrapTheme/img/demo-screen-1.jpg" className="img-fluid" alt="" />>
                            </div>
                            <div className="button"> 
                            </div>
                            </div>
                        </div>
                        </div>
                    </div>
                    </div>
                </div>
                </header>

                <section className="features" id="features">
                <div className="container">
                    <div className="section-heading text-center">
                    <h2>Unlimited Features, Unlimited Fun</h2>
                    <p className="text-muted">Check out what you can do with this app theme!</p>
                    <hr />
                    </div>
                    <div className="row">
                    <div className="col-lg-4 my-auto">
                        <div className="device-container">
                        <div className="device-mockup iphone6_plus portrait white">
                            <div className="device">
                            <div className="screen">
                                <img src="bootstrapTheme/img/demo-screen-1.jpg" className="img-fluid" alt="" />
                            </div>
                            <div className="button">
                            </div>
                            </div>
                        </div>
                        </div>
                    </div>
                    <div className="col-lg-8 my-auto">
                        <div className="container-fluid">
                        <div className="row">
                            <div className="col-lg-6">
                            <div className="feature-item">
                                <i className="icon-screen-smartphone text-primary"></i>
                                <h3>Device Mockups</h3>
                                <p className="text-muted">Ready to use HTML/CSS device mockups, no Photoshop required!</p>
                            </div>
                            </div>
                            <div className="col-lg-6">
                            <div className="feature-item">
                                <i className="icon-camera text-primary"></i>
                                <h3>Flexible Use</h3>
                                <p className="text-muted">Put an image, video, animation, or anything else in the screen!</p>
                            </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-lg-6">
                            <div className="feature-item">
                                <i className="icon-present text-primary"></i>
                                <h3>Free to Use</h3>
                                <p className="text-muted">As always, this theme is free to download and use for any purpose!</p>
                            </div>
                            </div>
                            <div className="col-lg-6">
                            <div className="feature-item">
                                <i className="icon-lock-open text-primary"></i>
                                <h3>Open Source</h3>
                                <p className="text-muted">Since this theme is MIT licensed, you can use it commercially!</p>
                            </div>
                            </div>
                        </div>
                        </div>
                    </div>
                    </div>
                </div>
                </section>

                <section className="cta">
                <div className="cta-content">
                    <div className="container">
                    <h2>Stop waiting.<br />>Start building.</h2>
                    <a href="#contact" className="btn btn-outline btn-xl js-scroll-trigger">Let's Get Started!</a>
                    </div>
                </div>
                <div className="overlay"></div>
                </section>

                <section className="contact bg-primary" id="contact">
                <div className="container">
                    <h2>We
                    <i className="fas fa-heart"></i>
                    new friends!</h2>
                    <ul className="list-inline list-social">
                    <li className="list-inline-item social-twitter">
                        <a href="#">
                        <i className="fab fa-twitter"></i>
                        </a>
                    </li>
                    <li className="list-inline-item social-facebook">
                        <a href="#">
                        <i className="fab fa-facebook-f"></i>
                        </a>
                    </li>
                    <li className="list-inline-item social-google-plus">
                        <a href="#">
                        <i className="fab fa-google-plus-g"></i>
                        </a>
                    </li>
                    </ul>
                </div>
                </section>

                <footer>
                <div className="container">
                    <p>&copy; NewsRT 2018. All Rights Reserved.</p>
                    <ul className="list-inline">
                    <li className="list-inline-item">
                        <a href="#">Privacy</a>
                    </li>
                    <li className="list-inline-item">
                        <a href="#">Terms</a>
                    </li>
                    <li className="list-inline-item">
                        <a href="#">FAQ</a>
                    </li>
                    </ul>
                </div>
                </footer>
            </div>
        );
    }


}

export default HomePage;