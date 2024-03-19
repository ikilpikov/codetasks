import AuthForm from "../../Components/AuthForm/AuthForm";
import { authorization } from "../../Services/services";
const AuthPage = () => {
  return (
    <AuthForm mutationFn={authorization} buttonText="Войти" title="Вход" />
  );
};

export default AuthPage;
