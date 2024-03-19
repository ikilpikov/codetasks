import { Route, Routes } from "react-router-dom";
import RegistrationPage from "./Pages/RegistrationPage/RegistrationPage";
import TasksPage from "./Pages/TasksPage/TasksPage";
import AuthPage from "./Pages/AuthPage/AuthPage";
import TaskBodyPage from "./Pages/TaskBodyPage/TaskBodyPage";
import SandBoxPage from "./Pages/SandBoxPage/SandBoxPage";
import SolutionsPage from "./Pages/SolutionsPage/SolutionsPage";
import MainPage from "./Pages/MainPage/MainPage";
import CreateTaskPage from "./Pages/CreateTaskPage/CreateTaskPage";
import "./App.css";

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/auth" element={<AuthPage />} />
        <Route path="/create-task" element={<CreateTaskPage />} />
        <Route path="/tasks" element={<TasksPage />} />
        <Route path="/task">
          <Route path=":taskId" element={<TaskBodyPage />} />
        </Route>
        <Route path="/sandbox">
          <Route path=":taskId" element={<SandBoxPage />} />
        </Route>
        <Route path="solution">
          <Route path=":taskId" element={<SolutionsPage />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
