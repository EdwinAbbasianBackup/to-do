import React, {useEffect} from "react";
import { Container } from "reactstrap";
import LoginForm from "./LoginForm";
import "assets/css/custom-css.css";

function Login() {
    useEffect(() => {
        document.body.classList.add('login-bg');

        return () => {
            document.body.classList.remove('login-bg');
        };
    });

    return (
        <Container className="mt--6 d-flex justify-content-center align-items-center vh-100" fluid>
            <div className="col-md-8 col-lg-6 login-form-box">
                <LoginForm />
            </div>
        </Container>
    );
}

export default Login;
