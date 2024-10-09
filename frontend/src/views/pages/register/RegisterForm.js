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
  Container,
} from 'reactstrap';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const API_URL = process.env.REACT_APP_USERS_API_URL;
console.log('API_URL:', API_URL);

const RegisterForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  const resetForm = () => {
    setUsername('');
    setPassword('');
    setErrorMessage('');
    setSuccessMessage('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Attempting to register with:', username, password);

    try {
      await axios.post(API_URL + '/users', {
        username: username,
        password: password,
      });
      setSuccessMessage('Successfully registered! You can now login.');
      navigate('/auth/login');
      resetForm();
    } catch (error) {
      setErrorMessage(
        error.response.data.message || 'An error occurred during registration.'
      );
    }
  };

  return (
    <Container className="py-5">
      <Card className="bg-secondary border-3">
        <CardHeader className="bg-transparent pb-5">
          <div className="font-weight-700 font-weight-bolder text-center mt-2 mb-4">
            <h1>Register</h1>
          </div>
        </CardHeader>
        <CardBody className="px-lg-4 py-lg-5">
          <Form onSubmit={handleSubmit}>
            <FormGroup>
              <InputGroup className="input-group-alternative mb-3">
                <InputGroupText>
                  <i className="ni ni-email-83"></i>
                </InputGroupText>
                <Input
                  placeholder="Username"
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
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
                Register
              </Button>
              <Button
                type="button"
                className="btn-success btn-lg"
                onClick={() => navigate('/auth/login')}
              >
                Login
              </Button>
            </div>
          </Form>
          {errorMessage && (
            <div className="alert alert-danger">{errorMessage}</div>
          )}
          {successMessage && (
            <div className="alert alert-success">{successMessage}</div>
          )}
        </CardBody>
      </Card>
    </Container>
  );
};

export default RegisterForm;
