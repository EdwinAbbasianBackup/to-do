import axios from 'axios';

const API_URL = process.env.REACT_APP_BOARD_API_URL;

export const getColumn = async (UUID) => {
    try {
        const response = await axios.get(`${API_URL}/column/${UUID}`);
        return response.data;
    } catch (error) {
        console.error("Error fetching the column:", error);
        throw error;
    }
}

export const deleteColumn = async (UUID) => {
    try {
        await axios.delete(`${API_URL}/boards/column/${UUID}`);
    } catch (error) {
        console.error("Error deleting the column:", error);
        throw error;
    }
}

export const editColumnName = async (UUID, name) => {
    try {
        const response = await axios.patch(`${API_URL}/column/${UUID}`, {
            name: name
        });
        return response.data;
    } catch (error) {
        console.error("Error editing the column name:", error);
        throw error;
    }
}
