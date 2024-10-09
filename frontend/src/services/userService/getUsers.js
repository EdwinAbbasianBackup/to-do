import axios from 'axios';

const API_URL = process.env.REACT_APP_USERS_API_URL;

export const getAllUsers = async (username, password) => {
    try {
        const response = await axios.get(API_URL + "/users")
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};

export const getUserById = async (userId) => {
    try {
        const response = await axios.get(API_URL + `/users/${userId}`)
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
}