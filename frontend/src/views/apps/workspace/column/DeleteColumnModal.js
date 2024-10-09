import {deleteColumn} from "services/columnService/columnService";
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";


function DeleteColumnModal({ isOpen, toggle, columnId, refresh }) {
    const handleDeleteColumn = async () => {
        try {
            await deleteColumn(columnId);
            refresh();
            toggle();
        } catch (error) {
            console.error("Failed to delete the column.", error);
        }
    };

    return (
        <Modal isOpen={isOpen} toggle={toggle} fade={false}>
            <ModalHeader toggle={toggle}>Confirm Deletion</ModalHeader>
            <ModalBody>Are you sure you want to delete this column?</ModalBody>
            <ModalFooter>
                <Button color="danger" onClick={handleDeleteColumn}>Delete</Button>
                <Button color="secondary" onClick={toggle}>Cancel</Button>
            </ModalFooter>
        </Modal>
    );
}

export default DeleteColumnModal;