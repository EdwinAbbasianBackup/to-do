import React, { useState } from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, Form, Label, Input } from 'reactstrap';
import { createTask } from "services/taskService/createTask";

function AddTaskModal({ boardId, refresh }) {
    const [modal, setModal] = useState(false);
    const [taskName, setTaskName] = useState("");
    const [taskDescription, setTaskDescription] = useState("");

    const handleSave = async e => {
        e.preventDefault();

        const task = { title: taskName, description: taskDescription };

        try {
            await createTask(task, boardId);
            refresh();
            toggle();
        } catch (error) {
            console.error("Error while saving the task", error);
        }
    }

    const toggle = () => setModal(!modal);

    return (
        <div>
            <Button color="primary" onClick={toggle}>Add task</Button>
            <Modal isOpen={modal} toggle={toggle} fade={false} size="lg">
                <ModalHeader toggle={toggle}>Add Task</ModalHeader>
                <ModalBody>
                    <Form>
                        <Label for="taskName">Task Name</Label>
                        <Input type="text" value={taskName} onChange={e => setTaskName(e.target.value)} placeholder="Task Name" />

                        <Label for="taskDescription">Task Description</Label>
                        <Input type="textarea" value={taskDescription} onChange={e => setTaskDescription(e.target.value)} placeholder="Task Description" />
                    </Form>
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" onClick={handleSave}>Save Changes</Button>
                    <Button color="secondary" onClick={toggle}>Cancel</Button>
                </ModalFooter>
            </Modal>
        </div>
    );
}

export default AddTaskModal;
