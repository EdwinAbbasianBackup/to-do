import React from "react";
import {Spinner} from "reactstrap";

const LoadingOverlay = () => {
    return (
        <div className={"d-flex align-items-center justify-content-center vh-100"}>
            <div className={"text-center"}>
               <Spinner />
            </div>
        </div>
    )
}

export default LoadingOverlay;
