import React, { useEffect, useState } from "react";
import Column from "./column/Column";
import AlternativeHeader from "components/Headers/AlternativeHeader";
import {
    addUserToBoard,
    createColumnInBoard,
    editBoardName
} from "services/boardService/boardService";
import { Button } from "reactstrap";
import { deleteColumn } from "services/columnService/columnService";
import { getAllUsers } from "services/userService/getUsers";
import ActionModal from "./ActionModal";

function Workspaces({ workspace, refresh }) {
    const [columns, setColumns] = useState(workspace.columns || []);
    const [modalOpen, setModalOpen] = useState(false);
    const [actionType, setActionType] = useState("");
    const [inputValue, setInputValue] = useState("");
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState("");

    useEffect(() => {
        if (workspace && workspace.columns) {
            setColumns(workspace.columns);
        }
    }, [workspace]);

    useEffect(() => {
        async function fetchUsers() {
            try {
                const fetchedUsers = await getAllUsers();
                console.log("Fetched Users:", fetchedUsers);
                setUsers(fetchedUsers);
            } catch (error) {
                console.error("Failed to fetch users:", error);
            }
        }
        fetchUsers();
    }, []);

    function toggleModal(action = "") {
        setActionType(action);
        setModalOpen(!modalOpen);
    }

    async function handleCreateColumn(boardId, columnData) {
        try {
            await createColumnInBoard(boardId, columnData);
            refresh();
        } catch (error) {
            console.error("Failed to create column:", error);
        }
    }

    async function handleAddUserToBoard(boardId, userId) {
        try {
            await addUserToBoard(boardId, userId);
            refresh();
        } catch (error) {
            console.error("Failed to add user to board:", error);
        }
    }

    async function handleDeleteColumn(columnId) {
        try {
            await deleteColumn(columnId);
            refresh();
        } catch (error) {
            console.error("Failed to delete column:", error);
        }
    }

    async function handleEditBoardName(boardId, newBoardData) {
        try {
            await editBoardName(boardId, newBoardData);
            refresh();  // Refresh the data after updating the board name.
        } catch (error) {
            console.error("Failed to edit board name:", error);
        }
    }

    const handleColumnNameUpdate = (columnId, newColumnName) => {
        const updatedColumns = columns.map(column => {
            if (column.id === columnId) {
                return { ...column, name: newColumnName };
            }
            return column;
        });
        setColumns(updatedColumns);
    };


        function handleSubmit() {
            console.log("Current Action:", actionType); // Logging current action
            console.log("Selected User ID:", selectedUser); // Logging selected user ID before assigning

            if (actionType === "addColumn") {
                handleCreateColumn(workspace.id, { name: inputValue });
            } else if (actionType === "addUser") {
                if (!selectedUser) {
                    console.error("No user selected for assignment.");
                    return;
                }
                handleAddUserToBoard(workspace.id, selectedUser);
            } else if (actionType === "editName") {
                handleEditBoardName(workspace.id, { name: inputValue });
            }
            toggleModal();
        }


    if (!workspace) return null;

    return (
        <>
            <AlternativeHeader parentName={"Workspace: " + (workspace.name || "workspace")} />

            <Button onClick={() => toggleModal("addColumn")}>Add Column</Button>
            <Button onClick={() => toggleModal("addUser")}>Add User to Board</Button>
            <Button onClick={() => toggleModal("editName")}>Edit Board Name</Button>

            <ActionModal
                isOpen={modalOpen}
                toggle={toggleModal}
                actionType={actionType}
                inputValue={inputValue}
                setInputValue={setInputValue}
                users={users}
                handleSubmit={handleSubmit}
                setSelectedUser={setSelectedUser}
                selectedUser={selectedUser}
            />

            {columns && columns.map(column => (
                <Column
                    key={column.id}
                    column={column}
                    handleDelete={handleDeleteColumn}
                    refresh={refresh}
                    handleColumnNameUpdate={handleColumnNameUpdate}
                    allColumns={columns}
                />
            ))}
        </>
    );
}

export default React.memo(Workspaces);
