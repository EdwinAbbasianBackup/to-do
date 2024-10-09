import axios from 'axios';

const API_URL = process.env.REACT_APP_BOARD_API_URL;

export const update = async (task) => {

    console.log("UPDATE TASK", task);
    try {
        return await axios.patch(API_URL + `/tasks/${task.id}`, {
                title: task.title,
                description: task.description
            }
        );
    } catch (error) {
        return Promise.reject(error);
    }
};


export const assign = async (task, uuid) => {
    try {
        return await axios.patch(API_URL + `/tasks/${task}/assigned`, {
                userId: uuid,
            }
        )
    } catch (error) {
        return Promise.reject(error);
    }
};

export const unassigned = async (task, uuid) => {
    try {
        const response = axios.patch(API_URL + `/tasks/${task}/unassigned`, {
            userId: uuid,
            }
        );
        return Promise.resolve(response);
    } catch (error) {
        return Promise.reject(error);
    }
};

export const moveToColumn = async (task, columnUuid) => {
    try {
        const response = axios.put(API_URL + `/tasks/task/${task}/column/${columnUuid}`);
        return Promise.resolve(response);
    } catch (error) {
        return Promise.reject(error);
    }
};
