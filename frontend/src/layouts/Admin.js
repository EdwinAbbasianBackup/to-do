import React, { useState, useEffect, useRef } from "react";
import { useLocation, Route, Routes, Navigate } from "react-router-dom";
import AdminNavbar from "components/Navbars/AdminNavbar.js";
import AdminFooter from "components/Footers/AdminFooter.js";
import routes from "routes.js";

function Admin() {
  const [sidenavOpen, setSidenavOpen] = useState(true);
  const location = useLocation();
  const mainContentRef = useRef(null);

  useEffect(() => {
    document.documentElement.scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
    mainContentRef.current.scrollTop = 0;
  }, [location]);

  const getRoutes = (routes) => {
    return routes.flatMap((prop, key) => {
      if (prop.collapse) {
        return getRoutes(prop.views);
      }
      if (prop.layout === "/admin") {
        return <Route path={prop.path} element={prop.component} key={key} />;
      } else {
        return [];
      }
    });
  };

  const getBrandText = () => {
    const route = routes.find((route) =>
      location.pathname.includes(route.layout + route.path)
    );
    return route ? route.name : "Brand";
  };

  const toggleSidenav = () => {
    document.body.classList.toggle("g-sidenav-pinned");
    document.body.classList.toggle("g-sidenav-hidden");
    setSidenavOpen(!sidenavOpen);
  };

  const getNavbarTheme = () => {
    return location.pathname.includes("admin/alternative-dashboard")
      ? "dark"
      : "dark";
  };

  return (
    <>
      <div className="main-content" ref={mainContentRef}>
        <AdminNavbar
          theme={getNavbarTheme()}
          toggleSidenav={toggleSidenav}
          sidenavOpen={sidenavOpen}
          brandText={getBrandText()}
        />
        <Routes>
          {getRoutes(routes)}
          <Route path="*" element={<Navigate to="/admin/dashboard" />} />
        </Routes>
        <AdminFooter />
      </div>
      {sidenavOpen && <div className="backdrop d-xl-none" onClick={toggleSidenav} />}
    </>
  );
}

export default Admin;
