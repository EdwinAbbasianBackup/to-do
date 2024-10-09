import Dashboard from "views/pages/dashboards/Dashboard.js";
import WorkspacesPage from "./views/pages/workspaces/WorkspacePage";



const routes = [
  {
    name: "Dashboard",
    icon: "ni ni-shop text-primary",
    state: "dashboardsCollapse",
    path: "/dashboard",
    miniName: "D",
    component: <Dashboard />,
    layout: "/admin",
  },
  {
    name: "Workspaces",
    path: "/workspaces/:workspaceId",
    component: <WorkspacesPage />,
    layout: "/admin",
    display: false
  },
];

export default routes;
