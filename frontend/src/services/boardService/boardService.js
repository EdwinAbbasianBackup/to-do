import axios from 'axios';

const API_URL = process.env.REACT_APP_BOARD_API_URL;


export const createColumnInBoard = async (boardId, columnData) => {
    console.log("columnData", columnData.name);
    try {
        const response = await axios.put(`${API_URL}/boards/${boardId}/column`, {
            name: columnData.name,
        });
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};


export const addUserToBoard = async (boardId, userId) => {
    try {
        const response = await axios.put(`${API_URL}/boards/board/${boardId}/user/${userId}`);
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};


export const createBoardForOwner = async (userId, boardName) => {
    try {
        const response = await axios.post(`${API_URL}/boards/user/${userId}/board/${boardName}`);
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};


export const deleteBoard = async (UUID) => {
    try {
        const response = await axios.delete(`${API_URL}/boards/${UUID}`);
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};


export const editBoardName = async (UUID, newBoardData) => {
    try {
        const response = await axios.patch(`${API_URL}/boards/${UUID}`, {
            name: newBoardData.name,
        });
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};


export const findAllBoardsFromUser = async (UUID) => {
    try {
        const response = await axios.get(`${API_URL}/api/v1/boards/user/${UUID}`);
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};


export const findBoard = async (UUID) => {
    try {
        const response = await axios.get(`${API_URL}/api/v1/boards/board/${UUID}`);
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};
