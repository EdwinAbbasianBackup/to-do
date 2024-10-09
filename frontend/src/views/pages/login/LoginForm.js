import React, { useState } from 'react';
import {
  Button,
  FormGroup,
  Input,
  InputGroup,
  InputGroupText,
  Card,
  CardHeader,
  CardBody,
  Form,
} from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import { LoginService } from 'services/userService/loginService';
import Cookies from 'js-cookie';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await LoginService(email, password);

      if (response.message !== 'User not found') {
        console.log('success');
        Cookies.set('user', response.id, { secure: false });
        document.cookie = 'document=' + response.id;
        console.log('Cookie immediately after set:', Cookies.get('user'));

        const checkCookieAndNavigate = () => {
          const cookieValue = Cookies.get('user');
          if (cookieValue) {
            console.log('cookie set');
            window.location.href = '/admin/dashboard';
          } else {
            setTimeout(checkCookieAndNavigate, 100);
          }
        };

        checkCookieAndNavigate();
      } else {
        console.log('fail');
        setErrorMessage(response.message);
      }
    } catch (error) {
      setErrorMessage(error.message + ' Reload page or remove cookies');
    }
  };

  return (
    <Card className="bg-secondary border-3">
      <CardHeader className="bg-transparent pb-5">
        <div className="font-weight-700 font-weight-bolder text-center mt-2 mb-4">
          <h1>Login</h1>
        </div>
      </CardHeader>
      <CardBody className="px-lg-4 py-lg-5">
        <Form onSubmit={handleSubmit} className="bg-transparent">
          <FormGroup>
            <InputGroup className="input-group-alternative mb-3">
              <InputGroupText>
                <i className="ni ni-email-83"></i>
              </InputGroupText>
              <Input
                placeholder="Username"
                type="text"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </InputGroup>
          </FormGroup>
          <FormGroup>
            <InputGroup className="input-group-alternative mb-3">
              <InputGroupText>
                <i className="ni ni-lock-circle-open"></i>
              </InputGroupText>
              <Input
                placeholder="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </InputGroup>
          </FormGroup>
          <div className="text-center">
            <Button type="submit" className="btn-info btn-lg">
              Sign in
            </Button>
            <Button
              type="button"
              className="btn-warning btn-lg"
              onClick={() => navigate('/auth/register')}
            >
              Register
            </Button>
          </div>
        </Form>
        {errorMessage && (
          <div className="alert alert-danger">{errorMessage}</div>
        )}
      </CardBody>
    </Card>
  );
};

export default LoginForm;
