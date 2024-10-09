import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, FormGroup, Label, Input } from 'reactstrap';
import {editColumnName} from "services/columnService/columnService";

function EditColumnNameModal({ isOpen, toggle, columnId, refresh }) {
    const [columnName, setColumnName] = React.useState('');

    const handleEditColumnName = async () => {
        try {
            await editColumnName(columnId, columnName);
            refresh();
            toggle();
        } catch (error) {
            console.error("Failed to edit the column name.", error);
        }
    };

    const handleInputChange = (e) => {
        setColumnName(e.target.value);
    };


    return (
        <Modal isOpen={isOpen} toggle={toggle} fade={false}>
            <ModalHeader toggle={toggle}>Edit Column Name</ModalHeader>
            <ModalBody>
                <FormGroup>
                    <Label for="newColumnName">New Column Name:</Label>
                    <Input
                        type="text"
                        name="newColumnName"
                        id="newColumnName"
                        value={columnName}
                        onChange={handleInputChange}
                    />
                </FormGroup>
            </ModalBody>
            <ModalFooter>
                <Button color="success" onClick={handleEditColumnName}>Save</Button>
                <Button color="secondary" onClick={toggle}>Cancel</Button>
            </ModalFooter>
        </Modal>
    );
}

export default EditColumnNameModal;
