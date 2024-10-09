import React from 'react';
import { Button, Form, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

function ActionModal({ isOpen, toggle, actionType, inputValue, setInputValue, users, handleSubmit, setSelectedUser, selectedUser }) {
    return (
        <Modal isOpen={isOpen} toggle={toggle} fade={false}>
            <ModalHeader toggle={toggle}>
                {actionType === "addColumn" && "Add Column"}
                {actionType === "addUser" && "Add User to Board"}
                {actionType === "editName" && "Edit Board Name"}
            </ModalHeader>
            <ModalBody>
                <Form>
                    <FormGroup>
                        <Label for="inputValue">
                            {actionType === "addColumn" && "Column Name"}
                            {actionType === "addUser" && "Select User"}
                            {actionType === "editName" && "New Board Name"}
                        </Label>
                        {actionType === "addUser" ? (
                            <Input type="select" name="selectUser" value={selectedUser} onChange={e => {
                                console.log("Selected User ID:", e.target.value);
                                setSelectedUser(e.target.value);
                                }} >
                                <option value="" disabled>Select a user</option>
                                {users.map(user => <option key={user.id} value={user.id}>{user.username}</option>)}
                            </Input>
                        ) : (
                            <Input
                                type="text"
                                name="inputValue"
                                id="inputValue"
                                value={inputValue}
                                onChange={(e) => setInputValue(e.target.value)}
                            />
                        )}
                    </FormGroup>
                </Form>
            </ModalBody>
            <ModalFooter>
                <Button color="primary" onClick={handleSubmit}>Submit</Button>
                <Button color="secondary" onClick={toggle}>Cancel</Button>
            </ModalFooter>
        </Modal>
    );
}

export default ActionModal;
