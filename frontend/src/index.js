import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import axios from 'axios';
import "react-notification-alert/dist/animate.css";
import "react-perfect-scrollbar/dist/css/styles.css";
import "sweetalert2/dist/sweetalert2.min.css";
import "select2/dist/css/select2.min.css";
import "quill/dist/quill.core.css";
import "@fortawesome/fontawesome-free/css/all.min.css";
import "assets/vendor/nucleo/css/nucleo.css";
import "./assets/scss/cnsd-dashboard.scss?v1.2.1";
import AdminLayout from "layouts/Admin.js";
import Login from "./views/pages/login/Login";
import RegisterForm from "./views/pages/register/RegisterForm";
import Cookies from "js-cookie";
axios.defaults.headers.post['Content-Type'] = 'application/json';
const isAuthenticated = !!Cookies.get('user');


const root = ReactDOM.createRoot(document.getElementById("root"));



root.render(
  <BrowserRouter>
    <Routes>
      {
        isAuthenticated ? (
          <>
            <Route path="/admin/*" element={<AdminLayout />} />
            <Route path="/" element={<AdminLayout />} />
             <Route path="/auth/login" element={<Login />} />
              <Route path="/auth/register" element={<RegisterForm />} />
          </>
        ) : (
          <>
            <Route path="/auth/login" element={<Login />} />
            <Route path="/login" element={<Login />} />
            <Route path="/auth/register" element={<RegisterForm />} />
            <Route path="*" element={<Navigate to="/auth/login" replace />} />
          </>
        )
      }
    </Routes>
  </BrowserRouter>
);
