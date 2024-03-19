import { useNavigate } from "react-router-dom";
import { NavLink } from "react-router-dom";
import { useEffect, useState } from "react";
import ChangeProperty from "../../Components/ChangeProperty/ChangeProperty";
import "./MainPage.scss";

enum Role {
  user = "ROLE_USER",
  admin = "ROLE_Admin",
}
const MainPage = () => {
  const [visibleTopic, setVisibleTopic] = useState(false);
  const [visibleLanguages, setVisibleLanguages] = useState(false);
  const navigator = useNavigate();
  const role = localStorage.getItem("role");
  useEffect(() => {
    if (!role) {
      navigator("/auth");
    } else if (role === Role.user) navigator("/tasks");
  }, [role]);

  return (
    <div className="mainPage">
      <header>
        <NavLink to={"/tasks"} className="header__link">
          Список задач
        </NavLink>
        <NavLink to={"/create-task"} className="header__link">
          Создать задачу
        </NavLink>
        <button onClick={() => setVisibleTopic(!visibleTopic)}>
          Edit topics
        </button>
        <button onClick={() => setVisibleLanguages(!visibleLanguages)}>
          Edit languages
        </button>
      </header>

      <div className="changeProperty">
        {visibleTopic && <ChangeProperty property="topic" />}
        {visibleLanguages && <ChangeProperty property="programming-language" />}
      </div>
    </div>
  );
};

export default MainPage;
