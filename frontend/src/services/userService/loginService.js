import axios from 'axios';

const API_URL = process.env.REACT_APP_USERS_API_URL;

export const LoginService = async (username, password) => {
    try {
        const response = await axios.post(API_URL + "/users/login",
            {
                "username": username,
                "password": password
            });

        console.log(response.data);

        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};
