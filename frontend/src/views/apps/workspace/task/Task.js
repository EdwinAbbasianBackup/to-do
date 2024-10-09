import React from 'react';
import {
    Button,
    Card,
    CardBody,
    CardHeader,
    CardSubtitle,
    CardTitle,
    Col, Input, InputGroup, InputGroupText,
    ListGroupItem,
    Row
} from "reactstrap";
import EditTaskModal from "./EditTaskModal";
import {deleteTask} from "services/taskService/deleteTask";
import {assign, moveToColumn, unassigned, update} from "services/taskService/editTask";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faExchangeAlt} from "@fortawesome/free-solid-svg-icons";

function Task({ task, columnID, refresh, allColumns }) {
    const [message, setMessage] = React.useState('');


    function handleDelete(taskId) {
        deleteTask(columnID ,taskId).then(() => {
            refresh();
        }).catch(error => {
            console.error("Error while deleting the task", error);
        });
    }



    const handleRemoveAssignee = (id, userUUID) => {
        console.log("UNASSIGNING", id, userUUID);

        if (userUUID === undefined) {
            return;
        }
        unassigned(id, userUUID).then((response) => {
            setMessage(response.message);
            refresh();
        }).catch((error) => {
            console.log(error);
            setMessage(error.message);
        })
    };

    const handleAssign = (id, userUUID) => {
        if (userUUID === undefined) {
            return;
        }
        assign(id, userUUID).then((response) => {
            setMessage(response.message);
            refresh();
        }).catch((error) => {
            console.log(error);
            setMessage(error.message);
        })
    };

    function handleSave(task) {
        update(task).then((response) => {
            console.log(response);
            setMessage(response.message);
        }).catch((error) => {
            console.log(error);
            setMessage(error.message);
        }).finally(
            () => {
                setMessage('');
                refresh();
            }
        )
    }

      async function handleMoveToColumn(taskId, targetColumnId) {
        if(!targetColumnId) return;

        try {
            await moveToColumn(taskId, targetColumnId);
            refresh();
        } catch (error) {
            console.error("Failed to move task to another column:", error);
        }
    }

    return (
        <ListGroupItem className={"p-3"} style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)', background: "#E0F0FF" }}>
            <Card style={{ boxShadow: '0 4px 8px rgba(0,0,0,0.1)', margin: '10px 0', borderRadius: '5px' }} className={"p-3"} >
                <CardHeader>
                    <Row>
                        <Col>
                            <CardTitle>
                                <strong>Title:</strong> {task?.title}
                            </CardTitle>
                            <CardSubtitle>
                                <strong>Description:</strong> {task?.description}
                            </CardSubtitle>
                        </Col>
                        <Col>
                            <CardTitle>
                                <strong>Assigned to</strong>
                                {task?.assignees && Object.entries(task.assignees).map(([key, value]) => (
                                    <div key={key}>
                                        {value}
                                    </div>
                                ))}
                            </CardTitle>
                        </Col>
                    </Row>

                </CardHeader>
                <CardBody>
                    <Row style={{ marginBottom: '10px' }}>
                        <Col md="2"><strong>Status:</strong> {task?.status}</Col>
                        <Col md="2"><strong>Labels:</strong> {task?.labels || "No labels"}</Col>
                    </Row>
                    <Row>
                        <Col md="10">
                            <EditTaskModal
                                fields={task}
                                message={message}
                                update={handleSave}
                                assign={handleAssign}
                                unassigned={handleRemoveAssignee}
                            />
                        </Col>
                        <Col md="2">
                            <Button color="danger" onClick={() => handleDelete(task?.id)} block>Delete</Button>
                        </Col>
                    </Row>
                     <Row className="mt-3">
                        <Col>
                            <InputGroup>
                                    <InputGroupText addonType="prepend">
                                        <FontAwesomeIcon icon={faExchangeAlt} />
                                    </InputGroupText>
                                <Input type="select" onChange={(e) => handleMoveToColumn(task?.id, e.target.value)}>
                                    <option value="">Move to Column...</option>
                                    {
                                        allColumns.map(column => (
                                        column.id !== columnID && <option key={column.id} value={column.id}>{column.name}</option>))
                                    }
                                </Input>
                            </InputGroup>
                        </Col>
                    </Row>
                </CardBody>
            </Card>
        </ListGroupItem>
    );
}

export default Task;