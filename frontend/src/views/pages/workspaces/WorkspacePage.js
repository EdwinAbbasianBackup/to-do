import AlternativeHeader from "components/Headers/AlternativeHeader";
import {Container} from "reactstrap";
import Workspaces from "../../apps/workspace/Workspaces";
import useWorkspaceData from "services/workspaceData";
import {useParams} from "react-router-dom";
import React from "react";
import LoadingOverlay from "components/Information/LoadingOverlayPage";
import ErrorPage from "components/Information/ErrorPage";


function WorkspacesPage() {
    const { workspaces: initialWorkspaces, loading, error, refresh } = useWorkspaceData();
    const { workspaceId } = useParams();

    const selectedWorkspace = initialWorkspaces.find(workspace => workspace.id === workspaceId);


    if (loading ){
        return (
            <>
                <Container fluid={true} draggable={false}>
                    <LoadingOverlay />
                </Container>
            </>
            )
    }

    if (error) {
        return (
            <>
                <Container fluid={true} draggable={false}>
                    <ErrorPage />
                </Container>
            </>
        )
    }

    if (!selectedWorkspace) {
        return (
            <>
                <Container fluid={true} draggable={false}>
                    <AlternativeHeader title={"Workspace not found"} />
                </Container>
            </>
        )
    }

    return (
        <>
            <Container fluid={true} draggable={false}>
                <Workspaces workspace={selectedWorkspace} refresh={refresh} />
            </Container>
        </>
    );
}
export default React.memo(WorkspacesPage);
