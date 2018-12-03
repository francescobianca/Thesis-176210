import React from 'react';
import Button from '@material-ui/core/Button';
import '../../css/Form.css';
import axios from 'axios';

class LoginPage extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            emailValue:'',
            passwordValue:'',
            toMainPage: false,
        }
    }

    handleClick(event){
        var email = this.state.emailValue;
        var password = this.state.passwordValue;

        if (email != "" && password!="")  {
            // Allora mando i dati e verifico se sono corretti.
            axios.post("http://localhost:8080/TestAppTesi/login",{
                email: email,
                password: password,
            }, {
            headers: {
                "Content-Type": "application/json"
            },
            }).then(res => {
                console.log(res.data);
                //Gestire i casi di esito positivo e di esito negativo.
                if (res.data == "DatiErrati")
                    // fai una cosa
                    ;
                else if (res.data == "Ok")
                    // Va bene --> fai qualcos'altro

                    sessionStorage.setItem('email',email);

                    this.setState({
                        toMainPage: true
                    });

            }, err => {
                alert("Server rejected response with: " + err);
            });

        }
        else {
            // Mancano dei dati.

        }

    }

    updateEmailValue(evt) {
        this.setState({
            emailValue: evt.target.value
        });
    }

    updatePasswordValue(evt) {
        this.setState({
            passwordValue: evt.target.value
        });
    }

    render() {

        if (this.state.toMainPage == true) 
            this.props.history.push("/MainPage")

        return (
            <div>
            <div className="background"></div>

            <div className="background2"></div>
                <div className="loginForm"> 
                <hgroup>
                <h1>Login</h1>
                </hgroup>

                <form>
                    <div className="group">
                        <input type="email" className="used" value={this.state.emailValue} onChange={evt => this.updateEmailValue(evt)}/><span className="highlight"></span><span className="bar"></span>
                        <label>Username</label>
                    </div>
                    <div className="group">
                        <input type="password" className="used" value={this.state.passwordValue} onChange={evt => this.updatePasswordValue(evt)}/><span className="highlight"></span><span className="bar"></span>
                        <label>Password</label>
                    </div>
                    <Button id="buttonTest" variant="contained" color="secondary" onClick={(event) => this.handleClick(event)}>
                        Login
                    </Button>    
                </form>

                <div className="powered">
                    Powered by Francesco Bianca
                </div>
            </div>
        </div>
        );
    }


}

export default LoginPage;