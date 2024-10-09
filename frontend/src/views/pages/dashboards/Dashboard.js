import React, {useState} from "react";
import {
    Card,
    CardBody,
    Container,
    Row,
    Col,
    CardTitle, CardHeader, CardSubtitle,
    Button, Modal, ModalHeader, ModalBody, Input, ModalFooter
} from "reactstrap";
import { FaRegBuilding, FaTrash } from 'react-icons/fa';
import {useNavigate} from "react-router-dom";
import AlternativeHeader from "components/Headers/AlternativeHeader";
import useWorkspaceData from "services/workspaceData";
import axios from "axios";
import Cookies from "js-cookie";
import {deleteBoard} from "services/boardService/boardService";

const API_URL = process.env.REACT_APP_BOARD_API_URL;
function Dashboard() {
    const navigate = useNavigate();
    const { workspaces: initialWorkspaces, refresh } = useWorkspaceData();
    const [isModalOpen, setModalOpen] = useState(false);
    const [boardName, setBoardName] = useState('');
    const userID = Cookies.get('user')

    console.log("initialWorkspaces", initialWorkspaces);
    console.log('API_URL:', API_URL);
    const handleAddWorkspace = async() => {
        try {
            if (boardName) {
                await axios.post(`${API_URL}/boards/user/${userID}/board/${boardName}`);
                setModalOpen(false);
                refresh();
            }
        } catch (err) {
            console.error("Error creating workspace:", err);
        }
    }

    function handleDeleteWorkspace(id) {
        deleteBoard(id).then(() => refresh());
    }

    return (
            <>
            <AlternativeHeader parentName="Dashboard" />
            <Container className="mt--6" fluid>
                <Modal isOpen={isModalOpen} toggle={() => setModalOpen(!isModalOpen)}>
                    <ModalHeader toggle={() => setModalOpen(!isModalOpen)}>Add Workspace</ModalHeader>
                    <ModalBody>
                        <Input
                            type="text"
                            placeholder="Workspace Name"
                            value={boardName}
                            onChange={(e) => setBoardName(e.target.value)}
                        />
                    </ModalBody>
                    <ModalFooter>
                        <Button color="primary" onClick={() => handleAddWorkspace("userID")}>Create</Button>{' '}
                        <Button color="secondary" onClick={() => setModalOpen(false)}>Cancel</Button>
                    </ModalFooter>
                </Modal>
                <Row md="12">
                    <Col md="12">
                        <Card>
                            <CardHeader className="d-flex justify-content-between">
                                <div>
                                    <CardTitle tag="h3">All workspaces</CardTitle>
                                    <CardSubtitle tag="h6" className="mb-2 text-muted">Select or create</CardSubtitle>
                                </div>
                                <Button color="primary" onClick={() => setModalOpen(true)}>Add Workspace</Button>
                            </CardHeader>
                            <CardBody>
                                <Row>
                                  {initialWorkspaces.map((workspace) => (
                                    <Col md="4" key={workspace.id} className="mb-3 d-flex justify-content-center align-items-center">
                                        <Button
                                            size="lg"
                                            outline
                                            color="primary"
                                            onClick={() => navigate(`/admin/workspaces/${workspace.id}`)}
                                            style={{ fontSize: '24px', padding: '20px 40px', marginRight: '10px' }}
                                        >
                                            <FaRegBuilding size={64} className="mr-3"/> {workspace.name}
                                        </Button>

                                        {/* Display the delete button but disable it for non-owners */}
                                        <Button
                                            color="link"
                                            className="p-0"
                                            disabled={userID !== workspace.boardOwner}
                                            onClick={() => handleDeleteWorkspace(workspace.id)}
                                        >
                                            <FaTrash size={24} color={(userID !== workspace.boardOwner) ? "gray" : "red"} />
                                        </Button>
                                    </Col>
                                ))}
                                </Row>
                            </CardBody>
                            </Card>
                        </Col>
                    </Row>
                </Container>
            </>
        );
    }

export default Dashboard;

