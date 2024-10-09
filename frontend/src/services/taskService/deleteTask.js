import axios from 'axios';

const API_URL = process.env.REACT_APP_BOARD_API_URL;

export const deleteTask = async (columnUuid, uuid) => {
    try {
        const response = await axios.delete(API_URL + "/tasks/columns/" + columnUuid + "/task/" + uuid);
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};
