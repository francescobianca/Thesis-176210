import React from 'react';
import '../../css/AddSite.css';

import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Checkbox from '@material-ui/core/Checkbox';
import Button from '@material-ui/core/Button';
import axios from 'axios';

class AggiungiSiti extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            sites: [],
            checked: []
        }
    }

    // Prima funzione che viene caricata. Devo fare qua la lettura dei siti.
    componentDidMount() {
        
        axios.post("http://localhost:8080/TestAppTesi/leggiSiti",{
            email : sessionStorage.getItem('email')
        }, {
        headers: {
            "Content-Type": "application/json"
        },
        }).then((response) => {
            this.setState({sites: response.data})
        })
       .catch((error)=>{
          console.log(error);
       });

    }

    handleClick(event){
        //console.log(this.state.checked.length);

        // A fine scelta devo salvare i siti per nuovi per l'utente.
        for (var i = 0; i<this.state.checked.length; i++) 
            console.log(this.state.checked[i]); //Questa fase ancora non funziona bene 

        if (this.state.checked.length > 0) {
        
            axios.post("http://localhost:8080/TestAppTesi/aggiungiSiti",{
                email : sessionStorage.getItem('email'),
                sites : this.state.checked,
            }, {
            headers: {
                "Content-Type": "application/json"
            },
            }).then((response) => {
                this.setState({checked: []})
                this.componentDidMount();
            })
            .catch((error)=>{
                console.log(error);
            });

        }

    }

    handleToggle = value => () => {
        const { checked } = this.state;
        const currentIndex = checked.indexOf(value);
        const newChecked = [...checked];
    
        if (currentIndex === -1) {
          newChecked.push(value);
        } else {
          newChecked.splice(currentIndex, 1);
        }
    
        this.setState({
          checked: newChecked,
        });
      };

    render() {

        var sessionEmail="";
        if (sessionStorage.getItem('email')!=null)
            sessionEmail = sessionStorage.getItem('email');

        const { sites } = this.state;
        var hosts = [];
        for (var i in sites) {
            //console.log(sites[i].host)
            hosts.push(sites[i].host);
        }

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
                                <a className="nav-link js-scroll-trigger" href="/MainPage">Home</a>
                            </li>
                        </ul>
                        </div>
                    </div>
                </nav>

                <header className="masthead">
                <div className="container h-100">

                    <div className="row h-100">
                        <div className="aggiungi">

                            <List>
                            {hosts.map(item => (
                                <ListItem key={item} role={undefined} dense button  onClick={this.handleToggle(item)}>
                                    <Checkbox
                                        tabIndex={-1}
                                        disableRipple
                                    />
                                    <ListItemText primary={` ${item }`} />
                                </ListItem>    
                            ))}
                            </List>
                            
                            <div className="buttonAdd">
                            <Button variant="contained" color="secondary" onClick={(event) => this.handleClick(event)}>
                                Conferma
                            </Button>
                            </div>

                        </div>
                    </div>
                    
                </div>
                </header>

            </div>
        );

    }

}

export default AggiungiSiti;