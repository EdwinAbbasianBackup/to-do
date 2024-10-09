import React from "react";
import { Breadcrumb, BreadcrumbItem, Container, Row, Col } from "reactstrap";
import { Link } from "react-router-dom";  // Import the Link component

function AlternativeHeader({parentName, name}) {
  return (
    <>
      <div className="header pb-6">
        <Container fluid>
          <div className="header-body">
            <Row className="align-items-center py-4">
              <Col lg="6" xs="7">
                <h1 className="h2 d-inline-block mb-0">{parentName}</h1>{" "}
                <Breadcrumb
                  className="d-none d-md-inline-block ml-md-4"
                  listClassName="breadcrumb-links"
                >
                  <h2>{name}</h2>
                  <BreadcrumbItem>
                    <Link to="/admin/dashboard">
                      <i className="fas fa-home" />
                    </Link>
                  </BreadcrumbItem>
                </Breadcrumb>
              </Col>
            </Row>
          </div>
        </Container>
      </div>
    </>
  );
}

export default AlternativeHeader;
