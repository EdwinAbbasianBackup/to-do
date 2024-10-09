import axios from 'axios';

const API_URL = process.env.REACT_APP_BOARD_API_URL;

export const createTask = async (task, columnId) => {
    try {
        const response = await axios.post(API_URL + "/tasks/task/" + columnId, {
            title: task.title,
            description: task.description,
        });
        return response.data;
    } catch (error) {
        return Promise.reject(error);
    }
};
