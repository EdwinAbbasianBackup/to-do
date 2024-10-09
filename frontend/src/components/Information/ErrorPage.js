import React from "react";

const ErrorPage = (props) => {
    const {...data} = props;

    return (
        <div className="d-flex align-items-center justify-content-center vh-100">
            <div className="text-center">
                <h1 className="display-1 fw-bold">Error</h1>
                <p className="lead">
                    Something went wrong..
                    <br/>
                    <span className={"font-weight-bold"}>{
                        data.error && data.error.message ? data.error.message : "Unknown error"

                    }</span>
                </p>
            </div>
        </div>
    )
}

export default ErrorPage;
