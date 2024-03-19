import React, { useState, FC } from "react";
import { useMutation } from "@tanstack/react-query";
import { useNavigate, NavLink } from "react-router-dom";
import { AxiosError } from "axios";
import { JwtPayload, jwtDecode } from "jwt-decode";
import { useTasksStore } from "../../store";
import "./AuthForm.scss";

interface IAuthForm {
  mutationFn: (data: any) => Promise<any>; // Передаем функцию для выполнения мутации
  buttonText: string;
  title: string;
}

interface JWT extends JwtPayload {
  role: string;
}

const AuthForm: FC<IAuthForm> = ({ mutationFn, buttonText, title }) => {
  const [userData, setUserData] = useState({ userName: "", password: "" });
  const navigator = useNavigate();
  const { reset } = useTasksStore();
  const { mutate, isError, error } = useMutation({
    mutationFn,
    onSuccess: (response) => {
      localStorage.setItem("token", response.data.token);
      const decoded = jwtDecode(response.data.token) as JWT;
      localStorage.setItem("role", decoded.role);
      console.log(response);
      navigator("/");
    },
  });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    reset();
    mutate(userData);
  };

  return (
    <form className="registration" onSubmit={handleSubmit}>
      <h2>{title}</h2>
      {isError && (
        <p className="registration__error">
          {String((error as AxiosError).response?.data)}
        </p>
      )}
      <input
        className={isError ? "registration__error-input" : ""}
        type="text"
        placeholder="Логин"
        value={userData.userName}
        onChange={(e) => setUserData({ ...userData, userName: e.target.value })}
      />
      <input
        className={isError ? "registration__error-input" : ""}
        type="text"
        placeholder="Пароль"
        value={userData.password}
        onChange={(e) => setUserData({ ...userData, password: e.target.value })}
      />
      <button className="registration__button">{buttonText}</button>
      {title === "Регистрация" ? (
        <NavLink to={"/auth"}>Есть аккаунт?</NavLink>
      ) : (
        title === "Вход" && <NavLink to={"/register"}>Нет аккаунта?</NavLink>
      )}
    </form>
  );
};

export default AuthForm;
