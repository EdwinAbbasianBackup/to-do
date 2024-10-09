/* eslint-disable no-unused-vars */
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { useEffect, useState } from "react";
import {
    Button, Col, Form, Input, InputGroup, Label, ListGroup, ListGroupItem,
    Modal, ModalBody, ModalFooter, ModalHeader, Row
} from "reactstrap";
import { getAllUsers } from "services/userService/getUsers";

function EditTaskModal({ fields, update, assign, unassigned, message }) {
    const [modal, setModal] = useState(false);
    const [newAssigneeName, setNewAssigneeName] = useState('');
    const [sysMessage, setSysMessage] = useState(message || '');
    const [users, setUsers] = useState([]);
    const assigneeEntries = Object.entries(fields?.assignees || {});

    useEffect(() => {
        getAllUsers()
            .then(fetchedUsers => {
                setUsers(fetchedUsers);
            })
            .catch(error => {
                console.error("Error fetching users:", error);
            });
    }, []);

    const toggle = () => setModal(!modal);

    function handleUpdate(e) {
        e.preventDefault();
        const taskName = e.target?.taskName?.value;
        const taskDescription = e.target?.taskDescription?.value;

        const task = {
            id: fields.id,
            title: taskName,
            description: taskDescription,
        };
        update(task);
    }


    function handleAssigned(e) {
        e.preventDefault();

        if (!newAssigneeName) {
            console.error("No user selected for assignment");
            return;
        }

        assign(fields.id, newAssigneeName);
    }

    return (
        <div>
            <Button color="primary" onClick={toggle}>Edit</Button>
            <Modal isOpen={modal} toggle={toggle} fade={false} size={"lg"}>
                <ModalHeader toggle={toggle}>Edit Task</ModalHeader>
                    <Form onSubmit={handleUpdate}>
                        <ModalBody>
                            <Label for="taskName">Task Name</Label>
                            <Input type="text" name="taskName" id="taskName" placeholder="Task Name" defaultValue={fields.title} />

                            <Label for="taskDescription">Task Description</Label>
                            <Input type="text" name="taskDescription" id="taskDescription" placeholder="Task Description" defaultValue={fields.description} />

                            <Label for={"assignee"}>Assignees</Label>
                            <ListGroup>
                                {assigneeEntries.map(([id, name]) => (
                                    <ListGroupItem key={id} className="d-flex justify-content-between align-items-center">
                                        {name}
                                        <FontAwesomeIcon icon={faTimes} style={{ cursor: 'pointer' }} onClick={() => unassigned(fields.id, id)} />
                                    </ListGroupItem>
                                ))}
                            </ListGroup>

                            <InputGroup className={"mt-1"}>
                                <Input type="select" value={newAssigneeName} onChange={(e) => setNewAssigneeName(e.target.value)}>
                                    <option value="" disabled>Select a user</option>
                                    {users.map(user => (
                                        <option key={user.id} value={user.id}>{user.username}</option>
                                    ))}
                                </Input>
                                <Button type="button" onClick={handleAssigned}>Assign</Button>
                            </InputGroup>

                            <Label for={"status"}>Status</Label>
                            <Input type="select" name={"status"} id={"status"} defaultValue={fields.status}>
                                <option>Not Started</option>
                                <option>In Progress</option>
                                <option>Completed</option>
                            </Input>

                            <Label for={"priority"}>Priority</Label>
                            <Input type="select" name={"priority"} id={"priority"} defaultValue={fields.priority}>
                                <option>Low</option>
                                <option>Medium</option>
                                <option>High</option>
                            </Input>
                        </ModalBody>
                         <ModalFooter>
                            <Row>
                                <Col md="3">{sysMessage}</Col>
                                <Col md="4">
                                    <Button color="danger" onClick={toggle}>Cancel</Button>
                                </Col>
                                <Col md={4}>
                                    <Button color="success" type="submit">Save changes</Button>
                                </Col>
                            </Row>
                        </ModalFooter>
                    </Form>

            </Modal>
        </div>
    );
}

export default EditTaskModal;
