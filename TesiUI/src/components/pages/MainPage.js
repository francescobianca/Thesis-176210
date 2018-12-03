import React from 'react';
import '../../css/MainPage.css';
import Button from '@material-ui/core/Button';
import axios from 'axios';

import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';
import CircularProgress from '@material-ui/core/CircularProgress';

class MainPage extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            news: [],
            progressBar: false,
        }
    }

    componentDidMount() {
        axios.post("http://localhost:8080/TestAppTesi/caricaNews",{
            email : sessionStorage.getItem('email')
        }, {
        headers: {
            "Content-Type": "application/json"
        },
        }).then((response) => {
            this.setState({news: response.data})
            this.setState({progressBar: true})
            //console.log(response);
        })
       .catch((error)=>{
          console.log(error);
       });
    }

    handleClick(event){
        
    }
    

    render() {

        var sessionEmail="";
        if (sessionStorage.getItem('email')!=null)
            sessionEmail = sessionStorage.getItem('email');

        const { news } = this.state;
        /*for (var i in news) {
            console.log(news[i])
        }*/

        if (this.state.progressBar == false)
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
                                    <a className="nav-link js-scroll-trigger">{sessionEmail}</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link js-scroll-trigger" href="/AggiungiSiti">Aggiungi Siti</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link js-scroll-trigger" href="/RimuoviSiti">Rimuovi Siti</a>
                                </li>
                            </ul>
                            </div>
                        </div>
                    </nav>

                    <header className="masthead">
                        <div className="container h-100">

                            <div className="progressBar">
                                <CircularProgress color="secondary"/>
                            </div>

                        </div>
                    </header>

                </div>
            )
        else 
            return (
                <div className="backgroundDiv">
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
                                    <a className="nav-link js-scroll-trigger">{sessionEmail}</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link js-scroll-trigger" href="/AggiungiSiti">Aggiungi Siti</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link js-scroll-trigger" href="/RimuoviSiti">Rimuovi Siti</a>
                                </li>
                            </ul>
                            </div>
                        </div>
                    </nav>

                   
                    <div className="container h-100">

                        <div className="news">
                            
                            <div className="container row">
                            {news.map(item => (
                                <Card id="cardElement">
                                    <CardActionArea>
                                        <CardMedia
                                        id="cardMedia"
                                        component="img"
                                        alt="Contemplative Reptile"
                                        height="250"
                                        image={item.locImmagine}
                                        title="Contemplative Reptile"
                                        />
                                        <CardContent>
                                        <Typography gutterBottom variant="h5" component="h2">
                                            {item.titolo}
                                        </Typography>
                                        <Typography component="p">
                                            {item.fonte}
                                            <br />
                                            Tag: {item.keywords}
                                            <br />
                                            Data: {item.data}
                                        </Typography>
                                        </CardContent>
                                    </CardActionArea>
                                    <CardActions>
                                        <Button size="small" color="primary" href={item.loc}>
                                        Learn More
                                        </Button>
                                    </CardActions>
                                </Card>
                            ))}
                            </div>
                    
                        </div>

                    </div>
                    

                </div>
            );

    }

}

export default MainPage;

