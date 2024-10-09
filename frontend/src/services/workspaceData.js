import useFetch from "./useFetch";
import Cookies from 'js-cookie';
const apiUrl = process.env.REACT_APP_BOARD_API_URL;

function transformBoards(data) {
    const normalizedData = Array.isArray(data) ? data : [data];

    return normalizedData?.map(board => ({
        id: board?.id,
        name: board.name,
        boardOwner: board?.owner,
        members: board?.members,
        columns: board?.columns?.map(column => ({
            id: column?.id,
            name: column?.name,
            tasks: column?.tasks?.map(task => ({
                id: task?.id,
                title: task?.title,
                description: task?.description,
                labels: task?.labels,
                assignees: task?.assignees
            }))
        }))
    }));
}

function useWorkspaceData() {
    const userToken = Cookies.get('user');
    const { data: boardData, loading: boardLoading, error: boardError, refresh } = useFetch(apiUrl + '/boards/user/' + userToken);
    const workspaces = boardData ? transformBoards(boardData) : [];

    return { workspaces, loading: boardLoading, error: boardError, refresh };
}

export default useWorkspaceData;