import React, { useEffect, useState } from 'react';
import { Card, CardTitle, CardBody, ListGroup, Button } from 'reactstrap';
import Task from '../task/Task';
import AddTaskModal from './AddTaskModal';
import DeleteColumnModal from './DeleteColumnModal';
import EditColumnNameModal from './EditColumnNameModal';

function Column({ column, updateBoardTasks, refresh, allColumns }) {
    const [tasks, setTasks] = useState([]);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);

    useEffect(() => {
        setTasks(column.tasks);
    }, [column]);

    const openDeleteModal = () => setIsDeleteModalOpen(true);
    const closeDeleteModal = () => setIsDeleteModalOpen(false);
    const openEditModal = () => setIsEditModalOpen(true);
    const closeEditModal = () => setIsEditModalOpen(false);

    console.log(column)

    return (
        <>
            <Card body style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)', background: "#E0F0FF", marginBottom: '15px', position: 'relative' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 15px', borderBottom: '1px solid rgba(0,0,0,0.1)' }}>
                    <CardTitle style={{ fontSize: '1.5rem', margin: 0 }}>
                        {column.name}
                    </CardTitle>
                    <div>
                        <Button color="danger" onClick={openDeleteModal} style={{ marginRight: '10px' }}>Delete</Button>
                        <Button color="warning" onClick={openEditModal}>Edit Name</Button>
                    </div>
                </div>
                <CardBody>
                    <ListGroup style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)', background: "#E0F0FF", marginBottom: '15px' }}>
                        {
                            tasks && tasks.map(task => (
                                <Task
                                    refresh={refresh}
                                    key={task?.id}
                                    task={task}
                                    updateBoardTasks={updateBoardTasks}
                                    columnID={column.id}
                                    allColumns={allColumns} />
                            ))
                        }
                    </ListGroup>
                </CardBody>
                <AddTaskModal refresh={refresh} boardId={column.id} />
            </Card>

            <DeleteColumnModal isOpen={isDeleteModalOpen} toggle={closeDeleteModal} columnId={column.id} refresh={refresh} />
            <EditColumnNameModal isOpen={isEditModalOpen} toggle={closeEditModal} columnId={column.id} refresh={refresh} />
        </>
    );
}

export default React.memo(Column);
