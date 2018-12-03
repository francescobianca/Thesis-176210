import React from 'react';
import Button from '@material-ui/core/Button';
import '../../css/Form.css';
import axios from 'axios';

class RegistrationPage extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            nameValue: '',
            surnameValue:'',
            emailValue:'',
            passwordValue:'',
            toLogin:false,
        }
    }

    handleClick(event){

        var first_name = this.state.nameValue;
        var last_name = this.state.surnameValue;
        var email = this.state.emailValue;
        var password = this.state.passwordValue;

        var checkFirstName = false;
        var checkLastName = false;
        var checkEmail = false;
        var checkPassword = false;
        
        //Inserisco i pattern da rispettare per email e password.
        var Email_Pattern=/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (Email_Pattern.test(email))
            checkEmail = true;
        
        //Minimo 8 caratteri, almeno una lettera, un numero ed un carattere speciale.
        var Password_Pattern=/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/
        if (Password_Pattern.test(password))
            checkPassword = true;

        // controlla che il nome ed il cognome non contengano numeri
        var Name_Pattern=/\d+/g;
        if (first_name != "" && !Name_Pattern.test(first_name))
            checkFirstName = true;

        if (last_name != "" && !Name_Pattern.test(last_name))
            checkLastName = true;

        if (checkFirstName && checkLastName && checkEmail && checkPassword) {
            
            axios.post("http://localhost:8080/TestAppTesi/registrazione",{
                nome: first_name,
                cognome: last_name,
                email: email,
                password: password,
            }, {
            headers: {
                "Content-Type": "application/json"
            },
            }).then(res => {
                console.log(res.data);
                //Gestire i casi di esito positivo e di esito negativo.
                if (res.data == "UtentePresente")
                    // fai una cosa
                    ;
                else if (res.data == "Ok")
                    // Va bene --> fai qualcos'altro
                    this.setState({
                        toLogin: true
                    });

            }, err => {
                alert("Server rejected response with: " + err);
            });


        } else {
            alert('Mancano dati');
        }

    }

    updateNameValue(evt) {
        this.setState({
            nameValue: evt.target.value
        });
    }

    updateSurnameValue(evt) {
        this.setState({
            surnameValue: evt.target.value
        });
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

        if (this.state.toLogin == true) 
            this.props.history.push("/login")

        return (
            <div>
            <div className="background"></div>
            <div className="background2"></div>

                <div className="loginForm"> 
                <hgroup>
                <h1>Registrati</h1>
                </hgroup>

                <form>
                    <div className="group">
                        <input type="text" className="used"  value={this.state.nameValue} onChange={evt => this.updateNameValue(evt)} /><span className="highlight"></span><span className="bar"></span>
                        <label>Nome</label>
                    </div>
                    <div className="group">
                        <input type="text" className="used" value={this.state.surnameValue} onChange={evt => this.updateSurnameValue(evt)} /><span className="highlight"></span><span className="bar"></span>
                        <label>Cognome</label>
                    </div>
                    <div className="group">
                        <input type="text" className="used" value={this.state.emailValue} onChange={evt => this.updateEmailValue(evt)} /><span className="highlight"></span><span className="bar"></span>
                        <label>Email</label>
                    </div>
                    <div className="group">
                        <input type="password" className="used" value={this.state.passwordValue} onChange={evt => this.updatePasswordValue(evt)} /><span className="highlight"></span><span className="bar"></span>
                        <label>Password</label>
                    </div>    
                    <Button id="buttonTest" variant="contained" color="secondary" onClick={(event) => this.handleClick(event)}>
                        Sign In
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

export default RegistrationPage;